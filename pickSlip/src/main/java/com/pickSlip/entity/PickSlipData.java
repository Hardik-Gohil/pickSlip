package com.pickSlip.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "pick_slip_data")
@Data
public class PickSlipData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pickSlipDataId;

	@Column(length = 510)
	private String operatingUnit;

	private BigInteger customerNumber;

	@Column(length = 510)
	private String customerName;

	@Column(length = 510)
	private String orderType;

	private BigInteger orderNumber;

	private LocalDate orderDate;

	private LocalDate promiseDate;

	@Column(length = 510)
	private String productCategory;

	@Column(length = 510)
	private String webOrderNo;

	@Column(length = 510)
	private String city;

	@Column(length = 510)
	private String agentCode;

	@Column(length = 510)
	private String orderedItem;

	private BigInteger pickedQty;

	private BigDecimal weight;

	private BigDecimal volume;

	private BigDecimal unitPrice;

	private BigDecimal amount;

	private BigInteger deliveryNo;

	@Column(length = 510)
	private String pickSlipNo;

	private LocalDate pickSlipDate;

	private LocalTime pickslipGenTime;

	@Column(length = 510)
	private String createdBy;

	private LocalDateTime pickSlipUpdatedDate;

	@Column(length = 510)
	private String updatedBy;
	
	@Column(length = 510)
	private String oriPickslipNo;

	private LocalDate shipDateAndTime;

	private LocalTime shipConfirmTime;

	@Column(length = 510)
	private String shipConfirmBy;

	@Column(length = 510)
	private String subInventory;

	@Column(length = 510)
	private String destinationSubInventory;

	@Column(length = 510)
	private String status;
	
	private Integer systemStatus;
	
	@Column(columnDefinition="int default 0")
	private Integer fileUploadCount = 0;

	private LocalDateTime systemCreatedOn;

}
