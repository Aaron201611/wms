/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年2月23日 下午2:32:10<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.util;

import java.math.BigDecimal;

import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.entity.MetaLocation;
import com.yunkouan.util.NumberUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.vo.MetaLocationVO;

/**
 * 库位工具类<br/><br/>
 * @version 2017年2月23日 下午2:32:10<br/>
 * @author andy wang<br/>
 */
public class LocationUtil {
	
	/**
	 * 为库位对象中的XYZ属性设置默认值
	 * @param metaLocation 库位对象
	 * @version 2017年3月7日 下午8:21:42<br/>
	 * @author andy wang<br/>
	 */
	public static void defXYZ ( MetaLocation metaLocation ) {
		if ( metaLocation == null ) {
			return ;
		}
		if ( metaLocation.getX() == null ) {
			metaLocation.setX(0);
		}
		if ( metaLocation.getY() == null ) {
			metaLocation.setY(0);
		}
		if ( metaLocation.getZ() == null ) {
			metaLocation.setZ(0);
		}
	}
	
	/**
	 * 计算库位剩余库容
	 * @param metaLocation 库位对象
	 * @return
	 * @version 2017年3月6日 下午2:31:58<br/>
	 * @author andy wang<br/>
	 */
	public static BigDecimal surplusCapacity ( MetaLocation metaLocation ) {
		if ( metaLocation == null ) {
			return BigDecimal.valueOf(0);
		}
		LocationUtil.defCapacity(metaLocation);
		return BigDecimal.valueOf(NumberUtil.sub(metaLocation.getMaxCapacity(),metaLocation.getUsedCapacity(),metaLocation.getPreusedCapacity()));
	}
	
	/**
	 * 设置默认库容
	 * @param metaLocation 库位对象
	 * @version 2017年3月6日 下午2:33:21<br/>
	 * @author andy wang<br/>
	 */
	public static void defCapacity ( MetaLocation metaLocation ) {
		if ( metaLocation == null ) {
			return ;
		}
		if ( metaLocation.getMaxCapacity() == null ) {
			metaLocation.setMaxCapacity(BigDecimal.valueOf(0));
		}
		if ( metaLocation.getUsedCapacity() == null ) {
			metaLocation.setUsedCapacity(BigDecimal.valueOf(0));
		}
		if ( metaLocation.getPreusedCapacity() == null ) {
			metaLocation.setPreusedCapacity(BigDecimal.valueOf(0));
		}
	}
	
	
	/**
	 * 货品数量转换库容
	 * @param qty 货品数量
	 * @param sku 货品数据
	 * @return 库容
	 * @version 2017年3月3日 下午11:44:14<br/>
	 * @author andy wang<br/>
	 */
	public static BigDecimal capacityConvert ( Double qty , MetaSku sku ) {
		if ( sku == null || qty == null ) {
			return BigDecimal.valueOf(0);
		}
		Double perVolume = sku.getPerVolume();
		if ( perVolume == null ) {
			perVolume = 0d;
		}
		return BigDecimal.valueOf(NumberUtil.mul(qty, perVolume));
	}
	
	/**
	 * 校验库容量
	 * —— true 没有足够库容量
	 * —— false 有足够库容量
	 * @return
	 * @version 2017年2月26日 下午5:37:09<br/>
	 * @author andy wang<br/>
	 */
	public static boolean checkCapacity ( MetaLocationVO metaLocationVO ) {
		if ( metaLocationVO == null || metaLocationVO.getLocation() == null ) {
			return false;
		}
		MetaLocation location = metaLocationVO.getLocation();
		if ( location.getUsedCapacity() == null ) {
			location.setUsedCapacity(BigDecimal.valueOf(0));
		}

		if ( location.getMaxCapacity() == null ) {
			location.setMaxCapacity(BigDecimal.valueOf(0));
			
		}
		if ( location.getPreusedCapacity() == null ) {
			location.setPreusedCapacity(BigDecimal.valueOf(0));
		}
		return metaLocationVO.getCapacity().compareTo(LocationUtil.surplusCapacity(location)) > 0;
	}
	
	
	/**
	 * 校验库位是否能操作
	 * @param metaLocation 库位
	 * @param isThrowException 库位不能操作时是否抛出异常
	 * @return
	 * @version 2017年2月24日 下午5:21:21<br/>
	 * @author andy wang<br/>
	 */
	public static boolean checkLocation ( MetaLocation metaLocation , boolean isThrowException ) throws Exception {
		// 判断库位是否存在
		if ( metaLocation == null ) {
			if ( isThrowException ) {
				throw new BizException("err_main_location_null");
			} else {
				return false;
			}
		}
		// 判断库位是否冻结
		if ( Constant.LOCATION_BLOCK_TRUE == metaLocation.getIsBlock() ) {
			if ( isThrowException ) {
				throw new BizException("err_rec_putaway_active_tempLocationBlock");
			} else {
				return false;
			}
		}
		// 判断库位是否生效
		if ( Constant.LOCATION_STATUS_ACTIVE != metaLocation.getLocationStatus() ) {
			if ( isThrowException ) {
				throw new BizException("err_main_location_notActive");
			} else {
				return false;
			}
		}
		return true;
	}
	
}
