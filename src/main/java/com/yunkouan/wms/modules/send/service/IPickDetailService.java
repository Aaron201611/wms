package com.yunkouan.wms.modules.send.service;

import java.util.List;

import com.yunkouan.wms.modules.send.vo.SendPickDetailVo;

public interface IPickDetailService {
	
	/**
	 * 根据拣货id查询拣货明细
	 * @param pickId
	 * @param pickType
	 * @return
	 * @version 2017年2月17日 下午2:15:50<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPickDetailVo> qryPickDetails(String pickId);
	
	/**
	 * 根据id获取拣货明细
	 * @param pickDetailId
	 * @return
	 */
	public SendPickDetailVo getVoById(String pickDetailId);
	
	/**
	 * 查询明细列表
	 * @param param
	 * @return
	 */
	public List<SendPickDetailVo> qryDetails(SendPickDetailVo param);
	
	/**
	 * 查找拣货明细及库位
	 * @param param
	 * @return
	 */
	public SendPickDetailVo qryDetailAndLocationById(String pickDetailId)throws Exception;
	
	/**
	 * 根据发货明细id查询拣货明细
	 * @param deliveryDetailId
	 * @return
	 */
	public List<SendPickDetailVo> qryDetailsAndLocation(SendPickDetailVo param,int pickType) throws Exception;
	/**
	 * 删除记录
	 * @param pickDetailVo
	 * @throws Exception
	 */
	public void delEntity(SendPickDetailVo pickDetailVo)throws Exception;
	

}
