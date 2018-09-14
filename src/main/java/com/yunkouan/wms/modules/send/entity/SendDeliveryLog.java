package com.yunkouan.wms.modules.send.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;


/**
 * The persistent class for the send_delivery_log database table.
 * 
 */
@Entity
@Table(name="send_delivery_log")
@NamedQuery(name="SendDeliveryLog.findAll", query="SELECT s FROM SendDeliveryLog s")
public class SendDeliveryLog extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

    @NotNull(message="{valid_delivery_id_isnull}",groups={ValidSave.class,ValidUpdate.class})
	@Column(name="delivery_id")
	private String deliveryId;

    @NotNull(message="{valid_log_type_isnull}",groups={ValidSave.class,ValidUpdate.class})
	@Column(name="log_type")
	private String logType;

	private String note;

	@Column(name="op_person")
	private String opPerson;

    @NotNull(message="{valid_org_id_isnull}",groups={ValidSave.class,ValidUpdate.class})
	@Column(name="org_id")
	private String orgId;

    @NotNull(message="{valid_warehouseId_isnull}",groups={ValidSave.class,ValidUpdate.class})
	@Column(name="warehouse_id")
	private String warehouseId;

	public SendDeliveryLog() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeliveryId() {
		return this.deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getLogType() {
		return this.logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOpPerson() {
		return this.opPerson;
	}

	public void setOpPerson(String opPerson) {
		this.opPerson = opPerson;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getWarehouseId() {
		return this.warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

}