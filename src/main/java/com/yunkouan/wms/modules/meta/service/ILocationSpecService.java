/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:32:24<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.*;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;

/**
 * 库位规格业务接口<br/><br/>
 * @version 2017年3月13日下午2:32:24<br/>
 * @author andy wang<br/>
 */
public interface ILocationSpecService {

	
	/**
	 * 更新并生效库位规格
	 * @param param_specVO 库位规格对象
	 * @return 生效后的仓库
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO updateAndEnable ( MetaLocationSpecVO param_specVO ) throws Exception;
	
	/**
	 * 保存并生效库位规格
	 * @param param_specVO 库位规格对象
	 * @return 生效后的库位规格
	 * @throws Exception
	 * @version 2017年6月19日 下午2:09:06<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO addAndEnable ( MetaLocationSpecVO param_specVO ) throws Exception ;
	
	/**
	 * 页面显示库位规格
	 * @param param_locSpecVO 查询条件
	 * @return
	 * @throws Exception
	 * @version 2017年4月4日 下午2:25:12<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocationSpecVO> showLocSpec ( MetaLocationSpecVO param_locSpecVO ) throws Exception;
	
	/**
	 * 更新库位规格
	 * @param param_locSpecVO 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	public void updateLocSpec(MetaLocationSpecVO param_locSpecVO) throws Exception;

	/**
	 * 失效库位规格
	 * @param param_listLocSpecId 库位规格id集合
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	public void inactiveLocSpec(List<String> param_listLocSpecId) throws Exception;

	/**
	 * 生效库位规格
	 * @param param_listLocSpecId 库位规格id集合
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	public void activeLocSpec(List<String> param_listLocSpecId) throws Exception;

	/**
	 * 根据库位规格id，查询库位规格信息
	 * @param param_locSpecId 库位规格id
	 * @return 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO viewLocSpec(String param_locSpecId) throws Exception;

	/**
	 * 保存库位规格
	 * @param param_locSpecVO 要保存的库位规格
	 * @return 保存后的库位规格（包含id）
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO insertLocSpec(MetaLocationSpecVO param_locSpecVO) throws Exception;

	/**
	 * 根据条件，分页查询库位规格
	 * @param metaLocationSpecVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日下午2:32:24<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings({ "rawtypes" })
	public Page listLocSpec(MetaLocationSpecVO metaLocationSpecVO) throws Exception;

}