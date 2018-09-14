package com.yunkouan.wms.common.strategy;

import java.util.List;

/**
 * @function 单据编号生成规则
 * @author tphe06
 */
public abstract class INoRule {
	/**
	 * 生成ASN单号
	 * @param orgid 组织id
	 * @param docType Asn单来源 
	 * @return
	 */
	public abstract String getAsnNo(String orgid, Integer docType);
	/**
	 * 生成上架单号
	 * @param orgid
	 * @return
	 */
	public abstract String getPutawayNo(String orgid);
	/**
	 * 生成发货单号
	 * @param orgid
	 * @param docType
	 * @return
	 */
	public abstract String getDeliveryNo(String orgid, Integer docType);
	/**
	 * 生成拣货单号
	 * @param orgid
	 * @param docType
	 * @return
	 */
	public abstract String getPickNo(String orgid, Integer docType);
	/**
	 * 生成波次单号
	 * @param orgid
	 * @return
	 */
	public abstract String getWaveNo(String orgid);
	/**
	 * 生成盘点单编号
	 * @param orgid
	 * @return
	 */
	public abstract String getCountNo(String orgid);
	/**
	 * 生成不良品申请单编号
	 * @param orgid
	 * @return
	 */
	public abstract String getApplyRejectsNo(String orgid);
	/**
	 * 生成调账单编号
	 * @param orgid
	 * @return
	 */
	public abstract String getAdjustNo(String orgid);
	/**
	 * 生成异常单编号
	 * @param orgid
	 * @return
	 */
	public abstract String getExceptionNo(String orgid);
	/**
	 * 生成库位规格编号
	 * @param orgid
	 * @return
	 */
	public abstract String getLocationSpecNo(String orgid);
	/**
	 * 生成补货移位
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:10:46<br/>
	 * @author 王通<br/>
	 */
	public abstract String getShiftReplenishmentNo(String orgid);
	/**
	 * 生成退货移位
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:10:46<br/>
	 * @author 王通<br/>
	 */
	public abstract String getShiftRejectNo(String orgid);
	/**
	 * 生成拣货移位
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:10:46<br/>
	 * @author 王通<br/>
	 */
	public abstract String getShiftPickNo(String orgid);
	/**
	 * 生成库间移位
	 * @param orgid
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月24日 上午11:10:46<br/>
	 * @author 王通<br/>
	 */
	public abstract String getShiftInternalNo(String orgid);
	/**
	 * 辅材
	 * @param orgId
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月29日 上午11:32:05<br/>
	 * @author 王通<br/>
	 */
	public abstract String getMaterialNo(String orgid);

	/**
	 * 生成货品前缀
	 * @param orgid
	 * @return
	 */
	public abstract String getSkuPrefixNo(String orgid);
	/**
	 * 生成货品后缀
	 * @param orgid
	 * @return
	 */
	public abstract List<String> getSkuSuffixNo(String orgid, int sum);
	/**
	 * 生成客商编号
	 * @param orgid
	 * @return
	 */
	public abstract String getMerchantNo(String orgid);
	/**
	 * 生成货品编号
	 * @param orgid
	 * @return
	 */
	public abstract String getSkuTypeNo(String orgid);
	/**
	 * 生成申报单流水号
	 * @param orgId
	 * @return
	 */
	public abstract String getApplicationNo(String orgId);
	/**
	 * 生成核放单流水号
	 * @param orgId
	 * @return
	 */
	public abstract String getExamineNo(String orgId);
	
	
}