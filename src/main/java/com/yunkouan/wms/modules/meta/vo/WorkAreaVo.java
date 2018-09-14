package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.meta.entity.MetaWorkArea;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 工作区数据传输类
 * @author tphe06 2017年2月21日
 */
public class WorkAreaVo extends BaseVO {
	private static final long serialVersionUID = 7433258744971099406L;

	/**工作区实体类*/
	@Valid
	private MetaWorkArea entity;
	/**@Fields 排序字段 */
	private String orderBy = "work_area_id2 desc";
	/**@Fields 工作区名称模糊查询字段 */
	private String workAreaNameLike;

	public WorkAreaVo(){}
	public WorkAreaVo(MetaWorkArea entity) {
		this.entity = entity;
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaWorkArea.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(workAreaNameLike)) {
			c.andLike("workAreaName", "%"+workAreaNameLike+"%");
		}
		if(StringUtils.isNoneBlank(entity.getWorkAreaName())) {
			c.andEqualTo("workAreaName", entity.getWorkAreaName());
		}
		if(StringUtils.isNoneBlank(entity.getAreaId())) {
			c.andEqualTo("areaId", entity.getAreaId());
		}
		c.andEqualTo("orgId", entity.getOrgId());
		c.andEqualTo("warehouseId", entity.getWarehouseId());
		return example;
	}

	public MetaWorkArea getEntity() {
		return entity;
	}
	public WorkAreaVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public void setWorkAreaNameLike(String workAreaNameLike) {
		this.workAreaNameLike = workAreaNameLike;
	}
}