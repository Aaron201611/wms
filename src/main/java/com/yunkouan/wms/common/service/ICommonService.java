package com.yunkouan.wms.common.service;

import com.yunkouan.wms.common.vo.CommonVo;

/**
 * 通用接口获取编号
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午3:06:58<br/>
 * @author 王通<br/>
 */
//@Deprecated
public interface ICommonService {
	/**
	 * @param vo
	 * @return
	 * @Description 
	 * @version 2017年2月17日 下午3:07:43<br/>
	 * @author 王通<br/>
	 */
	public String getNo(CommonVo vo);
	
	
//	public String getNo(CommonVo vo,Integer len);
}