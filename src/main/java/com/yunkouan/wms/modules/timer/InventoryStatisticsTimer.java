package com.yunkouan.wms.modules.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yunkouan.wms.modules.inv.dao.IInventoryStatisticsDao;

/** 
* @Description: 每月定时统计进销存数据
* @author 王通
* @date 2018年4月26日21:17:11
*/
@Component
public class InventoryStatisticsTimer {
	private static Log log = LogFactory.getLog(InventoryStatisticsTimer.class);

	@Autowired
	IInventoryStatisticsDao statisticsDao;

	private static Boolean running = false;
	private boolean start() {
		synchronized(running) {
			if(running) return false;
			running = true;
			return true;
		}
	}
	private void stop() {
		synchronized(running) {
			running = false;
		}
	}

	@Scheduled(cron = "0 0 0 1 * ?") // 每月1号执行
	public void run() {
		log.debug("进入执行方法！");
		if(!start()) return;
		try {
			//根据企业和仓库查询每个的库存统计,并更新上个月的入账、出账、盈亏、期末
			statisticsDao.updateLastMonth();
			//创建当月的期初数，为上个月的期末数，若上月查无结果则为库存数
			statisticsDao.insertThisMonth();
		} catch(Throwable e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		} finally {
			stop();
		} 
	}
//	private String getLoginParam() {
//		TelecAccount account = new TelecAccount();
//		account.setAccountType(false);
//		account = accountService.queryOne(account);
//		if(account == null) return null;
//		UsernamePasswordToken vo = new UsernamePasswordToken();
//		vo.setLoginAccount(account.getAccountNo());
//		vo.setLoginPassword("admin1234");
//		vo.setType(ITelecAccountService.ACCOUNT_TYPE_CREW);
//		return getPostParam(vo);
//	}
//	private List<TelecMessageVo> query() {
//		TelecDeclareRealtime q = new TelecDeclareRealtime();
//		q.setIsSync(true);
//		q.setIsCheck(false);
//		List<TelecDeclareRealtime> list = declareService.query(q);
//		if(list == null || list.size() == 0) return null;
//		List<TelecMessageVo> messages = new ArrayList<TelecMessageVo>();
//		for(TelecDeclareRealtime time : list) {
//			TelecMessageVo vo = new TelecMessageVo();
//			vo.getEntity().setDeclareId(time.getId());
//			vo.getEntity().setShowPos((short)1);
//			messages.add(vo);
//		}
//		return messages;
//	}
//	private static String getPostParam(Object vo) {
//		if(vo == null) return null;
//		return JsonUtil.toJson(vo);
//	}
//	private void updateSync(String id, TelecDeclareRealtime d) {
//		TelecDeclareRealtimeVo vo = new TelecDeclareRealtimeVo();
//		vo.getEntity().setId(id);
//		vo.getEntity().setIsCheck(true);
//		if(d != null) {
//			vo.getEntity().setCheckType(d.getCheckType());
//			vo.getEntity().setCheckResult(d.getCheckResult());
//		}
//		declareService.sign(vo, new Principal());
//	}
}