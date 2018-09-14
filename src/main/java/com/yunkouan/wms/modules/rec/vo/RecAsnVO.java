/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:15:24<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.DateUtil;

import com.yunkouan.util.PubUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.entity.RecAsnDetail;
import com.yunkouan.wms.modules.rec.util.valid.ValidBatchConfirm;
import com.yunkouan.wms.modules.rec.util.valid.ValidSplit;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * ASN单VO对象<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午4:15:24<br/>
 * @author andy wang<br/>
 */
public class RecAsnVO extends BaseVO {
	
	private static final long serialVersionUID = 7539617122998544603L;
	
	/**
	 * 构造方法
	 * @version 2017年2月11日 下午10:35:59<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO(){
		this(new RecAsn());
	}
	/**
	 * 
	 * 构造方法
	 * @param asn
	 * @version 2017年2月11日 下午10:36:04<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO( RecAsn asn ){
		super();
		this.asn = asn;
	}
	
	/**
	 * ASN单对象
	 * @version 2017年2月11日下午10:40:44<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private RecAsn asn;
	
	/**
	 * 单据来源描述
	 * @version 2017年2月22日下午5:14:47<br/>
	 * @author andy wang<br/>
	 */
	private String dataFromComment;
	
	/**
	 * 货主名称
	 * @version 2017年2月11日下午10:41:50<br/>
	 * @author andy wang<br/>
	 */
	private String ownerComment;
	
	/**
	 * 海关客商代码
	 */
	private String hgMerchantNo;
	
	/**
	 * 发货方名称
	 * @version 2017年2月11日下午11:00:08<br/>
	 * @author andy wang<br/>
	 */
	private String senderComment;
	
	/**
	 * 仓库名
	 * @version 2017年2月11日下午11:04:07<br/>
	 * @author andy wang<br/>
	 */
	private String warehouseComment;
	
	private String warehouseNo;
	
	/**
	 * 创建人姓名
	 * @version 2017年2月11日下午11:14:34<br/>
	 * @author andy wang<br/>
	 */
	private String createPersonComment;
	
	/**
	 * 更新人姓名
	 * @version 2017年2月11日下午11:15:21<br/>
	 * @author andy wang<br/>
	 */
	private String updatePersonComment;
	
	/**
	 * 同步erp状态说明
	 */
	private String syncErpStatusComment;
	
	/**
	 * 发送结果
	 */
	private String transStatusComment;
	
	/**
	 * 页面显示Asn单明细VO集合
	 * @version 2017年2月12日下午9:07:22<br/>
	 * @author andy wang<br/>
	 */
	private List<RecAsnDetailVO> listAsnDetailVO;
	
	/**
	 * 需要新增或修改的Asn单明细集合
	 * @version 2017年2月14日下午2:24:29<br/>
	 * @author andy wang<br/>
	 */
//	@Valid
	@NotEmpty(message="{valid_rec_asn_detail_empty}",groups={ValidSplit.class})
	private List<RecAsnDetail> listSaveAsnDetail;
	
	/**
	 * Asn单id集合
	 * @version 2017年2月18日下午1:49:32<br/>
	 * @author andy wang<br/>
	 */
	@Size(min=0,message="{valid_rec_asn_listAsnId_size}",groups={ValidBatchConfirm.class})
	@JsonIgnoreProperties(allowSetters=true)
	private List<String> listAsnId;
	
	/**
	 * 库位Id（批量收货时使用）
	 * @version 2017年2月18日下午1:51:40<br/>
	 * @author andy wang<br/>
	 */
	@NotNull(message="{valid_rec_asn_locationId_isnull}",groups={ValidBatchConfirm.class})
	private String locationId;
	
	/**
	 * 组织名
	 * @version 2017年2月20日上午9:55:24<br/>
	 * @author andy wang<br/>
	 */
	private String orgComment;
	
	/**
	 * 单据类型描述
	 * @version 2017年2月20日上午9:58:58<br/>
	 * @author andy wang<br/>
	 */
	private String docTypeComment;
	
