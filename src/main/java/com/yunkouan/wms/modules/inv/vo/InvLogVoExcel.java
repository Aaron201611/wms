package com.yunkouan.wms.modules.inv.vo;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 库存调整日志导出模板
 * @author zwb
 *
 */
public class InvLogVoExcel {
	private int index;

	private String logTypeName;
	//invLog 对象获取
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date createTime;

	private String opTypeName;

	private String ownerName;
	//invLog 对象获取
	private String invoiceBill;

	private String locationName;

	private String skuNo;

	private String skuName;
	//invLog 对象获取
	private String batchNo;

	private String measureUnit;

	private String specModel;
	//invLog 对象获取
	private String numberWeightVolume;
	//invLog 对象获取
	private String note;

	private String opPersonName;


	public InvLogVoExcel(InvLogVO invLogVO,int index){
		if(invLogVO!=null){
			this.index=index;
			this.logTypeName=invLogVO.getLogTypeName();
			this.opTypeName=invLogVO.getOpTypeName();
			//invLog 对象获取
			this.createTime=invLogVO.getInvLog().getCreateTime();
			this.opTypeName=invLogVO.getOpTypeName();
			this.ownerName=invLogVO.getOwnerName();
			//invLog 对象获取
			this.invoiceBill=invLogVO.getInvLog().getInvoiceBill();
			this.locationName=invLogVO.getLocationName();
			this.skuNo=invLogVO.getSkuNo();
			this.skuName=invLogVO.getSkuName();
			//invLog 对象获取
			this.batchNo= invLogVO.getInvLog().getBatchNo();
			this.measureUnit=invLogVO.getMeasureUnit();
			this.specModel=invLogVO.getSpecModel();
			//invLog 对象获取
			double qty=invLogVO.getInvLog().getQty();
			double weight=invLogVO.getInvLog().getWeight();
			double volume=invLogVO.getInvLog().getVolume();
			String volumeStr=this.big(volume);

			StringBuffer sb=new StringBuffer();
			sb.append(qty).append("/")
			.append(weight).append("/")
			.append(volumeStr);

			this.numberWeightVolume=sb.toString();
			//invLog 对象获取
			this.note=invLogVO.getInvLog().getNote();
			this.opPersonName=invLogVO.getOpPersonName();
		}
	}
	private  String big(double d) {
		BigDecimal bg=new BigDecimal(d);
		return bg.divide(new BigDecimal(1), 4, BigDecimal.ROUND_HALF_UP).toString();
	}

	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	public String getLogTypeName() {
		return logTypeName;
	}



	public void setLogTypeName(String logTypeName) {
		this.logTypeName = logTypeName;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}



	public String getOpTypeName() {
		return opTypeName;
	}



	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}



	public String getOwnerName() {
		return ownerName;
	}



	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}



	public String getInvoiceBill() {
		return invoiceBill;
	}



	public void setInvoiceBill(String invoiceBill) {
		this.invoiceBill = invoiceBill;
	}



	public String getLocationName() {
		return locationName;
	}



	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}



	public String getSkuNo() {
		return skuNo;
	}



	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}



	public String getSkuName() {
		return skuName;
	}



	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}



	public String getBatchNo() {
		return batchNo;
	}



	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}



	public String getMeasureUnit() {
		return measureUnit;
	}



	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}



	public String getSpecModel() {
		return specModel;
	}



	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}



	public String getNumberWeightVolume() {
		return numberWeightVolume;
	}



	public void setNumberWeightVolume(String numberWeightVolume) {
		this.numberWeightVolume = numberWeightVolume;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getOpPersonName() {
		return opPersonName;
	}



	public void setOpPersonName(String opPersonName) {
		this.opPersonName = opPersonName;
	}
}
