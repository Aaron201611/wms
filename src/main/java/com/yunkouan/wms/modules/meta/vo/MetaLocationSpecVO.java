/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:11:54<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.meta.vo;

import javax.validation.Valid;

import com.yunkouan.base.BaseVO;
import com.yunkouan.saas.modules.sys.entity.MetaLocationSpec;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 库位规格VO<br/><br/>
 * @version 2017年3月13日下午2:11:54<br/>
 * @author andy wang<br/>
 */
public class MetaLocationSpecVO extends BaseVO {

	private static final long serialVersionUID = 6023457494532134420L;

	/**
	 * 构造方法
	 * @version 2017年3月13日下午2:11:54<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO() {
		this(new MetaLocationSpec());
	}

	/**
	 * 构造方法
	 * @param locSpec 库位规格
	 * @version 2017年3月13日下午2:11:54<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO(MetaLocationSpec locSpec) {
		super();
		this.locSpec = locSpec;
	}

	/**
	 * 库位规格对象
	 * @version 2017年3月13日下午2:11:54<br/>
	 * @author andy wang<br/>
	 */
	@Valid
	private MetaLocationSpec locSpec;

	/**
	 * 库位规格状态中文描述
	 * @version 2017年3月13日下午2:11:54<br/>
	 * @author andy wang<br/>
	 */
	private String locSpecStatusComment;
	
	/**
	 * 仓库中文名称
	 * @version 2017年4月4日下午11:10:27<br/>
	 * @author andy wang<br/>
	 */
	private String warehouseComment;
	
	/**
	 * 最小载重
	 * @version 2017年4月4日下午11:11:02<br/>
	 * @author andy wang<br/>
	 */
	private Double minWeight;
	
	/**
	 * 最大载重
	 * @version 2017年4月4日下午11:11:12<br/>
	 * @author andy wang<br/>
	 */
	private Double maxWeight;
	
	
	/**
	 * 模糊查询库位规格编号
	 * @version 2017年4月4日下午11:11:53<br/>
	 * @author andy wang<br/>
	 */
	private String likeLocSpecNo;
	
	/**
	 * 模糊查询库位规格名称
	 * @version 2017年4月4日下午11:12:13<br/>
	 * @author andy wang<br/>
	 */
	private String likeLocSpecName;

	/* getset *********************************************/

	/**
	 * 属性 likeLocSpecNo getter方法
	 * @return 属性likeLocSpecNo
	 * @author andy wang<br/>
	 */
	public String getLikeLocSpecNo() {
		return likeLocSpecNo;
	}

	/**
	 * 属性 likeLocSpecNo setter方法
	 * @param likeLocSpecNo 设置属性likeLocSpecNo的值
	 * @author andy wang<br/>
	 */
	public void setLikeLocSpecNo(String likeLocSpecNo) {
		this.likeLocSpecNo = likeLocSpecNo;
	}

	/**
	 * 属性 likeLocSpecName getter方法
	 * @return 属性likeLocSpecName
	 * @author andy wang<br/>
	 */
	public String getLikeLocSpecName() {
		return likeLocSpecName;
	}

	/**
	 * 属性 likeLocSpecName setter方法
	 * @param likeLocSpecName 设置属性likeLocSpecName的值
	 * @author andy wang<br/>
	 */
	public void setLikeLocSpecName(String likeLocSpecName) {
		this.likeLocSpecName = likeLocSpecName;
	}

	/**
	 * 属性 minWeight getter方法
	 * @return 属性minWeight
	 * @author andy wang<br/>
	 */
	public Double getMinWeight() {
		return minWeight;
	}

	/**
	 * 属性 minWeight setter方法
	 * @param minWeight 设置属性minWeight的值
	 * @author andy wang<br/>
	 */
	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	/**
	 * 属性 maxWeight getter方法
	 * @return 属性maxWeight
	 * @author andy wang<br/>
	 */
	public Double getMaxWeight() {
		return maxWeight;
	}

	/**
	 * 属性 maxWeight setter方法
	 * @param maxWeight 设置属性maxWeight的值
	 * @author andy wang<br/>
	 */
	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	/**
	 * 属性 locSpec getter方法
	 * @return 属性locSpec
	 * @author andy wang<br/>
	 */
	public MetaLocationSpec getLocSpec() {
		return locSpec;
	}

