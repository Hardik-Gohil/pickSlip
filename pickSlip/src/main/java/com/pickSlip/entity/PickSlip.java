package com.pickSlip.entity;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "pick_slip", indexes = { @Index(name = "index_pickSlipNo", columnList = "pickSlipNo", unique = true) })
@Data
public class PickSlip implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pickSlipId;

	@Column(length = 510)
	private String pickSlipNo;

	private BigInteger customerNumber;
	
	@Column(length = 510)
	private String customerName;

	private BigInteger orderNumber;
	
	@Column(length = 510)
	private String productCategory;

	@Column(length = 510)
	private String city;

	private BigInteger deliveryNo;

	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate pickSlipDate;

	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime pickslipGenTime;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime pickSlipUpdatedDate;

	private Integer systemStatus;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime systemCreatedOn;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "pick_slip_id")
	private List<PickSlipItem> pickSlipItems;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "qr_code_id")
	@JsonIgnore
	private QRCode qrCode;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
