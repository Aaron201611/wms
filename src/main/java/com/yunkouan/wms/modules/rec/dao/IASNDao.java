/**
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * 创建日期:<br/> 2017年2月8日 下午4:04:01<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.rec.dao;

import java.util.List;

import com.yunkouan.wms.modules.rec.entity.RecAsn;
import com.yunkouan.wms.modules.rec.vo.RecAsnVO;

import tk.mybatis.mapper.common.Mapper;

/**
 * ASN单数据层接口<br/><br/>
 * <b>创建日期</b>:<br/> 2017年2月8日 下午4:04:01<br/>
 * @author andy wang<br/>
 */
public interface IASNDao extends Mapper<RecAsn> {

	
	/**
	 * 根据Asn单id，查询Asn单信息（注意：锁行）
	 * @param asnVO 查询条件
	 * —— asnId Asn单id 必填
	 * —— warehouseId 仓库id
	 * —— orgId 企业id
	 * @return Asn单对象
	 * @throws Exception
	 * @version 2017年3月10日 上午11:04:56<br/>
	 * @author andy wang<br/>
	 */
	public RecAsn selectAsnForUpdate ( RecAsnVO asnVO ) throws Exception;
	
	
	/**
	 * 查询ASN单列表
	 * @param asnVO 查询条件
	 * @return ASN列表
	 * @throws Expception
	 * 创建日期:<br/> 2017年2月9日 下午2:43:56<br/>
	 * @author andy wang<br/>
	 */
	@Deprecated
	public List<RecAsn> selectAsn( RecAsnVO asnVO ) throws Exception;
	
	List<String>getTask(String orgId);
}