/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:26:57<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.saas.modules.sys.entity.MetaLocationSpec;
import com.yunkouan.wms.modules.meta.vo.MetaLocationSpecVO;

/**
 * 库位规格外调接口<br/><br/>
 * @version 2017年3月13日下午2:26:57<br/>
 * @author andy wang<br/>
 */
public interface ILocationSpecExtlService {

	/**
	 * 根据条件，分页查询库位规格
	 * @param metaLocationSpecVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listLocSpecByPage(MetaLocationSpecVO metaLocationSpecVO) throws Exception;

	/**
	 * 根据库位规格编号，查询库位规格
	 * @param areaNo 库位规格编号
	 * @return 库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecByNo(String locSpecNo) throws Exception;

	/**
	 * 根据库位规格名，查询库位规格
	 * @param areaName 库位规格名
	 * @return 库位规格
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecByName(String locSpecName) throws Exception;

	/**
	 * 保存库位规格
	 * @param metaLocationSpec 要保存的库位规格对象
	 * @return 保存后的库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec insertLocSpec(MetaLocationSpec metaLocationSpec) throws Exception;

	/**
	 * 更新库位规格
	 * @param areaVO 更新的库位规格内容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocSpec(MetaLocationSpecVO locSpecVO) throws Exception;

	/**
	 * 根据库位规格id，更新库位规格状态
	 * @param areaId 库位规格id
	 * @param status 库位规格状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocSpecStatusById(String locSpecId, Integer status) throws Exception;

	/**
	 * 根据库位规格id，查询库位规格信息
	 * @param locSpecId 库位规格id
	 * @return 库位规格信息
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec findLocSpecById(String locSpecId) throws Exception;

	/**
	 * 根据条件，查询库位规格
	 * @param metaLocationSpecVO 查询条件
	 * @return 库位规格对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:26:57<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocationSpec> listLocSpecByExample(MetaLocationSpecVO metaLocationSpecVO) throws Exception;

}
