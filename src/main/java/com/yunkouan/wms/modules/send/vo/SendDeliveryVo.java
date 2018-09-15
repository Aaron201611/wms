package com.yunkouan.wms.modules.send.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BillPrefix;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class SendDeliveryVo extends BaseVO {
	private static final long serialVersionUID = -1215592945913407054L;

	@Valid
	private SendDelivery sendDelivery;
	private List<DeliveryDetailVo> DeliveryDetailVoList = new ArrayList<DeliveryDetailVo>();
	private List<SendDeliveryLogVo> logList;

	private String ownerName;
	private String merchantShortName;
	private String waveNo;
	/**仓库名称*/
	private String warehouseName;
	private String warehouseNo;
	/**收货方*/
	private String receiverName;
	/**发货方*/
	private String senderName;
	private String provinceName;
	private String cityName;
	private String countyName;

	/**寄件人详情**/
	private MetaMerchant consignor;
	/**consignorName:寄件人（客商）名称**/
	private String consignorName;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private String beginTime;

	@JsonFormat(pattern="yyyy/MM/dd",locale = "zh" , timezone="GMT+8")
	private String endTime;
	private String sendEndTime;
	private String sendStartTime;

	private String docTypeComment;//单据类型说明

	private String dataFromComment;//数据来源说明

	private String statusComment;//状态说明

	private String transStatusComment;

	private BillPrefix fieldName;

	private Boolean waveIsNull;//波次单是否为空

	private List<Integer> deliveryStatusList;//发货单状态列表

	private List<String> notInDeliveryIdList;//不包含的发货单id列表

	private List<String> merchantIdList;

	private List<String> delDetailIds = new ArrayList<String>();//删除的明细id列表

	private List<String> loadConfirmIds = new ArrayList<String>();//装车确认id列表

	private List<String> warehouseIdList;

	private List<String> operIdList = new ArrayList<String>();

	private List<SendDeliveryMaterialVo> deliveryMaterialVoList = new ArrayList<SendDeliveryMaterialVo>();//辅材列表

	private String srcNoLike;

	private String deliveryNoLike;

	private Boolean hasAllPick;

	private Integer statusLessThan;

	private String carrierName;

	private Integer greaterThanStatus;

	private Integer lessThanStatus;

	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date greaterThanUpdateTime;

	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date lessThanUpdateTime;

	@JsonIgnore
	private Example example;

	private String expressName;

	private String skuNo;

	private String skuName;

	private String loctionNo;

	private SendPickVo sendPickVo;

	private String printExpressStatusComment;

	private String isPackageComment;

	private String isCheckWeightComment;

	private String isSendScanComment;

	private String orderByStr = "update_time desc";

	private String expressServiceCodeName;
	
	private String interceptStatusComment;
	
	private String inspectRedsultComment;

	/**
	 * 由于发送erp 和其他物流公司用的是同一个字段
	 */
	private String sendStatusDesc;

	public SendDeliveryVo(){
		sendDelivery = new SendDelivery();
		example = new Example(SendDelivery.class);
		example.createCriteria(); 
	}


	public SendDeliveryVo(SendDelivery sendDelivery) {
		setSendDelivery(sendDelivery);
	}


	public SendDelivery getSendDelivery() {
		return sendDelivery;
	}

	public void setSendDelivery(SendDelivery sendDelivery) {
		this.sendDelivery = sendDelivery;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(sendDelivery.getDocType() != null){
			this.docTypeComment = paramService.getValue(CacheName.DELIVERY_TYPE, sendDelivery.getDocType());
		}

		if(sendDelivery.getDataFrom() != null){
			this.dataFromComment = paramService.getValue(CacheName.ASN_DATAFROM, sendDelivery.getDataFrom());
		}

		if(sendDelivery.getDeliveryStatus() != null){
			this.statusComment = paramService.getValue(CacheName.SEND_STATUS, sendDelivery.getDeliveryStatus());
		}

		if(!StringUtil.isTrimEmpty(sendDelivery.getExpressServiceCode())){
			this.expressName = paramService.getValue(CacheName.EXPRESS_SERVICE_CODE, sendDelivery.getExpressServiceCode());
		}

		if ( StringUtil.isNotBlank(sendDelivery.getTransStatus())) {
			this.transStatusComment = paramService.getValue(CacheName.MSMQ_CHK_RESULT, this.sendDelivery.getTransStatus());
		}
		if(sendDelivery.getExpressBillPrintTimes() != null){
			if(sendDelivery.getExpressBillPrintTimes() > 0){
				this.printExpressStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 1);
			}else{
				this.printExpressStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 0);
			}
		}else{
			this.printExpressStatusComment = paramService.getValue(CacheName.PRINT_STATUS, 0);
		}
		if(sendDelivery.getIsPackage() != null){
			this.isPackageComment = paramService.getValue(CacheName.SCAN_STATUS, sendDelivery.getIsPackage());
		}else{
			this.isPackageComment = paramService.getValue(CacheName.SCAN_STATUS, 0);
		}

		if(sendDelivery.getIsCheckWeight() != null){
			this.isCheckWeightComment = paramService.getValue(CacheName.SCAN_STATUS, sendDelivery.getIsCheckWeight());
		}else{
			this.isCheckWeightComment = paramService.getValue(CacheName.SCAN_STATUS, 0);
		}
		if(sendDelivery.getIsSendScan() != null){
			this.isSendScanComment = paramService.getValue(CacheName.SCAN_STATUS, sendDelivery.getIsSendScan());
		}else{
			this.isSendScanComment = paramService.getValue(CacheName.SCAN_STATUS, 0);
		}
		if(sendDelivery.getInterceptStatus() != null){
			this.interceptStatusComment = paramService.getValue(CacheName.SEND_INTERCEPT_STATUS, sendDelivery.getInterceptStatus());
		}
		if(StringUtil.isNotBlank(sendDelivery.getInspectResult())){
			this.inspectRedsultComment = paramService.getValue(CacheName.INSPECT_RESULT, sendDelivery.getInspectResult());
		}
	}


	public List<DeliveryDetailVo> getDeliveryDetailVoList() {
		return DeliveryDetailVoList;
	}

	public void setDeliveryDetailVoList(List<DeliveryDetailVo> deliveryDetailVoList) {
		DeliveryDetailVoList = deliveryDetailVoList;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
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


	public String getDocTypeComment() {
		return docTypeComment;
	}


	public void setDocTypeComment(String docTypeComment) {
		this.docTypeComment = docTypeComment;
	}


	public String getDataFromComment() {
		return dataFromComment;
	}


	public void setDataFromComment(String dataFromComment) {
		this.dataFromComment = dataFromComment;
	}


	public String getStatusComment() {
		return statusComment;
	}


	public void setStatusComment(String statusComment) {
		this.statusComment = statusComment;
	}

	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getWaveNo() {
		return waveNo;
	}


	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}


	public List<String> getMerchantIdList() {
		return merchantIdList;
	}


	public void setMerchantIdList(List<String> merchantIdList) {
		this.merchantIdList = merchantIdList;
	}

	public List<String> getLoadConfirmIds() {
		return loadConfirmIds;
	}


	public void setLoadConfirmIds(List<String> loadConfirmIds) {
		this.loadConfirmIds = loadConfirmIds;
	}

	public List<SendDeliveryMaterialVo> getDeliveryMaterialVoList() {
		return deliveryMaterialVoList;
	}

	public void setDeliveryMaterialVoList(List<SendDeliveryMaterialVo> deliveryMaterialVoList) {
		this.deliveryMaterialVoList = deliveryMaterialVoList;
	}


	public String getSrcNoLike() {
		return srcNoLike;
	}


	public void setSrcNoLike(String srcNoLike) {
		this.srcNoLike = srcNoLike;
	}


	public String getDeliveryNoLike() {
		return deliveryNoLike;
	}


	public void setDeliveryNoLike(String deliveryNoLike) {
		this.deliveryNoLike = deliveryNoLike;
	}


	public Date getGreaterThanUpdateTime() {
		return greaterThanUpdateTime;
	}


	public void setGreaterThanUpdateTime(Date greaterThanUpdateTime) {
		this.greaterThanUpdateTime = greaterThanUpdateTime;
	}


	public Date getLessThanUpdateTime() {
		return lessThanUpdateTime;
	}


	public void setLessThanUpdateTime(Date lessThanUpdateTime) {
		this.lessThanUpdateTime = lessThanUpdateTime;
	}


	public String getExpressName() {
		return expressName;
	}


	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}


	@JsonIgnore
	public Example getExample() {
		return example;
	}

	@JsonIgnore
	public void setExample(Example example) {
		this.example = example;
	}

	@JsonIgnore
	public Criteria getCriteria(int n){
		Criteria c = example.getOredCriteria().get(n);
		return c;
	}

	public SendDeliveryVo getCondition(int n){
		example.setOrderByClause(orderByStr);
		Criteria c = example.getOredCriteria().get(n);
//		Criteria c2 = example.createCriteria();
//		example.or(c2);
		if(sendDelivery.getDeliveryStatus() != null){
			c.andEqualTo("deliveryStatus", sendDelivery.getDeliveryStatus());
//			c2.andEqualTo("deliveryStatus", sendDelivery.getDeliveryStatus());
		}
		if(sendDelivery.getInterceptStatus() != null){
			c.andEqualTo("interceptStatus", sendDelivery.getInterceptStatus());
//			c2.andEqualTo("interceptStatus", sendDelivery.getInterceptStatus());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getOwner())){
			c.andEqualTo("owner",sendDelivery.getOwner());
//			c2.andEqualTo("owner",sendDelivery.getOwner());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getSender())){
			c.andEqualTo("sender",sendDelivery.getSender());
//			c2.andEqualTo("sender",sendDelivery.getSender());
		}
		//		if(StringUtils.isNotEmpty(sendDelivery.getReceiver())){
		//			c.andEqualTo("receiver",sendDelivery.getReceiver());
		//		}
		if(StringUtils.isNotEmpty(sendDelivery.getContactPerson())){
			c.andEqualTo("contactPerson",sendDelivery.getContactPerson());
//			c2.andEqualTo("contactPerson",sendDelivery.getContactPerson());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getContactPhone())){
			c.andEqualTo("contactPhone",sendDelivery.getContactPhone());
//			c2.andEqualTo("contactPhone",sendDelivery.getContactPhone());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getProvince())){
			c.andEqualTo("province",sendDelivery.getProvince());
//			c2.andEqualTo("province",sendDelivery.getProvince());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getCity())){
			c.andEqualTo("city",sendDelivery.getCity());
//			c2.andEqualTo("city",sendDelivery.getCity());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getCounty())){
			c.andEqualTo("county",sendDelivery.getCounty());
//			c2.andEqualTo("county",sendDelivery.getCounty());
		}
		if(sendDelivery.getDocType() != null){
			c.andEqualTo("docType",sendDelivery.getDocType());
//			c2.andEqualTo("docType",sendDelivery.getDocType());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getSrcNo())){
			c.andEqualTo("srcNo",sendDelivery.getSrcNo());
//			c2.andEqualTo("srcNo",sendDelivery.getSrcNo());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getDeliveryNo())){
			c.andEqualTo("deliveryNo",sendDelivery.getDeliveryNo());
//			c2.andEqualTo("deliveryNo",sendDelivery.getDeliveryNo());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getExpressServiceCode())){
			c.andEqualTo("expressServiceCode",sendDelivery.getExpressServiceCode());
//			c2.andEqualTo("expressServiceCode",sendDelivery.getExpressServiceCode());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getExpressBillNo())){
			c.andEqualTo("expressBillNo",sendDelivery.getExpressBillNo());
//			c2.andEqualTo("expressBillNo",sendDelivery.getExpressBillNo());
		}
		if(waveIsNull == null && StringUtils.isNotEmpty(sendDelivery.getWaveId())){
			c.andEqualTo("waveId",sendDelivery.getWaveId());
//			c2.andEqualTo("waveId",sendDelivery.getWaveId());
		}
		if(waveIsNull != null && waveIsNull){
			c.andCondition("wave_id is null");
//			c2.andCondition("wave_id is null");
		}
		if(deliveryStatusList != null && !deliveryStatusList.isEmpty()){
			c.andIn("deliveryStatus", deliveryStatusList);
//			c2.andIn("deliveryStatus", deliveryStatusList);
		}
		if(loadConfirmIds != null && !loadConfirmIds.isEmpty()){
			c.andIn("deliveryId", loadConfirmIds);
//			c2.andIn("deliveryId", loadConfirmIds);
		}
		if(notInDeliveryIdList != null && !notInDeliveryIdList.isEmpty()){
			c.andNotIn("deliveryId", notInDeliveryIdList);
//			c2.andNotIn("deliveryId", notInDeliveryIdList);
		}
		if(greaterThanStatus != null){
			c.andGreaterThan("deliveryStatus", greaterThanStatus);
//			c2.andGreaterThan("deliveryStatus", greaterThanStatus);
		}
		if(lessThanStatus != null){
			c.andLessThan("deliveryStatus", lessThanStatus);
//			c2.andLessThan("deliveryStatus", lessThanStatus);
		}
		if(StringUtils.isNotEmpty(beginTime)){
			c.andGreaterThanOrEqualTo("orderTime", beginTime+" 00:00:00");
//			c2.andGreaterThanOrEqualTo("orderTime", beginTime+" 00:00:00");
		}
		if (StringUtils.isNotEmpty(endTime)) {
			c.andLessThanOrEqualTo("orderTime", endTime+" 23:59:59");
//			c2.andLessThanOrEqualTo("orderTime", endTime+" 23:59:59");
		}
		if(StringUtils.isNotEmpty(sendStartTime)){
			c.andGreaterThanOrEqualTo("shipmentTime", sendStartTime);
//			c2.andGreaterThanOrEqualTo("shipmentTime", sendStartTime);
		}
		if (StringUtils.isNotEmpty(sendEndTime)) {
			c.andLessThanOrEqualTo("shipmentTime", sendEndTime);
//			c2.andLessThanOrEqualTo("shipmentTime", sendEndTime);
		}
		if(StringUtils.isNotEmpty(sendDelivery.getOrgId())){
			c.andCondition("org_id=", sendDelivery.getOrgId());
//			c2.andCondition("org_id=", sendDelivery.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendDelivery.getWarehouseId())){
			c.andCondition("warehouse_id=", sendDelivery.getWarehouseId());
//			c2.andCondition("warehouse_id=", sendDelivery.getWarehouseId());
		}
		if(merchantIdList != null && !merchantIdList.isEmpty()){
			c.andIn("owner", merchantIdList);
//			c2.andIn("owner", merchantIdList);
		}
		if(!StringUtil.isTrimEmpty(provinceName)){
			c.andLike("province", '%'+provinceName+'%');
//			c2.andLike("province", '%'+provinceName+'%');
		}
		if(!StringUtil.isTrimEmpty(sendDelivery.getReceiver())){
			c.andLike("receiver", '%'+sendDelivery.getReceiver()+'%');
//			c2.andLike("receiver", '%'+sendDelivery.getReceiver()+'%');
		}
		if(!StringUtil.isTrimEmpty(cityName)){
			c.andLike("city", '%'+cityName+'%');
//			c2.andLike("city", '%'+cityName+'%');
		}
		if(!StringUtil.isTrimEmpty(countyName)){
			c.andLike("county", '%'+countyName+'%');
//			c2.andLike("county", '%'+countyName+'%');
		}
		if(!StringUtil.isTrimEmpty(srcNoLike)){
			c.andLike("srcNo",'%'+srcNoLike+'%');
//			c2.andLike("srcNo",'%'+srcNoLike+'%');
		}
		if(!StringUtil.isTrimEmpty(deliveryNoLike)){
			c.andLike("deliveryNo",'%'+deliveryNoLike+'%');
//			c2.andLike("deliveryNo",'%'+deliveryNoLike+'%');
		}
		if(hasAllPick != null && hasAllPick){
			c.andCondition("order_qty > pick_qty");
//			c2.andCondition("order_qty > pick_qty");
		}
		if(statusLessThan != null){
			c.andLessThan("deliveryStatus", statusLessThan);
//			c2.andLessThan("deliveryStatus", statusLessThan);
		}
		if (greaterThanUpdateTime != null) {
			c.andGreaterThanOrEqualTo("updateTime", greaterThanUpdateTime);
//			c2.andGreaterThanOrEqualTo("updateTime", greaterThanUpdateTime);
		}
		if (lessThanUpdateTime != null) {
			c.andLessThanOrEqualTo("updateTime", lessThanUpdateTime);
//			c2.andLessThanOrEqualTo("updateTime", lessThanUpdateTime);
		}
		if(warehouseIdList != null && !warehouseIdList.isEmpty()){
			c.andIn("warehouseId", warehouseIdList);
//			c2.andIn("warehouseId", warehouseIdList);
		}
		if(sendDelivery.getExpressBillPrintTimes() != null){
			if(sendDelivery.getExpressBillPrintTimes() > 0){
				c.andGreaterThanOrEqualTo("expressBillPrintTimes", sendDelivery.getExpressBillPrintTimes());
//				c2.andGreaterThanOrEqualTo("expressBillPrintTimes", sendDelivery.getExpressBillPrintTimes());
			}else{
				c.andIsNull("expressBillPrintTimes");
//				c2.andIsNull("expressBillPrintTimes");
			}
		}
		if(!StringUtil.isTrimEmpty(sendDelivery.getDeliveryNo1())){
			c.andLike("deliveryNo1", "%"+sendDelivery.getDeliveryNo1()+"%");
//			c2.andLike("deliveryNo1", "%"+sendDelivery.getDeliveryNo1()+"%");
		}
//		if(StringUtils.isNotEmpty(sendStatusDesc)){
//			List<Integer>list=new ArrayList<>();
//			list.add(Constant.SEND_JUST_ERP_SUCCESS);
//			list.add(Constant.SEND_WULIU_ERP_SUCCESS);
//			if("是".equals(sendStatusDesc)){
//
//				c.andIn("sendStatus", list);
//				c2.andIn("sendStatus", list);
//			}else if("否".equals(sendStatusDesc)){
//				c.andNotIn("sendStatus", list);
//				c2.andIsNull("sendStatus");
//			}
//		}
		return this;
	}

	public SendDeliveryVo andContion(Criteria c,String condition){
		c.andCondition(condition);
		return this;
	}

	//	public SendDeliveryVo defaultCondition(Criteria c){
	//		if(StringUtils.isNotEmpty(sendDelivery.getOrgId())){
	//			c.andCondition("org_id=", sendDelivery.getOrgId());
	//		}
	//		if(StringUtils.isNotEmpty(sendDelivery.getWarehouseId())){
	//			c.andCondition("warehouse_id=", sendDelivery.getWarehouseId());
	//		}
	//		return this;
	//	}

	public SendDeliveryVo or(){
		example.or();
		return this;
	}

	public void transFormToVoList(List<SendDeliveryDetail> detailList){
		detailList.stream().forEach(t->{
			DeliveryDetailVo vo = new DeliveryDetailVo();
			vo.setDeliveryDetail(t);
			this.DeliveryDetailVoList.add(vo);
		});
	}

	public List<String> toDetailIdList(){
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return null;

		List<String> detailIds = DeliveryDetailVoList.stream().map(p->p.getDeliveryDetail().getDeliveryDetailId()).collect(Collectors.toList());

		return detailIds;
	}

	//统计订单数量
	public void calTotalOrder() throws Exception{
		calTotalOrderQty();
		calTotalOrderWeight();
		calTotalOrderVolume();
	}

	public void calTotalOrderQty(){
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getOrderQty()).sum();
		this.sendDelivery.setOrderQty(sum);		
	}

	public void calTotalOrderWeight() throws Exception{
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getOrderWeight()).sum();
		this.sendDelivery.setOrderWeight(sum);
	}

	public void calTotalOrderVolume() throws Exception{
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getOrderVolume()).sum();
		this.sendDelivery.setOrderVolume(sum);
	}

	/**
	 * 统计拣货数量
	 * @throws Exception 
	 */
	public void calTotalPick() throws Exception{
		calPickQty();
		calPickWeight();
		calPickVolume();
	}

	public void calPickQty(){
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getPickQty()).sum();
		this.sendDelivery.setPickQty(sum);
	}

	public void calPickWeight() throws Exception{
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getPickWeight()).sum();
		this.sendDelivery.setPickWeight(sum);
	}

	public void calPickVolume() throws Exception{
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getPickVolume()).sum();
		this.sendDelivery.setPickVolume(sum);
	}

	/**
	 * 统计货品种类数量
	 */
	public void calSkuQty(){
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		this.sendDelivery.setOrderSkuQty(this.DeliveryDetailVoList.size());
	}

	public String getValueInCache(String cacheName,String key){
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		String value = paramService.getValue(cacheName, key);
		return value;
	}


	public Map<String,DeliveryDetailVo> listToMap(){
		Map<String,DeliveryDetailVo> map = DeliveryDetailVoList.stream().collect(Collectors
				.toMap((p)->p.getDeliveryDetail().getDeliveryDetailId(),(p)->p));

		return map;
	}

	/**
	 * 检查发货明细的skuid与批次号是否重复
	 * @return
	 */
	public boolean skuIsDuplicate(){
		if(DeliveryDetailVoList != null && !DeliveryDetailVoList.isEmpty()){
			for(int i= 0;i<DeliveryDetailVoList.size();i++){
				SendDeliveryDetail detail_1 = DeliveryDetailVoList.get(i).getDeliveryDetail();

				for(int j=i+1;j<DeliveryDetailVoList.size();j++){
					SendDeliveryDetail detail_2 = DeliveryDetailVoList.get(j).getDeliveryDetail();
					if(detail_1.getSkuId().equals(detail_2.getSkuId()) && equal(detail_1.getBatchNo(), detail_2.getBatchNo())){
						return true;
					}
				}//for				
			}//for
		}//if
		return false;
	}

	private static boolean equal(String bathNo1, String bathNo2) {
		if(bathNo1 == null && bathNo2 == null) return true;
		if(bathNo1 == null && bathNo2 != null) return false;
		if(bathNo1 != null && bathNo2 == null) return false;
		return bathNo1.trim().equals(bathNo2.trim());
	}

	public BillPrefix getFieldName() {
		return fieldName;
	}


	public void setFieldName(BillPrefix fieldName) {
		this.fieldName = fieldName;

	}

	public Boolean getWaveIsNull() {
		return waveIsNull;
	}


	public void setWaveIsNull(Boolean waveIsNull) {
		this.waveIsNull = waveIsNull;
	}


	public List<Integer> getDeliveryStatusList() {
		return deliveryStatusList;
	}


	public void setDeliveryStatusList(List<Integer> deliveryStatusList) {
		this.deliveryStatusList = deliveryStatusList;
	}	


	public List<String> getNotInDeliveryIdList() {
		return notInDeliveryIdList;
	}


	public void setNotInDeliveryIdList(List<String> notInDeliveryIdList) {
		this.notInDeliveryIdList = notInDeliveryIdList;
	}

	public List<String> getDelDetailIds() {
		return delDetailIds;
	}

	public void setDelDetailIds(List<String> delDetailIds) {
		this.delDetailIds = delDetailIds;
	}

	public Boolean getHasAllPick() {
		return hasAllPick;
	}


	public void setHasAllPick(Boolean hasAllPick) {
		this.hasAllPick = hasAllPick;
	}

	public Integer getStatusLessThan() {
		return statusLessThan;
	}


	public void setStatusLessThan(Integer statusLessThan) {
		this.statusLessThan = statusLessThan;
	}


	public String getCarrierName() {
		return carrierName;
	}


	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}


	public Integer getGreaterThanStatus() {
		return greaterThanStatus;
	}


	public void setGreaterThanStatus(Integer greaterThanStatus) {
		this.greaterThanStatus = greaterThanStatus;
	}


	public Integer getLessThanStatus() {
		return lessThanStatus;
	}


	public void setLessThanStatus(Integer lessThanStatus) {
		this.lessThanStatus = lessThanStatus;
	}


	public void calReviewQty(){
		if(this.DeliveryDetailVoList == null || DeliveryDetailVoList.isEmpty()) return;
		double sum = DeliveryDetailVoList.stream().mapToDouble(t->t.getDeliveryDetail().getReviewQty()).sum();
		this.sendDelivery.setReviewQty(sum);
	}


	public void toFieldName(){
		if(this.sendDelivery.getDocType() == null) return;
		//普通出仓
		if(sendDelivery.getDocType().intValue() == Constant.DELIVERY_TYPE_NORMAL_OUT){
			this.fieldName = BillPrefix.DOCUMENT_PREFIX_OUTGOING_NORMAL;
		}
		//转仓出库
		else if(sendDelivery.getDocType().intValue() == Constant.DELIVERY_TYPE_SWITCH_OUT){
			this.fieldName = BillPrefix.DOCUMENT_PREFIX_OUTGOING_SWITCH;
		}
		//其他出库
		else if(sendDelivery.getDocType().intValue() == Constant.DELIVERY_TYPE_ORTHER_OUT){
			this.fieldName = BillPrefix.DOCUMENT_PREFIX_OUTGOING_OTHER;
		}
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


	public String getConsignorName() {
		return consignorName;
	}


	public void setConsignorName(String consignorName) {
		this.consignorName = consignorName;
	}


	public String getWarehouseNo() {
		return warehouseNo;
	}


	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}


	public String getTransStatusComment() {
		return transStatusComment;
	}


	public void setTransStatusComment(String transStatusComment) {
		this.transStatusComment = transStatusComment;
	}


	public List<String> getOperIdList() {
		return operIdList;
	}


	public void setOperIdList(List<String> operIdList) {
		this.operIdList = operIdList;
	}


	public String getSkuNo() {
		return skuNo;
	}


	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}


	public MetaMerchant getConsignor() {
		return consignor;
	}


	public void setConsignor(MetaMerchant consignor) {
		this.consignor = consignor;
	}


	public List<SendDeliveryLogVo> getLogList() {
		return logList;
	}


	public void setLogList(List<SendDeliveryLogVo> logList) {
		this.logList = logList;
	}


	public String getSkuName() {
		return skuName;
	}


	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}


	public String getLoctionNo() {
		return loctionNo;
	}


	public void setLoctionNo(String loctionNo) {
		this.loctionNo = loctionNo;
	}


	public List<String> getWarehouseIdList() {
		return warehouseIdList;
	}


	public void setWarehouseIdList(List<String> warehouseIdList) {
		this.warehouseIdList = warehouseIdList;
	}


	public SendPickVo getSendPickVo() {
		return sendPickVo;
	}


	public void setSendPickVo(SendPickVo sendPickVo) {
		this.sendPickVo = sendPickVo;
	}


	public String getPrintExpressStatusComment() {
		return printExpressStatusComment;
	}


	public void setPrintExpressStatusComment(String printExpressStatusComment) {
		this.printExpressStatusComment = printExpressStatusComment;
	}


	public String getIsPackageComment() {
		return isPackageComment;
	}


	public void setIsPackageComment(String isPackageComment) {
		this.isPackageComment = isPackageComment;
	}


	public String getIsCheckWeightComment() {
		return isCheckWeightComment;
	}


	public void setIsCheckWeightComment(String isCheckWeightComment) {
		this.isCheckWeightComment = isCheckWeightComment;
	}


	public String getIsSendScanComment() {
		return isSendScanComment;
	}


	public void setIsSendScanComment(String isSendScanComment) {
		this.isSendScanComment = isSendScanComment;
	}


	public String getOrderByStr() {
		return orderByStr;
	}


	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}


	public String getSendEndTime() {
		return sendEndTime;
	}


	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}


	public String getSendStartTime() {
		return sendStartTime;
	}


	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}


	public String getSendStatusDesc() {
		return sendStatusDesc;
	}



	public void setSendStatusDesc(String sendStatusDesc) {
		this.sendStatusDesc = sendStatusDesc;
	}


	public String getExpressServiceCodeName() {

		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if(this.sendDelivery.getExpressServiceCode()!=null){
			this.expressServiceCodeName = paramService.getValue(CacheName.EXPRESS_SERVICE_CODE, this.sendDelivery.getExpressServiceCode());
		}
		return expressServiceCodeName;
	}



	public void setExpressServiceCodeName(String expressServiceCodeName) {
		this.expressServiceCodeName = expressServiceCodeName;
	}


	public String getInterceptStatusComment() {
		return interceptStatusComment;
	}


	public void setInterceptStatusComment(String interceptStatusComment) {
		this.interceptStatusComment = interceptStatusComment;
	}


	public String getInspectRedsultComment() {
		return inspectRedsultComment;
	}


	public void setInspectRedsultComment(String inspectRedsultComment) {
		this.inspectRedsultComment = inspectRedsultComment;
	}
	
	


}