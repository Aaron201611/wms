package com.yunkouan.wms.modules.send.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.send.dao.IExpressBillDao;
import com.yunkouan.wms.modules.send.entity.SendExpressBill;
import com.yunkouan.wms.modules.send.service.IExpressBillService;
import com.yunkouan.wms.modules.send.vo.SendExpressBillVo;

/**
 * 发货单业务实现类
 */
@Service
public class ExpressBillServiceImpl extends BaseService implements IExpressBillService{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IExpressBillDao expressBillDao;

	/**
	 * 新增
	 */
	public void add(SendExpressBillVo expressBillVo) throws Exception {
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		String orgId = loginUser.getOrgId();
		String warehouseId = LoginUtil.getWareHouseId();
		
		expressBillVo.getSendExpressBill().setExpressBillId(IdUtil.getUUID());
		expressBillVo.getSendExpressBill().setExpressBillStatus(Constant.EXPRESS_BILL_STATUS_OPEN);
		expressBillVo.getSendExpressBill().setOrgId(orgId);
		expressBillVo.getSendExpressBill().setWarehouseId(warehouseId);
		expressBillVo.getSendExpressBill().setCreatePerson(userId);
		expressBillVo.getSendExpressBill().setUpdatePerson(userId);
		
		expressBillDao.insertSelective(expressBillVo.getSendExpressBill());
		
	}
	
	/**
	 * 生效
	 * @param billId
	 * @throws Exception
	 */
	public void enable(String billId) throws Exception{
		if(StringUtils.isEmpty(billId))
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	//检查是否打开状态
    	checkStatus(billId,Constant.EXPRESS_BILL_STATUS_OPEN,"express_bill_is_not_open");
    	
    	//更新状态为生效
    	updateStatus(billId, Constant.EXPRESS_BILL_STATUS_ENABLE);
	}
	
	/**
	 * 取消
	 * @param billId
	 * @throws Exception
	 */
	public void cancel(String billId) throws Exception{
		if(StringUtils.isEmpty(billId))
    		throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
    	
    	//检查是否打开状态
    	checkStatus(billId,Constant.EXPRESS_BILL_STATUS_ENABLE,"express_bill_is_not_enable");
    	
    	//更新状态为生效
    	updateStatus(billId, Constant.EXPRESS_BILL_STATUS_CANCEL);
	}
	
    /**
     * 检查状态
     * @param rentId
     * @param isStatus
     * @param msg
     * @version 2017年3月8日 下午1:27:29<br/>
     * @author Aaron He<br/>
     */
    public void checkStatus(String billId,int isStatus,String msg){
    	SendExpressBill expressBill = expressBillDao.selectByPrimaryKey(billId);
    	if(expressBill.getExpressBillStatus().intValue() != isStatus)
    		throw new BizException(msg);
    }
	
    /**
     * 更新状态
     * @param rentId
     * @param operator
     * @version 2017年3月8日 下午1:26:11<br/>
     * @author Aaron He<br/>
     */
    public void updateStatus(String billId,int status) throws Exception{
		//获取登录用户
		Principal loginUser = LoginUtil.getLoginUser();
		String userId = loginUser.getUserId();
		
		SendExpressBill expressBill = new SendExpressBill();
		expressBill.setExpressBillId(billId);
		expressBill.setExpressBillStatus(status);
		expressBill.setUpdatePerson(userId);
		
		expressBillDao.updateByPrimaryKeySelective(expressBill);
    }
	

}