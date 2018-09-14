package com.yunkouan.wms.modules.send.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.wms.modules.send.entity.SendDelivery;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SendPickLocationVo;
import com.yunkouan.wms.modules.send.vo.SendStasticsVo;
import com.yunkouan.wms.modules.send.vo.TotalVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 发货单数据层接口
 */
public interface IDeliveryDao extends Mapper<SendDelivery>{
	
	/**
	 * 查询发货单列表
	 * @param entity
	 * @return
	 * @version 2017年2月14日 下午2:18:18<br/>
	 * @author Aaron He<br/>
	 */
	List<SendDelivery> qryList(SendDelivery entity);
	
	/**
	 * 查询没关联波次单，状态是部分拣货或者部分发运，数量大于0的发货单
	 * @param entity
	 * @return
	 * @version 2017年3月7日 上午10:44:06<br/>
	 * @author Aaron He<br/>
	 */
	List<SendDelivery> qryListNotFinish(@Param("orgId")String orgId,@Param("warehouseId")String warehouseId);
	
	/**
	 * 更新波次单号
	 * @param entity
	 * @version 2017年3月1日 下午3:59:11<br/>
	 * @author Aaron He<br/>
	 */
	void updateWaveId(SendDelivery entity);
	
	/**
	 * 批量更新波次单号
	 * @param waveId
	 * @param deliveryIds
	 * @param deliveryStatus
	 * @version 2017年3月1日 下午4:45:03<br/>
	 * @author Aaron He<br/>
	 */
	void batchUpdateWaveId(@Param("waveId")String waveId,@Param("deliveryIds")List<String> deliveryIds,
			@Param("deliveryStatus")int deliveryStatus,@Param("operator")String operator,
			@Param("updateTime")Date updateTime);
	
	/**
	 * 删除波次单号
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月2日 下午1:29:28<br/>
	 * @author Aaron He<br/>
	 */
	void delWaveId(@Param("waveId")String waveId,@Param("orgId")String orgId,@Param("warehouseId")String warehouseId,
			@Param("deliveryStatus")int deliveryStatus,@Param("operator")String operator);
	
	/**
	 * 根据波次单号更新状态
	 * @param param
	 * @version 2017年3月2日 下午5:45:35<br/>
	 * @author Aaron He<br/>
	 */
	void updateStatusByWaveId(SendDelivery param);
	
	/**
	 * 增加快递单打印次数
	 * @param param
	 * @return
	 */
	int incExpressPrintTimes(@Param("deliveryId")String deliveryId);
	
	List<SendStasticsVo> qryOutStaticsList(SendPickLocationVo vo);

	TotalVo selectTotalOrder(SendDeliveryVo sendDeliveryVo);

	TotalVo selectTotalSend(SendPickLocationVo vo);
	public List<String>getTask(String orgId);
}