package com.yunkouan.wms.modules.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.base.BaseController;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.wms.modules.sys.service.IHomepageService;
import com.yunkouan.wms.modules.sys.vo.EventVO;

/**
 * 主页接口
 * <br/><br/>
 * @Description 
 * @version 2017年5月13日 下午5:09:42<br/>
 * @author 王通<br/>
 */
@Controller

@RequestMapping("${adminPath}/homepage")
public class HomepageController extends BaseController {

	@Autowired
    private IHomepageService homepageService;
	/**
	 * 列出待办事项列表
	 * @author 王通
	 * @date 2017年5月13日18:02:00
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel list() throws Exception {
		EventVO vo = this.homepageService.list();
    	ResultModel rm = new ResultModel();
    	rm.setObj(vo);
        return rm;
	}
	@RequestMapping(value = "/warehostTask", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel warehostTask() throws Exception {
		List<EventVO> tasks = this.homepageService.warehouseTasks();
    	ResultModel rm = new ResultModel();
    	rm.setList(tasks);
        return rm;
	}
}