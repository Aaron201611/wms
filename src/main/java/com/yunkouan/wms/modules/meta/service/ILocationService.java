/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月12日 下午7:35:22<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.*;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位业务接口<br/><br/>
 * @version 2017年3月12日 下午7:35:22<br/>
 * @author andy wang<br/>
 */
public interface ILocationService {

	
	/**
	 * 更新并生效库位
	 * @param param_locationVO 库位对象
	 * @return 生效后的库位
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO updateAndEnable ( MetaLocationVO param_locationVO ) throws Exception ;
	
	/**
	 * 保存并生效库位
	 * @param param_locationVO 库位对象
	 * @return 生效后的库位
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO addAndEnable ( MetaLocationVO param_locationVO ) throws Exception ;
	
	
	/**
	 * 库位库容度重算
	 * @param param_listLocationId 库位id集合
	 * @throws Exception
	 * @version 2017年5月15日 下午6:43:26<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( List<String> param_listLocationId ) throws Exception;

	/**
     * 更新库位
     * @param param_areaVO 库位信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
     */
    public void updateLoc ( MetaLocationVO param_areaVO ) throws Exception;
	
	/**
	 * 失效库位
	 * @param param_listLocId 库位id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:17:57<br/>
	 * @author andy wang<br/>
	 */
    public void inactiveLoc ( List<String> param_listLocId ) throws Exception;
	
	/**
	 * 生效库位
	 * @param param_listLocId 库位id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:16:53<br/>
	 * @author andy wang<br/>
	 */
    public void activeLoc ( List<String> param_listLocId ) throws Exception;
	
	/**
	 * 根据库位id，查询库位信息
	 * @param param_areaId 库位id
	 * @return 库位信息
	 * @throws Exception
	 * @version 2017年3月11日 下午1:57:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO viewLoc ( String param_areaId ) throws Exception;
	
	/**
	 * 保存库位
	 * @param param_areaVO 要保存的库位
	 * @return 保存后的库位（包含id）
	 * @throws Exception
	 * @version 2017年3月11日 上午9:43:25<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationVO insertLoc ( MetaLocationVO param_areaVO ) throws Exception;
	
	/**
	 * 根据条件，分页查询库位
	 * @param metaLocationVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午1:33:09<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes"})
	public Page listLoc ( MetaLocationVO metaLocationVO, boolean isTouch) throws Exception;

	public void changeLoc(MetaLocationVO metaLocationVO) throws Exception;

	public void autoBind() throws Exception;

	public MetaLocationVO findLoc(MetaLocationVO vo);
}