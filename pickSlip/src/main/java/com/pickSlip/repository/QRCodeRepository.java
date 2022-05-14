package com.pickSlip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickSlip.entity.QRCode;

public interface QRCodeRepository extends JpaRepository<QRCode, Long> {

}
