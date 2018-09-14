package com.yunkouan.wms.modules.ts.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.modules.ts.entity.TsTask;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public class TsTaskVo extends BaseVO{
	private static final long serialVersionUID = 7524575688667869215L;
	
	private TsTask tsTask = new TsTask();
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss",locale = "zh" , timezone="GMT+8")
	private Date lastItemTime; 
	
	@JsonIgnore
	private Example example;
	
	private List<Integer> statusList;
	
	private String orderByClause = "notice_time asc";
	
	private String groupByClause;
	
	private String taskStatusComment;//状态说明
	
	private List<String> opIdList;
	
	private String operer;
	
	public TsTaskVo(){
			
	}

	public TsTask getTsTask() {
		return tsTask;
	}

	public void setTsTask(TsTask tsTask) {
		this.tsTask = tsTask;
		if(tsTask.getTaskStatus() != null){
			ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
			this.taskStatusComment = paramService.getValue(CacheName.TASK_STATUS, tsTask.getTaskStatus());
		}
	}

	public Date getLastItemTime() {
		return lastItemTime;
	}

	public void setLastItemTime(Date lastItemTime) {
		this.lastItemTime = lastItemTime;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
	public void addStatus(Integer status) {
		if(this.statusList == null) this.statusList = new ArrayList<Integer>();
		this.statusList.add(status);
	}

	@JsonIgnore
	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
	
	public String getGroupByClause() {
		return groupByClause;
	}

	public void setGroupByClause(String groupByClause) {
		this.groupByClause = groupByClause;
	}
	

	public String getTaskStatusComment() {
		return taskStatusComment;
	}

	public void setTaskStatusComment(String taskStatusComment) {
		this.taskStatusComment = taskStatusComment;
	}

	@JsonIgnore
	public Example getConditionExample(){
		this.example = new Example(TsTask.class);	
		this.example.setOrderByClause(this.orderByClause);
		Criteria c = this.example.createCriteria();
		if(!StringUtil.isTrimEmpty(tsTask.getOrgId())){
			c.andEqualTo("orgId",tsTask.getOrgId());
		}
		if(!StringUtil.isTrimEmpty(tsTask.getWarehouseId())){
			c.andEqualTo("warehouseId",tsTask.getWarehouseId());
		}
		if(!StringUtil.isTrimEmpty(tsTask.getOpPerson())){
			c.andEqualTo("opPerson",tsTask.getOpPerson());
		}
		if(tsTask.getTaskType() != null){
			c.andEqualTo("taskType",tsTask.getTaskType());
		}
		if(tsTask.getTaskStatus() != null){
			c.andEqualTo("taskStatus",tsTask.getTaskStatus());
		}
		if(statusList != null && statusList.size() != 0){
			c.andIn("taskStatus", statusList);
		}
		if(!StringUtil.isTrimEmpty(tsTask.getOpId())){
			c.andEqualTo("opId",tsTask.getOpId());
		}
//		if(lastItemTime != null){
//			c.andGreaterThan("createTime", lastItemTime);
//		}
		return this.example;
	}

	public List<String> getOpIdList() {
		return opIdList;
	}

	public void setOpIdList(List<String> opIdList) {
		this.opIdList = opIdList;
	}

	public String getOperer() {
		return operer;
	}

	public void setOperer(String operer) {
		this.operer = operer;
	}
	
}