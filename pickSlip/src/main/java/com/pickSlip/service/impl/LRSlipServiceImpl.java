package com.pickSlip.service.impl;

import static com.pickSlip.utility.PickSlipUtility.getBigInteger;
import static com.pickSlip.utility.PickSlipUtility.getString;
import static com.pickSlip.utility.PickSlipUtility.getLocalDate;
import static com.pickSlip.utility.PickSlipUtility.isRowEmpty;
import static com.pickSlip.utility.PickSlipUtility.localDateFormatter;
import static com.pickSlip.utility.PickSlipUtility.localDateTimeFormatter;
import static com.pickSlip.utility.PickSlipUtility.regex;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.dto.LRSlipDto;
import com.pickSlip.service.LRSlipService;

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
public class LRSlipServiceImpl implements LRSlipService {
	
	@Autowired
	private HttpServletRequest httpServletRequest;

	@Override
	public ResponseEntity<Resource> lrSlipUploadPrint(RedirectAttributes redir, MultipartFile file) {
		try {
			Workbook wb = null;
			if (file.getOriginalFilename().endsWith("xlsx")) {
				wb = new XSSFWorkbook(file.getInputStream());
			} else {
				wb = new HSSFWorkbook(file.getInputStream());
			}
			Sheet sheet = wb.getSheetAt(0);
			int previousRowCount = sheet.getLastRowNum();
			boolean isFirstRowFound = false;
			List<LRSlipDto> lrSlipDtoList = new ArrayList<>();
			for (int i = 0; i < previousRowCount + 1; i++) {
				Row row = sheet.getRow(i);
				if (isRowEmpty(row) || (!isFirstRowFound && !getString(row, 0).replaceAll(regex, "").equalsIgnoreCase("ConsignmentNo"))) {
					continue;
				} else if (!isFirstRowFound) {
					isFirstRowFound = true;
					continue;
				}
				LRSlipDto lrSlipDto = new LRSlipDto();
				lrSlipDto.setConsignmentNo(getString(row, 0));
				lrSlipDto.setReferenceNumber(getString(row, 1));
				lrSlipDto.setConsigneeCompanyName(getString(row, 3));
				lrSlipDto.setConsigneeCity(getString(row, 4));
				lrSlipDto.setTotalPackages(getBigInteger(row, 7));
				lrSlipDto.setConsigneeAddress(getString(row, 22));
				lrSlipDto.setConsignmentCreationDate(ObjectUtils.isEmpty(getLocalDate(row, 29)) ? "" : localDateFormatter.format(getLocalDate(row, 29)));
				lrSlipDto.setInvoiceDate(getString(row, 85));
				lrSlipDtoList.add(lrSlipDto);
			}
			
			if (CollectionUtils.isNotEmpty(lrSlipDtoList)) {
				try {
					String path = httpServletRequest.getRequestURL().toString().replace(httpServletRequest.getRequestURI(), "");
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("khodiyarMaa", path + "/resources/logo/khodiyar_maa.png");
					parameters.put("printedOn", LocalDateTime.now().format(localDateTimeFormatter));
					InputStream input = PickSlipServiceImpl.class.getResourceAsStream("/" + "jasper/LRSlipData.jrxml");
					JasperDesign jasperDesign = JRXmlLoader.load(input);
					JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
					List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
					for (LRSlipDto lrSlipDto : lrSlipDtoList) {
						List<Object> fieldList = new ArrayList<Object>();
						fieldList.add(lrSlipDto);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(fieldList));
						jasperPrints.add(jasperPrint);
					}
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
					exporter.exportReport();
					ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
					String fileName = "PRSlips.pdf";
					return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").contentType(MediaType.APPLICATION_PDF).body(resource);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
