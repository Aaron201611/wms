package com.yunkouan.wms.modules.assistance.vo;

import java.util.ArrayList;
import java.util.List;

public class MessageBody {
	private List<WarehouseHead> WarehouseDocument = new ArrayList<WarehouseHead>();
	private WarehouseRtnDocument WarehouseRtnDocument = new WarehouseRtnDocument();

	public List<WarehouseHead> getWarehouseDocument() {
		return WarehouseDocument;
	}

	public void setWarehouseDocument(List<WarehouseHead> warehouseDocument) {
		WarehouseDocument = warehouseDocument;
	}

	public WarehouseRtnDocument getWarehouseRtnDocument() {
		return WarehouseRtnDocument;
	}

	public void setWarehouseRtnDocument(WarehouseRtnDocument warehouseRtnDocument) {
		WarehouseRtnDocument = warehouseRtnDocument;
	}
}