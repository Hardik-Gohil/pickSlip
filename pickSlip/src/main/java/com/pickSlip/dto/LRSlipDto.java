package com.pickSlip.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class LRSlipDto {

//	A
//	B
//	D
//	E
//	H
//	W
//  AD	
//	CH

	private String consignmentNo;
	private String referenceNumber;
	private String consigneeCompanyName;
	private String consigneeCity;
	private BigInteger totalPackages;
	private String consigneeAddress;
	private String consignmentCreationDate;
	private String invoiceDate;
}
