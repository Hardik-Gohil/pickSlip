package com.pickSlip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickSlip.entity.PickSlip;

public interface PickSlipRepository extends JpaRepository<PickSlip, Long> {

	@Modifying
	@Query("UPDATE PickSlip pickSlip SET pickSlip.systemStatus = :systemStatus WHERE pickSlip.pickSlipNo IN (:pickSlipNoList)")
	void updatePickSlipSystemStatus(@Param("pickSlipNoList") List<String> pickSlipNoList, @Param("systemStatus") Integer systemStatus);

	List<PickSlip> findAllBySystemStatus(Integer systemStatus);

	List<PickSlip> findAllByPickSlipNoIn(List<String> pickSlipNoList);

}
