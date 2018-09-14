package com.yunkouan.wms.common.vo;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.constant.BillPrefix;

/**
 * 通用服务VO类
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午5:47:54<br/>
 * @author 王通<br/>
 */
public class CommonVo  extends BaseVO  {
    /**
	 * @version 2017年2月17日 下午5:46:34<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 2767972757342339003L;

	private String id;
	/**
     * 机构ID
     */
    private String orgId;
    /**
     * 仓库ID
     */
    private String warehouseId;
    /**
     * 字段名称（前缀）
     */
	private String fieldName;
	/**
	 * 是否包含日期
	 */
	private Boolean containDate = true;

	/**
	 * 
	 * 构造方法
	 * @param orgId 机构id
	 * @param fieldName 字段名称（前缀）
	 * @version 2017年2月17日 下午2:37:52<br/>
	 * @author 王通<br/>
	 */
    public CommonVo(String orgId, BillPrefix prefix) {
    	this.setOrgId(orgId);
    	this.setFieldName(prefix.getPrefix());
    }
	/**
	 * 
	 * 构造方法
	 * @param orgId 机构id
	 * @param fieldName 字段名称（前缀）
	 * @version 2017年2月17日 下午2:37:52<br/>
	 * @author 王通<br/>
	 */
    public CommonVo(String orgId, BillPrefix prefix, Boolean containDate) {
    	this.setOrgId(orgId);
    	this.setFieldName(prefix.getPrefix());
    	this.setContainDate(containDate);
    }

	/**
	 * 属性 orgId getter方法
	 * @return 属性orgId
	 * @author 王通<br/>
	 */
    public String getOrgId() {
		return orgId;
	}

	/**
	 * 属性 orgId setter方法
	 * @param orgId 设置属性orgId的值
	 * @author 王通<br/>
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 属性 fieldName getter方法
	 * @return 属性fieldName
	 * @author 王通<br/>
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 属性 fieldName setter方法
	 * @param fieldName 设置属性fieldName的值
	 * @author 王通<br/>
	 */
	private void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 属性 warehouseId getter方法
	 * @return 属性warehouseId
	 * @author 王通<br/>
	 */
	public String getWarehouseId() {
		return warehouseId;
	}

	/**
	 * 属性 warehouseId setter方法
	 * @param warehouseId 设置属性warehouseId的值
	 * @author 王通<br/>
	 */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	/**
	 * 属性 containDate getter方法
	 * @return 属性containDate
	 * @author 王通<br/>
	 */
	public Boolean getContainDate() {
		return containDate;
	}
	/**
	 * 属性 containDate setter方法
	 * @param containDate 设置属性containDate的值
	 * @author 王通<br/>
	 */
	public void setContainDate(Boolean containDate) {
		this.containDate = containDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}