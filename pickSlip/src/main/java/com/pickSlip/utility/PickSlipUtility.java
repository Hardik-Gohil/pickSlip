package com.pickSlip.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pickSlip.entity.LocationMaster;
import com.pickSlip.service.LocationMasterService;

@Component
public class PickSlipUtility {
	
	public static String regex = "[^A-Za-z0-9s\\/\\_]";
	public final static byte[] EmptyZip = { 80, 75, 05, 06, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00 };
	public static DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	public static List<String> commonImageFileFormats = Arrays.asList("tif", "tiff", "bmp", "jpg", "jpeg", "gif", "png", "eps");
	private static ConcurrentMap<String, LocationMaster> locationMasterMap = new ConcurrentHashMap<>();
	
	private static LocationMasterService locationMasterService;
	
	@Autowired
	private void setLocationMasterService(LocationMasterService locationMasterService) {
		PickSlipUtility.locationMasterService = locationMasterService;
	}

	public static LocationMaster getLocationMaster(String itemCode) {
		if (MapUtils.isNotEmpty(locationMasterMap)) {
			return locationMasterMap.get(itemCode);
		} else {
			synchronized (locationMasterMap) {
				if (MapUtils.isNotEmpty(locationMasterMap)) {
					return locationMasterMap.get(itemCode);
				} else {
					List<LocationMaster> locationMasterList = locationMasterService.getLocationData();
					locationMasterMap = CollectionUtils.isNotEmpty(locationMasterList) ? locationMasterList.stream().collect(Collectors.toConcurrentMap(LocationMaster::getItemCode, lm -> lm)) : new ConcurrentHashMap<>();
					return locationMasterMap.get(itemCode);
				}
			}
		}
	}
	
	public static void clearLocationMaster() {
		locationMasterMap.clear();
	}
	
	public static String getString(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		if (!ObjectUtils.isEmpty(cell)) {
			switch (cell.getCellType()) {
			case NUMERIC:
				return Double.toString(cell.getNumericCellValue());
			case STRING:
				return cell.getStringCellValue().trim();
			default:
				return cell.getStringCellValue().trim();
			}
		}
		return "";
	}

	public static BigDecimal getBigDecimal(Row row, int colIndex) {
		String val = getString(row, colIndex);
		if (!ObjectUtils.isEmpty(val)) {
			return new BigDecimal(val);
		}
		return BigDecimal.ZERO;
	}

	public static BigInteger getBigInteger(Row row, int colIndex) {
		String val = getString(row, colIndex);
		if (!ObjectUtils.isEmpty(val)) {
			return new BigDecimal(val).toBigInteger();
		}
		return BigInteger.ZERO;
	}

	public static LocalDate getLocalDate(Row row, int colIndex) {
		String val = getString(row, colIndex);
		if (!ObjectUtils.isEmpty(val)) {
			return DateUtil.getJavaDate(row.getCell(colIndex).getNumericCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}

	public static LocalTime getLocalTime(Row row, int colIndex) {
		String val = getString(row, colIndex);
		if (!ObjectUtils.isEmpty(val)) {
			return DateUtil.getJavaDate(DateUtil.getExcelDate(LocalDate.now()) + row.getCell(colIndex).getNumericCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		}
		return null;
	}

	public static LocalDateTime getLocalDateTime(Row row, int colIndex) {
		String val = getString(row, colIndex);
		if (!ObjectUtils.isEmpty(val)) {
			return DateUtil.getJavaDate(row.getCell(colIndex).getNumericCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
		return null;
	}

	public static boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != CellType.BLANK)
				return false;
		}
		return true;
	}

	public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageConfig con = new MatrixToImageConfig();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
		byte[] pngData = pngOutputStream.toByteArray();
		return pngData;
	}
}
