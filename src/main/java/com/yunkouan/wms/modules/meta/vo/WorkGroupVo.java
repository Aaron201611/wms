package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.meta.entity.MetaWorkGroup;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
* @Description: 班组数据传输类
* @author tphe06
* @date 2017年3月14日
*/
public class WorkGroupVo extends BaseVO {
	private static final long serialVersionUID = 4590900189331114064L;

	@Valid
	private MetaWorkGroup entity;
	/**@Fields 排序字段 */
	private String orderBy = "work_group_id2 desc";
	/**@Fields 班组编号模糊查询字段 */
	private String workGroupNoLike;
	/**@Fields 班组名称模糊查询字段 */
	private String workGroupNameLike;

	public WorkGroupVo(){}
	public WorkGroupVo(MetaWorkGroup entity)  {
		this.entity = entity;
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaWorkGroup.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(workGroupNoLike)) {
			c.andLike("workGroupNo", "%"+workGroupNoLike+"%");
		}
		if(StringUtils.isNoneBlank(workGroupNameLike)) {
			c.andLike("workGroupName", "%"+workGroupNameLike+"%");
		}
		if(StringUtils.isNoneBlank(entity.getWorkGroupNo())) {
			c.andEqualTo("workGroupNo", entity.getWorkGroupNo());
		}
		if(StringUtils.isNoneBlank(entity.getWorkGroupName())) {
			c.andEqualTo("workGroupName", entity.getWorkGroupName());
		}
		if(entity.getWorkGroupStatus() != null) {
			c.andEqualTo("workGroupStatus", entity.getWorkGroupStatus());
		}
		c.andEqualTo("orgId", entity.getOrgId());
		c.andEqualTo("warehouseId", entity.getWarehouseId());
		return example;
	}

	public MetaWorkGroup getEntity() {
		return entity;
	}
	public WorkGroupVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public void setWorkGroupNoLike(String workGroupNoLike) {
		this.workGroupNoLike = workGroupNoLike;
	}
	public void setWorkGroupNameLike(String workGroupNameLike) {
		this.workGroupNameLike = workGroupNameLike;
	}
}