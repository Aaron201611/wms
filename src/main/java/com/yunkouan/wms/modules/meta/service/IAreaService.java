/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月11日 下午3:29:19<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.meta.vo.MetaAreaVO;

/**
 * 库区业务接口<br/><br/>
 * @version 2017年3月11日 下午3:29:19<br/>
 * @author andy wang<br/>
 */
public interface IAreaService {
	
	
	/**
	 * 更新并生效库区
	 * @param param_areaVO 库区对象
	 * @return 生效后的库区
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO updateAndEnable ( MetaAreaVO param_areaVO ) throws Exception;
	
	/**
	 * 保存并生效库区
	 * @param param_areaVO 库区对象
	 * @return 生效后的库区
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO addAndEnable ( MetaAreaVO param_areaVO ) throws Exception;
	
	/**
	 * 页面显示库区
	 * @return
	 * @throws Exception
	 * @version 2017年4月4日 下午2:06:17<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaAreaVO> showArea ( MetaAreaVO param_areaVO ) throws Exception;
	
	/**
     * 更新库区
     * @param param_areaVO 库区信息
     * @throws Exception
     * @version 2017年2月18日 下午2:44:59<br/>
     * @author andy wang<br/>
     */
    public void updateArea ( MetaAreaVO param_areaVO ) throws Exception;
	
	/**
	 * 失效库区
	 * @param param_listAreaId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:17:57<br/>
	 * @author andy wang<br/>
	 */
    public void inactiveArea ( List<String> param_listAreaId ) throws Exception;
	
	/**
	 * 生效库区
	 * @param param_listAreaId 库区id集合
	 * @throws Exception
	 * @version 2017年3月11日 下午2:16:53<br/>
	 * @author andy wang<br/>
	 */
    public void activeArea ( List<String> param_listAreaId ) throws Exception;
	
	/**
	 * 根据库区id，查询库区信息
	 * @param param_areaId 库区id
	 * @return 库区信息
	 * @throws Exception
	 * @version 2017年3月11日 下午1:57:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO viewArea ( String param_areaId ) throws Exception;
	
	/**
	 * 保存库区
	 * @param param_areaVO 要保存的库区
	 * @return 保存后的库区（包含id）
	 * @throws Exception
	 * @version 2017年3月11日 上午9:43:25<br/>
	 * @author andy wang<br/>
	 */
	public MetaAreaVO insertArea ( MetaAreaVO param_areaVO ) throws Exception;
	
	/**
	 * 根据条件，分页查询库区
	 * @param metaAreaVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月11日 下午1:33:09<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes"})
	public Page listArea ( MetaAreaVO metaAreaVO ) throws Exception;

	public void change(MetaAreaVO metaAreaVO) throws Exception;

}