package com.pickSlip.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickSlip.entity.PickSlipData;

public interface PickSlipDataRepository extends JpaRepository<PickSlipData, Long> {

	@Query(value = "SELECT DISTINCT pick_slip_no FROM pickSlip.pick_slip_data pickSlipData WHERE pick_slip_no IN(:pickSlipNos)", nativeQuery = true)
	List<String> findAllByPickSlipNoIn(@Param("pickSlipNos") Set<String> pickSlipNos);
	
	@Modifying
	@Query("UPDATE PickSlipData pickSlipData SET pickSlipData.systemStatus = :systemStatus WHERE pickSlipData.pickSlipNo IN (:pickSlipNoList)")
	void updatePickSlipSystemStatus(@Param("pickSlipNoList") List<String> pickSlipNoList, @Param("systemStatus") Integer systemStatus);
	
	@Modifying
	@Query("UPDATE PickSlipData pickSlipData SET pickSlipData.fileUploadCount = pickSlipData.fileUploadCount + :count WHERE pickSlipData.pickSlipNo = :pickSlipNo")
	void updateFileUploadCount(@Param("count") Integer count, @Param("pickSlipNo") String pickSlipNo);
	
	List<PickSlipData> findAllBySystemStatus(Integer systemStatus);

//	@Query(value = "SELECT new com.pickSlip.dto.PickSlipDataDto(pick_slip_no,operating_unit,customer_number,customer_name,order_type,order_number,order_date,"
//			+ "       promise_date,product_category,web_order_no,city,agent_code,delivery_no,pick_slip_date, "
//			+ "       pickslip_gen_time,created_by,pick_slip_updated_date,updated_by,ship_date_and_time,ship_confirm_time,ship_confirm_by, "
//			+ "       sub_inventory,dstination_sub_inventory,STATUS,system_status,system_created_on, "
//			+ "       GROUP_CONCAT(ordered_item),GROUP_CONCAT(picked_qty),GROUP_CONCAT(weight),GROUP_CONCAT(volume),GROUP_CONCAT(unit_price),GROUP_CONCAT(amount)) "
//			+ "FROM pickSlip.pick_slip_data"
//			+ "GROUP BY pick_slip_no,operating_unit,customer_number,customer_name,order_type,order_number,order_date,promise_date,product_category,web_order_no,city,agent_code,delivery_no,pick_slip_date,pickslip_gen_time,created_by,pick_slip_updated_date,updated_by,ship_date_and_time,ship_confirm_time,ship_confirm_by,sub_inventory,dstination_sub_inventory,STATUS,system_created_on,system_status ", nativeQuery = true)
//	List<PickSlipDataDto> getPickSlipData();

//	@Query(value = "SELECT new com.pickSlip.dto.PickSlipDataDto(pickSlipData.pickSlipNo,pickSlipData.operatingUnit,pickSlipData.customerNumber,pickSlipData.customerName,pickSlipData.orderType,pickSlipData.orderNumber,pickSlipData.orderDate, "
//			+ "       pickSlipData.promiseDate,pickSlipData.productCategory,pickSlipData.webOrderNo,pickSlipData.city,pickSlipData.agentCode,pickSlipData.deliveryNo,pickSlipData.pickSlipDate, "
//			+ "       pickSlipData.pickslipGenTime,pickSlipData.createdBy,pickSlipData.pickSlipUpdatedDate,pickSlipData.updatedBy,pickSlipData.shipDateAndTime,pickSlipData.shipConfirmTime,pickSlipData.shipConfirmBy, "
//			+ "       pickSlipData.subInventory,pickSlipData.dstinationSubInventory,pickSlipData.status,pickSlipData.systemStatus,pickSlipData.systemCreatedOn, "
//			+ "       GROUP_CONCAT(pickSlipData.orderedItem),GROUP_CONCAT(pickSlipData.pickedQty),GROUP_CONCAT(pickSlipData.weight),GROUP_CONCAT(pickSlipData.volume),GROUP_CONCAT(pickSlipData.unitPrice),GROUP_CONCAT(pickSlipData.amount),GROUP_CONCAT(IFNULL(locationMaster.location, 'NA'))) "
//			+ "FROM PickSlipData pickSlipData "
//			+ "LEFT JOIN LocationMaster locationMaster WITH locationMaster.itemCode = pickSlipData.orderedItem "
//			+ "GROUP BY pickSlipData.pickSlipNo,pickSlipData.operatingUnit,pickSlipData.customerNumber,pickSlipData.customerName,pickSlipData.orderType,pickSlipData.orderNumber,pickSlipData.orderDate,pickSlipData.promiseDate,pickSlipData.productCategory,pickSlipData.webOrderNo,pickSlipData.city,pickSlipData.agentCode,pickSlipData.deliveryNo,pickSlipData.pickSlipDate,pickSlipData.pickslipGenTime,pickSlipData.createdBy,pickSlipData.pickSlipUpdatedDate,pickSlipData.updatedBy,pickSlipData.shipDateAndTime,pickSlipData.shipConfirmTime,pickSlipData.shipConfirmBy,pickSlipData.subInventory,pickSlipData.dstinationSubInventory,pickSlipData.status,pickSlipData.systemStatus,pickSlipData.systemCreatedOn ")
//	List<PickSlipDataDto> getPickSlipData();

}
