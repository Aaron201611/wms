/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月20日 下午2:53:18<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.PubUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.entity.RecPutawayDetail;
import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.util.valid.ValidConfirm;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 上架单VO<br/><br/>
 * @version 2017年2月20日 下午2:53:18<br/>
 * @author andy wang<br/>
 */
public class RecPutawayVO extends BaseVO {

	private static final long serialVersionUID = 8690375128511224800L;
	
	/**
	 * 构造方法
	 * @version 2017年2月20日 下午3:04:06<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO(){
		this( new RecPutaway() );
	}
	
	/**
	 * 构造方法
	 * @param putaway 上架单对象
	 * @version 2017年2月20日 下午3:04:10<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO( RecPutaway putaway ){
		this.putaway = putaway;
	}
	
	/**
	 * 上架单对象
	 * @version 2017年2月20日下午2:55:10<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	@NotNull(message="{valid_rec_putaway_putaway_notnull}",groups={ValidSave.class,ValidUpdate.class})
	private RecPutaway putaway;
	
	/**
	 * 上架单明细VO集合
	 * @version 2017年2月20日下午2:59:57<br/>
	 * @author andy wang<br/>
	 */
	private List<RecPutawayDetailVO> listPutawayDetailVO;
	
	/**
	 * 需要保存上架单明细
	 * @version 2017年2月20日下午2:56:47<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	@NotEmpty(message="{valid_rec_putaway_listSavePutawayDetailVO_notnull}",groups={ValidSave.class})
	private List<RecPutawayDetailVO> listSavePutawayDetailVO;
	
	/**
	 * 记录更新的上架单操作明细id
	 * —— key：id
	 * —— value：id
	 * @version 2017年2月27日下午5:49:19<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listUpdatePutawayLocation;
	
	/**
	 * 要删除的上架单明细集合
	 * @version 2017年2月27日下午8:54:55<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listRemovePutawayDetail;
	
	/**
	 * 要删除的上架单操作明细集合
	 * @version 2017年2月27日下午8:55:01<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listRemovePutawayLocation;
	
	/**
	 * 上架单id集合
	 * @version 2017年2月28日上午9:00:10<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listPutawayId;
	
	private MetaMerchant ownerMerchant;
	/**
	 * 客商名
	 * @version 2017年2月28日下午2:11:49<br/>
	 * @author andy wang<br/>
	 */
	private String ownerComment;
	
	/**
	 * 仓库名
	 * @version 2017年2月28日下午2:13:26<br/>
	 * @author andy wang<br/>
	 */
	private String warehouseComment;
	
	private String warehouseNo;
	
	/**
	 * 父上架单编号
	 * @version 2017年2月28日下午2:13:52<br/>
	 * @author andy wang<br/>
	 */
	private String parentNo;
	
	
	/**
	 * 父上架单id
	 * @version 2017年2月28日下午2:14:11<br/>
	 * @author andy wang<br/>
	 */
	private String parentId;
	
	/**
	 * 单据来源
	 * @version 2017年2月28日下午2:15:14<br/>
	 * @author andy wang<br/>
	 */
	private String docTypeComment;
	
	/**
	 * 上架单状态描述
	 * @version 2017年2月28日下午3:11:17<br/>
	 * @author andy wang<br/>
	 */
	private String putawayStatusComment;
	
	
	/**
	 * 作业人员描述
	 * @version 2017年2月28日下午3:11:45<br/>
	 * @author andy wang<br/>
	 */
	private String opPersonComment;
	
	/**
	 * 创建人描述
	 * @version 2017年2月28日下午3:12:24<br/>
	 * @author andy wang<br/>
	 */
	private String createPersonComment;
	
	/**
	 * 更新人描述
	 * @version 2017年2月28日下午3:12:34<br/>
	 * @author andy wang<br/>
	 */
	private String updatePersonComment;
	
	/**
	 * ASN单的PO单号
	 * @version 2017年2月28日下午3:16:36<br/>
	 * @author andy wang<br/>
	 */
	private String likePoNo;
	
