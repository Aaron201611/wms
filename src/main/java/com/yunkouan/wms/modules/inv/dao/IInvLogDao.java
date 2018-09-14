package com.yunkouan.wms.modules.inv.dao;

import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;

import tk.mybatis.mapper.common.Mapper;

public interface IInvLogDao extends Mapper<InvLog> {
	/**
	 * 查询过期记录
	 * @param vo
	 * @return
	 */
	public InvLogVO getOutList(InvLogVO vo);

	/**
	 * 批量备份
	 * @param vo
	 * @return
	 */
	public Integer backup(InvLogVO vo);
}