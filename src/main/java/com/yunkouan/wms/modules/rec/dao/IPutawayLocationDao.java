/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月22日 下午1:46:26<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.dao;

import java.util.List;

import com.yunkouan.wms.modules.rec.entity.RecPutawayLocation;
import com.yunkouan.wms.modules.rec.vo.RecPutawayLocationVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 上架单操作明细Dao<br/><br/>
 * @version 2017年2月22日 下午1:46:26<br/>
 * @author andy wang<br/>
 */
public interface IPutawayLocationDao extends Mapper<RecPutawayLocation> {

	/**
	 * 根据上架单id，查询上架单操作明细
	 * @param recPutawayLocationVO 查询条件
	 * @return 上架单操作明细
	 * @throws Exception
	 * @version 2017年3月3日 上午10:19:14<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutawayLocation> selectPtwLocationByPtwId ( RecPutawayLocationVO recPutawayLocationVO ) throws Exception;
}



