package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.modules.meta.entity.MetaPack;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 包装数据传输类
 * @author tphe06 2017年2月14日
 */
public class PackVo extends BaseVO {
	private static final long serialVersionUID = -7708233606832806609L;

	/**@Fields 包装实体类 */
	@Valid
	private MetaPack entity;
	/**@Fields 排序字段 */
	private String orderBy = "pack_id2 desc";
	/**@Fields 包装编码模糊查询字段 */
	private String packNoLike;
	/**@Fields 包装单位模糊查询字段 */
	private String packUnitLike;
	/**@Fields 包装级别模糊查询字段 */
	private String packLevelLike;

	/**@Fields 包装状态名称 */
	private String packStatus;

	public PackVo(){}
	public PackVo(MetaPack entity) {
		this.entity = entity;
		if(this.entity == null) return;
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		this.packStatus = paramService.getValue(CacheName.COMMON_STATUS, this.entity.getPackStatus());
	}

	@Override
	public Example getExample() {
		Example example = new Example(MetaPack.class);
		example.setOrderByClause(orderBy);
		if(entity == null) return example;
		Criteria c = example.createCriteria();
		if(StringUtils.isNoneBlank(packNoLike)) {
			c.andLike("packNo", StringUtil.likeEscapeH(packNoLike));
		}
		if(StringUtils.isNoneBlank(packUnitLike)) {
			c.andLike("packUnit", StringUtil.likeEscapeH(packUnitLike));
		}
		if(StringUtils.isNoneBlank(packLevelLike)) {
			c.andLike("packLevel", StringUtil.likeEscapeH(packLevelLike));
		}
		if(StringUtils.isNoneBlank(entity.getPackNo())) {
			c.andEqualTo("packNo", entity.getPackNo());
		}
		if(StringUtils.isNoneBlank(entity.getPackUnit())) {
			c.andEqualTo("packUnit", entity.getPackUnit());
		}
		if(StringUtils.isNoneBlank(entity.getPackLevel())) {
			c.andEqualTo("packLevel", entity.getPackLevel());
		}
		if(entity.getPackStatus() != null) {
			c.andEqualTo("packStatus", entity.getPackStatus());
		} else {
			c.andNotEqualTo("packStatus", Constant.STATUS_CANCEL);
		}
		return example;
	}

	public MetaPack getEntity() {
		return entity;
	}
	public PackVo orderByUpdateTimeDesc() {
		this.orderBy = "update_time desc ";
		return this;
	}
	public String getPackStatus() {
		return packStatus;
	}
	public void setPackNoLike(String packNoLike) {
		this.packNoLike = packNoLike;
	}
	public void setPackUnitLike(String packUnitLike) {
		this.packUnitLike = packUnitLike;
	}
	public void setPackLevelLike(String packLevelLike) {
		this.packLevelLike = packLevelLike;
	}
}