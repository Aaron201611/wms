/**
 * Created on 2017年2月16日
 * InvShiftVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.inv.vo;

import java.util.List;

import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.inv.entity.InvShift;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 移位单VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class InvShiftVO extends BaseVO {

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
	public InvShiftVO(){
		this.invShift = new InvShift();
	}
	
	/**
	 * 
	 * 构造方法
	 * @param shift
	 * @version 2017年2月16日 上午10:07:21<br/>
	 * @author 王通<br/>
	 */
	public InvShiftVO(InvShift invShift){
		this.invShift = invShift;
	}

	/**
	 * 移位单对象
	 * @version 2017年2月16日 上午10:07:48<br/>
	 * @author 王通<br/>
	 */
	private InvShift invShift;
	
	
	/**
	 * 创建人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String createUserName;
	/**
	 * 移入库位查询条件
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String inLocationName;
	/**
	 * 移出库位查询条件
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String outLocationName;
	/**
	 * 修改人姓名
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String updateUserName;
	
	/**
	 * 作业人员
	 * @version 2017年3月8日 上午10:59:02<br/>
	 * @author 王通<br/>
	 */
	private String opPersonName;

	/**
	 * 移位类型名称
	 * @version 2017年2月16日 上午11:39:36<br/>
	 * @author 王通<br/>
	 */
	private String shiftTypeName;
	/**
	 * 页面显示移位单明细VO集合
	 * @version 2017年2月16日10:11:40<br/>
	 * @author 王通<br/>
	 */
	private List<InvShiftDetailVO> listInvShiftDetailVO;
	/**
	 * 移位单单号
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String shiftNo;
	/**
	 * 移位单状态，仅供查找预分配时使用
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String shiftStatus;
	/**
	 * 移位单状态
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private String shiftStatusName;
	/**
	 * 状态列表
	 * @version 2017年2月16日10:11:40<br/>
	 * @author 王通<br/>
	 */
	private List<Integer> listInvShiftStatus;
	
	/**
	 * 预分配总数
	 * @version 2017年3月20日 下午4:08:55<br/>
	 * @author 王通<br/>
	 */
	private Double totalQty;
	/**
	 * 批量操作（如生效失效）时的id集合
	 * @version 2017年7月20日 上午10:14:24<br/>
	 * @author 王通<br/>
	 */
	private List<String> shiftIdList;
	/**
	 * 任务列表VO
	 * @version 2017年7月20日 上午9:58:38<br/>
	 * @author 王通<br/>
	 */
	private TsTaskVo tsTaskVo = new TsTaskVo();
	/* getset *************************************************/


	//策略实现
	public double remindQty;
	

	/**
	 * 属性 createUserName getter方法
	 * @return 属性createUserName
	 * @author 王通<br/>
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * 属性 createUserName setter方法
	 * @param createUserName 设置属性createUserName的值
	 * @author 王通<br/>
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * 属性 updateUserName getter方法
	 * @return 属性updateUserName
	 * @author 王通<br/>
	 */
	public String getUpdateUserName() {
		return updateUserName;
	}

	/**
	 * 属性 updateUserName setter方法
	 * @param updateUserName 设置属性updateUserName的值
	 * @author 王通<br/>
	 */
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	/**
	 * 属性 listInvShiftDetailVO getter方法
	 * @return 属性listInvShiftDetailVO
	 * @author 王通<br/>
	 */
	public List<InvShiftDetailVO> getListInvShiftDetailVO() {
		return listInvShiftDetailVO;
	}
	/**
	 * 属性 listInvShiftDetailVO setter方法
	 * @param listInvShiftDetailVO 设置属性listInvShiftDetailVO的值
	 * @author 王通<br/>
	 */
	public void setListInvShiftDetailVO(List<InvShiftDetailVO> listInvShiftDetailVO) {
		this.listInvShiftDetailVO = listInvShiftDetailVO;
	}



	/* method ********************************************/
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public InvShiftVO searchCache () {
		return this;
	}
	
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年2月16日10:20:03<br/>
	 * @author 王通<br/>
	 */
	public void setLoginUser( Principal loginUser ) {
		if ( loginUser == null || this.invShift == null ) {
			return;
		}
		this.invShift.setOrgId(loginUser.getOrgId());
		this.invShift.setWarehouseId(LoginUtil.getWareHouseId());
	}

	/**
	 * @param invShift
	 * @return
	 * @Description 
	 * @version 2017年2月16日 上午10:47:07<br/>
	 * @author 王通<br/>
	 */
	public InvShiftVO setEntity(InvShift invShift) {
		this.invShift = invShift;
		return this;
	}
	/*
	  * <p>Title: getExample</p>
	  * <p>Description: </p>
	  * @return
	  * @see com.yunkouan.base.BaseVO#getExample()
	  */
	
	
	@Override
	public Example getExample() {
		Example example = new Example(InvShift.class);
		example.setOrderByClause("shift_id2 desc");
		Criteria c = example.createCriteria();
		c.andEqualTo("warehouseId", this.invShift.getWarehouseId());
		c.andEqualTo("orgId", this.invShift.getOrgId());
//		if(!StringUtil.isTrimEmpty(this.getInLocationName())) {
//			c.andEqualTo("inLocation", this.getInLocationName());
//		}
//		if(!StringUtil.isTrimEmpty(this.getOutLocationName())) {
//			c.andEqualTo("outLocation", this.getOutLocationName());
//		}
		if(invShift.getShiftStatus() != null) {
			c.andEqualTo("shiftStatus", invShift.getShiftStatus());
		}
		return example;
	}

	/**
	 * 属性 listInvShiftStatus getter方法
	 * @return 属性listInvShiftStatus
	 * @author 王通<br/>
	 */
	public List<Integer> getListInvShiftStatus() {
		return listInvShiftStatus;
	}

	/**
	 * 属性 listInvShiftStatus setter方法
	 * @param listInvShiftStatus 设置属性listInvShiftStatus的值
	 * @author 王通<br/>
	 */
	public void setListInvShiftStatus(List<Integer> listInvShiftStatus) {
		this.listInvShiftStatus = listInvShiftStatus;
	}


	public Double getTotalQty() {
		return totalQty;
	}
	

	public void setTotalQty(Double totalQty) {
		this.totalQty = totalQty;
	}
	

	/**
	 * 属性 shiftNo getter方法
	 * @return 属性shiftNo
	 * @author 王通<br/>
	 */
	public String getShiftNo() {
		return shiftNo;
	}

	/**
	 * 属性 shiftNo setter方法
	 * @param shiftNo 设置属性shiftNo的值
	 * @author 王通<br/>
	 */
	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	/**
	 * 属性 shiftStatusName getter方法
	 * @return 属性shiftStatusName
	 * @author 王通<br/>
	 */
	public String getShiftStatusName() {
		return shiftStatusName;
	}

	/**
	 * 属性 shiftStatusName setter方法
	 * @param shiftStatusName 设置属性shiftStatusName的值
	 * @author 王通<br/>
	 */
	public void setShiftStatusName(String shiftStatusName) {
		this.shiftStatusName = shiftStatusName;
	}

	/**
	 * 属性 shiftTypeName getter方法
	 * @return 属性shiftTypeName
	 * @author 王通<br/>
	 */
	public String getShiftTypeName() {
		return shiftTypeName;
	}

	/**
	 * 属性 shiftTypeName setter方法
	 * @param shiftTypeName 设置属性shiftTypeName的值
	 * @author 王通<br/>
	 */
	public void setShiftTypeName(String shiftTypeName) {
		this.shiftTypeName = shiftTypeName;
	}

	/**
	 * 属性 opPersonName getter方法
	 * @return 属性opPersonName
	 * @author 王通<br/>
	 */
	public String getOpPersonName() {
		return opPersonName;
	}

	/**
	 * 属性 opPersonName setter方法
	 * @param opPersonName 设置属性opPersonName的值
	 * @author 王通<br/>
	 */
	public void setOpPersonName(String opPersonName) {
		this.opPersonName = opPersonName;
	}

	/**
	 * 属性 inLocationName getter方法
	 * @return 属性inLocationName
	 * @author 王通<br/>
	 */
	public String getInLocationName() {
		return inLocationName;
	}

	/**
	 * 属性 inLocationName setter方法
	 * @param inLocationName 设置属性inLocationName的值
	 * @author 王通<br/>
	 */
	public void setInLocationName(String inLocationName) {
		this.inLocationName = inLocationName;
	}

	/**
	 * 属性 outLocationName getter方法
	 * @return 属性outLocationName
	 * @author 王通<br/>
	 */
	public String getOutLocationName() {
		return outLocationName;
	}

	/**
	 * 属性 outLocationName setter方法
	 * @param outLocationName 设置属性outLocationName的值
	 * @author 王通<br/>
	 */
	public void setOutLocationName(String outLocationName) {
		this.outLocationName = outLocationName;
	}

	/**
	 * 属性 invShift getter方法
	 * @return 属性invShift
	 * @author 王通<br/>
	 */
	public InvShift getInvShift() {
		return invShift;
	}

	/**
	 * 属性 invShift setter方法
	 * @param invShift 设置属性invShift的值
	 * @author 王通<br/>
	 */
	public void setInvShift(InvShift invShift) {
		this.invShift = invShift;
	}

	/**
	 * 属性 shiftStatus getter方法
	 * @return 属性shiftStatus
	 * @author 王通<br/>
	 */
	public String getShiftStatus() {
		return shiftStatus;
	}

	/**
	 * 属性 shiftStatus setter方法
	 * @param shiftStatus 设置属性shiftStatus的值
	 * @author 王通<br/>
	 */
	public void setShiftStatus(String shiftStatus) {
		this.shiftStatus = shiftStatus;
	}

	/**
	 * 属性 tsTaskVo getter方法
	 * @return 属性tsTaskVo
	 * @author 王通<br/>
	 */
	public TsTaskVo getTsTaskVo() {
		return tsTaskVo;
	}

	/**
	 * 属性 tsTaskVo setter方法
	 * @param tsTaskVo 设置属性tsTaskVo的值
	 * @author 王通<br/>
	 */
	public void setTsTaskVo(TsTaskVo tsTaskVo) {
		this.tsTaskVo = tsTaskVo;
	}

	/**
	 * 属性 shiftIdList getter方法
	 * @return 属性shiftIdList
	 * @author 王通<br/>
	 */
	public List<String> getShiftIdList() {
		return shiftIdList;
	}

	/**
	 * 属性 shiftIdList setter方法
	 * @param shiftIdList 设置属性shiftIdList的值
	 * @author 王通<br/>
	 */
	public void setShiftIdList(List<String> shiftIdList) {
		this.shiftIdList = shiftIdList;
	}
	
	
}