	/**
	 * 属性 locSpec setter方法
	 * @param locSpec 设置属性locSpec的值
	 * @author andy wang<br/>
	 */
	public void setLocSpec(MetaLocationSpec locSpec) {
		this.locSpec = locSpec;
	}

	/**
	 * 属性 locSpecStatusComment getter方法
	 * @return 属性locSpecStatusComment
	 * @author andy wang<br/>
	 */
	public String getLocSpecStatusComment() {
		return locSpecStatusComment;
	}

	/**
	 * 属性 locSpecStatusComment setter方法
	 * @param locSpecStatusComment 设置属性locSpecStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setLocSpecStatusComment(String locSpecStatusComment) {
		this.locSpecStatusComment = locSpecStatusComment;
	}

	/**
	 * 属性 warehouseComment getter方法
	 * @return 属性warehouseComment
	 * @author andy wang<br/>
	 */
	public String getWarehouseComment() {
		return warehouseComment;
	}

	/**
	 * 属性 warehouseComment setter方法
	 * @param warehouseComment 设置属性warehouseComment的值
	 * @author andy wang<br/>
	 */
	public void setWarehouseComment(String warehouseComment) {
		this.warehouseComment = warehouseComment;
	}

	/**
	 * 属性 locSpecStatusComment getter方法
	 * @return 属性locSpecStatusComment
	 * @author andy wang<br/>
	 */
	public String getLocationSpecStatusComment() {
		return locSpecStatusComment;
	}

	/**
	 * 属性 locSpecStatusComment setter方法
	 * @param locSpecStatusComment 设置属性locSpecStatusComment的值
	 * @author andy wang<br/>
	 */
	public void setLocationSpecStatusComment(String locSpecStatusComment) {
		this.locSpecStatusComment = locSpecStatusComment;
	}

	/* method *********************************************/

	/**
	 * 查询缓存参数信息
	 * @return
	 * @version 2017年3月13日下午2:11:54<br/>
	 * @author andy wang<br/>
	 */
	public MetaLocationSpecVO searchCache() {
		if (this.locSpec == null) {
			return this;
		}
		ISysParamService paramService = SpringContextHolder.getBean(ISysParamService.class);
		if (this.locSpec.getSpecStatus() != null) {
			this.locSpecStatusComment = paramService.getValue(CacheName.LOCATIONSPEC_STATUS, this.locSpec.getSpecStatus());
		}
		return this;
	}

	@Override
	public Example getExample() {
		if (this.locSpec == null) {
			return null;
		}
		Example example = new Example(MetaLocationSpec.class);
		Criteria criteria = example.createCriteria();
		if (!StringUtil.isTrimEmpty(this.locSpec.getSpecId())) {
			criteria.andEqualTo("specId", this.locSpec.getSpecId());
		}
		if (!StringUtil.isTrimEmpty(this.locSpec.getSpecNo())) {
			criteria.andEqualTo("specNo", this.locSpec.getSpecNo());
		}
		if (!StringUtil.isTrimEmpty(this.locSpec.getSpecName())) {
			criteria.andEqualTo("specName", this.locSpec.getSpecName());
		}
		if (!StringUtil.isTrimEmpty(this.getLikeLocSpecName())) {
			criteria.andLike("specName", StringUtil.likeEscapeH(this.getLikeLocSpecName()));
		}
		if (!StringUtil.isTrimEmpty(this.getLikeLocSpecNo())) {
			criteria.andLike("specNo", StringUtil.likeEscapeH(this.getLikeLocSpecNo()));
		}
		if ( this.minWeight != null ) {
			criteria.andGreaterThanOrEqualTo("specWeight", this.minWeight);
		}
		if ( this.maxWeight != null ) {
			criteria.andLessThanOrEqualTo("specWeight", this.maxWeight);
		}
		if ( this.locSpec.getSpecStatus() != null ) {
			criteria.andEqualTo("specStatus", this.locSpec.getSpecStatus());
		}
		return example;
	}

}
