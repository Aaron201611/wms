package com.yunkouan.wms.modules.inv.service.impl;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.exception.BizException;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.modules.inv.dao.IInvLogDao;
import com.yunkouan.wms.modules.inv.service.IInvLogService;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;

@Service
public class IInvLogServiceImpl extends BaseService implements IInvLogService {
	private static Log log = LogFactory.getLog(IInvLogServiceImpl.class);

	@Autowired
	private IInvLogDao dao;

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public Integer backup(Integer days) {
		Date outDay = DateUtils.addDays(new Date(), -1*days);
		String date = DateFormatUtils.format(outDay, "yyyy-MM-dd");
		if(log.isInfoEnabled()) log.info("过期日期："+date);
		// 查询过期日志
		InvLogVO vo = new InvLogVO();
		vo.setOutDate(date);
		vo = dao.getOutList(vo);
		if(log.isDebugEnabled() && vo.getStartIndex() != null) log.debug("备份库存日志开始记录："+vo.getStartIndex());
		if(log.isDebugEnabled() && vo.getEndIndex() != null) log.debug("备份库存日志结束记录："+vo.getEndIndex());
		if(log.isDebugEnabled()) log.debug("备份库存日志数量："+vo.getSum());
		if(vo.getSum() == 0) return 0;
		// 备份日志
		dao.backup(vo);
		// 删除日志
		int result = dao.deleteByExample(vo.getExample());
		if(result != vo.getSum()) throw new BizException("inv_log_backup_faile");
		return result;
	}
}