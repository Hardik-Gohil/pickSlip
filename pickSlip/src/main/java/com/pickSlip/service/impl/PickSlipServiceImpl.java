package com.pickSlip.service.impl;

import static com.pickSlip.utility.PickSlipUtility.EmptyZip;
import static com.pickSlip.utility.PickSlipUtility.commonImageFileFormats;
import static com.pickSlip.utility.PickSlipUtility.getBigDecimal;
import static com.pickSlip.utility.PickSlipUtility.getBigInteger;
import static com.pickSlip.utility.PickSlipUtility.getLocalDate;
import static com.pickSlip.utility.PickSlipUtility.getLocalDateTime;
import static com.pickSlip.utility.PickSlipUtility.getLocalTime;
import static com.pickSlip.utility.PickSlipUtility.getQRCodeImage;
import static com.pickSlip.utility.PickSlipUtility.getString;
import static com.pickSlip.utility.PickSlipUtility.isRowEmpty;
import static com.pickSlip.utility.PickSlipUtility.localDateFormatter;
import static com.pickSlip.utility.PickSlipUtility.localDateTimeFormatter;
import static com.pickSlip.utility.PickSlipUtility.localTimeFormatter;
import static com.pickSlip.utility.PickSlipUtility.regex;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.dto.PickSlipDataDto;
import com.pickSlip.entity.LocationMaster;
import com.pickSlip.entity.PickSlip;
import com.pickSlip.entity.PickSlipData;
import com.pickSlip.entity.PickSlipItem;
import com.pickSlip.entity.QRCode;
import com.pickSlip.repository.PickSlipDataRepository;
import com.pickSlip.repository.PickSlipDataTablesRepository;
import com.pickSlip.repository.PickSlipRepository;
import com.pickSlip.service.LocationMasterService;
import com.pickSlip.service.PickSlipService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class PickSlipServiceImpl implements PickSlipService {

	@Autowired
	private PickSlipDataRepository pickSlipDataRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LocationMasterService locationMasterService;

	@Autowired
	private HttpServletRequest context;

	@Autowired
	private PickSlipRepository pickSlipRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PickSlipDataTablesRepository pickSlipDataTablesRepository;
	
	@Value("${file.upload.path}")
	private String fileUploadPath;

	@Override
	public void pickSlipTab(Model model, String tabId) {
		switch (tabId) {
		case "1":
			model.addAttribute("activeTab", tabId);
			break;
		case "2":
			model.addAttribute("activeTab", tabId);
			break;
		case "3":
			model.addAttribute("activeTab", tabId);
			break;
		default:
			model.addAttribute("activeTab", "1");
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private List<PickSlipDataDto> getPickSlipData(String whereCondition) {
		String query = "SELECT new com.pickSlip.dto.PickSlipDataDto(pickSlipData.pickSlipNo, pickSlipData.customerNumber, pickSlipData.customerName, pickSlipData.orderNumber, pickSlipData.productCategory, pickSlipData.city, pickSlipData.deliveryNo, pickSlipData.pickSlipDate, pickSlipData.pickslipGenTime, pickSlipData.pickSlipUpdatedDate, pickSlipData.systemCreatedOn, "
				+ "       GROUP_CONCAT(pickSlipData.orderedItem),GROUP_CONCAT(pickSlipData.pickedQty),GROUP_CONCAT(pickSlipData.weight),GROUP_CONCAT(pickSlipData.volume),GROUP_CONCAT(pickSlipData.unitPrice),GROUP_CONCAT(pickSlipData.amount),GROUP_CONCAT(IFNULL(pickSlipData.subInventory, 'NA'))) "
				+ "FROM PickSlipData pickSlipData "
				+ "WHERE " + whereCondition + " "
				+ "GROUP BY pickSlipData.pickSlipNo, pickSlipData.customerNumber, pickSlipData.customerName, pickSlipData.orderNumber, pickSlipData.productCategory, pickSlipData.city, pickSlipData.deliveryNo, pickSlipData.pickSlipDate, pickSlipData.pickslipGenTime, pickSlipData.pickSlipUpdatedDate, pickSlipData.systemCreatedOn ";
		List<PickSlipDataDto> pickSlipDataDtoList = (List<PickSlipDataDto>) entityManager.createQuery(query)
				.getResultList();
		return pickSlipDataDtoList;
	}

	@Override
	@Transactional
	public void uploadFile(RedirectAttributes redir, MultipartFile file) {
		try {
			String path = context.getRequestURL().toString().replace(context.getRequestURI(), "");
			Workbook wb = null;
			if (file.getOriginalFilename().endsWith("xlsx")) {
				wb = new XSSFWorkbook(file.getInputStream());
			} else {
				wb = new HSSFWorkbook(file.getInputStream());
			}
			Sheet stabilizationSheet = wb.getSheetAt(0);
			int previousRowCount = stabilizationSheet.getLastRowNum();
			boolean isFirstRowFound = false;
			List<PickSlipData> pickSlipDataList = new ArrayList<>();
			Set<String> newAddedPickSlipNos = new LinkedHashSet<String>();
			for (int i = 0; i < previousRowCount + 1; i++) {
				Row row = stabilizationSheet.getRow(i);
				if (isRowEmpty(row) || (!isFirstRowFound && !getString(row, 0).replaceAll(regex, "").equalsIgnoreCase("operatingunit"))) {
					continue;
				} else if (!isFirstRowFound) {
					isFirstRowFound = true;
					i++;i++;
					continue;
				}
				LocalDateTime now = LocalDateTime.now();
				PickSlipData pickSlipData = new PickSlipData();
				pickSlipData.setOperatingUnit(getString(row, 0));
				pickSlipData.setCustomerNumber(getBigInteger(row, 1));
				pickSlipData.setCustomerName(getString(row, 2));
				pickSlipData.setOrderType(getString(row, 3));
				pickSlipData.setOrderNumber(getBigInteger(row, 4));
				pickSlipData.setOrderDate(getLocalDate(row, 5));
				pickSlipData.setPromiseDate(getLocalDate(row, 6));
				pickSlipData.setProductCategory(getString(row, 7));
				pickSlipData.setWebOrderNo(getString(row, 8));
				pickSlipData.setCity(getString(row, 9));
				pickSlipData.setAgentCode(getString(row, 10));
				pickSlipData.setOrderedItem(getString(row, 11).split("\\.")[0]);
				pickSlipData.setPickedQty(getBigInteger(row, 12));
				pickSlipData.setWeight(getBigDecimal(row, 13));
				pickSlipData.setVolume(getBigDecimal(row, 14));
				pickSlipData.setUnitPrice(getBigDecimal(row, 15));
				pickSlipData.setAmount(getBigDecimal(row, 16));
				pickSlipData.setDeliveryNo(getBigInteger(row, 17));
				pickSlipData.setPickSlipNo(getString(row, 18));
				pickSlipData.setPickSlipDate(getLocalDate(row, 19));
				pickSlipData.setPickslipGenTime(getLocalTime(row, 20));
				pickSlipData.setCreatedBy(getString(row, 21));
				pickSlipData.setPickSlipUpdatedDate(getLocalDateTime(row, 22));
				pickSlipData.setUpdatedBy(getString(row, 23));
				pickSlipData.setOriPickslipNo(getString(row, 24));
				pickSlipData.setShipDateAndTime(getLocalDate(row, 25));
				pickSlipData.setShipConfirmTime(getLocalTime(row, 26));
				pickSlipData.setShipConfirmBy(getString(row, 27));
				pickSlipData.setSubInventory(getString(row, 28));
				pickSlipData.setDestinationSubInventory(getString(row, 29));
				pickSlipData.setStatus(getString(row, 30));
				pickSlipData.setSystemStatus(0);
				pickSlipData.setSystemCreatedOn(now);
				pickSlipDataList.add(pickSlipData);
				newAddedPickSlipNos.add(pickSlipData.getPickSlipNo());
			}
			if (CollectionUtils.isNotEmpty(newAddedPickSlipNos)) {
				List<String> alreadyExistsPickSlipNos = pickSlipDataRepository.findAllByPickSlipNoIn(newAddedPickSlipNos);
				if (CollectionUtils.isNotEmpty(alreadyExistsPickSlipNos)) {
					pickSlipDataList = pickSlipDataList.stream()
							.filter(ps -> !alreadyExistsPickSlipNos.contains(ps.getPickSlipNo()))
							.collect(Collectors.toList());
					newAddedPickSlipNos = new LinkedHashSet<String>(newAddedPickSlipNos.stream()
							.filter(ps -> !alreadyExistsPickSlipNos.contains(ps)).collect(Collectors.toList()));
				}
				if (CollectionUtils.isNotEmpty(pickSlipDataList)) {
					pickSlipDataRepository.saveAll(pickSlipDataList);
					String whereCondition = "pickSlipData.pickSlipNo In ('" + String.join("','", newAddedPickSlipNos) + "')";
					List<PickSlipDataDto> pickSlipDataDtoList = getPickSlipData(whereCondition);
					List<PickSlip> pickSlipList = new ArrayList<>();
					pickSlipDataDtoList.forEach(pickSlipDataDto -> {
						PickSlip pickSlip = modelMapper.map(pickSlipDataDto, PickSlip.class);
						pickSlip.setPickSlipItems(modelMapper.map(pickSlipDataDto.getPickSlipDataItem(), new TypeToken<List<PickSlipItem>>() {}.getType()));
						pickSlip.setSystemStatus(0);
						QRCode qrCode = new QRCode();
						try {
							qrCode.setPhoto(getQRCodeImage(path + "/upload-photos?pickSlipNo=" + pickSlip.getPickSlipNo(), 500, 500));
						} catch (Exception e) {
							e.printStackTrace();
						}
						pickSlip.setQrCode(qrCode);
						pickSlipList.add(pickSlip);
					});
//					List<PickSlip> pickSlipList1 = new ArrayList<>();
//					for (int i = 0; i < 2000; i++) {
//						for (PickSlip pickSlip : pickSlipList) {
//							pickSlip = (PickSlip) pickSlip.clone();
//							pickSlip.setPickSlipNo(pickSlip.getPickSlipNo().replace("706-", i + "-"));
//							pickSlipList1.add(pickSlip);
//							List<PickSlipItem> pickSlipItemList = new ArrayList<>();
//							for (PickSlipItem pickSlipItem : pickSlip.getPickSlipItems()) {
//								pickSlipItemList.add((PickSlipItem) pickSlipItem.clone());
//							}
//							pickSlip.setPickSlipItems(pickSlipItemList);
//						}
//					}
					pickSlipRepository.saveAll(pickSlipList);
				}
				redir.addFlashAttribute("alreadyExistsPickSlipNos", String.join(", ", alreadyExistsPickSlipNos));
				redir.addFlashAttribute("newAddedPickSlipNos", String.join(", ", newAddedPickSlipNos));
			}
			redir.addFlashAttribute("uploadPickSlipFileIsSuccess", Boolean.TRUE);
			redir.addFlashAttribute("uploadPickSlipFileMessage", "Uploaded the file successfully: " + file.getOriginalFilename());

		} catch (Exception e) {
			e.printStackTrace();
			redir.addFlashAttribute("uploadPickSlipFileIsSuccess", Boolean.FALSE);
			redir.addFlashAttribute("uploadPickSlipFileMessage", "Could not upload the file: " + file.getOriginalFilename() + "!");
		}
	}

	@Override
	public DataTablesOutput<PickSlip> getPickSlips(DataTablesInput input, Integer systemStatus) {
		Specification<PickSlip> additionalSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("systemStatus"), systemStatus);
		return pickSlipDataTablesRepository.findAll(input, additionalSpecification);
	}
	
	@Override
	@Transactional
	public void pickSlipUpdateStatus(String pickSlipNos, Integer status) {
		if (StringUtils.hasText(pickSlipNos)) {
			List<String> pickSlipNoList = new ArrayList<String>(Arrays.asList(pickSlipNos.split(",")));
			pickSlipRepository.updatePickSlipSystemStatus(pickSlipNoList, status);
			pickSlipDataRepository.updatePickSlipSystemStatus(pickSlipNoList, status);
		}
	}
	
	@Override
	public ResponseEntity<Resource> getPickSlipDataPrint(ModelMap modelMap, String pickSlipNos, HttpServletRequest request, HttpServletResponse respons) {
		List<String> pickSlipNoList = new ArrayList<String>(Arrays.asList(pickSlipNos.split(",")));
		List<PickSlip> pickSlipList = pickSlipRepository.findAllByPickSlipNoIn(pickSlipNoList);
		
		if (CollectionUtils.isNotEmpty(pickSlipList)) {
			try {
				String path = request.getRequestURL().toString().replace(request.getRequestURI(), "");
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("companyIcon1", path + "/resources/logo/companyIcon1.PNG");
				parameters.put("companyIcon2", path + "/resources/logo/companyIcon2.PNG");
				parameters.put("printedOn", LocalDateTime.now().format(localDateTimeFormatter));
				InputStream input = PickSlipServiceImpl.class.getResourceAsStream("/" + "jasper/PickSlipData.jrxml");
				JasperDesign jasperDesign = JRXmlLoader.load(input);
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
				for (PickSlip pickSlip : pickSlipList) {
					int srNo = 0;
					BigInteger total = BigInteger.ZERO;
					BigInteger totalMasterPcs = BigInteger.ZERO;
					BigInteger totalInnerPcs = BigInteger.ZERO;
					BigInteger totalLoosePcs = BigInteger.ZERO;
					
					Comparator<String> comparator = new Comparator<String>() {
						@Override
						public int compare(final String o1, final String o2) {
							if (o1.isEmpty())
								return Integer.MAX_VALUE;
							else if (o2.isEmpty())
								return Integer.MIN_VALUE;
							else
								return o1.compareTo(o2);
						}
					};
					
					Collections.sort(pickSlip.getPickSlipItems(), Comparator.comparing(PickSlipItem::getLocation, comparator));					
					
					List<PickSlipItem> pickSlipItemList = new ArrayList<>();
					for (PickSlipItem pickSlipItem : pickSlip.getPickSlipItems()) {
						pickSlipItem.setSrNo(String.valueOf(++srNo));
						total = total.add(pickSlipItem.getPickedQty());
						pickSlipItemList.add(pickSlipItem);
						String[] masterInnerLoosePcs = pickSlipItem .getMasterInnerLoosePcs().split(" / ");
						if (StringUtils.hasText(pickSlipItem.getMasterInnerLoosePcs())) {
							totalMasterPcs = totalMasterPcs.add(new BigInteger(masterInnerLoosePcs[0]));
							totalInnerPcs = totalInnerPcs.add(new BigInteger(masterInnerLoosePcs[1]));
							totalLoosePcs = totalLoosePcs.add(new BigInteger(masterInnerLoosePcs[2]));
						}
					}
					PickSlipItem pickSlipItem = new PickSlipItem();
					pickSlipItem.setSrNo("");
					pickSlipItem.setOrderedItem("");
					pickSlipItem.setLocation("Total");
					pickSlipItem.setPickedQty(total);
					pickSlipItem.setSubInventory("");
					pickSlipItem.setMasterPcs("");
					pickSlipItem.setInnerPcs("");
					pickSlipItem.setMasterInnerLoosePcs(totalMasterPcs + " / " + totalInnerPcs + " / " + totalLoosePcs);
					pickSlipItemList.add(pickSlipItem);
					
					parameters.put("tableData", new JRBeanCollectionDataSource(pickSlipItemList));
					parameters.put("pickSlipDate", ObjectUtils.isEmpty(pickSlip.getPickSlipDate()) ? "" : pickSlip.getPickSlipDate().format(localDateFormatter));
					parameters.put("pickSlipUpdatedDate", ObjectUtils.isEmpty(pickSlip.getPickSlipUpdatedDate()) ? "" : pickSlip.getPickSlipUpdatedDate().format(localDateTimeFormatter));
					parameters.put("qrCode", new ByteArrayInputStream(pickSlip.getQrCode().getPhoto()));
					List<Object> fieldList = new ArrayList<Object>();
					fieldList.add(pickSlip);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(fieldList));
					jasperPrints.add(jasperPrint);
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
				exporter.exportReport();
				ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(resource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public ResponseEntity<Resource> exportPickSlipData(Integer systemStatus) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Pick_Slip_Data");
			String[] headers = { "Operating Unit", "Customer Number", "Customer Name", "Order Type", "Order Number",
					"Order Date", "Promise Date", "Product Category", "Web Order No", "City", "Agent Code",
					"Ordered Item", "Location", "Picked Qty", "Weight(In KGS)", "Volume (In CFT)", "Unit Price",
					"Amount", "Delivery No", "Pick Slip No", "PickSlip  Date", "Pickslip Gen Time", "Created By",
					"Pick Slip Updated Date", "Updated By", "Ori Pickslip No", "ShipDate And Time", "Ship confirm time",
					"Ship Confirm By", "Sub Inventory", "Destination Subinventory", "Status",
					"Pick Slip Uploaded Date Time", "File Uploaded" };
			Row row = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				row.createCell(i).setCellValue(headers[i]);
			}

			List<PickSlipData> pickSlipDataist = pickSlipDataRepository.findAllBySystemStatus(systemStatus);
			List<LocationMaster> locationMasterList = locationMasterService.getLocationData();
			Map<String, String> locationMasterMap = CollectionUtils.isNotEmpty(locationMasterList) ? locationMasterList.stream().collect(Collectors.toMap(LocationMaster::getItemCode, LocationMaster::getLocation)) : new HashedMap<>();

			for (int i = 0; i < pickSlipDataist.size(); i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(pickSlipDataist.get(i).getOperatingUnit());
				dataRow.createCell(1).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getCustomerNumber()) ? null : pickSlipDataist.get(i).getCustomerNumber().doubleValue());
				dataRow.createCell(2).setCellValue(pickSlipDataist.get(i).getCustomerName());
				dataRow.createCell(3).setCellValue(pickSlipDataist.get(i).getOrderType());
				dataRow.createCell(4).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getOrderNumber()) ? null : pickSlipDataist.get(i).getOrderNumber().doubleValue());
				dataRow.createCell(5).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getOrderDate()) ? "" : localDateFormatter.format(pickSlipDataist.get(i).getOrderDate()));
				dataRow.createCell(6).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getPromiseDate()) ? "" : localDateFormatter.format(pickSlipDataist.get(i).getPromiseDate()));
				dataRow.createCell(7).setCellValue(pickSlipDataist.get(i).getProductCategory());
				dataRow.createCell(8).setCellValue(pickSlipDataist.get(i).getWebOrderNo());
				dataRow.createCell(9).setCellValue(pickSlipDataist.get(i).getCity());
				dataRow.createCell(10).setCellValue(pickSlipDataist.get(i).getAgentCode());
				dataRow.createCell(11).setCellValue(pickSlipDataist.get(i).getOrderedItem());
				dataRow.createCell(12).setCellValue(locationMasterMap.get(pickSlipDataist.get(i).getOrderedItem()));
				dataRow.createCell(13).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getPickedQty()) ? null : pickSlipDataist.get(i).getPickedQty().doubleValue());
				dataRow.createCell(14).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getWeight()) ? null : pickSlipDataist.get(i).getWeight().doubleValue());
				dataRow.createCell(15).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getVolume()) ? null : pickSlipDataist.get(i).getVolume().doubleValue());
				dataRow.createCell(16).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getUnitPrice()) ? null : pickSlipDataist.get(i).getUnitPrice().doubleValue());
				dataRow.createCell(17).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getAmount()) ? null : pickSlipDataist.get(i).getAmount().doubleValue());
				dataRow.createCell(18).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getDeliveryNo()) ? null : pickSlipDataist.get(i).getDeliveryNo().doubleValue());
				dataRow.createCell(19).setCellValue(pickSlipDataist.get(i).getPickSlipNo());
				dataRow.createCell(20).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getPickSlipDate()) ? "" : localDateFormatter.format(pickSlipDataist.get(i).getPickSlipDate()));
				dataRow.createCell(21).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getPickslipGenTime()) ? "" : localTimeFormatter.format(pickSlipDataist.get(i).getPickslipGenTime()));
				dataRow.createCell(22).setCellValue(pickSlipDataist.get(i).getCreatedBy());
				dataRow.createCell(23).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getPickSlipUpdatedDate()) ? "" : localDateTimeFormatter.format(pickSlipDataist.get(i).getPickSlipUpdatedDate()));
				dataRow.createCell(24).setCellValue(pickSlipDataist.get(i).getUpdatedBy());
				dataRow.createCell(25).setCellValue(pickSlipDataist.get(i).getOriPickslipNo());
				dataRow.createCell(26).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getShipDateAndTime()) ? "" : localDateFormatter.format(pickSlipDataist.get(i).getShipDateAndTime()));
				dataRow.createCell(27).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getShipConfirmTime()) ? "" : localTimeFormatter.format(pickSlipDataist.get(i).getShipConfirmTime()));
				dataRow.createCell(28).setCellValue(pickSlipDataist.get(i).getShipConfirmBy());
				dataRow.createCell(29).setCellValue(pickSlipDataist.get(i).getSubInventory());
				dataRow.createCell(30).setCellValue(pickSlipDataist.get(i).getDestinationSubInventory());
				dataRow.createCell(31).setCellValue(pickSlipDataist.get(i).getStatus());
				dataRow.createCell(32).setCellValue(ObjectUtils.isEmpty(pickSlipDataist.get(i).getSystemCreatedOn()) ? "" : localDateTimeFormatter.format(pickSlipDataist.get(i).getSystemCreatedOn()));
				dataRow.createCell(33).setCellValue(pickSlipDataist.get(i).getFileUploadCount() > 0 ? "YES" : "NO");
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);

			ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
			String filename = systemStatus == 0 ? "PickSlipData" : "ArchivedPickSlipData";
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + filename + ".xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void uploadPhotos(Model model, String pickSlipNo) {
		List<PickSlip> pickSlipList = pickSlipRepository.findAllByPickSlipNoIn(Arrays.asList(pickSlipNo));
		if (CollectionUtils.isNotEmpty(pickSlipList)) {
			model.addAttribute("pickSlip", pickSlipList.get(0));
		}
		model.addAttribute("localDateFormatter", localDateFormatter);
		model.addAttribute("localDateTimeFormatter", localDateTimeFormatter);
	}

	@Override
	@Transactional
	public String uploadPhotos(String pickSlipNo, MultipartFile[] files) {
		if (!ObjectUtils.isEmpty(files)) {
			try {
				String directoryPath = fileUploadPath + pickSlipNo;
				Path path = Paths.get(directoryPath);
				Files.createDirectories(path);
				for (int i = 0; i < files.length; i++) {
					BufferedImage image = ImageIO.read(files[i].getInputStream());
					if (!ObjectUtils.isEmpty(files) && commonImageFileFormats.contains(FileNameUtils.getExtension(files[i].getOriginalFilename()).toLowerCase())) {
						File output = new File(directoryPath + "\\" + files[i].getOriginalFilename());
						OutputStream out = new FileOutputStream(output);
						ImageWriter writer = ImageIO.getImageWritersByFormatName(FileNameUtils.getExtension(files[i].getOriginalFilename())).next();
						ImageOutputStream ios = ImageIO.createImageOutputStream(out);
						writer.setOutput(ios);

						ImageWriteParam param = writer.getDefaultWriteParam();
						if (param.canWriteCompressed()) {
							param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
							param.setCompressionQuality(0.7f);
						}

						writer.write(null, new IIOImage(image, null, null), param);

						out.close();
						ios.close();
						writer.dispose();
					} else {
						Files.copy(files[i].getInputStream(), path.resolve(files[i].getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
					}
				}
				pickSlipDataRepository.updateFileUploadCount(files.length, pickSlipNo);
				return "true";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "false";
	}

	@Override
	public ResponseEntity<ByteArrayResource> downloadZip(HttpServletResponse response, String pickSlipNo) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream outEmptyZip = new ByteArrayOutputStream();
		String directoryPath = fileUploadPath + pickSlipNo;
		Path path = Paths.get(directoryPath);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		response.setHeader("Content-Disposition", "attachment; filename=" + pickSlipNo + ".zip");
		if (Files.isDirectory(path)) {
			try (Stream<Path> paths = Files.walk(path); ZipOutputStream zippedOut = new ZipOutputStream(out)) {
				outEmptyZip.write(EmptyZip);
				List<String> fileNames = new ArrayList<>();
				paths.filter(Files::isRegularFile).forEach(file -> {
					fileNames.add(file.toString());
				});
				for (String file : fileNames) {
					FileSystemResource resource = new FileSystemResource(file);
					ZipEntry e = new ZipEntry(resource.getFilename());
					// Configure the zip entry, the properties of the file
					e.setSize(resource.contentLength());
					e.setTime(System.currentTimeMillis());
					// etc.
					zippedOut.putNextEntry(e);
					// And the content of the resource:
					StreamUtils.copy(resource.getInputStream(), zippedOut);
					zippedOut.closeEntry();
				}
				zippedOut.finish();

				if (out.size() == 0) {
					out = outEmptyZip;
				}
				outEmptyZip.close();
				ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
				return ResponseEntity.ok().headers(headers).body(resource);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				outEmptyZip.write(EmptyZip);
				outEmptyZip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ByteArrayResource resource = new ByteArrayResource(outEmptyZip.toByteArray());
		return ResponseEntity.ok().headers(headers).body(resource);
	}
}