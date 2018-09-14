package com.yunkouan.wms.modules.monitor.service;

import java.util.*;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.entity.WarningHandler;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;

/**
 * 保质期预警服务接口
 */
public interface IShelflifeWarningService {

	/**
	 * 分页查询异常列表
	 * @param shelflifeWarningVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:03:01<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public Page<ShelflifeWarningVO> listByPage(ShelflifeWarningVO shelflifeWarningVO) throws Exception;

	/**
	 * 提交处理信息
	 * @param handler
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月5日 下午3:44:49<br/>
	 * @author 王通<br/>
	 */
	public void handler(WarningHandler handler);

	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:51:10<br/>
	 * @author 王通<br/>
	 */
	public Integer count();
	
	public List<ShelflifeWarningVO> count(String orgId);


}