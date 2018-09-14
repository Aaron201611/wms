package com.yunkouan.wms.common.entity;

import javax.persistence.Id;
import com.yunkouan.base.BaseObj;

/**
 * @function 单据编号数据层接口
 * @author tphe06
 */
public class Sequence extends BaseObj {
	private static final long serialVersionUID = 7887891784649586244L;

	@Id
	private Integer id;
	private String orgId;
	private String warehouseId;
	private String seqName;
	private Integer currentValue;
	private Integer sIncrement;
	private String currentValueTxt;

	public Integer getId() {
		return id;
	}
	public String getOrgId() {
		return orgId;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public String getSeqName() {
		return seqName;
	}
	public Integer getCurrentValue() {
		return currentValue;
	}
	public Integer getsIncrement() {
		return sIncrement;
	}
	public String getCurrentValueTxt() {
		return currentValueTxt;
	}
	public Sequence setId(Integer id) {
		this.id = id;
		return this;
	}
	public Sequence setOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}
	public Sequence setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
		return this;
	}
	public Sequence setSeqName(String seqName) {
		this.seqName = seqName;
		return this;
	}
	public Sequence setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
		return this;
	}
	public Sequence setsIncrement(Integer sIncrement) {
		this.sIncrement = sIncrement;
		return this;
	}
	public Sequence setCurrentValueTxt(String currentValueTxt) {
		this.currentValueTxt = currentValueTxt;
		return this;
	}
}