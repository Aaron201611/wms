package com.yunkouan.wms.common.constant;

public enum BizStatus {
	
	/**
	 * 参数错误
	 */
	PARAMETER_ERROR(540,"parameter_error"),
	
	/**
	 * 
	 * 参数为空
	 */
	PARAMETER_IS_NULL(541,"parameter_is_null"),
	
	/**
	 * 类型为空
	 */
	TYPE_IS_NULL(543,"type_is_null"),
	/**
	 * 数据为空
	 */
	DATA_IS_NULL(544,"data_is_null"),

	/**
	 * 发货单状态不是打开
	 */
	SEND_DELIVERY_STATUS_IS_NOT_OPEN(550,"Send_Delivery_is_not_open"),
	
	/**
	 * 发货单关联波次单
	 */
	SEND_DELIVERY_ASSOCIATE_WAVE(551,"Send_Delivery_associate_wave"),
	
	/**
	 * 发货单明细为空
	 */
	SEND_DELIVERYDETAIL_IS_NULL(552,"Delivery_detail_is_empty"),
	
	/**
	 * 发货单拆分错误
	 */
	SEND_DELIVERY_SPLIT_ERROR(553,"Send_delivery_split_error"),
	
	/**
	 * 发货单状态不是生效
	 */
	SEND_DELIVERY_STATUS_IS_NOT_ACTIVE(554,"Send_delivery_status_is_not_active"),
	/**
	 * 发货单状态不是部分拣货和全部拣货
	 */
	SEND_DELIVERY_STATUS_IS_NOT_PARTPICK_AND_ALLPICK(555,"Send_delivery_status_is_not_partpick_and_allpick"),
	/**
	 * sku与批次号重复
	 */
	SKU_AND_BATCHNO_IS_DUPLIPCATIVE(556,"sku_and_batchno_isduplicative"),
	/**
	 * 拆分的发货单不能修改
	 */
	SPLIT_DELIVERY_CANNOT_MODIFY(557,"split_delivery_cannot_modify"),
	/**
	 * 发货单no创建异常
	 */
	DELIVERY_NO_CREATE_ERROR(558,"delivery_no_create_error"),	
	/**
	 * 发货单单数未做拆分
	 */
	DELIVERY_NUM_NO_SPLIT(549,"delivery_num_no_split"),
	/**
	 * 运单号为空
	 */
	EXPRESS_BILL_NO_IS_NULL(548,"express_bill_no_is_null"),
	/**
	 * 发货单id为空
	 */
	DELIVERY_ID_IS_NULL(601,"delivery_id_is_null"),
	/**
	 * 发货单不存在
	 */
	DELIVERY_IS_NOT_EXIT(602,"delivery_is_not_exit"),
	/**
	 * 发货单不能取消
	 */
	DELIVERY_CAN_NOT_CANCEL(603,"delivery_can_not_cancel"),
	/**
	 * 辅材为空
	 */
	MATERIAL_IS_NULL(604,"materials_is_null"),
	/**
	 * 发货明细货品代码为空
	 */
	DELIVERY_DETAIL_SKUNO_IS_NULL(607,"delivery_detail_skuno_is_null"),
	/**
	 * 货品不存在
	 */
	SKU_IS_NOT_EXISTS(608,"sku_is_not_exists"),
	/**
	 * 复核数量与订单数量不一致
	 */
	REVIEW_QTY_NOT_EQUAL_ORDER_QTY(605,"review_qty_not_equal_order_qty"),
	/**
	 * 发货单状态不是已打包
	 */
	DELIVERY_STATUS_IS_NOT_PACKAGED(606,"delivery_status_is_not_packaged"),
	/**
	 * 发货单状态不是已打包或查验中
	 */
	DELIVERY_STATUS_IS_NOT_PACK_OR_CHECK(606,"delivery_status_is_not_pack_or_check"),
	/**
	 * 运单号不能重复
	 */
	EXPRESS_BILLNO_IS_DUPLICATED(606,"express_billNo_is_duplicated"),
	/**
	 * 发货单源单号重复
	 */
	DELIVERY_SRCNO_IS_REDUPLICATED(559,"delivery_srcno_is_reduplicated"),
	/**
	 * 拣货单状态不是打开
	 */
	PICK_STATUS_IS_NOT_OPEN(561,"Pick_status_is_not_open"),
	
	/**
	 * 拣货单状态不是打开
	 */
	PICK_STATUS_IS_NOT_ACTIVE(562,"Pick_status_is_not_active"),
	
	/**
	 * 计划拣货库位为空
	 */
	PLAN_PICK_NUMANDLOCATION_ERROR(563,"plan_pick_num_and_location_error"),
	
	/**
	 * 实际拣货库位为空
	 */
	REAL_PICK_NUMANDLOCATION_ERROR(564,"real_pick_num_and_location_error"),
	
