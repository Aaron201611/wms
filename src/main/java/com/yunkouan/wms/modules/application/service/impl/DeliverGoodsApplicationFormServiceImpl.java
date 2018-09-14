package com.yunkouan.wms.modules.application.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.application.dao.IDeliverGoodsApplicationFormDao;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplication;
import com.yunkouan.wms.modules.application.entity.DeliverGoodsApplicationForm;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationFormService;
import com.yunkouan.wms.modules.application.service.IDeliverGoodsApplicationService;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationFormVo;
import com.yunkouan.wms.modules.application.vo.DeliverGoodsApplicationVo;

@Service
public class DeliverGoodsApplicationFormServiceImpl extends BaseService implements IDeliverGoodsApplicationFormService{

	@Autowired
	private IDeliverGoodsApplicationFormDao applicationFormDao;
	
	@Autowired
	private IDeliverGoodsApplicationService applicationService;
	
	@Autowired
	private ISysParamService paramService;

	/**
	 * 分页查询
	 * @param formVo
	 * @return
	 */
	public ResultModel pageList(DeliverGoodsApplicationFormVo formVo){
		ResultModel result = new ResultModel();
		Page<DeliverGoodsApplicationFormVo> page = null;
		if ( formVo.getCurrentPage() == null ) {
			formVo.setCurrentPage(0);
		}
		if ( formVo.getPageSize() == null ) {
			formVo.setPageSize(20);
		}
		page = PageHelper.startPage(formVo.getCurrentPage()+1, formVo.getPageSize());
		
		//不查询取消状态的记录
		if(formVo.getEntity().getStatus() == null){
			formVo.setStatusLessThan(Constant.APPLICATIONFORM_STATUS_CANCAL);
		}
		List<DeliverGoodsApplicationForm> recordList = applicationFormDao.selectByExample(formVo.getExample());
		if(recordList == null || recordList.isEmpty()) return result;
		result.setPage(page);
		
		List<DeliverGoodsApplicationFormVo> voList = new ArrayList<DeliverGoodsApplicationFormVo>();
		for(DeliverGoodsApplicationForm entity:recordList){
			DeliverGoodsApplicationFormVo vo = chg(entity);
			voList.add(vo);
		}
		result.setList(voList);
		
		return result;
		
	}
	private DeliverGoodsApplicationFormVo chg(DeliverGoodsApplicationForm entity) {
		DeliverGoodsApplicationFormVo vo = new DeliverGoodsApplicationFormVo(new DeliverGoodsApplicationForm());
		if (entity == null) {
			return vo;
		}
		vo.setEntity(entity);
		vo.setGpIEFlagName(paramService.getValue("I_E_FLAG", entity.getGpIEFlag()));
		vo.setStatusName(paramService.getValue("STATUS", entity.getStatus()));
		return vo;
	}
	/**
	 * 查询
	 * @param formVo
	 * @return
	 */
	public List<DeliverGoodsApplicationFormVo> qryList(DeliverGoodsApplicationFormVo formVo){
		List<DeliverGoodsApplicationForm> recordList = applicationFormDao.selectByExample(formVo.getExample());
		
		List<DeliverGoodsApplicationFormVo> voList = new ArrayList<DeliverGoodsApplicationFormVo>();
		for(DeliverGoodsApplicationForm entity:recordList){
			DeliverGoodsApplicationFormVo vo = chg(entity);
			voList.add(vo);
		}
		return voList;
	}
	/**
	 * 新增
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo add(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception{
		if(formVo == null) throw new BizException("data_is_null");
		
		//检查申请表编号是否已存在
		formNoIsExist(formVo.getEntity().getApplicationFormNo());
		
		String id = IdUtil.getUUID();
		formVo.getEntity().setId(id);
		formVo.getEntity().setStatus(Constant.APPLICATIONFORM_STATUS_OPEN);
		formVo.getEntity().setOrgId(p.getOrgId());
		formVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		formVo.getEntity().setCreatePerson(p.getUserId());
		formVo.getEntity().setUpdatePerson(p.getUserId());
		applicationFormDao.insertSelective(formVo.getEntity());
		
		return formVo;
	}
	
	/**
	 * 检查申报表编号是否存在
	 * @param fromNo
	 */
	public void formNoIsExist(String formNo){
		DeliverGoodsApplicationFormVo formVo = new DeliverGoodsApplicationFormVo(new DeliverGoodsApplicationForm());
		formVo.getEntity().setApplicationFormNo(formNo);
		List<DeliverGoodsApplicationForm> list = applicationFormDao.selectByExample(formVo.getExample());
		if(list != null && list.size() > 0)
			throw new BizException("application_form_no_is_exist");
	}
	