	/**
	 * ASN单的ASN单号
	 * @version 2017年2月28日下午3:16:40<br/>
	 * @author andy wang<br/>
	 */
	private String likeAsnNo;
	
	/**
	 * ASN单集合
	 * @version 2017年2月28日下午5:59:41<br/>
	 * @author andy wang<br/>
	 */
	private List<RecAsnVO> listRecAsnVO;
	
	/**
	 * 查询条件 - 是否查询出取消状态的上架单
	 * —— true 允许查询出取消状态的上架单
	 * —— false 不允许查询出取消状态的上架单
	 * @version 2017年3月1日下午8:21:19<br/>
	 * @author andy wang<br/>
	 */
	@JsonIgnore
	private Boolean showCancel;
	
	/**
	 * 需要保存的上架单操作明细集合
	 * @version 2017年3月3日上午11:16:14<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private List<RecPutawayLocation> listSavePutawayLocation;

	/**
	 * 上架单状态集合(查询条件)
	 * @version 2017年3月20日下午3:54:42<br/>
	 * @author andy wang<br/>
	 */
	private List<Integer> listPtwStatus;
	
	/**
	 * 统计上架单对应货品的总数量数量
	 * @version 2017年3月20日下午5:11:30<br/>
	 * @author andy wang<br/>
	 */
	private Double sumQty;
	
	/**
	 * 计划上架总数量/重量/体积
	 * @version 2017年3月23日下午2:39:18<br/>
	 * @author andy wang<br/>
	 */
	private String totalPlanComment;
	
	/**
	 * 实际上架总数量/重量/体积
	 * @version 2017年3月23日下午2:39:22<br/>
	 * @author andy wang<br/>
	 */
	private String totalRealComment;
	
	
	/**
	 * 订单数量
	 * @version 2017年3月31日下午4:57:45<br/>
	 * @author andy wang<br/>
	 */
	private Double orderQty;
	
	
	/**
	 * 订单重量
	 * @version 2017年3月31日下午4:57:51<br/>
	 * @author andy wang<br/>
	 */
	private Double orderWeight;
	
	/**
	 * 订单体积
	 * @version 2017年3月31日下午4:57:54<br/>
	 * @author andy wang<br/>
	 */
	private Double orderVolume;
	
	/**
	 * 收货数量
	 * @version 2017年3月31日下午4:58:05<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveQty;
	
	/**
	 * 收货重量
	 * @version 2017年3月31日下午4:59:45<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveWeight;
	
	
	/**
	 * 收货体积
	 * @version 2017年3月31日下午4:59:49<br/>
	 * @author andy wang<br/>
	 */
	private Double receiveVolume;
	
	
	/**
	 * 模糊查询货主
	 * @version 2017年4月5日下午5:27:04<br/>
	 * @author andy wang<br/>
	 */
	private String likeOwnerName;
	
	/**
	 * 提示信息
	 * @version 2017年5月11日上午10:27:49<br/>
	 * @author andy wang<br/>
	 */
	private String message;
	private boolean success;
	
	
	/* 新属性20170718 *********************************************/
	/** 需要保存的上架单操作明细集合 <br/> add by andy wang */
	private List<RecPutawayLocationVO> listSavePutawayLocationVO;
	/** 任务VO <br/> add by andy wang */
	private TsTaskVo tsTaskVo = new TsTaskVo();
	
	/* getset *********************************************/
	
	/**
	 * 属性 putaway getter方法
	 * @return 属性putaway
	 * @author andy wang<br/>
	 */
	public RecPutaway getPutaway() {
		return putaway;
	}
	/**
	 * 属性 putaway setter方法
	 * @param putaway 设置属性putaway的值
	 * @author andy wang<br/>
	 */
	public void setPutaway(RecPutaway putaway) {
		this.putaway = putaway;
	}

	public TsTaskVo getTsTaskVo() {
		return tsTaskVo;
	}

	public void setTsTaskVo(TsTaskVo tsTaskVo) {
		this.tsTaskVo = tsTaskVo;
	}

	public List<RecPutawayLocationVO> getListSavePutawayLocationVO() {
		return listSavePutawayLocationVO;
	}

