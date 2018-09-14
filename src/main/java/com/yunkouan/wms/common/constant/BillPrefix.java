package com.yunkouan.wms.common.constant;

public enum BillPrefix {
	
	/**
	 * 普通入库
	 */
	DOCUMENT_PREFIX_INCOMING_NORMAL("IN"),
	/**
	 * 转仓入库
	 */
	DOCUMENT_PREFIX_INCOMING_TRANSFER("IT"),
	/**
	 * 退货入库
	 */
	DOCUMENT_PREFIX_INCOMING_BACK("IB"),
	/**
	 * 拒收入库
	 */
	DOCUMENT_PREFIX_INCOMING_REJECTION("IR"),
	/**
	 * 其他入库
	 */
	DOCUMENT_PREFIX_INCOMING_OTHER("IO"),
	/**
	 * 收货上架
	 */
	DOCUMENT_PREFIX_PUTAWAY_RECEIPT("PR"),
	/**
	 * 加工上架
	 */
	DOCUMENT_PREFIX_PUTAWAY_MACHINING("PM"),

	/**
	 * 普通出库
	 */
	DOCUMENT_PREFIX_OUTGOING_NORMAL("ON"),
	/**
	 * 转仓出库
	 */
	DOCUMENT_PREFIX_OUTGOING_SWITCH("OT"),
	/**
	 * 其他出库
	 */
	DOCUMENT_PREFIX_OUTGOING_OTHER("OO"),
	/**
	 * 领用出库
	 */
	DOCUMENT_PREFIX_OUTGOING_TAKE("OK"),
	/**
	 * 样品出库
	 */
	DOCUMENT_PREFIX_OUTGOING_SAMPLE("OS"),
	/**
	 * 调拨出库
	 */
	DOCUMENT_PREFIX_OUTGOING_TRANSFER("OF"),
	/**
	 * 爆款出库
	 */
	DELIVERY_TYPE_BURST_OUT("BO"),

	/**
	 * 发货拣货
	 */
	DOCUMENT_PREFIX_PICKING_OUTGOING("PO"),
	/**
	 * 波次拣货
	 */
	DOCUMENT_PREFIX_PICKING_WAVE("PW"),
	/**
	 * 加工拣货
	 */
	DOCUMENT_PREFIX_PICKING_MACHINING("PM"),
	/**
	 * 波次单
	 */
	DOCUMENT_PREFIX_WAVE_DOCUMENT("WD"),
	/**
	 * 库内移位
	 */
	DOCUMENT_PREFIX_MOVE_INTERNAL("MI"),
	/**
	 * 补货移位
	 */
	DOCUMENT_PREFIX_MOVE_REPLENISHMENT("MR"),
	/**
	 * 退货移位
	 */
	DOCUMENT_PREFIX_MOVE_REJECT("ME"),
	/**
	 * 拣货移位
	 */
	DOCUMENT_PREFIX_MOVE_PICK("MP"),
	/**
	 * 盘点单
	 */
	DOCUMENT_PREFIX_CHECK_STOCK("CD"),
	/**
	 * 装车单
	 */
	DOCUMENT_PREFIX_BILL_LOADING ("BL"),
	/**
	 * 调账单
	 */
	DOCUMENT_ADJUST_BILL("AB"),
	/**
	 * 异常单
	 */
	DOCUMENT_EXCEPTION("EX"),
	/**
	 * 盘点单
	 */
	DOCUMENT_COUNT("CO"),
	/**
	 * 包装编码
	 */
	DOCUMENT_PACK("PK"),
	/**
	 * 库位规格
	 */
	DOCUMENT_LOCATION_SPEC("LS"),
	/**
	 * 辅材
	 */
	DOCUMENT_MATERIAL("MA"),
	/**
	 * 货品前缀
	 */
	DOCUMENT_PREFIX_SKU("SK"),
	/**
	 * 货品后缀
	 */
	DOCUMENT_SUFFIX_SKU("SU"),
	/**
	 * 客商
	 */
	DOCUMENT_MERCHANT("MT"),
	/**
	 * 货品类型
	 */
	DOCUMENT_SKU_TYPE("ST"),
	/**
	 * 申报单
	 */
	APPLICATION_NO(""),
	/**
	 * 核放单
	 */
	EXAMINE_NO(""),
	/**
	 * 辅助系统文件流水
	 */
	DOCUMENT_PREFIX_ASSIS_FILE_NAME("FN"),
	/**
	 * 辅助系统消息流水
	 */
	DOCUMENT_PREFIX_ASSIS_MESSAGE_ID("AS"),
	/**
	 * 不良品调整
	 */
	DOCUMENT_REJECTS_CHANGE("RC"),
	;

	private final String prefix;

	private BillPrefix(String prefix){
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}