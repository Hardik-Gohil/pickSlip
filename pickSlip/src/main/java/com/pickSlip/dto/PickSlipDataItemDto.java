package com.pickSlip.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PickSlipDataItemDto {

	private String orderedItem;

	private BigInteger pickedQty;

	private BigDecimal weight;

	private BigDecimal volume;

	private BigDecimal unitPrice;

	private BigDecimal amount;
	
	private String subInventory;
	
	private String srNo;
	
	public PickSlipDataItemDto(String orderedItem, BigInteger pickedQty, BigDecimal weight, BigDecimal volume, BigDecimal unitPrice, BigDecimal amount, String subInventory) {
		super();
		this.orderedItem = orderedItem;
		this.pickedQty = pickedQty;
		this.weight = weight;
		this.volume = volume;
		this.unitPrice = unitPrice;
		this.amount = amount;
		this.subInventory = subInventory;
	}

	public PickSlipDataItemDto() {
		super();
	}

}
