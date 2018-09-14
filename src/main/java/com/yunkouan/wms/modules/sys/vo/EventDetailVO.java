/**
 * Created on 2017年2月16日
 * InvShiftVO.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2016 =======================================================
 */
package com.yunkouan.wms.modules.sys.vo;

import com.yunkouan.base.BaseVO;

/**
 * 首页VO对象<br/><br/>
 * @Description 
 * @version 2017年2月16日 上午9:52:01<br/>
 * @author 王通<br/>
 */
public class EventDetailVO extends BaseVO {

	/**
	 * 
	 * @version 2017年2月16日 上午10:06:30<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 7066852346579800875L;

	
	/**
	 * 构造方法
	 * @param url
	 * @param text
	 * @param value
	 * @version 2017年5月15日 上午10:22:55<br/>
	 * @author 王通<br/>
	 */
	public EventDetailVO(String url, String param, String text, Integer value) {
		super();
		this.url = url;
		this.setParam(param);
		this.text = text;
		this.value = value;
	}

	/**
	 * 构造方法
	 * @param url
	 * @param text
	 * @param value
	 * @version 2017年5月15日 上午10:22:55<br/>
	 * @author 王通<br/>
	 */
	public EventDetailVO(String url, String text, Integer value) {
		super();
		this.url = url;
		this.text = text;
		this.value = value;
	}
	/**
	 * 跳转地址
	 * @version 2017年3月14日 上午9:19:16<br/>
	 * @author 王通<br/>
	 */
	private String url;
	/**
	 * 跳转地址附带参数
	 * @version 2017年3月14日 上午9:19:16<br/>
	 * @author 王通<br/>
	 */
	private String param;
	/**
	 * 提示文字
	 * @version 2017年3月14日 上午9:18:57<br/>
	 * @author 王通<br/>
	 */
	private String text;
	/**
	 * 统计数字
	 * @version 2017年2月16日 上午10:09:12<br/>
	 * @author 王通<br/>
	 */
	private Integer value;
	
	/* getset *************************************************/
	
	

	/**
	 * 属性 url getter方法
	 * @return 属性url
	 * @author 王通<br/>
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 属性 url setter方法
	 * @param url 设置属性url的值
	 * @author 王通<br/>
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 属性 text getter方法
	 * @return 属性text
	 * @author 王通<br/>
	 */
	public String getText() {
		return text;
	}

	/**
	 * 属性 text setter方法
	 * @param text 设置属性text的值
	 * @author 王通<br/>
	 */
	public void setText(String text) {
		this.text = text;
	}


	/* method ********************************************/
	/**
	 * 查询缓存
	 * @return
	 * 创建日期:<br/> 2017年2月16日10:19:42<br/>
	 * @author 王通<br/>
	 */
	public EventDetailVO searchCache () {
		return this;
	}

	/**
	 * 属性 value getter方法
	 * @return 属性value
	 * @author 王通<br/>
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 属性 value setter方法
	 * @param value 设置属性value的值
	 * @author 王通<br/>
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * 属性 param getter方法
	 * @return 属性param
	 * @author 王通<br/>
	 */
	public String getParam() {
		return param;
	}

	/**
	 * 属性 param setter方法
	 * @param param 设置属性param的值
	 * @author 王通<br/>
	 */
	public void setParam(String param) {
		this.param = param;
	}


}
