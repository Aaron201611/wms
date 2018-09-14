/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月5日 上午10:07:53<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.inv.util.flow;

import java.util.ArrayList;
import java.util.List;

import com.yunkouan.util.PubUtil;

/**
 * 业务工作流抽象类，用于整合，执行整个业务流程 <br/><br/>
 * @version 2017年3月5日 上午10:07:53<br/>
 * @author andy wang<br/>
 */
public abstract class AbstractFlow<T> implements Flow<T> {
	
	/**
	 * 节点集合
	 * @version 2017年3月5日上午10:09:49<br/>
	 * @author andy wang<br/>
	 */
	private List<FlowNode> listNode;
	
	/**
	 * 流程上下文
	 * @version 2017年3月5日下午6:06:24<br/>
	 * @author andy wang<br/>
	 */
	protected FlowContext fc;
	
	/**
	 * 流程节点索引
	 * @version 2017年3月5日下午1:51:38<br/>
	 * @author andy wang<br/>
	 */
//	private int flowIndex;
	
	/**
	 * 构造方法
	 * @version 2017年3月5日 下午1:53:35<br/>
	 * @author andy wang<br/>
	 */
	public AbstractFlow() {
		this(new FlowContext());
	}
	
	/**
	 * 构造方法
	 * @param fc 上下文对象
	 * @version 2017年3月7日 下午4:39:22<br/>
	 * @author andy wang<br/>
	 */
	public AbstractFlow( FlowContext fc ) {
		listNode = new ArrayList<FlowNode>();
		this.fc = fc;
	}
	
	/**
	 * 初始化流程节点
	 * @version 2017年3月5日下午6:16:09<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void initFlow() throws Exception;

	/**
	 * 添加节点
	 * @param node 节点
	 * @version 2017年3月5日 下午1:50:52<br/>
	 * @author andy wang<br/>
	 */
	protected final void addNode(FlowNode node) {
		listNode.add(node);
	}

	/**
	 * 获取下一个节点
	 * @return 下一个节点
	 * @version 2017年3月5日 下午1:53:15<br/>
	 * @author andy wang<br/>
	 */
//	protected final FlowNode nextNode () {
//		if ( this.flowIndex >= this.listNode.size() ) {
//			return null;
//		}
//		return this.listNode.get(this.flowIndex++);
//	}
	
	/**
	 * 节点索引重置
	 * @version 2017年3月5日 下午1:54:36<br/>
	 * @author andy wang<br/>
	 */
//	private final void reset() {
//		this.flowIndex = 0;
//	}
	
	/**
	 * 开始流程
	 * @throws Exception
	 * @version 2017年3月5日 下午2:27:55<br/>
	 * @author andy wang<br/>
	 */
	@Override
	public void startFlow() throws Exception {
		this.initFlow();
		if ( PubUtil.isEmpty(this.listNode) ) {
			throw new RuntimeException("Process not defined");
		}
		this.beforeFlow();
		for (FlowNode flowNode : listNode) {
			if ( !flowNode.executeBefore(fc) ) {
				break;
			}
			if ( !flowNode.execute(fc) ) {
				break;
			}
			if ( !flowNode.executeAfter(fc) ) {
				break;
			}
		}
		this.afterFlow();
//		this.reset();
	}
	
	
	/**
	 * 执行 流程启动前 业务处理
	 * @version 2017年3月6日 下午3:49:31<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void beforeFlow() throws Exception;
	
	/**
	 * 执行 流程执行完毕后 业务处理
	 * @version 2017年3月6日 下午3:49:41<br/>
	 * @author andy wang<br/>
	 */
	protected abstract void afterFlow() throws Exception;
}