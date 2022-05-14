package com.pickSlip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "qr_code")
@Data
public class QRCode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qrCodeId;

	@Lob
	@Column(name = "photo", columnDefinition = "BLOB")
	private byte[] photo;
}
