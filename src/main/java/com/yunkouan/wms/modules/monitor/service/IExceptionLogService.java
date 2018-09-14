package com.yunkouan.wms.modules.monitor.service;

import java.util.*;

import com.github.pagehelper.Page;
import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.vo.ExceptionLogVO;

/**
 * 异常服务接口
 */
public interface IExceptionLogService {

	/**
	 * 分页查询异常列表
	 * @param exceptionLogVO
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:03:01<br/>
	 * @author 王通<br/>
	 */
	public Page<ExceptionLogVO> listByPage(ExceptionLogVO exceptionLogVO);

	/**
	 * 查看异常管理详情
	 * @param id
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:03:34<br/>
	 * @author 王通<br/>
	 */
	public ExceptionLogVO view(String id);

	/**
	 * 登记异常
	 * @param log
	 * @return
	 * @required exType,involveBill,exStatus,exDesc,exLevel
	 * @optional 
	 * @Description 
	 * @version 2017年3月13日 下午2:04:26<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ExceptionLog add(ExceptionLog log) throws Exception;

	/**
	 * 修改异常/处理异常
	 * @param log
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:04:44<br/>
	 * @author 王通<br/>
	 */
	public ExceptionLog update(ExceptionLog log);

	/**
	 * 取消异常
	 * @param exceptionLogIdList
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午2:06:07<br/>
	 * @author 王通<br/>
	 */
	public void cancel(List<String> exceptionLogIdList);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 上午10:32:58<br/>
	 * @author 王通<br/>
	 */
	public int countByExample(ExceptionLogVO vo);
	
	public List<ExceptionLog> countByExample2(ExceptionLogVO vo);

	public List<String>getTask(String orgId);
}