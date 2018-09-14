/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 上午10:08:34<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow;

/**
 * 业务工作流的流程节点接口定义 <br/><br/>
 * @version 2017年3月5日 上午10:08:34<br/>
 * @author andy wang<br/>
 */
public interface FlowNode {
	
	/**
	 * 节点运行前
	 * @param fc 上下文对象
	 * @return 是否继续流程
	 * —— true 继续流程
	 * —— false 退出流程
	 * @throws Exception
	 * @version 2017年3月5日 下午2:25:05<br/>
	 * @author andy wang<br/>
	 */
	public Boolean executeBefore( FlowContext fc ) throws Exception;
	
	/**
	 * 运行节点
	 * @param fc 上下文对象
	 * @return 是否继续流程
	 * —— true 继续流程
	 * —— false 退出流程
	 * @throws Exception
	 * @version 2017年3月5日 上午10:09:14<br/>
	 * @author andy wang<br/>
	 */
	public Boolean execute( FlowContext fc ) throws Exception;
	
	/**
	 * 节点运行后
	 * @param fc 上下文对象
	 * @return 是否继续流程
	 * —— true 继续流程
	 * —— false 退出流程
	 * @throws Exception
	 * @version 2017年3月5日 下午2:25:25<br/>
	 * @author andy wang<br/>
	 */
	public Boolean executeAfter( FlowContext fc ) throws Exception;
	
}
