package com.yunkouan.wms.modules.send.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;

import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.send.vo.SendDeliveryVo;
import com.yunkouan.wms.modules.send.vo.SysExpressNoPoolVo;

import tk.mybatis.mapper.util.StringUtil;

/** 
* @Description: 快递公司接口
* @author tphe06
* @date 2018年4月18日 下午3:50:18  
*/
@Scope("prototype")//此注解不得删除!!
@Transactional(readOnly=false, rollbackFor=Exception.class)
public abstract class AbstractExpressService extends Thread {
	private static Log log = LogFactory.getLog(AbstractExpressService.class);

	/**数据库池中少于多少个运单号的时候获取一批数量的运单号**/
//	public static final int POOL_MIN = 5;
	/**一次从快递公司获取多少个运单号**/
//	public static final int POOL_BATH = 20;
	@Autowired
	private ISysParamService paramService;

	@Autowired
	private ISysExpressNoPoolService service;

	private String expressCode;
	private Principal p;

	/** 
	* @Title: get 
	* @Description: 如果少于指定数量则获取一批运单号并入库，异步
	* @auth tphe06
	* @time 2018 2018年4月18日 下午3:50:35
	* void
	*/
	public void get(String expressCode, int size, Principal p) {
		String min = paramService.getKey(CacheName.ST_NO_POOL_MIN);
		Integer POOL_MIN = StringUtil.isEmpty(min)?0:Integer.valueOf(min);
		if(size > POOL_MIN) return;
		this.expressCode = expressCode;
		this.p = p;
		this.start();
	}
	/** 
	* @Title: getAndReturnOne 
	* @Description: 如果数据库中不存在则获取一批运单号并返回其中一个，其余入库
	* @auth tphe06
	* @time 2018 2018年4月18日 下午3:50:41
	* @return
	* String
	 * @throws Exception 
	 * @throws IOException 
	*/
	public String getAndReturnOne(String expressCode, Principal p) throws IOException, Exception {
		this.expressCode = expressCode;
		this.p = p;
		List<String> list = get(1);
		if(list == null || list.isEmpty()) return null;
		SysExpressNoPoolVo vo = new SysExpressNoPoolVo();
		vo.getEntity().setExpressBillNo(list.get(0));
		vo.getEntity().setExpressServiceCode(expressCode);
		vo.getEntity().setIsUsed(true);
		vo.getEntity().setUseTime(new Date());
		service.insert(vo, p);
		this.start();
		return list.get(0);
	}
	/** 
	* @Title: get 
	* @Description: 从物流公司获取N个运单号
	* @auth tphe06
	* @time 2018 2018年4月20日 上午11:25:21
	* @param sum
	* @return
	* @throws IOException
	* @throws Exception
	* List<String>
	*/
	protected abstract List<String> get(int sum) throws IOException, Exception;

	/** 
	* @Title: isBathPush 
	* @Description: 是否支持批量推送订单
	* @auth tphe06
	* @time 2018 2018年4月19日 下午2:57:48
	* @return
	* boolean
	*/
	protected abstract boolean isBathPush();
	/** 
	* @Title: push 
	* @Description: 往快递公司推送订单，返回推送成功列表
	* @auth tphe06
	* @time 2018 2018年4月18日 下午6:22:45
	* @param list
	* @return
	* @throws IOException
	* Vip007Result
	*/
	public List<SendDeliveryVo> push(List<SendDeliveryVo> list) throws Exception {
		if(list == null || list.isEmpty()) return null;
		if(!isBathPush()) {
			List<SendDeliveryVo> result = new ArrayList<SendDeliveryVo>();
			for(SendDeliveryVo vo : list) {
				try {
					SendDeliveryVo newVo = send(vo);
					if(newVo != null) result.add(newVo);
				} catch(IOException e) {
					//如果已经获取一定数量的运单号则需要利用，不得丢失
					if(result.size() > 0) {
						if(log.isErrorEnabled()) log.error(e.getMessage(), e);
						return result;
					} else {
						throw e;
					}
				}
			}
			return result;
		} else {
			return send(list);
		}
	}
	/** 
	* @Title: send 
	* @Description: 单个推送订单接口，如果推送失败返回null，如果支持批量接口，本接口可以不用实现
	* @auth tphe06
	* @time 2018 2018年4月19日 下午3:15:30
	* @param vo
	* @return
	* SendDeliveryVo
	 * @throws IOException 
	*/
	protected abstract SendDeliveryVo send(SendDeliveryVo vo) throws Exception;
	/** 
	* @Title: send 
	* @Description: 批量推送订单接口，返回推送成功列表，如果不支持请在isBathPush方法返回false，本接口可以不用实现
	* @auth tphe06
	* @time 2018 2018年4月19日 下午3:17:30
	* @param vo
	* @return
	* List<SendDeliveryVo>
	*/
	protected abstract List<SendDeliveryVo> send(List<SendDeliveryVo> vo) throws IOException;

	/** 
	* @Title: run 
	* @Description: 批量获取运单号并入库
	* @auth tphe06
	* @time 2018 2018年4月19日 上午10:24:17
	*/
	@Override
	public void run() {
		try {
			String bath = paramService.getKey(CacheName.ST_NO_POOL_BATH);
			Integer POOL_BATH = StringUtil.isEmpty(bath)?0:Integer.valueOf(bath);
			List<String> list = get(POOL_BATH);
			if(list != null) for(String code : list) {
				SysExpressNoPoolVo vo = new SysExpressNoPoolVo();
				vo.getEntity().setExpressBillNo(code);
				vo.getEntity().setExpressServiceCode(expressCode);
				vo.getEntity().setIsUsed(false);
				service.insert(vo, p);
			}
		} catch(Exception e) {
			if(log.isErrorEnabled()) log.error(e.getMessage(), e);
		}
	}
}