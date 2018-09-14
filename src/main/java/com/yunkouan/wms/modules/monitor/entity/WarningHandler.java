package com.yunkouan.wms.modules.monitor.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;

public class WarningHandler extends BaseEntity {
	/**
	 * 
	 * @version 2017年3月13日 下午1:55:59<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7609792741588444684L;

	@Id
    private String warningHandlerId;
	
    private String skuId;
    
    private String skuNo;
    
    private String skuName;
    
    private String batchNo;
    
    private String specModel;
    
    private String measureUnit;
    
    /**
     * 过期日期
     */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date expireDate;
    
    /**
     * 预警日期
     */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private Date warningDate;

    /**
     * 处理情况
     */
    @NotNull(message="{valid_lift_warn_handle_msg_is_null}",groups={ValidUpdate.class})
    @Length(max=512,message="{valid_lift_warn_handle_msg_length}",groups={ValidUpdate.class})
    private String handleMsg;

    private String orgId;

    private String warehouseId;

    /**
	 * 构造方法
	 * @param shelflifeWarningVO
	 * @version 2017年9月5日 下午4:15:51<br/>
	 * @author 王通<br/>
	 */
	public WarningHandler(ShelflifeWarningVO shelflifeWarningVO) {
		this.batchNo = shelflifeWarningVO.getBatchNo();
		this.expireDate = shelflifeWarningVO.getExpireDate();
		this.measureUnit = shelflifeWarningVO.getMeasureUnit();
		this.skuId = shelflifeWarningVO.getSkuId();
		this.skuNo = shelflifeWarningVO.getSkuNo();
		this.skuName = shelflifeWarningVO.getSkuName();
		this.specModel = shelflifeWarningVO.getSpecModel();
		this.warningDate = shelflifeWarningVO.getWarningDate();
	}

	/**
	 * 构造方法
	 * @version 2017年9月5日 下午4:20:39<br/>
	 * @author 王通<br/>
	 */
	public WarningHandler() {
	}

	public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
    }

	/**
	 * 属性 handleMsg getter方法
	 * @return 属性handleMsg
	 * @author 王通<br/>
	 */
	public String getHandleMsg() {
		return handleMsg;
	}

	/**
	 * 属性 handleMsg setter方法
	 * @param handleMsg 设置属性handleMsg的值
	 * @author 王通<br/>
	 */
	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	/**
	 * 属性 warningDate getter方法
	 * @return 属性warningDate
	 * @author 王通<br/>
	 */
	public Date getWarningDate() {
		return warningDate;
	}

	/**
	 * 属性 warningDate setter方法
	 * @param warningDate 设置属性warningDate的值
	 * @author 王通<br/>
	 */
	public void setWarningDate(Date warningDate) {
		this.warningDate = warningDate;
	}

	/**
	 * 属性 expireDate getter方法
	 * @return 属性expireDate
	 * @author 王通<br/>
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * 属性 expireDate setter方法
	 * @param expireDate 设置属性expireDate的值
	 * @author 王通<br/>
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * 属性 measureUnit getter方法
	 * @return 属性measureUnit
	 * @author 王通<br/>
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * 属性 measureUnit setter方法
	 * @param measureUnit 设置属性measureUnit的值
	 * @author 王通<br/>
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * 属性 specModel getter方法
	 * @return 属性specModel
	 * @author 王通<br/>
	 */
	public String getSpecModel() {
		return specModel;
	}

	/**
	 * 属性 specModel setter方法
	 * @param specModel 设置属性specModel的值
	 * @author 王通<br/>
	 */
	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	/**
	 * 属性 batchNo getter方法
	 * @return 属性batchNo
	 * @author 王通<br/>
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 属性 batchNo setter方法
	 * @param batchNo 设置属性batchNo的值
	 * @author 王通<br/>
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 属性 skuName getter方法
	 * @return 属性skuName
	 * @author 王通<br/>
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * 属性 skuName setter方法
	 * @param skuName 设置属性skuName的值
	 * @author 王通<br/>
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * 属性 skuId getter方法
	 * @return 属性skuId
	 * @author 王通<br/>
	 */
	public String getSkuId() {
		return skuId;
	}

	/**
	 * 属性 skuId setter方法
	 * @param skuId 设置属性skuId的值
	 * @author 王通<br/>
	 */
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	/**
	 * 属性 skuNo getter方法
	 * @return 属性skuNo
	 * @author 王通<br/>
	 */
	public String getSkuNo() {
		return skuNo;
	}

	/**
	 * 属性 skuNo setter方法
	 * @param skuNo 设置属性skuNo的值
	 * @author 王通<br/>
	 */
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	/**
	 * 属性 warningHandlerId getter方法
	 * @return 属性warningHandlerId
	 * @author 王通<br/>
	 */
	public String getWarningHandlerId() {
		return warningHandlerId;
	}

	/**
	 * 属性 warningHandlerId setter方法
	 * @param warningHandlerId 设置属性warningHandlerId的值
	 * @author 王通<br/>
	 */
	public void setWarningHandlerId(String warningHandlerId) {
		this.warningHandlerId = warningHandlerId;
	}
}