	public void setListSavePutawayLocationVO(List<RecPutawayLocationVO> listSavePutawayLocationVO) {
		this.listSavePutawayLocationVO = listSavePutawayLocationVO;
	}

	/**
	 * 属性 message getter方法
	 * @return 属性message
	 * @author andy wang<br/>
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 属性 message setter方法
	 * @param message 设置属性message的值
	 * @author andy wang<br/>
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 属性 likeOwnerName getter方法
	 * @return 属性likeOwnerName
	 * @author andy wang<br/>
	 */
	public String getLikeOwnerName() {
		return likeOwnerName;
	}

	/**
	 * 属性 likeOwnerName setter方法
	 * @param likeOwnerName 设置属性likeOwnerName的值
	 * @author andy wang<br/>
	 */
	public void setLikeOwnerName(String likeOwnerName) {
		this.likeOwnerName = likeOwnerName;
	}


	/**
	 * 属性 orderWeight getter方法
	 * @return 属性orderWeight
	 * @author andy wang<br/>
	 */
	public Double getOrderWeight() {
		return orderWeight;
	}

	/**
	 * 属性 orderWeight setter方法
	 * @param orderWeight 设置属性orderWeight的值
	 * @author andy wang<br/>
	 */
	public void setOrderWeight(Double orderWeight) {
		this.orderWeight = orderWeight;
	}

	/**
	 * 属性 orderVolume getter方法
	 * @return 属性orderVolume
	 * @author andy wang<br/>
	 */
	public Double getOrderVolume() {
		return orderVolume;
	}

	/**
	 * 属性 orderVolume setter方法
	 * @param orderVolume 设置属性orderVolume的值
	 * @author andy wang<br/>
	 */
	public void setOrderVolume(Double orderVolume) {
		this.orderVolume = orderVolume;
	}


	/**
	 * 属性 receiveWeight getter方法
	 * @return 属性receiveWeight
	 * @author andy wang<br/>
	 */
	public Double getReceiveWeight() {
		return receiveWeight;
	}

	/**
	 * 属性 receiveWeight setter方法
	 * @param receiveWeight 设置属性receiveWeight的值
	 * @author andy wang<br/>
	 */
	public void setReceiveWeight(Double receiveWeight) {
		this.receiveWeight = receiveWeight;
	}

	/**
	 * 属性 receiveVolume getter方法
	 * @return 属性receiveVolume
	 * @author andy wang<br/>
	 */
	public Double getReceiveVolume() {
		return receiveVolume;
	}

	/**
	 * 属性 receiveVolume setter方法
	 * @param receiveVolume 设置属性receiveVolume的值
	 * @author andy wang<br/>
	 */
	public void setReceiveVolume(Double receiveVolume) {
		this.receiveVolume = receiveVolume;
	}

	/**
	 * 属性 totalPlanComment setter方法
	 * @param totalPlanComment 设置属性totalPlanComment的值
	 * @author andy wang<br/>
	 */
	public void setTotalPlanComment(String totalPlanComment) {
		this.totalPlanComment = totalPlanComment;
	}

	/**
	 * 属性 totalRealComment setter方法
	 * @param totalRealComment 设置属性totalRealComment的值
	 * @author andy wang<br/>
	 */
	public void setTotalRealComment(String totalRealComment) {
		this.totalRealComment = totalRealComment;
	}

	/**
	 * 属性 totalPlanComment getter方法
	 * @return 属性totalPlanComment
	 * @author andy wang<br/>
	 */
	public String getTotalPlanComment() {
		return totalPlanComment;
	}

	/**
	 * 属性 totalPlanComment setter方法
	 * @param totalPlanComment 设置属性totalPlanComment的值
	 * @author andy wang<br/>
	 */
	public void setTotalPlanComment( Double qty , Double weight , Double volume ) {
		qty = qty == null ? 0d : qty;
		weight = weight == null ? 0d : weight;
		volume = volume == null ? 0d : volume;
		DecimalFormat df = new DecimalFormat("#");
		this.totalPlanComment = String.format("%s/%s/%s", qty,df.format(weight),df.format(volume));
	}

