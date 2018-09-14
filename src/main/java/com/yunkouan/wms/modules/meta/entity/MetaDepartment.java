package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 部门
 */
public class MetaDepartment extends BaseEntity {
	private static final long serialVersionUID = -6339910864133681162L;

	/**部门id*/
	@Id
	private String departmentId;

	/**部门编码*/
	@Length(max=32,message="{valid_skuType_skuTypeNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_skuType_skuTypeNo_notnull}",groups={ValidUpdate.class})
    private String departmentNo;

    /**部门名称*/
	@Length(max=16,message="{valid_skuType_skuTypeName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_skuType_skuTypeName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String departmentName;

    /**部门状态*/
	@NotNull(message="{valid_skuType_skuTypeStatus_notnull}",groups={ValidUpdate.class})
    private Integer departmentStatus;

	/**@Fields 备注 */
	private String note;
	/**
	 * 部门负责人
	 */
	private String principal;
	/**
	 * 负责人联系方式
	 */
	private String mobile;
	/**
	获取部门id  
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	
	/**
	 * @param 部门id the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	
	/**
	获取部门编码  
	 * @return the departmentNo
	 */
	public String getDepartmentNo() {
		return departmentNo;
	}
	
	/**
	 * @param 部门编码 the departmentNo to set
	 */
	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}
	
	/**
	获取部门名称  
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	
	/**
	 * @param 部门名称 the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	/**
	获取部门状态  
	 * @return the departmentStatus
	 */
	public Integer getDepartmentStatus() {
		return departmentStatus;
	}
	
	/**
	 * @param 部门状态 the departmentStatus to set
	 */
	public void setDepartmentStatus(Integer departmentStatus) {
		this.departmentStatus = departmentStatus;
	}
	
	/**
	获取@Fields备注  
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * @param @Fields备注 the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	获取部门负责人  
	 * @return the principal
	 */
	public String getPrincipal() {
		return principal;
	}
	

	/**
	 * @param 部门负责人 the principal to set
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	

	/**
	获取负责人联系方式  
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	

	/**
	 * @param 负责人联系方式 the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}