package com.yunkouan.wms.modules.intf.vo;

import java.util.List;
import com.yunkouan.base.BaseObj;

public class StockQueryVo extends BaseObj {
	private static final long serialVersionUID = 8379810992242573675L;

	private List<String> skuNoList;

	public List<String> getSkuNoList() {
		return skuNoList;
	}

	public void setSkuNoList(List<String> skuNoList) {
		this.skuNoList = skuNoList;
	}
}