	/**
	 * 属性 totalRealComment getter方法
	 * @return 属性totalRealComment
	 * @author andy wang<br/>
	 */
	public String getTotalRealComment() {
		return totalRealComment;
	}

	/**
	 * 属性 totalRealComment setter方法
	 * @param totalRealComment 设置属性totalRealComment的值
	 * @author andy wang<br/>
	 */
	public void setTotalRealComment( Double qty , Double weight , Double volume ) {
		qty = qty == null ? 0d : qty;
		weight = weight == null ? 0d : weight;
		volume = volume == null ? 0d : volume;
		DecimalFormat df = new DecimalFormat("#");
		this.totalRealComment = String.format("%s/%s/%s", qty,df.format(weight),df.format(volume));
	}


	public Double getSumQty() {
		return sumQty;
	}
	

	public void setSumQty(Double sumQty) {
		this.sumQty = sumQty;
	}
	

	public Double getOrderQty() {
		return orderQty;
	}
	

	public void setOrderQty(Double orderQty) {
		this.orderQty = orderQty;
	}
	

	public Double getReceiveQty() {
		return receiveQty;
	}
	

	public void setReceiveQty(Double receiveQty) {
		this.receiveQty = receiveQty;
	}
	

	/**
	 * 属性 listPtwStatus getter方法
	 * @return 属性listPtwStatus
	 * @author andy wang<br/>
	 */
	public List<Integer> getListPtwStatus() {
		if ( this.listPtwStatus == null ) {
			this.listPtwStatus = new ArrayList<Integer>();
		}
		return listPtwStatus;
	}

	/**
	 * 属性 listPtwStatus setter方法
	 * @param listPtwStatus 设置属性listPtwStatus的值
	 * @author andy wang<br/>
	 */
	public void setListPtwStatus(List<Integer> listPtwStatus) {
		this.listPtwStatus = listPtwStatus;
	}

	/**
	 * 属性 listSavePutawayLocation getter方法
	 * @return 属性listSavePutawayLocation
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> getListSavePutawayLocation() {
		if ( this.listSavePutawayLocation == null ) {
			this.listSavePutawayLocation = new ArrayList<RecPutawayLocation>();
		}
		return listSavePutawayLocation;
	}

	/**
	 * 属性 listSavePutawayLocation setter方法
	 * @param listSavePutawayLocation 设置属性listSavePutawayLocation的值
	 * @author andy wang<br/>
	 */
	public void setListSavePutawayLocation(List<RecPutawayLocation> listSavePutawayLocation) {
		this.listSavePutawayLocation = listSavePutawayLocation;
	}

	/**
	 * 属性 showCancel getter方法
	 * @return 属性showCancel
	 * @author andy wang<br/>
	 */
	public Boolean getShowCancel() {
		return showCancel;
	}

	/**
	 * 属性 showCancel setter方法
	 * @param showCancel 设置属性showCancel的值
	 * @author andy wang<br/>
	 */
	public void setShowCancel(Boolean showCancel) {
		this.showCancel = showCancel;
	}

	/**
	 * 属性 listRecAsnVO getter方法
	 * @return 属性listRecAsnVO
	 * @author andy wang<br/>
	 */
	public List<RecAsnVO> getListRecAsnVO() {
		return listRecAsnVO;
	}

	/**
	 * 属性 listRecAsnVO setter方法
	 * @param listRecAsnVO 设置属性listRecAsnVO的值
	 * @author andy wang<br/>
	 */
	public void setListRecAsnVO(List<RecAsnVO> listRecAsnVO) {
		this.listRecAsnVO = listRecAsnVO;
	}

	/**
	 * 属性 likePoNo getter方法
	 * @return 属性likePoNo
	 * @author andy wang<br/>
	 */
	public String getLikePoNo() {
		return likePoNo;
	}

	/**
	 * 属性 likePoNo setter方法
	 * @param likePoNo 设置属性poNo的值
	 * @author andy wang<br/>
	 */
	public void setLikePoNo(String likePoNo) {
		this.likePoNo = likePoNo;
	}

