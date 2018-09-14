package com.yunkouan.wms.modules.send.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.mybatis.MyBatisDao;
import com.yunkouan.wms.modules.send.entity.SendPick;
import com.yunkouan.wms.modules.send.vo.SendPickVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 拣货单数据层接口
 */
public interface IPickDao extends Mapper<SendPick>{

	/**
	 * 查询拣货单列表
	 * @param param
	 * @return
	 * @version 2017年2月16日 下午2:01:06<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPick> qryList(SendPick param);
	
	/**
	 * 根据发货单id更新状态
	 * @param deliveryId
	 * @return
	 * @version 2017年2月16日 下午2:46:01<br/>
	 * @author Aaron He<br/>
	 */
	public int updateStatusByParam(SendPick param);
	
	/**
	 * 根据波次单id查找拣货单
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年3月2日 下午4:24:23<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendPick> qryByWaveId(@Param("waveId")String waveId,@Param("orgId")String orgId,@Param("warehouseId")String warehouseId);
	
	/**
	 * 增加打印次数
	 * @param param
	 * @return
	 */
	public int incPrintTimes(SendPick param); 
	
	/**
	 * 查询打印列表
	 * @param sendPickVo
	 * @return
	 */
	public List<SendPick> qryPrintList(SendPickVo sendPickVo);
	
	public List<String>getTask(String orgId);
}