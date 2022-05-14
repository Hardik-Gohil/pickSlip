package com.pickSlip.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.pickSlip.entity.PickSlip;

public interface PickSlipDataTablesRepository extends DataTablesRepository<PickSlip, Long> {

}