	/**
	 * 发货方客商
	 * @version 2017年2月20日上午10:07:20<br/>
	 * @author andy wang<br/>
	 */
	private MetaMerchant senderMerchant;
	
	/**
	 * 要保存的Asn单明细
	 * @version 2017年2月20日上午10:12:33<br/>
	 * @author andy wang<br/>
	 */
	private RecAsnDetail saveRecAsnDetail;
	
	/**
	 * 上架单id
	 * @version 2017年2月28日下午5:27:51<br/>
	 * @author andy wang<br/>
	 */
	@JsonIgnore
	private String putawayId;
	
	/**
	 * ASN单状态描述
	 * @version 2017年3月1日下午1:30:45<br/>
	 * @author andy wang<br/>
	 */
	private String asnStatusComment;
	
	/**
	 * 父ASN单单号
	 * @version 2017年3月8日上午11:16:41<br/>
	 * @author andy wang<br/>
	 */
	private String parentAsnComment;
	
	/**
	 * 客商id集合
	 * @version 2017年3月8日下午3:38:27<br/>
	 * @author andy wang<br/>
	 */
	@JsonIgnore
	private List<String> listOwnerId;
	
	/**
	 * 创建人id集合
	 * @version 2017年3月8日下午3:39:26<br/>
	 * @author andy wang<br/>
	 */
	@JsonIgnore
	private List<String> listCPersonId;
	
	/**
	 * 上架单明细集合
	 * @version 2017年3月9日下午8:31:59<br/>
	 * @author andy wang<br/>
	 */
	private List<RecPutawayDetailVO> listPtwDetailVO;
	
	
	/**
	 * 记录页面更新的收货明细id
	 * @version 2017年3月10日上午9:50:13<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listUpdateDetailId;
	
	/**
	 * 是否显示取消Asn单（查询条件）
	 * —— true 显示取消状态的Asn单
	 * —— false 不显示取消状态的Asn单
	 * @version 2017年3月19日下午5:43:59<br/>
	 * @author andy wang<br/>
	 */
	@JsonIgnore
	private Boolean showCancel;
	
	/**
	 * 记录页面删除的收货明细id
	 * @version 2017年3月19日下午8:29:50<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listDelDetailId;
	
	/**
	 * 货品代码
	 * @version 2017年3月22日下午5:46:44<br/>
	 * @author andy wang<br/>
	 */
	private String skuNo;
	
	/**
	 * 货品代码集合
	 * @version 2017年3月23日上午10:04:22<br/>
	 * @author andy wang<br/>
	 */
	private List<String> listSkuId;

	/**
	 * 客商对象
	 * @version 2017年3月23日上午11:39:12<br/>
	 * @author andy wang<br/>
	 */
	private MetaMerchant ownerMerchant;
	
	/**
	 * 总实际上架数量
	 * @version 2017年3月27日下午3:24:18<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealPtwQty;
	
	/**
	 * 总实际上架重量
	 * @version 2017年3月27日下午3:24:22<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealPtwWeight;
	
	/**
	 * 总实际上架体积
	 * @version 2017年3月27日下午3:24:25<br/>
	 * @author andy wang<br/>
	 */
	private Double sumRealPtwVolume;
	
	/**
	 * 待上架数量
	 * @version 2017年3月30日下午3:51:04<br/>
	 * @author andy wang<br/>
	 */
	private Double stockQty;
	
	
	/**
	 * 工作人员中文名称
	 * @version 2017年3月31日下午7:49:52<br/>
	 * @author andy wang<br/>
	 */
	private String opPersonComment;
	

	private List<RecPutawayLocationVO> listPtwLocVO;
	
	private List<String> warehouseIdList;
	
	/**
	 * 暂存区待上架货品数量
	 * @version 2017年4月6日下午4:48:00<br/>
	 * @author andy wang<br/>
	 */
	private Integer stockCount;
	
	/**
	 * 模糊查询PO单号
	 * @version 2017年4月10日下午3:40:24<br/>
	 * @author andy wang<br/>
	 */
	private String likePoNo;
	
	/**
	 * 模糊查询ASN单号
	 * @version 2017年4月10日下午3:40:29<br/>
	 * @author andy wang<br/>
	 */
	private String likeAsnNo;
	
