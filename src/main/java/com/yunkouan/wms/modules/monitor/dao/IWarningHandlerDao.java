package com.yunkouan.wms.modules.monitor.dao;

import java.util.List;

import com.yunkouan.wms.modules.monitor.entity.ExceptionLog;
import com.yunkouan.wms.modules.monitor.entity.WarningHandler;
import com.yunkouan.wms.modules.monitor.vo.ShelflifeWarningVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * 警告处理数据层接口
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午5:57:56<br/>
 * @author 王通<br/>
 */
public interface IWarningHandlerDao  extends Mapper<WarningHandler>{

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月5日 下午4:03:07<br/>
	 * @author 王通<br/>
	 */
	List<ShelflifeWarningVO> findShelflifeList(ShelflifeWarningVO vo);
	

}