/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 下午2:11:41<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.util.flow;

/**
 * 业务工作流接口定义 <br/><br/>
 * @version 2017年3月5日 下午2:11:41<br/>
 * @author andy wang<br/>
 */
public interface Flow<T> {

	/**
	 * 开始流程
	 * @throws Exception 
	 * @version 2017年3月5日 下午2:27:55<br/>
	 * @author andy wang<br/>
	 */
	public void startFlow() throws Exception ;
	
	
	/**
	 * 获取工作流完成后的结果
	 * @return 结果
	 * @throws Exception
	 * @version 2017年3月6日 下午4:04:20<br/>
	 * @author andy wang<br/>
	 */
	public T getResult() throws Exception;
	
}
