package com.yunkouan.wms.modules.inv.service;

import org.springframework.http.ResponseEntity;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.inv.vo.InvInventoryStatisticsVO;

/**
 * 在库业务接口
 */
public interface IInventoryStatisticsService {


	/**
	 * 下载excel
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年10月23日 上午10:06:04<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> downloadExcel(InvInventoryStatisticsVO vo) throws Exception;

	public Page<InvInventoryStatisticsVO> listByPage(InvInventoryStatisticsVO vo) throws Exception;



}