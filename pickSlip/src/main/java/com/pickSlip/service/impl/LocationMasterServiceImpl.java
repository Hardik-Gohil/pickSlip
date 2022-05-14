package com.pickSlip.service.impl;

import static com.pickSlip.utility.PickSlipUtility.getString;
import static com.pickSlip.utility.PickSlipUtility.getBigInteger;
import static com.pickSlip.utility.PickSlipUtility.isRowEmpty;
import static com.pickSlip.utility.PickSlipUtility.regex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickSlip.entity.LocationMaster;
import com.pickSlip.repository.LocationMasterRepository;
import com.pickSlip.service.LocationMasterService;
import com.pickSlip.utility.PickSlipUtility;

@Service
public class LocationMasterServiceImpl implements LocationMasterService {

	@Autowired
	LocationMasterRepository locationMasterRepository;

	@Override
	@Transactional
	public void uploadFile(RedirectAttributes redir, MultipartFile file) {
		try {
			Workbook wb = null;
			if (file.getOriginalFilename().endsWith("xlsx")) {
				wb = new XSSFWorkbook(file.getInputStream());
			} else {
				wb = new HSSFWorkbook(file.getInputStream());
			}
			Sheet stabilizationSheet = wb.getSheetAt(0);
			int previousRowCount = stabilizationSheet.getLastRowNum();
			boolean isFirstRowFound = false;
			List<LocationMaster> locationList = new ArrayList<>();
			Set<String> duplicateItemCodeList  = new LinkedHashSet<String>();
			Set<String> itemCodes = new LinkedHashSet<String>();
			for (int i = 0; i < previousRowCount + 1; i++) {
				Row row = stabilizationSheet.getRow(i);
				if (isRowEmpty(row) || (!isFirstRowFound && !getString(row, 0).replaceAll(regex, "").equalsIgnoreCase("ItemCode"))) {
					continue;
				} else if (!isFirstRowFound) {
					isFirstRowFound = true;
					continue;
				}
				LocalDateTime now = LocalDateTime.now();
				LocationMaster locationMaster = new LocationMaster();
				locationMaster.setItemCode(getString(row, 0).split("\\.")[0]);
				locationMaster.setLocation(getString(row, 1));
				locationMaster.setMasterPcs(getBigInteger(row, 2));
				locationMaster.setInnerPcs(getBigInteger(row, 3));
				locationMaster.setSystemCreatedOn(now);
				locationList.add(locationMaster);
				
				if (!itemCodes.add(locationMaster.getItemCode())) {
					duplicateItemCodeList.add(locationMaster.getItemCode());
				}
			}
			
			if (CollectionUtils.isNotEmpty(locationList) && CollectionUtils.isEmpty(duplicateItemCodeList)) {
				locationMasterRepository.truncateLocationTable();
				locationMasterRepository.saveAll(locationList);
				PickSlipUtility.clearLocationMaster();
				redir.addFlashAttribute("uploadLocationFileIsSuccess", Boolean.TRUE);
				redir.addFlashAttribute("uploadLocationFileMessage", "Uploaded the file successfully: " + file.getOriginalFilename());
			} else {
				redir.addFlashAttribute("duplicateItemCodes", String.join(", ", duplicateItemCodeList));
				redir.addFlashAttribute("uploadLocationFileIsSuccess", Boolean.FALSE);
				redir.addFlashAttribute("uploadLocationFileMessage", "Could not upload the file: " + file.getOriginalFilename() + "!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			redir.addFlashAttribute("uploadLocationFileIsSuccess", Boolean.FALSE);
			redir.addFlashAttribute("uploadLocationFileMessage", "Could not upload the file: " + file.getOriginalFilename() + "!");
		}
	}

	@Override
	public List<LocationMaster> getLocationData() {
		return locationMasterRepository.findAll();
	}

	@Override
	public ResponseEntity<Resource> exportLocationMaster() {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("LocationMaster");
			String[] headers = { "Item Code", "Location", "Master(PCS)", "Inner(PCS)" };
			Row row = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				row.createCell(i).setCellValue(headers[i]);
			}

			List<LocationMaster> locationMasterList = locationMasterRepository.findAll();

			for (int i = 0; i < locationMasterList.size(); i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(locationMasterList.get(i).getItemCode());
				dataRow.createCell(1).setCellValue(locationMasterList.get(i).getLocation());
				dataRow.createCell(2).setCellValue(locationMasterList.get(i).getMasterPcs().doubleValue());
				dataRow.createCell(3).setCellValue(locationMasterList.get(i).getInnerPcs().doubleValue());
			}

			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);

			ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=LocationMaster.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
