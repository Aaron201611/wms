package com.yunkouan.wms.modules.meta.entity;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;

/**
 * 业务员实体类
 */
public class MetaSalesman extends BaseEntity {
	private static final long serialVersionUID = -3786589406112454534L;

	/**业务员id*/
	@Id
	private String salesmanId;

	/**业务员代码*/
	@Length(max=32,message="{valid_salesman_salesmanNo_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_salesman_salesmanNo_notnull}",groups={ValidUpdate.class})
    private String salesmanNo;

    /**业务员名称*/
	@Length(max=32,message="{valid_salesman_salesmanName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
	@NotNull(message="{valid_salesman_salesmanName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String salesmanName;


    /**业务员状态*/
    private Integer salesmanStatus;
    
    /**手机号*/
  	@Length(max=11,message="{valid_salesman_salesmanName_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
  	@NotNull(message="{valid_salesman_salesmanName_notnull}",groups={ValidSave.class,ValidUpdate.class})
    private String mobile;
  	
  	/**
  	 * 性别0：男，1：女
  	 */
  	private Integer sex;

    /**所属部门*/
    private String departmentId;
    
    private String departmentName;
    
	@Length(max=512,message="{valid_salesman_note_length}",groups={ValidSave.class,ValidUpdate.class,ValidSearch.class})
    private String note;

    @Override
	public void clear() {
		super.clear();
	}
    

	/**
	获取手机号  
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	



	/**
	 * @param 手机号 the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	



	/**
	获取性别0：男，1：女  
	 * @return the sex
	 */
	public Integer getSex() {
		return sex;
	}
	



	/**
	 * @param 性别0：男，1：女 the sex to set
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	



	/**
	获取业务员id  
	 * @return the salesmanId
	 */
	public String getSalesmanId() {
		return salesmanId;
	}
	

	/**
	 * @param 业务员id the salesmanId to set
	 */
	public void setSalesmanId(String salesmanId) {
		this.salesmanId = salesmanId;
	}
	

	/**
	获取业务员代码  
	 * @return the salesmanNo
	 */
	public String getSalesmanNo() {
		return salesmanNo;
	}
	

	/**
	 * @param 业务员代码 the salesmanNo to set
	 */
	public void setSalesmanNo(String salesmanNo) {
		this.salesmanNo = salesmanNo;
	}
	

	/**
	获取业务员名称  
	 * @return the salesmanName
	 */
	public String getSalesmanName() {
		return salesmanName;
	}
	

	/**
	 * @param 业务员名称 the salesmanName to set
	 */
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	

	/**
	获取业务员状态  
	 * @return the salesmanStatus
	 */
	public Integer getSalesmanStatus() {
		return salesmanStatus;
	}
	

	/**
	 * @param 业务员状态 the salesmanStatus to set
	 */
	public void setSalesmanStatus(Integer salesmanStatus) {
		this.salesmanStatus = salesmanStatus;
	}
	

	/**
	获取所属部门  
	 * @return the department
	

	/**
	获取note  
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	

	/**
	获取所属部门  
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	

	/**
	 * @param 所属部门 the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}


	/**
	获取departmentName  
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	


	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	
}