package com.yunkouan.wms.common;

import com.yunkouan.util.MD5Util;

public class TestSign {
	public static void main(String[] args) {
		String data = "{\"applicationVo\":{\"applicationGoodVoList\":[{\"applicationGoodSkuVoList\":[],\"entity\":{\"curr\":\"149\",\"decPrice\":\"15.0\",\"decQty\":\"2.0\",\"decTotal\":\"30.0\",\"dutyMode\":\"\",\"gmodel\":\"\",\"gno\":\"\",\"goodsName\":\"婴儿除菌洗手液无香味\",\"hsCode\":\"3401300000\",\"originCountry\":\"\",\"qty1\":\"\",\"qty2\":\"\",\"remark\":\"\",\"unit\":\"007\",\"unit1\":\"\",\"unit2\":\"\",\"useTo\":\"\"}},{\"applicationGoodSkuVoList\":[],\"entity\":{\"curr\":\"149\",\"decPrice\":\"10.0\",\"decQty\":\"2.0\",\"decTotal\":\"20.0\",\"dutyMode\":\"\",\"gmodel\":\"\",\"gno\":\"\",\"goodsName\":\"儿童除菌洗手液450ml无香型替换装\",\"hsCode\":\"3401300000\",\"originCountry\":\"\",\"qty1\":\"\",\"qty2\":\"\",\"remark\":\"\",\"unit\":\"007\",\"unit1\":\"\",\"unit2\":\"\",\"useTo\":\"\"}}],\"entity\":{\"applyOrgCode\":\"\",\"applyOrgName\":\"\",\"applyPerson\":\"\",\"applyTime\":\"\",\"grossWt\":\"10\",\"guaranteeId\":\"\",\"guaranteeTotal\":\"\",\"guaranteeType\":\"\",\"iEFlag\":\"I\",\"inputOrgCode\":\"\",\"inputOrgName\":\"\",\"linkMan\":\"\",\"linkPhone\":\"\",\"netWt\":\"10\",\"outEntrpriseCode\":\"\",\"outEntrpriseName\":\"\",\"packNo\":\"1\",\"receiveDeliverOrgCode\":\"\",\"receiveDeliverOrgName\":\"\",\"relApplicationNo\":\"\",\"relDays\":\"\",\"relType\":\"\",\"requireGuaranteeTotal\":\"\",\"tradeCode\":\"\",\"tradeName\":\"\",\"wrapType\":\"190\"}},\"deliveryDetailVoList\":[{\"deliveryDetail\":{\"orderQty\":\"2.0000\"},\"skuNo\":\"L1L\"},{\"deliveryDetail\":{\"orderQty\":\"2.0000\"},\"skuNo\":\"L2L\"}],\"sendDelivery\":{\"cit\":\"\",\"clerk\":\"\",\"consignor\":\"\",\"consignorAddress\":\"\",\"consignorPhone\":\"\",\"contactAddress\":\"杭州市人民大厦402\",\"contactPerson\":\"jjr001\",\"contactPhone\":\"13100000000\",\"county\":\"\",\"customer\":\"\",\"department\":\"\",\"docType\":\"200\",\"expressBillNo\":\"w2018042704\",\"expressServiceCode\":\"OTHER\",\"freightCharge\":\"0.0000\",\"grossWeight\":\"10\",\"insuranceCharge\":\"0.0000\",\"note\":\"\",\"orderTime\":\"2018/04/27 00:00:00\",\"province\":\"\",\"sendCity\":\"\",\"sendCounty\":\"\",\"sendProvince\":\"\",\"srcNo\":\"zwydd272\"}}";
		System.out.println(md5(data, "zyzhqcwhwms"));
	}

	private static String md5(String data, String key) {
	   String sign = new StringBuilder(data.trim()).append(key).toString();
	    return MD5Util.md5(sign);
	}
}