package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.wms.modules.meta.entity.MetaMachine;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 设备数据传输类
 * @author tphe06 2017年2月21日
 */
public class MachineVo extends BaseVO {
	private static final long serialVersionUID = -8208129288742171620L;

	/**设备实体类*/
	@Valid
	private MetaMachine entity;
	/**@Fields 排序字段 */
	private String orderBy = "machine_id2 desc";
	/**@Fields 设备编号模糊查询字段 */
	private String machineNoLike;
	/**@Fields 设备名称模糊查询字段 */
	private String machineNameLike;

	public MachineVo(){}
	public MachineVo(MetaMachine entity) {
		this.entity = entity;
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaMachine.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(machineNoLike)) {
			c.andLike("machineNo", "%"+machineNoLike+"%");
		}
		if(StringUtils.isNoneBlank(machineNameLike)) {
			c.andLike("machineName", "%"+machineNameLike+"%");
		}
		if(StringUtils.isNoneBlank(entity.getMachineNo())) {
			c.andEqualTo("machineNo", entity.getMachineNo());
		}
		if(StringUtils.isNoneBlank(entity.getMachineName())) {
			c.andEqualTo("machineName", entity.getMachineName());
		}
		if(entity.getWorkStatus() != null) {
			c.andEqualTo("workStatus", entity.getWorkStatus());
		}
		if(entity.getMachineStatus() != null) {
			c.andEqualTo("machineStatus", entity.getMachineStatus());
		}
		c.andEqualTo("orgId", entity.getOrgId());
		c.andEqualTo("warehouseId", entity.getWarehouseId());
		return example;
	}

	public MetaMachine getEntity() {
		return entity;
	}

	public MachineVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public void setMachineNoLike(String machineNoLike) {
		this.machineNoLike = machineNoLike;
	}
	public void setMachineNameLike(String machineNameLike) {
		this.machineNameLike = machineNameLike;
	}
}