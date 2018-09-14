package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunkouan.base.BaseVO;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.ICommonService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.vo.CommonVo;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.entity.SendPickDetail;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.ts.vo.TsTaskVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendPickVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6396458937196183920L;

	@Valid
	private SendPick sendPick = new SendPick();
	
	private List<SendPickDetailVo> sendPickDetailVoList = new ArrayList<SendPickDetailVo>();
	
	private String deliveryNo;
	
	private String waveNo;
	
	private String warehouseName;
	
	private String ownerName;
	
	private String merchantShortName;
	
	private String docTypeComment;
	
	private String statusComment;//状态说明
	
	private List<String> ownerList;
	
	private List<String> detailIdList = new ArrayList<String>();
	
	private List<String> waveIdList = new ArrayList<String>();
	
	private String opPersonName;
	
	private String expressServiceName;
	
	private Example example;
	
	private BillPrefix fieldName;
	
	private List<Integer> pickStatusList;
	
	private Boolean deliveryIsNull;
	
	private Boolean deliveryIsNotNull;
	
	private Boolean waveIsNotNull;
	
	private Boolean waveIsNull;
	
	private String pickNo;
	
	private Integer greaterThanStatus;
	
	private TsTaskVo tsTaskVo = new TsTaskVo();
	
	private SendDeliveryVo sendDeliveryVo;
	
	private List<String> pickIdList;
	
	private String printPickStatusComment;
	
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private String beginTime;
    
	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
    private String endTime;
	
	private Integer expressPrintStatus;
	
	private Integer pickPrintStatus;
	
	private String expressServiceCode;
	
	private String expressBillNo;
	
	private String orgId;
	
	private String warehouseId;
	
	private String srcNo;
	
	private String orderByStr = " create_time desc";
	
	private List <BizException>exceptions;

	public SendPick getSendPick() {
		return sendPick;
	}

	public void setSendPick(SendPick sendPick) {
		this.sendPick = sendPick;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(sendPick.getDocType() != null){
			this.docTypeComment = paramService.getValue(CacheName.PICK_TYPE, sendPick.getDocType());
		}
		if(sendPick.getPickStatus() != null){
			this.statusComment = paramService.getValue(CacheName.PICK_STATUS, sendPick.getPickStatus());
		}
		if(StringUtil.isNotBlank(sendPick.getExpressServiceCode())){
			this.expressServiceName = paramService.getValue(CacheName.EXPRESS_SERVICE_CODE, sendPick.getExpressServiceCode());
		}
		if(sendPick.getPickBillPrintTimes() != null){
			if(sendPick.getPickBillPrintTimes() > 0){
				this.printPickStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 1);
			}else{
				this.printPickStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 0);
			}
		}else{
			this.printPickStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 0);
		}
	}
	

	public String getStatusComment() {
		return statusComment;
	}

	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	public String getDocTypeComment() {
		return docTypeComment;
	}

	public void setDocTypeComment(String docTypeComment) {
		this.docTypeComment = docTypeComment;
	}

	public List<SendPickDetailVo> getSendPickDetailVoList() {
		return sendPickDetailVoList;
	}

	public void setSendPickDetailVoList(List<SendPickDetailVo> sendPickDetailVoList) {
		this.sendPickDetailVoList = sendPickDetailVoList;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}
	
	

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	

	public List<String> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(List<String> ownerList) {
		this.ownerList = ownerList;
	}	

	public List<String> getDetailIdList() {
		return detailIdList;
	}

	public void setDetailIdList(List<String> detailIdList) {
		this.detailIdList = detailIdList;
	}

	public List<Integer> getPickStatusList() {
		return pickStatusList;
	}

	public void setPickStatusList(List<Integer> pickStatusList) {
		this.pickStatusList = pickStatusList;
	}

	public BillPrefix getFieldName() {
		return fieldName;
	}

	public void setFieldName(BillPrefix fieldName) {
		this.fieldName = fieldName;
	}
	
	public Boolean getDeliveryIsNull() {
		return deliveryIsNull;
	}

	public void setDeliveryIsNull(Boolean deliveryIsNull) {
		this.deliveryIsNull = deliveryIsNull;
	}

	public Boolean getWaveIsNull() {
		return waveIsNull;
	}

	public void setWaveIsNull(Boolean waveIsNull) {
		this.waveIsNull = waveIsNull;
	}
	

	public Boolean getDeliveryIsNotNull() {
		return deliveryIsNotNull;
	}

	public void setDeliveryIsNotNull(Boolean deliveryIsNotNull) {
		this.deliveryIsNotNull = deliveryIsNotNull;
	}
	
	

	public String getOpPersonName() {
		return opPersonName;
	}

	public void setOpPersonName(String opPersonName) {
		this.opPersonName = opPersonName;
	}

	public Boolean getWaveIsNotNull() {
		return waveIsNotNull;
	}

	public void setWaveIsNotNull(Boolean waveIsNotNull) {
		this.waveIsNotNull = waveIsNotNull;
	}

	
	public String getPickNo() {
		return pickNo;
	}

	public void setPickNo(String pickNo) {
		this.pickNo = pickNo;
	}
	
	
	public Integer getGreaterThanStatus() {
		return greaterThanStatus;
	}

	public void setGreaterThanStatus(Integer greaterThanStatus) {
		this.greaterThanStatus = greaterThanStatus;
	}

	public TsTaskVo getTsTaskVo() {
		return tsTaskVo;
	}

	public void setTsTaskVo(TsTaskVo tsTaskVo) {
		this.tsTaskVo = tsTaskVo;
	}
	
	public SendDeliveryVo getSendDeliveryVo() {
		return sendDeliveryVo;
	}

	public void setSendDeliveryVo(SendDeliveryVo sendDeliveryVo) {
		this.sendDeliveryVo = sendDeliveryVo;
	}

	public List<String> getWaveIdList() {
		return waveIdList;
	}

	public void setWaveIdList(List<String> waveIdList) {
		this.waveIdList = waveIdList;
	}
	

	public List<String> getPickIdList() {
		return pickIdList;
	}

	public void setPickIdList(List<String> pickIdList) {
		this.pickIdList = pickIdList;
	}
	
	public String getExpressServiceName() {
		return expressServiceName;
	}

	public void setExpressServiceName(String expressServiceName) {
		this.expressServiceName = expressServiceName;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}
	
	public SendPickVo(){
		this.example = new Example(SendPick.class);
		example.createCriteria();
	}
	
	public Example getConditionExample(){
		example.setOrderByClause(orderByStr);
		Criteria c = example.getOredCriteria().get(0);
		if(ownerList != null && !ownerList.isEmpty()){
			c.andIn("owner", ownerList);
		}
		if(deliveryIsNull == null && !StringUtil.isTrimEmpty(sendPick.getDeliveryId())){
			c.andEqualTo("deliveryId",sendPick.getDeliveryId());
		}
		if(greaterThanStatus != null){
			c.andGreaterThan("pickStatus", greaterThanStatus);
		}
		if(deliveryIsNull != null && deliveryIsNull){
			c.andCondition("delivery_id is null");
		}
		if(deliveryIsNotNull != null && deliveryIsNotNull){
			c.andCondition("delivery_id is not null");
		}
		if(waveIsNotNull != null && waveIsNotNull){
			c.andCondition("wave_id is not null");
		}
		if(waveIsNull == null && !StringUtil.isTrimEmpty(sendPick.getWaveId())){
			c.andEqualTo("waveId",sendPick.getWaveId());
		}
		if(waveIsNull != null && waveIsNull){
			c.andCondition("wave_id is null");
		}
		if(sendPick.getPickStatus() != null){
			c.andEqualTo("pickStatus",sendPick.getPickStatus());
		}
		if(!StringUtil.isTrimEmpty(sendPick.getParentId())){
			c.andEqualTo("parentId",sendPick.getParentId());
		}
		if(StringUtils.isNotEmpty(sendPick.getOrgId())){
			c.andCondition("org_id=", sendPick.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendPick.getWarehouseId())){
			c.andCondition("warehouse_id=", sendPick.getWarehouseId());
		}
		if(pickStatusList != null && !pickStatusList.isEmpty()){
			c.andIn("pickStatus", pickStatusList);
		}
		if(detailIdList != null && !detailIdList.isEmpty()){
			c.andIn("deliveryId", detailIdList);
		}
		if(waveIdList != null && !waveIdList.isEmpty()){
			c.andIn("waveId", waveIdList);
		}
		if(!StringUtil.isTrimEmpty(pickNo)){
		    c.andLike("pickNo", '%'+pickNo+'%');
		}
		if(StringUtils.isNotEmpty(sendPick.getExpressBillNo())){
			c.andEqualTo("expressBillNo",sendPick.getExpressBillNo());
		}
		if(StringUtils.isNotEmpty(sendPick.getExpressServiceCode())){
			c.andEqualTo("expressServiceCode",sendPick.getExpressServiceCode());
		}
		if(pickIdList != null && !pickIdList.isEmpty()){
			c.andIn("pickId", pickIdList);
		}
		if(sendPick.getPickBillPrintTimes() != null){
			if(sendPick.getPickBillPrintTimes() > 0){
				c.andGreaterThanOrEqualTo("pickBillPrintTimes", sendPick.getPickBillPrintTimes());
			}else{
				c.andIsNull("pickBillPrintTimes");
			}
		}
		return example;
	}
	
	
	public SendPickVo andContition(String condition){
		Criteria c = example.getOredCriteria().get(0);
		c.andCondition(condition);
		return this;
	}
	
	/**
	 * 从发货单创建拣货单
	 * @param delivery
	 * @param sendNoUtils
	 * @param operator
	 * @version 2017年3月3日 下午4:17:00<br/>
	 * @author Aaron He<br/>
	 * @throws Exception 
	 */
	public void createPickFromDelivery(SendDeliveryVo deliveryVo,String operator) throws Exception{
		if(deliveryVo == null) return;

		SendDelivery delivery = deliveryVo.getSendDelivery();	
		//创建拣货单
		this.sendPick.setPickId(IdUtil.getUUID());
		this.sendPick.setOwner(delivery.getOwner());
		this.sendPick.setReceiptNo(delivery.getSrcNo());
		this.sendPick.setDocType(Constant.PICKTYPE_FORM_DELIVERY);
		this.sendPick.setDeliveryId(delivery.getDeliveryId());
		double orderQty = delivery.getOrderQty() - delivery.getPickQty();
		double orderWeight = NumberUtil.sub(delivery.getOrderWeight(), delivery.getPickWeight());
		double orderVolume = NumberUtil.sub(delivery.getOrderVolume(),delivery.getPickVolume());
		this.sendPick.setPlanPickQty(orderQty);
		this.sendPick.setPlanPickWeight(orderWeight);
		this.sendPick.setPlanPickVolume(orderVolume);
		this.sendPick.setOrgId(delivery.getOrgId());
		this.sendPick.setWarehouseId(delivery.getWarehouseId());
		this.sendPick.setPickStatus(Constant.PICK_STATUS_OPEN);
//		this.sendPick.setPickStatus(Constant.PICK_STATUS_ACTIVE);//拣货单直接生效
		this.sendPick.setCreatePerson(operator);
		this.sendPick.setUpdatePerson(operator);
		this.sendPick.setExpressServiceCode(delivery.getExpressServiceCode());
		this.sendPick.setExpressBillNo(delivery.getExpressBillNo());
		//创建拣货单明细
		createPickDetaiVoList(deliveryVo.getDeliveryDetailVoList(),delivery.getOwner(), operator);
		
	}
	
	/**
	 * 从波次单创建拣货单
	 * @param wave
	 * @param sendNoUtils
	 * @param operator
	 * @version 2017年3月3日 下午4:16:41<br/>
	 * @author Aaron He<br/>
	 * @throws Exception 
	 */
	public void createPickFromWave(SendWaveVo waveVo,String operator) throws Exception{
		if(waveVo == null) return;
		SendWave wave = waveVo.getSendWave();			
		//创建拣货单
		this.sendPick.setPickId(IdUtil.getUUID());
		this.sendPick.setDocType(Constant.PICKTYPE_FORM_WAVE);
		this.sendPick.setWaveId(wave.getWaveId());
		double orderQty = wave.getOrderQty() - wave.getPickQty();
		double orderWeight = NumberUtil.sub(wave.getOrderWeight(), wave.getPickWeight());
		double orderVolume = NumberUtil.sub(wave.getOrderVolume(),wave.getPickVolume());
		this.sendPick.setPlanPickQty(orderQty);
		this.sendPick.setPlanPickWeight(orderWeight);
		this.sendPick.setPlanPickVolume(orderVolume);
		this.sendPick.setOrgId(wave.getOrgId());
		this.sendPick.setWarehouseId(wave.getWarehouseId());
		this.sendPick.setPickStatus(Constant.PICK_STATUS_OPEN);
		this.sendPick.setCreatePerson(operator);
		this.sendPick.setUpdatePerson(operator);		
	}
	
	public void createPickDetaiVoList(List<DeliveryDetailVo> ddList,String owner,String operator){
		if(ddList == null || ddList.isEmpty()) return;
		
		for (DeliveryDetailVo record : ddList) {
			//发货明细待拣货数量为0，不需拣货
			if(record.getDeliveryDetail().getOrderQty()- record.getDeliveryDetail().getPickQty() == 0) continue;
			SendPickDetailVo pdVo = new SendPickDetailVo();
			SendPickDetail pickDetail = new SendPickDetail();
			pickDetail.setSkuId(record.getDeliveryDetail().getSkuId());
			pickDetail.setBatchNo(record.getDeliveryDetail().getBatchNo());
			pickDetail.setOwner(owner);
			pickDetail.setPackId(record.getDeliveryDetail().getSkuId());
			double orderQty = record.getDeliveryDetail().getOrderQty() - record.getDeliveryDetail().getPickQty();
			double orderWeight = NumberUtil.sub(record.getDeliveryDetail().getOrderWeight(), record.getDeliveryDetail().getPickWeight());
			double orderVolume = NumberUtil.sub(record.getDeliveryDetail().getOrderVolume(),record.getDeliveryDetail().getPickVolume());
			pickDetail.setOrderQty(orderQty);
			pickDetail.setOrderWeight(orderWeight);
			pickDetail.setOrderVolume(orderVolume);
			pickDetail.setDeliveryId(record.getDeliveryDetail().getDeliveryId());
			pickDetail.setDeliveryDetailId(record.getDeliveryDetail().getDeliveryDetailId());
			pickDetail.setMeasureUnit(record.getDeliveryDetail().getMeasureUnit());		
			pickDetail.setPickDetailId(IdUtil.getUUID());
			pickDetail.setPickId(this.sendPick.getPickId());
			pickDetail.setOrgId(record.getDeliveryDetail().getOrgId());
			pickDetail.setWarehouseId(record.getDeliveryDetail().getWarehouseId());
			pickDetail.setCreatePerson(operator);
			pickDetail.setUpdatePerson(operator);
			
			pdVo.setSendPickDetail(pickDetail);
			this.sendPickDetailVoList.add(pdVo);
		}
	}
	
	
	/**
	 * 根据上送拣货明细创建计划拣货库位
	 * @param detailVo
	 * @param operator
	 * @version 2017年3月3日 下午4:59:07<br/>
	 * @author Aaron He<br/>
	 */
	public void createPlanLocationsByHandle(SendPickVo reqParam,String operator){
		if(reqParam == null) return;
		//Map<devileryDetailId,SendPickDetailVo>
		Map<String,SendPickDetailVo> map = reqParam.listToMap();
		for (SendPickDetailVo sendPickDetailVo : this.sendPickDetailVoList) {
			if(map.containsKey(sendPickDetailVo.getSendPickDetail().getDeliveryDetailId())){
				List<SendPickLocationVo> locations = map.get(sendPickDetailVo.getSendPickDetail().getDeliveryDetailId()).getPlanPickLocations();
				//在计划拣货库位添加id，类型，创建人,PickDetailId
				locations.stream().forEach(p->{
					p.getSendPickLocation().setPickLocationId(IdUtil.getUUID());
					p.getSendPickLocation().setMeasureUnit(sendPickDetailVo.getSendPickDetail().getMeasureUnit());
					p.getSendPickLocation().setPickType(Constant.PICK_TYPE_PLAN);
					p.getSendPickLocation().setCreatePerson(operator);
					p.getSendPickLocation().setUpdatePerson(operator);
					p.getSendPickLocation().setPickDetailId(sendPickDetailVo.getSendPickDetail().getPickDetailId());
					p.getSendPickLocation().defaultValue();
				});
				sendPickDetailVo.setPlanPickLocations(locations);
			}else{
				throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
			}
		
		}//foreach
	}
	
	public void countTotalQty(){
		if(sendPickDetailVoList != null && !sendPickDetailVoList.isEmpty()){
			double total = sendPickDetailVoList.stream().mapToDouble(p->p.getSendPickDetail().getOrderQty()).sum();
			sendPick.setPlanPickQty(total);
		}
	}
	//map<pickdetailid,detail>
	public Map<String,SendPickDetailVo> listToMap2(){
		if(this.sendPickDetailVoList == null || this.sendPickDetailVoList.isEmpty()) return null;
		Map<String,SendPickDetailVo> map = sendPickDetailVoList.stream()
				.collect(Collectors.toMap((p)->p.getSendPickDetail().getPickDetailId(), (p)->p));
		return map;
	}
	
	//map<DeliveryDetailId,SendPickDetailVo>
	public Map<String,SendPickDetailVo> listToMap(){		
		if(this.sendPickDetailVoList == null || this.sendPickDetailVoList.isEmpty()) return null;
		Map<String,SendPickDetailVo> map = sendPickDetailVoList.stream()
				.collect(Collectors.toMap((p)->p.getSendPickDetail().getDeliveryDetailId(), (p)->p));
		return map;
	}
	
	public void calOrder() throws Exception{
		calTotalPlanQty();
		calTotalPlanWeight();
		calTotalPlanVolume();
	}
	
	public void calPickQty() throws Exception{
		calTotalRealQty();
		calTotalRealWeight();
		calTotalRealVolume();
	}
	
	public void calTotalPlanQty(){
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getOrderQty()).sum();
		this.sendPick.setPlanPickQty(sum);		
	}
	
	public void calTotalPlanWeight() throws Exception{
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getOrderWeight()).sum();
		this.sendPick.setPlanPickWeight(sum);
	}
	
	public void calTotalPlanVolume() throws Exception{
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getOrderVolume()).sum();
		this.sendPick.setPlanPickVolume(sum);
	}
	
	public void calTotalRealQty(){
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getPickQty()).sum();
		this.sendPick.setRealPickQty(sum);		
	}
	
	public void calTotalRealWeight() throws Exception{
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getPickWeight()).sum();
		this.sendPick.setRealPickWeight(sum);
	}
	
	public void calTotalRealVolume() throws Exception{
		if(this.sendPickDetailVoList == null || sendPickDetailVoList.isEmpty()) return;
		double sum = sendPickDetailVoList.stream().mapToDouble(t->t.getSendPickDetail().getPickVolume()).sum();
		this.sendPick.setRealPickVolume(sum);
	}
	
	public void toFieldName(){
		if(this.sendPick.getDocType() == null) return;
		//发货单拣货
		if(this.sendPick.getDocType().intValue() == Constant.PICKTYPE_FORM_DELIVERY){
			fieldName = BillPrefix.DOCUMENT_PREFIX_PICKING_OUTGOING;
		}
		//波次拣货单
		else if(this.sendPick.getDocType().intValue() == Constant.PICKTYPE_FORM_WAVE){
			fieldName = BillPrefix.DOCUMENT_PREFIX_PICKING_WAVE;
		}
		//加工拣货单
		else if(this.sendPick.getDocType().intValue() == Constant.PICKTYPE_FORM_HANDLE){
			fieldName = BillPrefix.DOCUMENT_PREFIX_PICKING_MACHINING;
		}		
	}
	/**
	 * 产生拣货单号
	 * @param sendNoUtils
	 */
	public void createPickNo(ICommonService commonService){
		toFieldName();
		if(StringUtils.isEmpty(sendPick.getOrgId()) || fieldName == null) 
			throw new BizException(BizStatus.PICK_NO_CREATE_ERROR.getReasonPhrase());
		CommonVo common = new CommonVo(sendPick.getOrgId(), fieldName);
		String pickNo = commonService.getNo(common);
		sendPick.setPickNo(pickNo);
	}
	
	public void setOrgIdAndWareHouseId(){
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		if(loginUser == null) return;
		//获取企业id与仓库id
		String orgId = loginUser.getOrgId();
		String wareHouseId = LoginUtil.getWareHouseId();
		this.sendPick.setOrgId(orgId);
		this.sendPick.setWarehouseId(wareHouseId);
	}

	/**
	 * 属性 merchantShortName getter方法
	 * @return 属性merchantShortName
	 * @author 王通<br/>
	 */
	public String getMerchantShortName() {
		return merchantShortName;
	}

	/**
	 * 属性 merchantShortName setter方法
	 * @param merchantShortName 设置属性merchantShortName的值
	 * @author 王通<br/>
	 */
	public void setMerchantShortName(String merchantShortName) {
		this.merchantShortName = merchantShortName;
	}

	public String getPrintPickStatusComment() {
		return printPickStatusComment;
	}

	public void setPrintPickStatusComment(String printPickStatusComment) {
		this.printPickStatusComment = printPickStatusComment;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getExpressPrintStatus() {
		return expressPrintStatus;
	}

	public void setExpressPrintStatus(Integer expressPrintStatus) {
		this.expressPrintStatus = expressPrintStatus;
	}

	public Integer getPickPrintStatus() {
		return pickPrintStatus;
	}

	public void setPickPrintStatus(Integer pickPrintStatus) {
		this.pickPrintStatus = pickPrintStatus;
	}

	public String getExpressServiceCode() {
		return expressServiceCode;
	}

	public void setExpressServiceCode(String expressServiceCode) {
		this.expressServiceCode = expressServiceCode;
	}

	public String getExpressBillNo() {
		return expressBillNo;
	}

	public void setExpressBillNo(String expressBillNo) {
		this.expressBillNo = expressBillNo;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getSrcNo() {
		return srcNo;
	}

	public void setSrcNo(String srcNo) {
		this.srcNo = srcNo;
	}

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public List<BizException> getExceptions() {
		return exceptions;
	}
	

	public void setExceptions(List<BizException> exceptions) {
		this.exceptions = exceptions;
	}
}
