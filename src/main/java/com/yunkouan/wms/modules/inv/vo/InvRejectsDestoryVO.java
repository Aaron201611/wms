/**
 * Created on 2017年2月16日
 * InvRejectsDestoryVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.Date;
import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestory;

/**
 * 不良品销毁单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvRejectsDestoryVO extends BaseVO {

	/**
	 * 
	 * @version 2017年2月16日 上午10:06:30<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7066852346579800875L;

	/**
	 * 
	 * 构造方法
	 * @version 2017年2月16日 上午10:06:17<br/>
	 * @author 王通<br/>
	 */
	public InvRejectsDestoryVO(){}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvRejectsDestoryVO(InvRejectsDestory invRejectsDestory){
		this.invRejectsDestory = invRejectsDestory;
	}

	/**
	 * 不良品销毁单对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	private InvRejectsDestory invRejectsDestory;
	
	/**
	 * 请求参数：是否修改申请单号
	 */
	private Boolean changeFormNo;
	
	/**
	 * 创建人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserName;
	/**
	 * 修改人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String updateUserName;
	
	/**
	 * 申报人员
	 * @version 2017年3月8日 上午10:59:02<br/>
	 * @author 王通<br/>
	 */
	private String applyPersonName;

	/**
	 * 页面显示不良品销毁单明细VO集合
	 * @version 2017年2月16日10:11:40<br/>
	 * @author 王通<br/>
	 */
	private List<InvRejectsDestoryDetailVO> listInvRejectsDestoryDetailVO;
	/**
	 * 不良品销毁单状态
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String statusName;
	
	/**
	 * 批量操作（如生效失效）时的id集合
	 * @version 2017年7月20日 上午10:14:24<br/>
	 * @author 王通<br/>
	 */
	private List<String> idList;
	
	private Date startOpDate;
	private Date endOpDate;
	private Date startApplyDate;
	private Date endApplyDate;
	private String formNoLike;
	
	
	public InvRejectsDestory getInvRejectsDestory() {
		return invRejectsDestory;
	}
	

	public void setInvRejectsDestory(InvRejectsDestory invRejectsDestory) {
		this.invRejectsDestory = invRejectsDestory;
	}
	

	public String getCreateUserName() {
		return createUserName;
	}
	

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	

	public String getUpdateUserName() {
		return updateUserName;
	}
	

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	
	public List<InvRejectsDestoryDetailVO> getListInvRejectsDestoryDetailVO() {
		return listInvRejectsDestoryDetailVO;
	}
	

	public void setListInvRejectsDestoryDetailVO(List<InvRejectsDestoryDetailVO> listInvRejectsDestoryDetailVO) {
		this.listInvRejectsDestoryDetailVO = listInvRejectsDestoryDetailVO;
	}
	

	public String getStatusName() {
		return statusName;
	}
	

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	

	public List<String> getIdList() {
		return idList;
	}
	

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

	public Boolean getChangeFormNo() {
		return changeFormNo;
	}

	public void setChangeFormNo(Boolean changeFormNo) {
		this.changeFormNo = changeFormNo;
	}

	public Date getStartOpDate() {
		return startOpDate;
	}
	

	public void setStartOpDate(Date startOpDate) {
		this.startOpDate = startOpDate;
	}
	

	public Date getEndOpDate() {
		return endOpDate;
	}
	

	public void setEndOpDate(Date endOpDate) {
		this.endOpDate = endOpDate;
	}
	

	public Date getStartApplyDate() {
		return startApplyDate;
	}
	

	public void setStartApplyDate(Date startApplyDate) {
		this.startApplyDate = startApplyDate;
	}
	

	public Date getEndApplyDate() {
		return endApplyDate;
	}
	

	public void setEndApplyDate(Date endApplyDate) {
		this.endApplyDate = endApplyDate;
	}

	public String getFormNoLike() {
		return formNoLike;
	}

	public void setFormNoLike(String formNoLike) {
		this.formNoLike = formNoLike;
	}

	public String getApplyPersonName() {
		return applyPersonName;
	}

	public void setApplyPersonName(String applyPersonName) {
		this.applyPersonName = applyPersonName;
	}
	
	


	
	
}
