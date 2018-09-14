/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午3:41:52<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.saas.modules.sys.entity.MetaArea;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;

/**
 * 库区外调接口<br/><br/>
 * @version 2017年3月11日 下午3:41:52<br/>
 * @author andy wang<br/>
 */
public interface IAreaExtlService {
	
	/**
	 * 根据库区名称，查询库区
	 * @param areaName 库区名称
	 * @throws Exception
	 * @version 2017年3月30日 上午9:58:45<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaArea> listAreaByName ( String areaName , boolean isLike ) throws Exception;
	
	/**
	 * 根据条件，分页查询库区
	 * @param metaAreaVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午5:01:16<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listAreaByPage ( MetaAreaVO metaAreaVO ) throws Exception;
	
	/**
	 * 根据库区编号，查询库区
	 * @param areaNo 库区编号
	 * @return 库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午4:28:08<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaByNo ( String areaNo , String areaId ) throws Exception;
	
	/**
	 * 根据库区名，查询库区
	 * @param areaName 库区名
	 * @return 库区
	 * @throws Exception
	 * @version 2017年3月11日 下午4:26:23<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaByName ( String areaName ) throws Exception;
	
	/**
	 * 保存库区
	 * @param metaArea 要保存的库区对象
	 * @return 保存后的库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午4:22:07<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea insertArea ( MetaArea metaArea ) throws Exception ;
	
	/**
	 * 更新库区
	 * @param areaVO 更新的库区内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月11日 下午4:20:11<br/>
	 * @author andy wang<br/>
	 */
	public int updateArea ( MetaAreaVO areaVO ) throws Exception ;
	
	/**
	 * 根据库区id，更新库区状态
	 * @param areaId 库区id
	 * @param status 库区状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月11日 下午4:16:32<br/>
	 * @author andy wang<br/>
	 */
	public int updateAreaStatusById ( String areaId , Integer status ) throws Exception;
	
	/**
	 * 根据库区id，查询库区信息
	 * @param areaId 库区id
	 * @return 库区信息
	 * @throws Exception
	 * @version 2017年3月11日 下午4:04:47<br/>
	 * @author andy wang<br/>
	 */
	public MetaArea findAreaById ( String areaId ) throws Exception;
	
	/**
	 * 根据条件，查询库区
	 * @param metaAreaVO 查询条件
	 * @return 库区对象
	 * @throws Exception
	 * @version 2017年3月11日 下午3:55:50<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaArea> listAreaByExample ( MetaAreaVO metaAreaVO ) throws Exception ;
	
}
