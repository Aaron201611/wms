/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月23日 下午2:29:57<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.service;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.Page;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位外调业务类<br/><br/>
 * @version 2017年2月23日 下午2:29:57<br/>
 * @author andy wang<br/>
 */
public interface ILocationExtlService {
	public static final int LENGTH = 6;

	/**
	 * 根据库位id，重算库容度
	 * @param param_locationId 库位id
	 * @throws Exception
	 * @version 2017年5月14日 下午2:58:27<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( String param_locationId ) throws Exception;
	/**
	 * 根据库位id，重算库容度
	 * @param param_listLocationId 库位id集合
	 * @throws Exception
	 * @version 2017年5月14日 下午4:55:04<br/>
	 * @author andy wang<br/>
	 */
	public void recalCapacity ( List<String> param_listLocationId ) throws Exception;
	
	/**
	 * 根据id，更新库容度
	 * @param metaLocationVO 库位VO
	 * @return
	 * @throws Exception
	 * @version 2017年5月14日 上午11:46:08<br/>
	 * @author andy wang<br/>
	 */
	public int updateCapacity ( MetaLocationVO metaLocationVO ) throws Exception ;
	
	/**
	 * 查询默认暂存区收货库位
	 * @return 默认暂存区收货库位
	 * @throws Exception
	 * @version 2017年3月22日 下午12:33:47<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findDefTempLoc () throws Exception;
	
	/**
	 * 根据库位id，冻结库位
	 * @param locId 库位id
	 * @param isBlock 是否冻结
	 * —— 0 未冻结
	 * —— 1 冻结
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月14日 下午3:34:12<br/>
	 * @author andy wang<br/>
	 */
	public int blockLoc( String locId ,Integer isBlock) throws Exception;
	
	/**
	 * 保存库位
	 * @param metaLocation 库位
	 * @return 保存后的库位对象
	 * @throws Exception
	 * @version 2017年3月12日 下午8:28:14<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation insertLoc ( MetaLocation metaLocation ) throws Exception;
	
	/**
	 * 根据库位编号，查询库位
	 * @param locationNo 库位编号
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年3月12日 下午9:24:41<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLocByNo ( String locationNo , String locationId ) throws Exception;
	
	/**
	 * 更新库位状态
	 * @param locationId 库位id
	 * @param status 库位状态
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月12日 下午9:18:11<br/>
	 * @author andy wang<br/>
	 */
	public int updateLocStatusById ( String locationId , Integer status ) throws Exception;
	
	/**
	 * 根据条件，分页查询库位
	 * @param metaLocationVO 查询条件
	 * @return 查询结果
	 * @throws Exception
	 * @version 2017年3月12日 下午8:47:46<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("rawtypes")
	public Page listLocByPage ( MetaLocationVO metaLocationVO ) throws Exception;
	
	/**
	 * 更新库位
	 * @param locationVO 库位对象
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月12日 下午7:55:06<br/>
	 * @author andy wang<br/>
	 */
	public int updateLoc ( MetaLocationVO locationVO ) throws Exception;
	
	/**
	 * 根据库位id，查询库位信息
	 * @param listLocId 库位id集合
	 * @return 库位集合
	 * @throws Exception
	 * @version 2017年3月6日 上午11:51:49<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocation> listLocById ( List<String> listLocId ) throws Exception;
	
	/**
	 * 扣减预分配库容度(方法内不进行数量与库容的转换。直接提供计算好的库容)
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 扣减预分配库容度<br/>
	 * @param skuId 移出的货品货品id
	 * @param locationId 移出的库位id
	 * @param packId 移出货品的包装id
	 * @param capacity 移出库容(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月4日 下午6:00:03<br/>
	 * @author andy wang<br/>
	 */
	public void minusPreusedCapacity ( String skuId , String locationId , String packId , BigDecimal capacity ) throws Exception;



	/**
	 * 根据库位规格id，更新最大库容
	 * @param specId 库位规格id
	 * @param maxCapacity 更新的最大库容
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月14日 上午11:52:02<br/>
	 * @author andy wang<br/>
	 */
	public double updateMaxCapacity ( String specId , BigDecimal maxCapacity ) throws Exception ;
	/**
	 * 增加预分配库容度(方法内不进行数量与库容的转换。直接提供计算好的库容)
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param capacity 移入库容(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年3月4日 下午6:00:03<br/>
	 * @author andy wang<br/>
	 */
	public void addPreusedCapacity ( String skuId , String locationId , String packId , BigDecimal capacity ) throws Exception;
	/**
	 * 增加预分配库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param qty 移入数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午2:30:07<br/>
	 * @author andy wang<br/>
	 */
	public void addPreusedCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception;
	/**
	 * 增加库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加库容度<br/>
	 * —— * 释放预分配库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移入的货品货品id
	 * @param locationId 移入的库位id
	 * @param packId 移入货品的包装id
	 * @param qty 移入数量(使用正数)
	 * @param isRelease 是否释放预分配库存
	 * —— true 释放预分配库存
	 * —— false 不操作释放
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void addCapacity ( String skuId , String locationId , String packId , Double qty , boolean isRelease ) throws Exception;



	/**
	 * 减少预分配库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），库位不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 增加/扣减预分配库容度<br/>
	 * @param skuId 移出的货品货品id
	 * @param locationId 移出的库位id
	 * @param packId 移出货品的包装id
	 * @param qty 移出数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月27日 下午2:30:07<br/>
	 * @author andy wang<br/>
	 */
	public void minusPreusedCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception ;
	
	/**
	 * 减少库容度
	 * —— 接口内容包括：<br/>
	 * —— * 判断库位是否能使用（是否存在、冻结、生效），不能使用报异常<br/>
	 * —— * 判断库容度是否足够，库容度不足够时抛异常<br/>
	 * —— * 减少库容度<br/>
	 * —— * 自动更新<strong>库容占比</strong><br/>
	 * @param skuId 移出货品id
	 * @param locationId 移出库位id
	 * @param packId 移出货品的包装id
	 * @param qty 移出数量(使用正数)
	 * @return 更新的数量
	 * @throws Exception
	 * @version 2017年2月23日 下午5:04:33<br/>
	 * @author andy wang<br/>
	 */
	public void minusCapacity ( String skuId , String locationId , String packId , Double qty ) throws Exception;
	
	/**
	 * 根据id，查询库位信息
	 * @param locationId 库位id
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年2月23日 下午2:49:45<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLocById ( String locationId ) throws Exception;
	
	/**
	 * 根据条件，查询库位
	 * @param metaLocationVO 查询条件
	 * @return 库位对象
	 * @throws Exception
	 * @version 2017年2月23日 下午2:45:34<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocation findLoc ( MetaLocationVO metaLocationVO ) throws Exception;
	
	/**
	 * 根据条件，查询库位
	 * @param metaLocationVO 查询条件
	 * @return 库位集合
	 * @throws Exception
	 * @version 2017年2月23日 下午2:31:44<br/>
	 * @author andy wang<br/>
	 */
	public List<MetaLocation> listLocByExample ( MetaLocationVO metaLocationVO ) throws Exception;
	
	public MetaLocation findLoc ( MetaLocation entity );
	
	/** 
	* @Title: findLocByT 
	* @Description: 查询退货区库位
	* @auth tphe06
	* @time 2018 2018年8月29日 上午11:35:44
	* @return
	* MetaLocation
	*/
	public MetaLocation findLocByT();
}