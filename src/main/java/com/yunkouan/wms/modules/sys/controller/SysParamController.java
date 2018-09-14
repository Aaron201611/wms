/*
 * Copyright Notice =========================================================
 * This file contains proprietary information of 中云智慧(北京)科技有限公司 Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2017 =======================================================
 * Company: yunkouan.com<br/>
 * @version 2017年3月13日下午2:02:47<br/>
 * @author andy wang<br/>
 */
package com.yunkouan.wms.modules.sys.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.saas.modules.sys.service.ISysParamExtlService;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.vo.CountryVO;
import com.yunkouan.saas.modules.sys.vo.SysParamVO;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.strategy.StrategyContext;
import com.yunkouan.wms.common.util.LoginUtil;

/**
 * 参数控制类<br/><br/>
 * @version 2017年3月13日下午2:02:47<br/>
 * @author andy wang<br/>
 */
@Controller

@RequestMapping("${adminPath}/param")
public class SysParamController extends BaseController {
	/**context:策略上下文**/
	@Autowired
	protected StrategyContext context;

	/** 参数业务类 <br/> add by andy wang */
	@Autowired
	private ISysParamService sysParamService;
	/** 参数外调业务类 <br/> add by andy wang */
	@Autowired
	private ISysParamExtlService sysParamExtlService;

	/**
	 * 失效参数
	 * @param listSysParamId 参数id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/disable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel disableSysParam(@RequestBody List<String> listSysParamId) throws Exception {
		ResultModel rm = new ResultModel();
		Principal p = LoginUtil.getLoginUser();
		this.sysParamService.inactiveSysParam(listSysParamId, p.getUserId());
		return rm;
	}

	/**
	 * 生效参数
	 * @param listSysParamId 参数id集合
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/enable", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel enableSysParam(@RequestBody List<String> listSysParamId) throws Exception {
		ResultModel rm = new ResultModel();
		Principal p = LoginUtil.getLoginUser();
		this.sysParamService.activeSysParam(listSysParamId, p.getUserId());
		return rm;
	}

	/**
	 * 更新参数信息
	 * @param sysParamVO 参数对象
	 * @param br 校验信息
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel updateSysParam(@Validated(value = { ValidUpdate.class }) @RequestBody SysParamVO sysParamVO,
			BindingResult br) throws Exception {
		if (br.hasErrors()) {
			return super.handleValid(br);
		}
		ResultModel rm = new ResultModel();
		Principal p = LoginUtil.getLoginUser();
		this.sysParamService.updateSysParam(sysParamVO, p.getUserId());
		return rm;
	}

	/**
	 * 查询单个参数
	 * @param sysParamId 参数id
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel viewSysParam(@RequestParam("id") String sysParamId) throws Exception {
		ResultModel rm = new ResultModel();
		SysParamVO sysParamVO = this.sysParamService.viewSysParam(sysParamId);
		rm.setObj(sysParamVO);
		return rm;
	}

	/**
	 * 查询参数列表
	 * @param sysParamVO 查询条件
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel listSysParam(@RequestBody SysParamVO sysParamVO) throws Exception {
		ResultModel rm = new ResultModel();
		Page<SysParamVO> listSysSystemParam = this.sysParamService.listSysParam(sysParamVO);
		rm.setPage(listSysSystemParam);
		return rm;
	}
	
	/**
	 * 查询国家省市列表
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年6月9日14:34:39<br/>
	 * @author 王通<br/>
	 */
	@RequestMapping(value = "/listCity", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel listCity() throws Exception {
		ResultModel rm = new ResultModel();
		List<CountryVO> list = this.sysParamService.listCity();
		rm.setList(list);
		return rm;
	}
	/**
	 * 保存参数
	 * @param sysParamVO 参数对象 
	 * @param br 校验对象
	 * @return 结果对象
	 * @throws Exception
	 * @version 2017年3月13日下午2:02:47<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel insertSysParam(@Validated(value = { ValidSave.class }) @RequestBody SysParamVO sysParamVO,
			BindingResult br) throws Exception {
		if (br.hasErrors()) {
			return super.handleValid(br);
		}
		ResultModel rm = new ResultModel();
		Principal p = LoginUtil.getLoginUser();
		Integer id2 = context.getStrategy4Id().getSysParamSeq();
		sysParamVO = this.sysParamService.insertSysParam(sysParamVO, p.getUserId(), id2);
		rm.setObj(sysParamVO);
		return rm;
	}
	
	/**
	 * 获取页面显示参数
	 * @param listCacheName 参数名
	 * @return 参数集合
	 * @throws Exception
	 * @version 2017年3月22日 下午2:41:49<br/>
	 * @author andy wang<br/>
	 */
	@RequestMapping(value = "/show", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel showParam ( @RequestBody List<String> listCacheName ) throws Exception {
//		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");  
//		System.out.println(StringUtil.toJson(listCacheName, true));
		ResultModel rm = new ResultModel();
		Map<String, List<Map<String, Object>>> showParam = this.sysParamExtlService.showParam(listCacheName);
		for (String key : showParam.keySet()) {
			List<Map<String, Object>> list = showParam.get(key);
			list.sort(new Comparator<Map<String,Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					String s1 = o1.get("key").toString();
					String s2 = o2.get("key").toString();
//					Integer s1 = 0;
//					if ( !StringUtil.isTrimEmpty(k1.toString()) && pattern.matcher(k1.toString()).matches() ) {
//						s1 = Integer.valueOf(k1.toString());
//					}
//					Integer s2 = 0;
//					if ( !StringUtil.isTrimEmpty(k2.toString()) && pattern.matcher(k2.toString()).matches() ) {
//						s2 = Integer.valueOf(k2.toString());
//					}
//					Integer result = 0;
//					if ( s1 > s2 ) {
//						result = 1;
//					} else if ( s1 == s2 ) {
//						result = 0;
//					} else {
//						result = -1;
//					}
					
					return s1.compareTo(s2);
				}
			});
		}
		rm.setObj(showParam);
		return rm;
	}
	
	@RequestMapping(value = "/refresh")
	@ResponseBody
	public String cache() throws Exception {
//		ParamCacheManager.refresh();
		return "缓存刷新完成";
	}

}