	/**
	 * 新增生效
	 * @param formVo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public DeliverGoodsApplicationFormVo saveAndEnable(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception{
		formVo = addOrUpdate(formVo,p);
		
		enable(formVo.getEntity().getId(), p);
		
		formVo.getEntity().setStatus(Constant.VEHICLE_STATUS_ACTIVE);
		
		return formVo;
	}
	
	/**
	 * 修改
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo update(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception{
		if(formVo == null) throw new BizException("data_is_null");
		
		formVo.getEntity().setUpdatePerson(p.getUserId());
		applicationFormDao.updateByPrimaryKeySelective(formVo.getEntity());
		return formVo;
	}
	
	/**
	 * 新增或更新
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo addOrUpdate(DeliverGoodsApplicationFormVo formVo,Principal p)throws Exception{
		if(formVo == null) throw new BizException("data_is_null");
		DeliverGoodsApplicationFormVo vo = null;
		if(StringUtil.isNotBlank(formVo.getEntity().getId())){
			vo = update(formVo, p);
		}else{
			vo = add(formVo,p);
		}
		return vo;
	}
	
	/**
	 * 查看
	 * @param formVo
	 * @return
	 */
	public DeliverGoodsApplicationFormVo view(String formId){
		DeliverGoodsApplicationForm record = applicationFormDao.selectByPrimaryKey(formId);
		DeliverGoodsApplicationFormVo vo = chg(record);
		
		return vo;
	}
	
	/**
	 * 生效
	 * @param formVo
	 * @return
	 */
	public void enable(String formId,Principal p)throws Exception{
		//检查状态是否打开
		queryAndCheckStatus(formId,Constant.APPLICATIONFORM_STATUS_OPEN,"applicationform_stauts_is_not_open");
		
		DeliverGoodsApplicationForm entity = new DeliverGoodsApplicationForm();
		entity.setId(formId);
		entity.setStatus(Constant.APPLICATIONFORM_STATUS_ACTIVE);
		entity.setUpdatePerson(p.getUserId());
		applicationFormDao.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 失效
	 * @param formVo
	 * @return
	 */
	public void disable(String formId,Principal p)throws Exception{
		//检查状态是否生效
		queryAndCheckStatus(formId,Constant.APPLICATIONFORM_STATUS_ACTIVE,"applicationform_stauts_is_not_active");
		//检查是否关联申请单
		DeliverGoodsApplicationVo applicationVo = new DeliverGoodsApplicationVo(new DeliverGoodsApplication());
		applicationVo.getEntity().setApplicationFormId(formId);
		applicationVo.setStatusNotIn(Arrays.asList("99"));
		applicationVo.getEntity().setOrgId(p.getOrgId());
		applicationVo.getEntity().setWarehouseId(LoginUtil.getWareHouseId());
		List<DeliverGoodsApplicationVo> appList = applicationService.qryList(applicationVo);
		if(appList != null && !appList.isEmpty()) 
			throw new BizException("applicationform_has_assocate_application");
		
		DeliverGoodsApplicationForm entity = new DeliverGoodsApplicationForm();
		entity.setId(formId);
		entity.setStatus(Constant.APPLICATIONFORM_STATUS_OPEN);
		entity.setUpdatePerson(p.getUserId());
		applicationFormDao.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 取消
	 * @param formVo
	 * @return
	 */
	public void cancel(String formId,Principal p) throws Exception{
		//检查状态是否打开
		queryAndCheckStatus(formId,Constant.APPLICATIONFORM_STATUS_OPEN,"applicationform_stauts_is_not_open");
		
		DeliverGoodsApplicationForm entity = new DeliverGoodsApplicationForm();
		entity.setId(formId);
		entity.setStatus(Constant.APPLICATIONFORM_STATUS_CANCAL);
		entity.setUpdatePerson(p.getUserId());
		applicationFormDao.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 查询并检查状态
	 * @param formId
	 * @param isStatus
	 * @param msg
	 */
	public void queryAndCheckStatus(String formId,int isStatus,String msg){
		DeliverGoodsApplicationForm record = applicationFormDao.selectByPrimaryKey(formId);
		if(record.getStatus().intValue() != isStatus) {
			throw new BizException(msg);
		}
	}
}