	/**
	 * 模糊查询创建人姓名
	 * @version 2017年4月10日下午3:48:59<br/>
	 * @author andy wang<br/>
	 */
	private String likeCreatePerson;
	
	/**
	 * 模糊查询货主
	 * @version 2017年4月10日下午3:49:37<br/>
	 * @author andy wang<br/>
	 */
	private String likeOwner;
	/**
	 * 订单日期开始
	 * @version 2017年4月10日下午3:49:37<br/>
	 * @author andy wang<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderDateStart;
	/**
	 * 订单日期结束
	 * @version 2017年4月10日下午3:49:37<br/>
	 * @author andy wang<br/>
	 */
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private Date orderDateEnd;
	
	/**
	 * 货品名称
	 * @version 2017年5月16日上午10:19:19<br/>
	 * @author andy wang<br/>
	 */
	private String skuName;
	
	/**
	 * 查询条件 - ASN单状态
	 * @version 2017年6月20日下午5:00:31<br/>
	 * @author andy wang<br/>
	 */
	private List<Integer> listAsnStatus;
	
	/* 新属性 20170720 ****************************************************/
	/** 任务VO <br/> add by andy wang */

//	@JsonIgnore
	private TsTaskVo tsTaskVo = new TsTaskVo();
	
	/* getset *************************************************/
	
	
	private DeliverGoodsApplicationVo applicationVo;
	
	
	/**
	 * 属性 senderMerchant getter方法
	 * @return 属性senderMerchant
	 * @author andy wang<br/>
	 */
	public MetaMerchant getSenderMerchant() {
		return senderMerchant;
	}
	/**
	 * 属性 senderMerchant setter方法
	 * @param senderMerchant 设置属性senderMerchant的值
	 * @author andy wang<br/>
	 */
	public void setSenderMerchant(MetaMerchant senderMerchant) {
		this.senderMerchant = senderMerchant;
	}
	public TsTaskVo getTsTaskVo() {
		return tsTaskVo;
	}
	public void setTsTaskVo(TsTaskVo tsTaskVo) {
		this.tsTaskVo = tsTaskVo;
	}
	/**
	 * 属性 listAsnStatus getter方法
	 * @return 属性listAsnStatus
	 * @author andy wang<br/>
	 */
	public List<Integer> getListAsnStatus() {
		return listAsnStatus;
	}
	/**
	 * 属性 listAsnStatus setter方法
	 * @param listAsnStatus 设置属性listAsnStatus的值
	 * @author andy wang<br/>
	 */
	public void setListAsnStatus(List<Integer> listAsnStatus) {
		this.listAsnStatus = listAsnStatus;
	}
	/**
	 * 属性 skuName getter方法
	 * @return 属性skuName
	 * @author andy wang<br/>
	 */
	public String getSkuName() {
		return skuName;
	}
	/**
	 * 属性 skuName setter方法
	 * @param skuName 设置属性skuName的值
	 * @author andy wang<br/>
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	/**
	 * 属性 likeCreatePerson getter方法
	 * @return 属性likeCreatePerson
	 * @author andy wang<br/>
	 */
	public String getLikeCreatePerson() {
		return likeCreatePerson;
	}
	/**
	 * 属性 likeCreatePerson setter方法
	 * @param likeCreatePerson 设置属性likeCreatePerson的值
	 * @author andy wang<br/>
	 */
	public void setLikeCreatePerson(String likeCreatePerson) {
		this.likeCreatePerson = likeCreatePerson;
	}
	/**
	 * 属性 likeOwner getter方法
	 * @return 属性likeOwner
	 * @author andy wang<br/>
	 */
	public String getLikeOwner() {
		return likeOwner;
	}
	/**
	 * 属性 likeOwner setter方法
	 * @param likeOwner 设置属性likeOwner的值
	 * @author andy wang<br/>
	 */
	public void setLikeOwner(String likeOwner) {
		this.likeOwner = likeOwner;
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
	 * @param likePoNo 设置属性likePoNo的值
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
	 * @param likeAsnNo 设置属性likeAsnNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeAsnNo(String likeAsnNo) {
		this.likeAsnNo = likeAsnNo;
	}
	/**
	 * 属性 stockCount getter方法
	 * @return 属性stockCount
	 * @author andy wang<br/>
	 */
	public Integer getStockCount() {
		return stockCount;
	}
	/**
	 * 属性 stockCount setter方法
	 * @param stockCount 设置属性stockCount的值
	 * @author andy wang<br/>
	 */
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	/**
	 * 属性 listPtwLocVO getter方法
	 * @return 属性listPtwLocVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> getListPtwLocVO() {
		return listPtwLocVO;
	}
	/**
	 * 属性 listPtwLocVO setter方法
	 * @param listPtwLocVO 设置属性listPtwLocVO的值
	 * @author andy wang<br/>
	 */
	public void setListPtwLocVO(List<RecPutawayLocationVO> listPtwLocVO) {
		this.listPtwLocVO = listPtwLocVO;
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
	public Double getStockQty() {
		return stockQty;
	}
	
	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	
	public Double getSumRealPtwQty() {
		return sumRealPtwQty;
	}
	
	public void setSumRealPtwQty(Double sumRealPtwQty) {
		this.sumRealPtwQty = sumRealPtwQty;
	}
	
	/**
	 * 属性 sumRealPtwWeight getter方法
	 * @return 属性sumRealPtwWeight
	 * @author andy wang<br/>
	 */
	public Double getSumRealPtwWeight() {
		return sumRealPtwWeight;
	}
	/**
	 * 属性 sumRealPtwWeight setter方法
	 * @param sumRealPtwWeight 设置属性sumRealPtwWeight的值
	 * @author andy wang<br/>
	 */
	public void setSumRealPtwWeight(Double sumRealPtwWeight) {
		this.sumRealPtwWeight = sumRealPtwWeight;
	}
	/**
	 * 属性 sumRealPtwVolume getter方法
	 * @return 属性sumRealPtwVolume
	 * @author andy wang<br/>
	 */
	public Double getSumRealPtwVolume() {
		return sumRealPtwVolume;
	}
	/**
	 * 属性 sumRealPtwVolume setter方法
	 * @param sumRealPtwVolume 设置属性sumRealPtwVolume的值
	 * @author andy wang<br/>
	 */
	public void setSumRealPtwVolume(Double sumRealPtwVolume) {
		this.sumRealPtwVolume = sumRealPtwVolume;
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
	 * 属性 ownerMerchant getter方法
	 * @return 属性ownerMerchant
	 * @author andy wang<br/>
	 */
	public MetaMerchant getOwnerMerchant() {
		return ownerMerchant;
	}
	/**
	 * 属性 ownerMerchant setter方法
	 * @param ownerMerchant 设置属性ownerMerchant的值
	 * @author andy wang<br/>
	 */
	public void setOwnerMerchant(MetaMerchant ownerMerchant) {
		this.ownerMerchant = ownerMerchant;
	}
	/**
	 * 属性 listSkuId getter方法
	 * @return 属性listSkuId
	 * @author andy wang<br/>
	 */
	public List<String> getListSkuId() {
		if ( this.listSkuId == null ) {
			this.listSkuId = new ArrayList<String>();
		}
		return listSkuId;
	}
	/**
	 * 属性 listSkuId setter方法
	 * @param listSkuId 设置属性listSkuId的值
	 * @author andy wang<br/>
	 */
	public void setListSkuId(List<String> listSkuId) {
		this.listSkuId = listSkuId;
	}
	/**
	 * 属性 skuNo getter方法
	 * @return 属性skuNo
	 * @author andy wang<br/>
	 */
	public String getSkuNo() {
		return skuNo;
	}
	/**
	 * 属性 skuNo setter方法
	 * @param skuNo 设置属性skuNo的值
	 * @author andy wang<br/>
	 */
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	/**
	 * 属性 listUpdateDetailId getter方法
	 * @return 属性listUpdateDetailId
	 * @author andy wang<br/>
	 */
	public List<String> getListUpdateDetailId() {
		if ( listUpdateDetailId == null ) {
			this.listUpdateDetailId = new ArrayList<String>();
		}
		return listUpdateDetailId;
	}
	/**
	 * 属性 listUpdateDetailId setter方法
	 * @param listUpdateDetailId 设置属性listUpdateDetailId的值
	 * @author andy wang<br/>
	 */
	public void setListUpdateDetailId(List<String> listUpdateDetailId) {
		this.listUpdateDetailId = listUpdateDetailId;
	}
	/**
	 * 属性 listDelDetailId getter方法
	 * @return 属性listDelDetailId
	 * @author andy wang<br/>
	 */
	public List<String> getListDelDetailId() {
		if ( listDelDetailId == null ) {
			this.listDelDetailId = new ArrayList<String>();
		}
		return this.listDelDetailId;
	}
	/**
	 * 属性 listDelDetailId setter方法
	 * @param listDelDetailId 设置属性listDelDetailId的值
	 * @author andy wang<br/>
	 */
	public void setListDelDetailId(List<String> listDelDetailId) {
		this.listDelDetailId = listDelDetailId;
	}
	
	/**
	 * 属性 listPtwDetailVO getter方法
	 * @return 属性listPtwDetailVO
	 * @author andy wang<br/>
	 */
	public List<RecPutawayDetailVO> getListPtwDetailVO() {
		return listPtwDetailVO;
	}
	/**
	 * 属性 listPtwDetailVO setter方法
	 * @param listPtwDetailVO 设置属性listPtwDetailVO的值
	 * @author andy wang<br/>
	 */
	public void setListPtwDetailVO(List<RecPutawayDetailVO> listPtwDetailVO) {
		this.listPtwDetailVO = listPtwDetailVO;
	}
	/**
	 * 属性 listOwnerId getter方法
	 * @return 属性listOwnerId
	 * @author andy wang<br/>
	 */
	public List<String> getListOwnerId() {
		if ( this.listOwnerId == null ) {
			this.listOwnerId = new ArrayList<String>();
		}
		return listOwnerId;
	}
	/**
	 * 属性 listOwnerId setter方法
	 * @param listOwnerId 设置属性listOwnerId的值
	 * @author andy wang<br/>
	 */
	public void setListOwnerId(List<String> listOwnerId) {
		this.listOwnerId = listOwnerId;
	}
	/**
	 * 属性 listCPersonId getter方法
	 * @return 属性listCPersonId
	 * @author andy wang<br/>
	 */
	public List<String> getListCPersonId() {
		return listCPersonId;
	}
	/**
	 * 属性 listCPersonId setter方法
	 * @param listCPersonId 设置属性listCPersonId的值
	 * @author andy wang<br/>
	 */
	public void setListCPersonId(List<String> listCPersonId) {
		this.listCPersonId = listCPersonId;
	}
	/**
	 * 属性 asnStatusComment getter方法
	 * @return 属性asnStatusComment
	 * @author andy wang<br/>
	 */
	public String getAsnStatusComment() {
		return asnStatusComment;
	}
	/**
	 * 属性 asnStatusComment setter方法
	 * @param asnStatusComment 设置属性asnStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setAsnStatusComment(String asnStatusComment) {
		this.asnStatusComment = asnStatusComment;
	}
	/**
	 * 属性 parentAsnComment getter方法
	 * @return 属性parentAsnComment
	 * @author andy wang<br/>
	 */
	public String getParentAsnComment() {
		return parentAsnComment;
	}
	/**
	 * 属性 parentAsnComment setter方法
	 * @param parentAsnComment 设置属性parentAsnComment的值
	 * @author andy wang<br/>
	 */
	public void setParentAsnComment(String parentAsnComment) {
		this.parentAsnComment = parentAsnComment;
	}
	/**
	 * 属性 putawayId getter方法
	 * @return 属性putawayId
	 * @author andy wang<br/>
	 */
	public String getPutawayId() {
		return putawayId;
	}
	/**
	 * 属性 putawayId setter方法
	 * @param putawayId 设置属性putawayId的值
	 * @author andy wang<br/>
	 */
	public void setPutawayId(String putawayId) {
		this.putawayId = putawayId;
	}
	/**
	 * 属性 dataFromComment getter方法
	 * @return 属性dataFromComment
	 * @author andy wang<br/>
	 */
	public String getDataFromComment() {
		return dataFromComment;
	}
	/**
	 * 属性 dataFromComment setter方法
	 * @param dataFromComment 设置属性dataFromComment的值
	 * @author andy wang<br/>
	 */
	public void setDataFromComment(String dataFromComment) {
		this.dataFromComment = dataFromComment;
	}
	/**
	 * 属性 saveRecAsnDetail getter方法
	 * @return 属性saveRecAsnDetail
	 * @author andy wang<br/>
	 */
	public RecAsnDetail getSaveRecAsnDetail() {
		return saveRecAsnDetail;
	}
	/**
	 * 属性 saveRecAsnDetail setter方法
	 * @param saveRecAsnDetail 设置属性saveRecAsnDetail的值
	 * @author andy wang<br/>
	 */
	public void setSaveRecAsnDetail(RecAsnDetail saveRecAsnDetail) {
		this.saveRecAsnDetail = saveRecAsnDetail;
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
	 * 属性 orgComment setter方法
	 * @param orgComment 设置属性orgComment的值
	 * @author andy wang<br/>
	 */
	public void setOrgComment(String orgComment) {
		this.orgComment = orgComment;
	}
	/**
	 * 属性 orgComment getter方法
	 * @return 属性orgComment
	 * @author andy wang<br/>
	 */
	public String getOrgComment() {
		return orgComment;
	}
	/**
	 * 属性 listSaveAsnDetail getter方法
	 * @return 属性listSaveAsnDetail
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetail> getListSaveAsnDetail() {
		if ( this.listSaveAsnDetail == null ) {
			this.listSaveAsnDetail = new ArrayList<RecAsnDetail>();
		}
		return listSaveAsnDetail;
	}
	/**
	 * 属性 listSaveAsnDetail setter方法
	 * @param listSaveAsnDetail 设置属性listSaveAsnDetail的值
	 * @author andy wang<br/>
	 */
	public void setListSaveAsnDetail(List<RecAsnDetail> listSaveAsnDetail) {
		this.listSaveAsnDetail = listSaveAsnDetail;
	}
	/**
	 * 属性 asn getter方法
	 * @return 属性asn
	 * @author andy wang<br/>
	 */
	public RecAsn getAsn() {
		return asn;
	}
	/**
	 * 属性 asn setter方法
	 * @param asn 设置属性asn的值
	 * @author andy wang<br/>
	 */
	public void setAsn(RecAsn asn) {
		this.asn = asn;
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
	 * 属性 senderComment getter方法
	 * @return 属性senderComment
	 * @author andy wang<br/>
	 */
	public String getSenderComment() {
		return senderComment;
	}
	/**
	 * 属性 senderComment setter方法
	 * @param senderComment 设置属性senderComment的值
	 * @author andy wang<br/>
	 */
	public void setSenderComment(String senderComment) {
		this.senderComment = senderComment;
	}
	/**
	 * 属性 wareHouseComment getter方法
	 * @return 属性wareHouseComment
	 * @author andy wang<br/>
	 */
	public String getWarehouseComment() {
		return warehouseComment;
	}
	/**
	 * 属性 wareHouseComment setter方法
	 * @param wareHouseComment 设置属性wareHouseComment的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseComment(String warehouseComment) {
		this.warehouseComment = warehouseComment;
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
	 * 属性 listAsnDetailVO getter方法
	 * @return 属性listAsnDetailVO
	 * @author andy wang<br/>
	 */
	public List<RecAsnDetailVO> getListAsnDetailVO() {
		if ( this.listAsnDetailVO == null ) {
			this.listAsnDetailVO = new ArrayList<RecAsnDetailVO>();
		}
		return listAsnDetailVO;
	}
	/**
	 * 属性 listAsnDetailVO setter方法
	 * @param listAsnDetailVO 设置属性listAsnDetailVO的值
	 * @author andy wang<br/>
	 */
	public void setListAsnDetailVO(List<RecAsnDetailVO> listAsnDetailVO) {
		this.listAsnDetailVO = listAsnDetailVO;
	}
	/**
	 * 属性 listAsnId getter方法
	 * @return 属性listAsnId
	 * @author andy wang<br/>
	 */
	public List<String> getListAsnId() {
		return listAsnId;
	}
	/**
	 * 属性 listAsnId setter方法
	 * @param listAsnId 设置属性listAsnId的值
	 * @author andy wang<br/>
	 */
	public void setListAsnId(List<String> listAsnId) {
		this.listAsnId = listAsnId;
	}
	/**
	 * 属性 locationId getter方法
	 * @return 属性locationId
	 * @author andy wang<br/>
	 */
	public String getLocationId() {
		return locationId;
	}
	/**
	 * 属性 locationId setter方法
	 * @param locationId 设置属性locationId的值
	 * @author andy wang<br/>
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	
	
	
	/* method ********************************************/
	
	
	public String getWarehouseNo() {
		return warehouseNo;
	}
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	public String getHgMerchantNo() {
		return hgMerchantNo;
	}
	public void setHgMerchantNo(String hgMerchantNo) {
		this.hgMerchantNo = hgMerchantNo;
	}
	public String getSyncErpStatusComment() {
		return syncErpStatusComment;
	}
	public void setSyncErpStatusComment(String syncErpStatusComment) {
		this.syncErpStatusComment = syncErpStatusComment;
	}
	
	public String getTransStatusComment() {
		return transStatusComment;
	}
	public void setTransStatusComment(String transStatusComment) {
		this.transStatusComment = transStatusComment;
	}
	
	public Date getOrderDateStart() {
		return orderDateStart;
	}
	
	public void setOrderDateStart(Date orderDateStart) {
		this.orderDateStart = orderDateStart;
	}
	
	public Date getOrderDateEnd() {
		return orderDateEnd;
	}
	
	public void setOrderDateEnd(Date orderDateEnd) {
		this.orderDateEnd = orderDateEnd;
	}
	
	/**
	 * 添加Asn单id，进入集合
	 * @param asnId Asn单id
	 * @version 2017年3月11日 上午9:31:05<br/>
	 * @author andy wang<br/>
	 */
	public void addAsnId ( String asnId ) {
		if ( this.listAsnId == null ) {
			this.listAsnId = new ArrayList<String>();
		}
		this.listAsnId.add(asnId);
	}
	
	
	/**
	 * 查询缓存<br/>
	 * 缓存包括：<br/>
	 * —— Asn单状态<br/>
	 * —— 单据类型<br/>
	 * —— 单据来源<br/>
	 * @return
	 * 创建日期:<br/> 2017年2月9日 下午2:53:13<br/>
	 * @author andy wang<br/>
	 */
	public RecAsnVO searchCache () {
		if ( this.getAsn() == null ) {
			this.asn = new RecAsn();
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if ( this.asn.getAsnStatus() != null ) {
			this.asnStatusComment = paramService.getValue(CacheName.ASN_STATUS, this.asn.getAsnStatus());
		}
		if ( this.asn.getDocType() != null ) {
			this.docTypeComment = paramService.getValue(CacheName.ASN_DOCTYPE, this.asn.getDocType());
		}
		if ( this.asn.getDataFrom() != null ) {
			this.dataFromComment = paramService.getValue(CacheName.ASN_DATAFROM, this.asn.getDataFrom());
		}
		if ( this.asn.getSyncErpStatus() != null ) {
			this.syncErpStatusComment = paramService.getValue(CacheName.ASN_SYNC_ERP_STATUS, this.asn.getSyncErpStatus());
		}
		if ( StringUtil.isNotBlank(this.asn.getTransStatus())) {
			this.transStatusComment = paramService.getValue(CacheName.MSMQ_CHK_RESULT, this.asn.getTransStatus());
		}
		
		return this;
	}
	
	/**
	 * 设置当前登录人信息（企业id，仓库id）
	 * @version 2017年2月11日 下午11:34:46<br/>
	 * @author andy wang<br/>
	 */
	public void loginInfo() {
		if ( this.asn == null ) {
			this.asn = new RecAsn();
		}
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser!= null){
			this.asn.setOrgId(loginUser.getOrgId());
			this.asn.setWarehouseId(LoginUtil.getWareHouseId());
		}
	}
	
	@Override
	public Example getExample() {
		this.loginInfo();
		Example example = new Example(RecAsn.class);
		Criteria criteria = example.createCriteria();
		if ( this.getShowCancel() != null && !this.getShowCancel() ) {
			criteria.andNotEqualTo("asnStatus", Constant.ASN_STATUS_CANCEL);
		}
		if ( !StringUtil.isTrimEmpty(this.getLikePoNo()) ) {
			criteria.andLike("poNo", StringUtil.likeEscapeH(this.getLikePoNo()));
		}
		if ( !StringUtil.isTrimEmpty(this.getLikeAsnNo()) ) {
			criteria.andLike("asnNo", StringUtil.likeEscapeH(this.getLikeAsnNo()));
		}
		if ( !PubUtil.isEmpty(this.listAsnStatus) ) {
			criteria.andIn("asnStatus", this.listAsnStatus);
		}
		if ( !PubUtil.isEmpty(this.listAsnId) ) {
			criteria.andIn("asnId", this.listAsnId);
		}
		if ( !PubUtil.isEmpty(this.listOwnerId) ) {
			criteria.andIn("owner", this.listOwnerId);
		}
		if ( !PubUtil.isEmpty(this.listCPersonId) ) {
			criteria.andIn("createPerson", this.listCPersonId);
		}
		if(this.asn == null) return example;
		if ( !StringUtil.isTrimEmpty(this.asn.getPoNo()) ) {
			criteria.andEqualTo("poNo", this.asn.getPoNo());
		}
		if ( !StringUtil.isTrimEmpty(this.asn.getAsnId()) ) {
			criteria.andEqualTo("asnId", this.asn.getAsnId());
		}
		if ( !StringUtil.isTrimEmpty(this.asn.getParentAsnId()) ) {
			criteria.andEqualTo("parentAsnId", this.asn.getParentAsnId());
		}
		if ( this.asn.getAsnStatus() != null ) {
			criteria.andEqualTo("asnStatus", this.asn.getAsnStatus());
		}
		if ( this.asn.getOrderDate() != null ) {
			criteria.andBetween("orderDate", DateUtil.setStartTime(this.asn.getOrderDate())
					, DateUtil.setEndTime(this.asn.getOrderDate()));
		}
		if (this.orderDateStart != null) {
			criteria.andGreaterThanOrEqualTo("orderDate", DateUtil.setStartTime(orderDateStart));
		}
		if (this.orderDateEnd != null) {
			criteria.andLessThanOrEqualTo("orderDate", DateUtil.setEndTime(orderDateEnd));
		}
		if ( this.asn.getDataFrom() != null ) {
			criteria.andEqualTo("dataFrom", this.asn.getDataFrom());
		}
		if ( !StringUtil.isTrimEmpty(this.asn.getAsnNo()) ) {
			criteria.andEqualTo("asnNo", this.asn.getAsnNo());
		}
		if ( !StringUtil.isTrimEmpty(this.asn.getOrgId()) ) {
			criteria.andEqualTo("orgId", this.asn.getOrgId());
		}
		if ( !StringUtil.isTrimEmpty(this.asn.getWarehouseId()) ) {
			criteria.andEqualTo("warehouseId", this.asn.getWarehouseId());
		}
		if(warehouseIdList != null && !warehouseIdList.isEmpty()){
			criteria.andIn("warehouseId", warehouseIdList);
		}
		return example;
	}
	public DeliverGoodsApplicationVo getApplicationVo() {
		return applicationVo;
	}
	public void setApplicationVo(DeliverGoodsApplicationVo applicationVo) {
		this.applicationVo = applicationVo;
	}
	public List<String> getWarehouseIdList() {
		return warehouseIdList;
	}
	public void setWarehouseIdList(List<String> warehouseIdList) {
		this.warehouseIdList = warehouseIdList;
	}
	
	
}