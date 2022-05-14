package com.pickSlip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.pickSlip.entity.LocationMaster;

public interface LocationMasterRepository extends JpaRepository<LocationMaster, Long> {

	@Modifying
	@Query(value = "truncate table location_master", nativeQuery = true)
	void truncateLocationTable();

}