	/**
	 * 属性 likeAsnNo getter方法
	 * @return 属性likeAsnNo
	 * @author andy wang<br/>
	 */
	public String getLikeAsnNo() {
		return likeAsnNo;
	}

	/**
	 * 属性 likeAsnNo setter方法
	 * @param likeAsnNo 设置属性asnNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeAsnNo(String likeAsnNo) {
		this.likeAsnNo = likeAsnNo;
	}

	/**
	 * 属性 ownerComment getter方法
	 * @return 属性ownerComment
	 * @author andy wang<br/>
	 */
	public String getOwnerComment() {
		return ownerComment;
	}

	/**
	 * 属性 ownerComment setter方法
	 * @param ownerComment 设置属性ownerComment的值
	 * @author andy wang<br/>
	 */
	public void setOwnerComment(String ownerComment) {
		this.ownerComment = ownerComment;
	}

	/**
	 * 属性 warehouseComment getter方法
	 * @return 属性warehouseComment
	 * @author andy wang<br/>
	 */
	public String getWarehouseComment() {
		return warehouseComment;
	}
	

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	/**
	 * 属性 warehouseComment setter方法
	 * @param warehouseComment 设置属性warehouseComment的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseComment(String warehouseComment) {
		this.warehouseComment = warehouseComment;
	}

	/**
	 * 属性 parentNo getter方法
	 * @return 属性parentNo
	 * @author andy wang<br/>
	 */
	public String getParentNo() {
		return parentNo;
	}

