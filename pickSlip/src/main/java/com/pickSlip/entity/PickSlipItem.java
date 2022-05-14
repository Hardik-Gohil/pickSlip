package com.pickSlip.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pickSlip.utility.PickSlipUtility;

import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "pick_slip_item")
@Data
public class PickSlipItem implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pickSlipItemId;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pick_slip_id")
	private PickSlip pickSlip;

	@Column(length = 510)
	private String orderedItem;

	private BigInteger pickedQty;

	private BigDecimal weight;

	private BigDecimal volume;

	private BigDecimal unitPrice;

	private BigDecimal amount;

	private String subInventory;

	@Transient
	@Getter(lombok.AccessLevel.NONE)
	private String location;

	@Transient
	@JsonIgnore
	private String srNo;

	@Transient
	@JsonIgnore
	@Getter(lombok.AccessLevel.NONE)
	private String masterPcs;

	@Transient
	@JsonIgnore
	@Getter(lombok.AccessLevel.NONE)
	private String innerPcs;

	@Transient
	@JsonIgnore
	@Getter(lombok.AccessLevel.NONE)
	private String masterInnerLoosePcs;
	
	@Transient
	@JsonIgnore
	@Getter(lombok.AccessLevel.NONE)
	private LocationMaster locationMaster;

	public LocationMaster getLocationMaster() {
		if (ObjectUtils.isEmpty(locationMaster)) {
			this.locationMaster = PickSlipUtility.getLocationMaster(this.orderedItem);
		}
		return locationMaster;
	}

	public String getLocation() {
		if (ObjectUtils.isEmpty(this.location)) {
			this.location = !ObjectUtils.isEmpty(getLocationMaster()) ? getLocationMaster().getLocation() : "";
		}
		return location;
	}
	
	public String getMasterPcs() {
		if (ObjectUtils.isEmpty(this.masterPcs)) {
			this.masterPcs = !ObjectUtils.isEmpty(getLocationMaster()) ? getLocationMaster().getMasterPcs().toString() : "";
		}
		return masterPcs;
	}

	public String getInnerPcs() {
		if (ObjectUtils.isEmpty(this.innerPcs)) {
			this.innerPcs = !ObjectUtils.isEmpty(getLocationMaster()) ? getLocationMaster().getInnerPcs().toString() : "";
		}
		return innerPcs;
	}

	public String getMasterInnerLoosePcs() {
		if (ObjectUtils.isEmpty(this.masterInnerLoosePcs) && !ObjectUtils.isEmpty(locationMaster)) {
			BigInteger remainingPickedQty = this.pickedQty;
			String temp = "";
			if (!ObjectUtils.isEmpty(getLocationMaster()) && getLocationMaster().getMasterPcs() != BigInteger.ZERO) {
				BigInteger master = remainingPickedQty.divide(getLocationMaster().getMasterPcs());
				if (master != BigInteger.ZERO) {
					temp += master + " / ";
					remainingPickedQty = remainingPickedQty.subtract(master.multiply(getLocationMaster().getMasterPcs()));
				} else {
					temp += "0 / ";
				}

			} else {
				temp += "0 / ";
			}

			if (!ObjectUtils.isEmpty(getLocationMaster()) && getLocationMaster().getInnerPcs() != BigInteger.ZERO) {
				BigInteger inner = remainingPickedQty.divide(getLocationMaster().getInnerPcs());
				if (inner != BigInteger.ZERO) {
					temp += inner + " / ";
					remainingPickedQty = remainingPickedQty.subtract(inner.multiply(getLocationMaster().getInnerPcs()));
				} else {
					temp += "0 / ";
				}

			} else {
				temp += "0 / ";
			}
			temp += remainingPickedQty;
			this.masterInnerLoosePcs = temp;
		} else if (ObjectUtils.isEmpty(this.masterInnerLoosePcs)) {
			this.masterInnerLoosePcs = "";
		}
		return masterInnerLoosePcs;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
