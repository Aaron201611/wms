package com.yunkouan.wms.modules.send.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import java.util.Date;

/**
 * The persistent class for the sys_express_no_pool database table.
 * 快递单号池entity
 */
@Entity
@Table(name="sys_express_no_pool")
public class SysExpressNoPool extends BaseEntity {
	private static final long serialVersionUID = 4342428730357972523L;

	@Id
	private String id;

	/**运单号**/
	@Column(name="express_bill_no")
	private String expressBillNo;

	/**快递公司代码**/
	@Column(name="express_service_code")
	private String expressServiceCode;

	/**是否已经使用**/
	@Column(name="is_used")
	private Boolean isUsed;

	@Column(name="org_id")
	private String orgId;

	/**使用时间**/
	@Column(name="use_time")
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", locale = "zh", timezone="GMT+8")
	private Date useTime;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpressBillNo() {
		return this.expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public String getExpressServiceCode() {
		return this.expressServiceCode;
	}

	public void setExpressServiceCode(String expressServiceCode) {
		this.expressServiceCode = expressServiceCode;
	}

	public Boolean getIsUsed() {
		return this.isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getUseTime() {
		return this.useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
}