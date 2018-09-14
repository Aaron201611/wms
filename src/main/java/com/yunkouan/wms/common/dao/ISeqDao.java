package com.yunkouan.wms.common.dao;

import com.yunkouan.wms.common.vo.SeqVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * @function 数据库备份主键生成接口（Oracle）
 * @author tphe06
 */
public interface ISeqDao extends Mapper<SeqVo> {
	public Long getAccSeq();
	public Long getAccRoleSeq();
	public Long getAdjustDetailSeq();
	public Long getAdjustSeq();
	public Long getAdministratorRoleSeq();
	public Long getAdminSeq();
	public Long getAdminRoleAuthSeq();
	public Long getAdminRoleSeq();
	public Long getAreaSeq();
	public Long getAsnDetailSeq();
	public Long getAsnSeq();
	public Long getAuthSeq();
	public Long getBusinessStaticSeq();
	public Long getCountDetailSeq();
	public Long getCountSeq();
	public Long getDeliveryDetailSeq();
	public Long getDeliverySeq();
	public Long getDockSeq();
	public Long getExpLogSeq();
	public Long getFingerSeq();
	public Long getInterfaceSeq();
	public Long getLocationSeq();
	public Long getLocationSpecSeq();
	public Long getInvLogSeq();
	public Long getMachineSeq();
	public Long getMerchantSeq();
	public Long getOpLogSeq();
	public Long getOrgAuthSeq();
	public Long getOrgSeq();
	public Long getOrgStrategySeq();
	public Long getPackSeq();
	public Long getPickDetailSeq();
	public Long getPickSeq();
	public Long getPickLocationSeq();
	public Long getPutawayDetailSeq();
	public Long getPutawayLocationSeq();
	public Long getRentSeq();
	public Long getRoleAuthSeq();
	public Long getRoleSeq();
	public Long getSequenceSeq();
	public Long getShiftDetailSeq();
	public Long getShiftSeq();
	public Long getSkuSeq();
	public Long getSkuPackSeq();
	public Long getSkuTypeSeq();
	public Long getSpecPackSeq();
	public Long getStockSeq();
	public Long getStrategySeq();
	public Long getStrategyImplSeq();
	public Long getSysParamSeq();
	public Long getTaskSeq();
	public Long getUserSeq();
	public Long getUserParamSeq();
	public Long getWarehouseSeq();
	public Long getWarnSeq();
	public Long getWaveSeq();
	public Long getWorkAreaSeq();
	public Long getWorkGroupSeq();
	public Long getPutawaySeq();
}