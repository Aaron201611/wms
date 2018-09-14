/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 下午1:27:36<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.service;

import java.util.List;

import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;

/**
 * 上架单操作明细外调业务类<br/><br/>
 * @version 2017年2月22日 下午1:27:36<br/>
 * @author andy wang<br/>
 */
public interface IPutawayLocationExtlService {
	
	/**
	 * 根据库位id，查询生效/作业中的上架单计划操作明细集合(库位库容度重算)
	 * @param param_locationId 库位id
	 * @return
	 * @throws Exception
	 * @version 2017年5月15日 下午4:58:05<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocationVO> listPtwLocRecalByLocId ( String param_locationId ) throws Exception;
	
	
	/**
	 * 根据上架单明细id，查询上架单操作明细
	 * @param listPtwDetailId 上架单明细id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月9日 下午8:14:02<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPtwLocationByDetailId ( List<String> listPtwDetailId , Boolean isPlan ) throws Exception;
	
	/**
	 * 根据上架单id，查询上架单操作明细
	 * @param putawayId 上架单id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月2日 下午10:35:25<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPutawayLocationByPtwId ( String putawayId , Boolean isPlan ) throws Exception;
	
	/**
	 * 根据上架单明细id，查询上架单操作明细
	 * @param ptwDetailId 上架单明细id
	 * @param isPlan 是否计划操作明细
	 * —— true 查询计划操作明细
	 * —— false 查询实际操作明细
	 * —— null 查询所有类型的操作明细
	 * @return 上架单操作明细集合
	 * @throws Exception
	 * @version 2017年3月1日 下午2:37:41<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPtwLocByPtwDetailId ( String ptwDetailId , Boolean isPlan ) throws Exception ;
	
	/**
	 * 根据上架单操作明细id，删除上架单操作明细
	 * @param listPutawayLocationId 上架单操作明细id集合
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:15:59<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayLocationById ( List<String> listPutawayLocationId ) throws Exception;
	
	/**
	 * 根据上架单明细id，删除上架单操作明细
	 * @param putawayDetailId 上架单明细id
	 * @return 删除的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午9:08:32<br/>
	 * @author andy wang<br/>
	 */
	public int deletePutawayLocationByPtwDetailId ( String putawayDetailId ) throws Exception;
	
	/**
	 * 更新上架单操作明细
	 * @param recPutawayLocationVO 上架单操作明细
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午7:53:35<br/>
	 * @author andy wang<br/>
	 */
	public int updatePutawayLocation ( RecPutawayLocationVO recPutawayLocationVO ) throws Exception;
	
	/**
	 * 根据条件，查询上架操作明细
	 * @param recPutawayLocationVO 查询条件
	 * @return 上架操作明细集合
	 * @throws Exception
	 * @version 2017年2月23日 上午11:07:22<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> listPutawayLocationByExample ( RecPutawayLocationVO recPutawayLocationVO ) throws Exception ;
	
	/**
	 * 保存上架单操作明细集合
	 * @param listLocation 上架单操作明细集合
	 * @return 保存后的上架单操作明细集合
	 * @throws Exception
	 * @version 2017年2月22日 下午2:01:00<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> insertPtwLocation ( List<RecPutawayLocation> listLocation ) throws Exception;
	
	/**
	 * 保存上架单操作明细
	 * @param location 上架单操作明细
	 * @return 保存后的上架单操作明细
	 * @throws Exception
	 * @version 2017年2月22日 下午1:35:48<br/>
	 * @author andy wang<br/>
	 */
	public RecPutawayLocation insertLocation ( RecPutawayLocation location ) throws Exception;
	
}
