package com.pickSlip.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "location_master")
@Data
public class LocationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locationMasterId;

	@Column(length = 510)
	private String location;

	@Column(length = 510)
	private String itemCode;
	
	private BigInteger masterPcs;
	
	private BigInteger innerPcs;

	private LocalDateTime systemCreatedOn;
}
