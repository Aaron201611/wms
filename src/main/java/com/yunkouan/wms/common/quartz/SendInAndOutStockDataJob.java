package com.yunkouan.wms.common.quartz;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaWarehouseSetting;
import com.yunkouan.saas.modules.sys.entity.MetaWarehouse;
import com.yunkouan.saas.modules.sys.service.IWarehouseExtlService;
import com.yunkouan.saas.modules.sys.vo.MetaWarehouseVO;
import com.yunkouan.wms.modules.meta.service.IWarehouseSettingService;
import com.yunkouan.wms.modules.meta.vo.MetaWarehouseSettingVo;
import com.yunkouan.wms.modules.rec.service.IASNService;
import com.yunkouan.wms.modules.send.service.IDeliveryService;

/**
 * 发送出入库数据
 * @author Aaron
 *
 */
@Component("sendInAndOutStockDataJob")
public class SendInAndOutStockDataJob {
	
	private static Log log = LogFactory.getLog(SendInAndOutStockDataJob.class);
	
	@Autowired
	private IDeliveryService deliveryService;
	
	@Autowired
	private IASNService asnService;
	
	@Autowired
	private IWarehouseExtlService warehouseExtlService;
	
	@Autowired
	private IWarehouseSettingService warehouseSettingService;
	
	public static String UTF_FORMAT = "UTF-8";
	
	public void execute() throws Exception{
		if(log.isInfoEnabled()) log.info("------发送出入库数据任务开发");
		//查询普通仓列表
		MetaWarehouseVO metaWarehouseVO = new MetaWarehouseVO();
		metaWarehouseVO.getWarehouse().setWarehouseType(Constant.WAREHOUSE_TYPE_NOTCUSTOMS);
		List<MetaWarehouse> listWrh = warehouseExtlService.listWareHouseByExample(metaWarehouseVO, null);
		if(listWrh == null || listWrh.isEmpty()) return;
		
		List<String> whIdList = listWrh.stream().map(t->t.getWarehouseId()).collect(Collectors.toList());
		
		//查询设置了同步库存的仓库配置列表
		MetaWarehouseSettingVo settingVo = new MetaWarehouseSettingVo(new MetaWarehouseSetting());
		settingVo.setWarehouseIdList(whIdList);
		settingVo.getEntity().setIsSyncStock(true);
		List<MetaWarehouseSetting> whsettingList = warehouseSettingService.qryByParam(settingVo);		
		if(whsettingList == null || whsettingList.isEmpty()) return;
		
		List<String> synIdList = whsettingList.stream().map(t->t.getWarehouseId()).collect(Collectors.toList());
		
		//将所有仓库的入库数据发送到海关
//		asnService.findAndSendInStockData(synIdList);
		//将所有仓库的出库数据发送到海关
//		deliveryService.findAndSendOutStockData(synIdList);
		
		if(log.isInfoEnabled()) log.info("------发送出入库数据任务结束");
	}
}
