package com.yunkouan.wms.modules.park.dao;

import java.util.*;

import org.apache.ibatis.annotations.Param;

import com.yunkouan.wms.modules.park.entity.ParkRent;

import tk.mybatis.mapper.common.Mapper;

/**
 * 仓库出租数据层接口
 *@Description TODO
 *@author Aaron
 *@date 2017年3月8日 上午10:44:39
 *version v1.0
 */
public interface IParkRentDao extends Mapper<ParkRent>{

	/**
	 * 查询出租列表
	 * @param rent
	 * @return
	 * @version 2017年3月8日 上午10:45:14<br/>
	 * @author Aaron He<br/>
	 */
	public List<ParkRent> qryList(@Param("orgList")List<String> orgList,@Param("whList")List<String> whList,
			@Param("merchantList")List<String> merchantList,@Param("rentStatus")Integer rentStatus);
	

}