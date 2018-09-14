package com.yunkouan.wms.common.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunkouan.wms.common.dao.ISeqDao;
import com.yunkouan.wms.common.strategy.IIdRule;

/**
 * @function Oracle的备份主键生成策略，使用数据库序列
 * @author tphe06
 */
@Service(value="idImpl4Oracle")
public class IdImpl4Oracle implements IIdRule {
	@Autowired
	private ISeqDao dao;

	@Override
	public Integer getAccSeq() {
		
		return dao.getAccSeq().intValue();
	}

	@Override
	public Integer getAccRoleSeq() {
		
		return dao.getAccRoleSeq().intValue();
	}

	@Override
	public Integer getAdjustDetailSeq() {
		
		return dao.getAdjustDetailSeq().intValue();
	}

	@Override
	public Integer getAdjustSeq() {
		
		return dao.getAdjustSeq().intValue();
	}

	@Override
	public Integer getAdministratorRoleSeq() {
		
		return dao.getAdministratorRoleSeq().intValue();
	}

	@Override
	public Integer getAdminSeq() {
		
		return dao.getAdminSeq().intValue();
	}

	@Override
	public Integer getAdminRoleAuthSeq() {
		
		return dao.getAdminRoleAuthSeq().intValue();
	}

	@Override
	public Integer getAdminRoleSeq() {
		
		return dao.getAdminRoleSeq().intValue();
	}

	@Override
	public Integer getAreaSeq() {
		
		return dao.getAreaSeq().intValue();
	}

	@Override
	public Integer getAsnDetailSeq() {
		
		return dao.getAsnDetailSeq().intValue();
	}

	@Override
	public Integer getAsnSeq() {
		
		return dao.getAsnSeq().intValue();
	}

	@Override
	public Integer getAuthSeq() {
		
		return dao.getAuthSeq().intValue();
	}

	@Override
	public Integer getBusinessStaticSeq() {
		
		return dao.getBusinessStaticSeq().intValue();
	}

	@Override
	public Integer getCountDetailSeq() {
		
		return dao.getCountDetailSeq().intValue();
	}

	@Override
	public Integer getCountSeq() {
		
		return dao.getCountSeq().intValue();
	}

	@Override
	public Integer getDeliveryDetailSeq() {
		
		return dao.getDeliveryDetailSeq().intValue();
	}

	@Override
	public Integer getDeliverySeq() {
		
		return dao.getDeliverySeq().intValue();
	}

	@Override
	public Integer getDockSeq() {
		
		return dao.getDockSeq().intValue();
	}

	@Override
	public Integer getExpLogSeq() {
		
		return dao.getExpLogSeq().intValue();
	}

	@Override
	public Integer getFingerSeq() {
		
		return dao.getFingerSeq().intValue();
	}

	@Override
	public Integer getInterfaceSeq() {
		
		return dao.getInterfaceSeq().intValue();
	}

	@Override
	public Integer getLocationSeq() {
		
		return dao.getLocationSeq().intValue();
	}

	@Override
	public Integer getLocationSpecSeq() {
		
		return dao.getLocationSpecSeq().intValue();
	}

	@Override
	public Integer getInvLogSeq() {
		
		return dao.getInvLogSeq().intValue();
	}

	@Override
	public Integer getMachineSeq() {
		
		return dao.getMachineSeq().intValue();
	}

	@Override
	public Integer getMerchantSeq() {
		
		return dao.getMerchantSeq().intValue();
	}

	@Override
	public Integer getOpLogSeq() {
		
		return dao.getOpLogSeq().intValue();
	}

	@Override
	public Integer getOrgAuthSeq() {
		
		return dao.getOrgAuthSeq().intValue();
	}

	@Override
	public Integer getOrgSeq() {
		
		return dao.getOrgSeq().intValue();
	}

	@Override
	public Integer getOrgStrategySeq() {
		
		return dao.getOrgStrategySeq().intValue();
	}

	@Override
	public Integer getPackSeq() {
		
		return dao.getPackSeq().intValue();
	}

	@Override
	public Integer getPickDetailSeq() {
		
		return dao.getPickDetailSeq().intValue();
	}

	@Override
	public Integer getPickSeq() {
		
		return dao.getPickSeq().intValue();
	}

	@Override
	public Integer getPickLocationSeq() {
		
		return dao.getPickLocationSeq().intValue();
	}

	@Override
	public Integer getPutawayDetailSeq() {
		
		return dao.getPutawayDetailSeq().intValue();
	}

	@Override
	public Integer getPutawayLocationSeq() {
		
		return dao.getPutawayLocationSeq().intValue();
	}

	@Override
	public Integer getRentSeq() {
		
		return dao.getRentSeq().intValue();
	}

	@Override
	public Integer getRoleAuthSeq() {
		
		return dao.getRoleAuthSeq().intValue();
	}

	@Override
	public Integer getRoleSeq() {
		
		return dao.getRoleSeq().intValue();
	}

	@Override
	public Integer getSequenceSeq() {
		
		return dao.getSequenceSeq().intValue();
	}

	@Override
	public Integer getShiftDetailSeq() {
		
		return dao.getShiftDetailSeq().intValue();
	}

	@Override
	public Integer getShiftSeq() {
		
		return dao.getShiftSeq().intValue();
	}

	@Override
	public Integer getSkuSeq() {
		
		return dao.getSkuSeq().intValue();
	}

	@Override
	public Integer getSkuPackSeq() {
		
		return dao.getSkuPackSeq().intValue();
	}

	@Override
	public Integer getSkuTypeSeq() {
		
		return dao.getSkuTypeSeq().intValue();
	}

	@Override
	public Integer getSpecPackSeq() {
		
		return dao.getSpecPackSeq().intValue();
	}

	@Override
	public Integer getStockSeq() {
		
		return dao.getStockSeq().intValue();
	}

	@Override
	public Integer getStrategySeq() {
		
		return dao.getStrategySeq().intValue();
	}

	@Override
	public Integer getStrategyImplSeq() {
		
		return dao.getStrategyImplSeq().intValue();
	}

	@Override
	public Integer getSysParamSeq() {
		
		return dao.getSysParamSeq().intValue();
	}

	@Override
	public Integer getTaskSeq() {
		
		return dao.getTaskSeq().intValue();
	}

	@Override
	public Integer getUserSeq() {
		
		return dao.getUserSeq().intValue();
	}

	@Override
	public Integer getUserParamSeq() {
		
		return dao.getUserParamSeq().intValue();
	}

	@Override
	public Integer getWarehouseSeq() {
		
		return dao.getWarehouseSeq().intValue();
	}

	@Override
	public Integer getWarnSeq() {
		
		return dao.getWarnSeq().intValue();
	}

	@Override
	public Integer getWaveSeq() {
		
		return dao.getWaveSeq().intValue();
	}

	@Override
	public Integer getWorkAreaSeq() {
		
		return dao.getWorkAreaSeq().intValue();
	}

	@Override
	public Integer getWorkGroupSeq() {
		
		return dao.getWorkGroupSeq().intValue();
	}

	@Override
	public Integer getPutawaySeq() {
		
		return dao.getPutawaySeq().intValue();
	}
}