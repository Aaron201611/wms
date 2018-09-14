package com.yunkouan.wms.modules.inv.controller;

import java.util.Date;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.inv.entity.InvInventoryStatistics;
import com.yunkouan.wms.modules.inv.service.IInventoryStatisticsService;
import com.yunkouan.wms.modules.inv.vo.InvInventoryStatisticsVO;

/**
 * 在库管理控制类
 */
@Controller

@RequestMapping("${adminPath}/inventoryStatistics")
@RequiresPermissions(value = { "inventoryStatistics.view" , "stockWarning.view"},logical=Logical.OR)
public class InventoryStatisticsController  extends BaseController {

    /**
     * 库存统计业务服务类
     * @author 王通<br/>
     */
	@Autowired
    private IInventoryStatisticsService inventoryStatisticsService;
    /**
     * Default constructor
     */
    public InventoryStatisticsController() {
    }

    /**
     * 库存数据列表查询
     * @return 
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
 	@ResponseBody
    public ResultModel list( @RequestBody InvInventoryStatisticsVO vo ) throws Exception {
    	Page<InvInventoryStatisticsVO> list = this.inventoryStatisticsService.listByPage(vo);
    	ResultModel rm = new ResultModel();
    	rm.setPage(list);
        return rm;
    }

    /**
     * 下载excel文件
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> downloadExcel(String skuNameLike, String skuNoLike, String ownerLike, Date startDate) throws Exception {
    	InvInventoryStatisticsVO vo = new InvInventoryStatisticsVO();
    	InvInventoryStatistics entity = vo.getInventoryStatistics();
    	if (StringUtil.isNoneBlank(skuNameLike)) {
    		vo.setSkuNameLike(skuNameLike);
    	}
    	if (StringUtil.isNoneBlank(skuNoLike)) {
        	vo.setSkuNoLike(skuNoLike);
    	}
    	if (StringUtil.isNoneBlank(skuNoLike)) {
        	vo.setSkuNoLike(skuNoLike);
    	}
    	if (StringUtil.isNoneBlank(ownerLike)) {
        	vo.setOwnerNameLike(ownerLike);
    	}
    	if (startDate != null) {
        	entity.setStatisticsDate(startDate);
    	}
    	vo.setPageSize(99999999);
    	return this.inventoryStatisticsService.downloadExcel(vo);
	}

}