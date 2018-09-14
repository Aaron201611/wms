package com.yunkouan.wms.common.constant;

public enum ExtInterf {
	/**INTERFACE_QUERY_STOCK:库存查询接口**/
	INTERFACE_QUERY_STOCK("11"),

	/**INTERFACE_SEND_ORDER:原始订单推送接口**/
	INTERFACE_SEND_ORDER("12"),

	/**INTERFACE_QUERY_ORDER_STATUS:查询订单状态接口**/
	INTERFACE_QUERY_ORDER_STATUS("14"),

	/**INTERFACE_CANCEL_ORDER:取消用户订单接口**/
	INTERFACE_CANCEL_ORDER("15"),

	/**INTERFACE_SYNC_SKU:查询所有货品信息接口**/
	INTERFACE_QUERY_SKU("16"),
	
	/**INTERFACE_IMPORT_ASN:导入收货清单**/
	INTERFACE_IMPORT_ASN("17"),
	
	/**INTERFACE_IMPORT_SKU:导入货品**/
	INTERFACE_IMPORT_SKU("18"),
	
	/**INTERFACE_QUERY_STOCK:库存查询接口2**/
	INTERFACE_QUERY_STOCK_NEW("19"),
	
	/**INTERFACE_ADD_SHIFT:新增移位单**/
	INTERFACE_ADD_SHIFT("20"),
	
	/**INTERFACE_CANCEL_ASN收货单取消*/
	INTERFACE_CANCEL_ASN("21");

	private final String value;

	private ExtInterf(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}