	/**
	 * 拣货货单拆分错误
	 */
	SEND_PICK_SPLIT_ERROR(565,"pick_split_error"),
	
	/**
	 * 还有订单数量没有分配
	 */
	SOME_ORDERS_NO_LOCATION(566,"some_orders_no_location"),
	
	/**
	 * 子拣货单状态不是打开
	 */
	SUB_PICK_IS_NOT_OPEN(567,"sub_pick_is_not_open"),
	
	/**
	 * 子拣货单已拆分
	 */
	SUB_PICK_HAS_SPLIT(578,"sub_pick_has_split"),
	
	/**
	 * 没有入库时间
	 */
	STOCK_HAVE_NO_INDATE(568,"stock_have_no_indate"),
	
	/**
	 * 订单数量与库位拣货数量不一致
	 */
	ORDER_QTY_NOT_EQUAL_PLANPICK_QTY(569,"order_qty_not_equal_planpick_qty"),
	/**
	 * 订单重量与库位拣货重量不一致
	 */
	ORDER_WEIGHT_NOT_EQUAL_PLANPICK_WEIGHT(570,"order_weight_not_equal_planpick_weight"),
	/**
	 * 订单体积与库位拣货体积不一致
	 */
	ORDER_VOLUME_NOT_EQUAL_PLANPICK_VOLUME(571,"order_volume_not_equal_planpick_volume"),
	/**
	 * 没有子单
	 */
	NO_SUB_SHEET(572,"no_sub_sheet"),
	/**
	 * 拣货单数据类型为空
	 */
	PICK_DOCTYPE_IS_NULL(573,"pick_doctype_is_null"),
	/**
	 * 拆分的拣货单不能修改
	 */
	SPLIT_PICK_CANNOT_MODIFY(574,"split_pick_cannot_modify"),
	/**
	 * 拣货单no创建异常
	 */
	PICK_NO_CREATE_ERROR(575,"pick_no_create_error"),
	/**
	 * 拣货单单数未做拆分
	 */
	PICK_NUM_NO_SPLIT(576,"pick_num_no_split"),
	/**
	 * 库存不足
	 */
	PICK_INVSTOCK_NOT_ENOUGH(577,"pick_invstock_not_enough"),
	/**
	 * 波次单状态不是打开
	 */
	WAVE_STATUS_IS_NOT_OPEN(580,"wave_status_is_not_open"),
	/**
	 * 波次单状态不是生效
	 */
	WAVE_STATUS_IS_NOT_ENABLE(581,"wave_status_is_not_enable"),	
	/**
	 * 发货单数量太少
	 */
	DELIVERY_QTY_LESS(582,"deliery_qty_less"),
	/**
	 * 出租状态不是打开
	 */
	PARKRENT_STATUS_IS_NOT_OPEN(585,"park_status_is_not_open"),
	/**
	 * 出租状态不是生效
	 */
	PARKRENT_STATUS_IS_NOT_ENABLE(586,"park_status_is_not_enable"),

	/**未指定仓库*/
	WAREHOUSE_UNDEFINE(590, "valid_login_warehouse_undefine"),
	
	/**
	 * 仓库已出租
	 */
	PARKRENT_WAREHOUSE_HAS_RENTED(591,"parkrent_warehouse_has_rented"),
	
	/**
	 * 任务状态不是打开
	 */
	TASK_STATUS_IS_NOT_OPEN(592,"task_status_is_not_open"),
	/**
	 * 任务状态不是执行
	 */
	TASK_STATUS_IS_NOT_EXEC(593,"task_status_is_not_exec"),
	/**
	 * 任务状态不是执行或打开，不能进行此操作
	 */
	TASK_STATUS_IS_NOT_EXEC_OR_OPEN(597,"task_status_is_not_exec_or_open"),
	
	/**
	 * 发货单不是 全部拣货状态
	 */
	DELIVERY_STATUS_IS_NOT_ALLPICK(594,"delivery_status_is_not_allpick"),
	/**
	 * 作业人员未选
	 */
	TASK_OPPERSON_IS_NULL(595,"task_opperson_is_null"),
	/**
	 * 任务正在执行，不能拒绝
	 */
	TASK_IS_EXEC_NOCANCEL(596,"task_is_exec_nocancel"),
	/**
	 * 面单不存在
	 */
	EXPRESS_BILL_NOT_EXIST(597,"express_bill_not_exist"),

	/**
	 * 服务接口调用失败
	 */
	SERVICE_INVOKE_ERROR(599,"service_invoke_error");
	
		
	
	private final int value;

	private final String reasonPhrase;
	
	BizStatus(int value,String reasonPhrase){
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}
	
	public int value(){
		return this.value;
	}

	public int getValue() {
		return value;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}
	
	
}
