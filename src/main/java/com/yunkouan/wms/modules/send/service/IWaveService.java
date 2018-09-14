package com.yunkouan.wms.modules.send.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.send.entity.SendWave;
import com.yunkouan.wms.modules.send.vo.DeliveryDetailVo;
import com.yunkouan.wms.modules.send.vo.SendPickVo;
import com.yunkouan.wms.modules.send.vo.SendWaveVo;

/**
 * 波次单服务接口
 */
public interface IWaveService {

	/**
	 * 查询发货单新增波次单
	 * @param param
	 * @version 2017年3月1日 下午3:54:39<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo serchAndAdd(SendWaveVo param,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 保存并生效
	 * @param param
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public SendWaveVo saveAndEnable(SendWaveVo param,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 查询波次单分页
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 * @version 2017年3月1日 下午7:20:14<br/>
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(SendWaveVo sendWaveVo,String orgId,String warehouseId);
	
	/**
	 * 查询波次单数量
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public int count(SendWaveVo sendWaveVo);

	/**
	 * 查询波次单数量
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @return
	 */
	public int count2(SendWaveVo sendWaveVo);
	
	public List<String>getTask(String orgId);
	/**
	 * 查询波次单列表
	 * @param sendWaveVo
	 * @return
	 * @version 2017年3月7日 上午9:23:00<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendWaveVo> qryNotFinishList(SendWaveVo sendWaveVo);
	
	/**
	 * 根据单号查询
	 * @param deliveryNo
	 * @version 2017年3月7日 下午6:34:25<br/>
	 * @author Aaron He<br/>
	 */
	public List<SendWave> qryByWaveNo(String waveNo,String orgId,String warehouseId);
	
	/**
	 * 查看波次单
	 * @param waveId
	 * @return
	 * @version 2017年3月1日 下午7:26:44<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo view(SendWaveVo reqParam,String warehouseId);
	/**
	 * 查询波次单实体
	 * @param waveId
	 * @return
	 */
	public SendWave getEntityById(String waveId);
	
	/**
	 * 删除波次单下面的发货单
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @version 2017年3月2日 下午1:16:41<br/>
	 * @author Aaron He<br/>
	 */
	public void delDeliveries(SendWaveVo sendWaveVo,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 更新波次单
	 * @param sendWaveVo
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月2日 下午1:57:43<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo searchAndUpdate(SendWaveVo param,String orgId,String warehouseId,String operator)throws Exception;
	
	/**
	 * 更新波次单发货数量
	 * @param waveId
	 * @param userId
	 * @throws Exception
	 */
	public void updateQty(String waveId,String userId)throws Exception;
	
	/**
	 * 生效波次单
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	public void active(String waveId,String orgId,String warehouseId,String operator) throws Exception;
	
	/**
	 * 生效波次单（新）
	 * @param waveId
	 * @param orgId
	 * @param warehouseId
	 * @param operator
	 * @throws Exception
	 */
	public void enable(String waveId,String orgId,String warehouseId,String operator) throws Exception;
	/**
	 * 失效波次单
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	public void disable(String waveId,String orgId,String warehouseId,String operator) throws Exception;
	
	
	/**
	 * 取消波次单
	 * @param waveId
	 * @throws Exception
	 * @version 2017年3月1日 下午7:45:41<br/>
	 * @author Aaron He<br/>
	 */
	public void abolish(String waveId,String orgId,String warehouseId,String operator) throws Exception;
	
	/**
	 * 根据波次单id查询发货明细
	 * @param waveId
	 * @return
	 * @throws Exception
	 */
	public List<DeliveryDetailVo> qryDeliveryDetailsByWaveId(String waveId) throws Exception;
	
	/**
	 * 
	 * @param waveId
	 * @param operator
	 * @throws Exception
	 * @version 2017年3月6日 下午2:24:06<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo updateAfterConfirmPick(String waveId,String operator)throws Exception;
	
	/**
	 * 拣货确认更新波次
	 * @param sendPickVo
	 * @param userId
	 * @throws Exception
	 */
	public void updateAfterCompletePick(SendPickVo sendPickVo,String userId) throws Exception;
	
	/**
	 * 登记到业务统计表
	 * @param waveVo
	 * @param operator
	 * @version 2017年3月11日 下午2:02:55<br/>
	 * @author Aaron He<br/>
	 */
	public void addOrgBusiStas(SendWaveVo waveVo,String operator) throws Exception;
	
	/**
	 * 获取波次单及发货单列表
	 * @param waveId
	 * @return
	 * @version 2017年3月1日 下午7:49:29<br/>
	 * @author Aaron He<br/>
	 */
	public SendWaveVo getSendWaveVoById(String waveId);
	
	/**
	 * 查询波次单的发货单明细
	 * @param waveVo
	 * @return
	 * @throws Exception
	 */
	public SendWaveVo printInfo(SendWaveVo waveVo) throws Exception;

}