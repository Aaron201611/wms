package com.yunkouan.wms.modules.send.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;

import tk.mybatis.mapper.common.Mapper;

/**
 * 发货单明细数据层接口
 */
public interface IDeliveryDetailDao extends Mapper<SendDeliveryDetail>{

	/**
	 * 根据发货单id删除明细
	 * @param deliveryId
	 * @return
	 * @version 2017年2月14日 下午1:40:07<br/>
	 * @author Aaron He<br/>
	 */
	public int delByDeliveryId(@Param("deliveryId")String deliveryId,@Param("orgId")String orgId,@Param("warehouseId")String warehouseId);
	
	/**
	 * 根据发货id查询发货明细
	 * @param deliveryId
	 * @return
	 * @version 2017年2月15日 下午1:22:03<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendDeliveryDetail> selectByDeliveryId(@Param("deliveryId")String deliveryId);
   

}