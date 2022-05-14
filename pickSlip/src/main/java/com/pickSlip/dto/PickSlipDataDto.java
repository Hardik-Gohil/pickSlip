package com.pickSlip.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;

@Data
public class PickSlipDataDto {

	private String operatingUnit;

	private BigInteger customerNumber;

	private String customerName;

	private String orderType;

	private BigInteger orderNumber;

	private LocalDate orderDate;

	private LocalDate promiseDate;

	private String productCategory;

	private String webOrderNo;

	private String city;

	private String agentCode;

	private BigInteger deliveryNo;

	private String pickSlipNo;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate pickSlipDate;

	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime pickslipGenTime;

	private String createdBy;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime pickSlipUpdatedDate;

	private String updatedBy;

	private LocalDate shipDateAndTime;

	private LocalTime shipConfirmTime;

	private String shipConfirmBy;

	private String destinationSubInventory;

	private String status;

	private Integer systemStatus;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime systemCreatedOn;

	@Getter(lombok.AccessLevel.NONE)
	List<PickSlipDataItemDto> pickSlipDataItem;

	@JsonIgnore
	private String orderedItemString;

	@JsonIgnore
	private String pickedQtyString;

	@JsonIgnore
	private String weightString;

	@JsonIgnore
	private String volumeString;

	@JsonIgnore
	private String unitPriceString;

	@JsonIgnore
	private String amountString;

	@JsonIgnore
	private String locationString;

	@JsonIgnore
	private String subInventoryString;

	public PickSlipDataDto(String pickSlipNo, BigInteger customerNumber, String customerName, BigInteger orderNumber,
			String productCategory, String city, BigInteger deliveryNo, LocalDate pickSlipDate,
			LocalTime pickslipGenTime, LocalDateTime pickSlipUpdatedDate, LocalDateTime systemCreatedOn,
			String orderedItemString, String pickedQtyString, String weightString, String volumeString,
			String unitPriceString, String amountString, String subInventoryString) {
		super();
		this.pickSlipNo = pickSlipNo;
		this.customerNumber = customerNumber;
		this.customerName = customerName;
		this.orderNumber = orderNumber;
		this.productCategory = productCategory;
		this.city = city;
		this.deliveryNo = deliveryNo;
		this.pickSlipDate = pickSlipDate;
		this.pickslipGenTime = pickslipGenTime;
		this.pickSlipUpdatedDate = pickSlipUpdatedDate;
		this.systemCreatedOn = systemCreatedOn;
		this.orderedItemString = orderedItemString;
		this.pickedQtyString = pickedQtyString;
		this.weightString = weightString;
		this.volumeString = volumeString;
		this.unitPriceString = unitPriceString;
		this.amountString = amountString;
		this.subInventoryString = subInventoryString;
	}

	public PickSlipDataDto() {
		super();
	}

	public List<PickSlipDataItemDto> getPickSlipDataItem() {
		pickSlipDataItem = new ArrayList<>();
		String[] orderedItemArr = this.orderedItemString.split(",");
		String[] pickedQtyArr = this.pickedQtyString.split(",");
		String[] weightArr = this.weightString.split(",");
		String[] volumeArr = this.volumeString.split(",");
		String[] unitPriceArr = this.unitPriceString.split(",");
		String[] amountArr = this.amountString.split(",");
		String[] subInventoryArr = this.subInventoryString.split(",");

		for (int i = 0; i < orderedItemArr.length; i++) {
			pickSlipDataItem
					.add(new PickSlipDataItemDto(orderedItemArr[i], new BigDecimal(pickedQtyArr[i]).toBigInteger(),
							new BigDecimal(weightArr[i]), new BigDecimal(volumeArr[i]), new BigDecimal(unitPriceArr[i]),
							new BigDecimal(amountArr[i]), subInventoryArr[i]));
		}
		return pickSlipDataItem;
	}

}