	/**
	 * 属性 parentNo setter方法
	 * @param parentNo 设置属性parentNo的值
	 * @author andy wang<br/>
	 */
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}

	/**
	 * 属性 parentId getter方法
	 * @return 属性parentId
	 * @author andy wang<br/>
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 属性 parentId setter方法
	 * @param parentId 设置属性parentId的值
	 * @author andy wang<br/>
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 属性 docTypeComment getter方法
	 * @return 属性docTypeComment
	 * @author andy wang<br/>
	 */
	public String getDocTypeComment() {
		return docTypeComment;
	}

	/**
	 * 属性 docTypeComment setter方法
	 * @param docTypeComment 设置属性docTypeComment的值
	 * @author andy wang<br/>
	 */
	public void setDocTypeComment(String docTypeComment) {
		this.docTypeComment = docTypeComment;
	}

	/**
	 * 属性 putawayStatusComment getter方法
	 * @return 属性putawayStatusComment
	 * @author andy wang<br/>
	 */
	public String getPutawayStatusComment() {
		return putawayStatusComment;
	}

	/**
	 * 属性 putawayStatusComment setter方法
	 * @param putawayStatusComment 设置属性putawayStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setPutawayStatusComment(String putawayStatusComment) {
		this.putawayStatusComment = putawayStatusComment;
	}

	/**
	 * 属性 opPersonComment getter方法
	 * @return 属性opPersonComment
	 * @author andy wang<br/>
	 */
	public String getOpPersonComment() {
		return opPersonComment;
	}

	/**
	 * 属性 opPersonComment setter方法
	 * @param opPersonComment 设置属性opPersonComment的值
	 * @author andy wang<br/>
	 */
	public void setOpPersonComment(String opPersonComment) {
		this.opPersonComment = opPersonComment;
	}

	/**
	 * 属性 createPersonComment getter方法
	 * @return 属性createPersonComment
	 * @author andy wang<br/>
	 */
	public String getCreatePersonComment() {
		return createPersonComment;
	}

	/**
	 * 属性 createPersonComment setter方法
	 * @param createPersonComment 设置属性createPersonComment的值
	 * @author andy wang<br/>
	 */
	public void setCreatePersonComment(String createPersonComment) {
		this.createPersonComment = createPersonComment;
	}

	/**
	 * 属性 updatePersonComment getter方法
	 * @return 属性updatePersonComment
	 * @author andy wang<br/>
	 */
	public String getUpdatePersonComment() {
		return updatePersonComment;
	}

	/**
	 * 属性 updatePersonComment setter方法
	 * @param updatePersonComment 设置属性updatePersonComment的值
	 * @author andy wang<br/>
	 */
	public void setUpdatePersonComment(String updatePersonComment) {
		this.updatePersonComment = updatePersonComment;
	}

	/**
	 * 属性 listPutawayId getter方法
	 * @return 属性listPutawayId
	 * @author andy wang<br/>
	 */
	public List<String> getListPutawayId() {
		return listPutawayId;
	}

	/**
	 * 属性 listPutawayId setter方法
	 * @param listPutawayId 设置属性listPutawayId的值
	 * @author andy wang<br/>
	 */
	public void setListPutawayId(List<String> listPutawayId) {
		this.listPutawayId = listPutawayId;
	}

	/**
	 * 属性 listRemovePutawayDetail getter方法
	 * @return 属性listRemovePutawayDetail
	 * @author andy wang<br/>
	 */
	public List<String> getListRemovePutawayDetail() {
		if ( this.listRemovePutawayDetail == null ) {
			this.listRemovePutawayDetail = new ArrayList<String>();
		}
		return listRemovePutawayDetail;
	}

	/**
	 * 属性 listRemovePutawayDetail setter方法
	 * @param listRemovePutawayDetail 设置属性listRemovePutawayDetail的值
	 * @author andy wang<br/>
	 */
	public void setListRemovePutawayDetail(List<String> listRemovePutawayDetail) {
		this.listRemovePutawayDetail = listRemovePutawayDetail;
	}

	/**
	 * 属性 listRemovePutawayLocation getter方法
	 * @return 属性listRemovePutawayLocation
	 * @author andy wang<br/>
	 */
	public List<String> getListRemovePutawayLocation() {
		if ( this.listRemovePutawayLocation == null ) {
			this.listRemovePutawayLocation = new ArrayList<String>();
		}
		return listRemovePutawayLocation;
	}

	/**
	 * 属性 listRemovePutawayLocation setter方法
	 * @param listRemovePutawayLocation 设置属性listRemovePutawayLocation的值
	 * @author andy wang<br/>
	 */
	public void setListRemovePutawayLocation(List<String> listRemovePutawayLocation) {
		this.listRemovePutawayLocation = listRemovePutawayLocation;
	}

	/**
	 * 属性 listPutawayDetailVO getter方法
	 * @return 属性listPutawayDetailVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetailVO> getListPutawayDetailVO() {
		return listPutawayDetailVO;
	}
	/**
	 * 属性 listPutawayDetailVO setter方法
	 * @param listPutawayDetailVO 设置属性listPutawayDetailVO的值
	 * @author andy wang<br/>
	 */
	public void setListPutawayDetailVO(List<RecPutawayDetailVO> listPutawayDetailVO) {
		this.listPutawayDetailVO = listPutawayDetailVO;
	}
	/**
	 * 属性 listSavePutawayDetailVO getter方法
	 * @return 属性listSavePutawayDetailVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetailVO> getListSavePutawayDetailVO() {
		if ( this.listSavePutawayDetailVO == null ) {
			this.listSavePutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		}
		return listSavePutawayDetailVO;
	}

	/**
	 * 属性 listSavePutawayDetailVO setter方法
	 * @param listSavePutawayDetailVO 设置属性listSavePutawayDetailVO的值
	 * @author andy wang<br/>
	 */
	public void setListSavePutawayDetailVO(List<RecPutawayDetailVO> listSavePutawayDetailVO) {
		this.listSavePutawayDetailVO = listSavePutawayDetailVO;
	}

	/**
	 * 属性 mapUpdatePutawayLocation getter方法
	 * @return 属性mapUpdatePutawayLocation
	 * @author andy wang<br/>
	 */
	public List<String> getListUpdatePutawayLocation() {
		if ( this.listUpdatePutawayLocation == null ) {
			this.listUpdatePutawayLocation = new ArrayList<String>();
		}
		return listUpdatePutawayLocation;
	}

	/**
	 * 属性 mapUpdatePutawayLocation setter方法
	 * @param mapUpdatePutawayLocation 设置属性mapUpdatePutawayLocation的值
	 * @author andy wang<br/>
	 */
	public void setListUpdatePutawayLocation(List<String> mapUpdatePutawayLocation) {
		this.listUpdatePutawayLocation = mapUpdatePutawayLocation;
	}

	public MetaMerchant getOwnerMerchant() {
		return ownerMerchant;
	}

	public void setOwnerMerchant(MetaMerchant ownerMerchant) {
		this.ownerMerchant = ownerMerchant;
	}
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/* method *************************************************/
	/**
	 * 设置上架单明细VO集合
	 * @param listPutawayDetail
	 * @version 2017年2月27日 下午4:09:25<br/>
	 * @author andy wang<br/>
	 */
	public void setListDetail(List<RecPutawayDetail> listPutawayDetail) {
		if ( PubUtil.isEmpty(listPutawayDetail) ) {
			return ;
		}
		if ( PubUtil.isEmpty(this.listPutawayDetailVO) ) {
			this.listPutawayDetailVO = new ArrayList<RecPutawayDetailVO>();
		}
		for (RecPutawayDetail recPutawayDetail : listPutawayDetail) {
			this.listPutawayDetailVO.add(new RecPutawayDetailVO(recPutawayDetail));
		}
	}
	
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月9日 下午2:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayVO searchCache () {
		if ( this.putaway == null ) {
			this.putaway = new RecPutaway();
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if ( this.putaway.getPutawayStatus() != null ) {
			this.putawayStatusComment = paramService.getValue(CacheName.PUTAWAY_STATUS, this.putaway.getPutawayStatus());
		}
		if ( this.putaway.getDocType() != null ) {
			this.docTypeComment = paramService.getValue(CacheName.PUTAWAY_DOCTYPE, this.putaway.getDocType());
		}
		return this;
	}
	
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @param loginUser 当前登录人对象
	 * @version 2017年2月11日 下午11:34:46<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo() {
		if ( this.putaway == null ) {
			this.putaway = new RecPutaway();
		}
		Principal loginUser = LoginUtil.getLoginUser();
		this.putaway.setOrgId(loginUser.getOrgId());
		this.putaway.setWarehouseId(LoginUtil.getWareHouseId());
	}

	/**
	 * 生成上架单Example
	 * @param recPutawayVO 上架单VO
	 * @return 上架单Example
	 * @version 2017年2月22日 上午11:44:41<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public Example getExample () {
		if ( this.putaway == null ) {
			this.putaway = new RecPutaway();
			this.loginInfo();
		}
		Example example = new Example(RecPutaway.class);
		Criteria criteria = example.createCriteria();
		if ( !StringUtil.isTrimEmpty(this.putaway.getPutawayId()) ) {
			criteria.andEqualTo("putawayId", this.putaway.getPutawayId());
		}
		if ( !StringUtil.isTrimEmpty(this.putaway.getAsnNo()) ) {
			criteria.andEqualTo("asnNo", this.putaway.getAsnNo());
		}
		if ( !StringUtil.isTrimEmpty(this.putaway.getPutawayNo()) ) {
			criteria.andEqualTo("putawayNo", this.putaway.getPutawayNo());
		}
		if ( !StringUtil.isTrimEmpty(this.putaway.getParentPutawayId()) ) {
			criteria.andEqualTo("parentPutawayId", this.putaway.getParentPutawayId());
		}
		if ( putaway.getPutawayStatus() != null ) {
			criteria.andEqualTo("putawayStatus", this.putaway.getPutawayStatus());
		}
		if ( !PubUtil.isEmpty(this.getListPutawayId()) ) {
			criteria.andIn("putawayId", this.getListPutawayId());
		}
		if ( !PubUtil.isEmpty(this.getListPtwStatus()) ) {
			criteria.andIn("putawayStatus", this.getListPtwStatus());
		}
		if ( !StringUtil.isTrimEmpty(this.putaway.getOrgId()) ) {
			criteria.andEqualTo("orgId", this.putaway.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(this.putaway.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId", this.putaway.getWarehouseId());
		}
		if ( this.getShowCancel() != null && !this.getShowCancel() ) {
			criteria.andNotEqualTo("putawayStatus", Constant.PUTAWAY_STATUS_CANCEL);
		}
		return example;
	}
	
}
