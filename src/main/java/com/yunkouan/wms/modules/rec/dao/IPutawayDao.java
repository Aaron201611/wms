/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:05:31<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.dao;

import java.util.List;

import com.yunkouan.wms.modules.rec.entity.RecPutaway;
import com.yunkouan.wms.modules.rec.vo.RecPutawayVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 上架单数据层接口<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午4:05:31<br/>
 * @author andy wang<br/>
 */
public interface IPutawayDao extends Mapper<RecPutaway> {
	
	/**
	 * 根据条件，查询上架单
	 * @param recPutawayVO 查询条件
	 * @return 上架单集合
	 * @throws Exception
	 * @version 2017年2月28日 下午3:42:38<br/>
	 * @author andy wang<br/>
	 */
	public List<RecPutaway> selectPutaway ( RecPutawayVO recPutawayVO ) throws Exception;
	
	List<String>getTask(String orgId);
}