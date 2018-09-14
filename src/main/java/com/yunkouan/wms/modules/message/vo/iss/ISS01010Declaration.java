package com.yunkouan.wms.modules.message.vo.iss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="Declaration",namespace="")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"logistisId","ieType","lineName","sortingLineStatus","shipNameEn","voyageNo","billNo","remark"})
public class ISS01010Declaration {
	
	private String logistisId;
	
	private String ieType;
	
	private String lineName;
	
	private String sortingLineStatus;
	
	private String shipNameEn;
	
	private String voyageNo;
	
	private String billNo;
	
	private String remark;

	@XmlElement(name="LogisticsID")
	public String getLogistisId() {
		return logistisId;
	}

	public void setLogistisId(String logistisId) {
		this.logistisId = logistisId;
	}

	@XmlElement(name="IEType")
	public String getIeType() {
		return ieType;
	}

	public void setIeType(String ieType) {
		this.ieType = ieType;
	}

	@XmlElement(name="LineName")
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	@XmlElement(name="SortingLineStatus")
	public String getSortingLineStatus() {
		return sortingLineStatus;
	}

	public void setSortingLineStatus(String sortingLineStatus) {
		this.sortingLineStatus = sortingLineStatus;
	}

	@XmlElement(name="ShipNameEn")
	public String getShipNameEn() {
		return shipNameEn;
	}

	public void setShipNameEn(String shipNameEn) {
		this.shipNameEn = shipNameEn;
	}

	@XmlElement(name="VoyageNo")
	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	@XmlElement(name="BillNo")
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@XmlElement(name="Remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
