package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.ISalesmanDao;
import com.yunkouan.wms.modules.meta.entity.MetaDepartment;
import com.yunkouan.wms.modules.meta.entity.MetaSalesman;
import com.yunkouan.wms.modules.meta.service.IDepartmentService;
import com.yunkouan.wms.modules.meta.vo.MetaDepartmentVo;
import com.yunkouan.wms.modules.meta.vo.MetaSalesmanVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 业务员
 */
@Service
@Transactional(readOnly=true)
public class SalesmanServiceImpl extends BaseService {
	@Autowired
	private ISalesmanDao dao;
	/**
	 * 分页查询业务员
	 * @param vo
	 * @param p
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	/*public ResultModel list(SalesmanVo vo, Principal p){
		Page<SalesmanVo> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<Salesman> list = dao.selectByExample(vo.getExampleLike());
		List<SalesmanVo> r = new ArrayList<SalesmanVo>();
		if(list != null)
			for(int i=0; i<list.size(); ++i) {
				Salesman entity = list.get(i);
				SalesmanVo dvo=new SalesmanVo();
				dvo.setEntity(entity);
				r.add(dvo);
			}
		return new ResultModel().setPage(page).setList(r);
	}*/


	/**
	 * 添加
	 * @param entity 
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel add(MetaSalesmanVo vo) throws DaoException, ServiceException{
		Principal p = LoginUtil.getLoginUser();
		MetaSalesman entity=vo.getEntity();
		if(entity==null||StringUtils.isBlank(entity.getDepartmentId())||StringUtils.isBlank(entity.getSalesmanNo())){
			throw new BizException("");
		}
		List<MetaSalesman>list=new ArrayList<MetaSalesman>();
		list=dao.selectByExample(vo.getExample("salesmanNo", vo.getEntity().getSalesmanNo()));
		if(list.size()>0)throw new BizException("");
		//补充系统生成的字段
		entity.setSalesmanStatus(Constant.STATUS_OPEN);
		String id = IdUtil.getUUID();
		entity.setSalesmanId(id);
		entity.setCreateTime(new Date());
		//entity.setCreatePerson(p.getUserId());
		int count=dao.insert(entity);
		if(count==0){
			throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		vo.setEntity(entity);
		return view(vo);
	}
	/**
	 * 生效
	 * @param idList
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel enable(MetaSalesmanVo vo) throws DaoException{
		Principal p = LoginUtil.getLoginUser();
		MetaSalesman entity=vo.getEntity();
		if(entity==null)throw new BizException("");

		entity.setSalesmanStatus(Constant.STATUS_ACTIVE);
		entity.setUpdatePerson(p.getUserId());
		entity.setUpdateTime(new Date());
		int n=dao.updateByExample(entity, new Example(MetaSalesman.class));
		if(n==0){
			throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		return new ResultModel();
	}
	/**
	 * 修改
	 * @param vo
	 * @param p
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel update(MetaSalesmanVo vo) throws DaoException, ServiceException {
		Principal p = LoginUtil.getLoginUser();
		MetaSalesman entity = vo.getEntity();
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		//检查编号是否唯一
		List<MetaSalesman>list=new ArrayList<MetaSalesman>();
		list=dao.selectByExample(vo.getExample("salesmanNo", entity.getSalesmanNo()));
		if(list.size()>0)throw new BizException("");
		//补充其他字段
		entity.setUpdatePerson(p.getUserId());
		entity.setUpdateTime(new Date());
		int n = dao.updateByPrimaryKeySelective(entity);
		if(n == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		vo.setEntity(entity);
		return view(vo);
	}
	/**
	 * 失效
	 * @param vo
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel disable(MetaSalesmanVo vo) throws DaoException, ServiceException {
		Principal p = LoginUtil.getLoginUser();
		MetaSalesman entity=vo.getEntity();
		if(entity==null)throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.setSalesmanStatus(Constant.STATUS_OPEN);
		entity.setUpdatePerson(p.getUserId());
		entity.setUpdateTime(new Date());
		int n= dao.updateByExample(entity, new Example(MetaDepartment.class));
		if(n==0)throw new DaoException(ErrorCode.DB_EXCEPTION);
		ResultModel m = new ResultModel();
		return m;
	}
	/**
	 * 取消
	 * @param vo
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(MetaSalesmanVo vo) throws ServiceException, DaoException {
		Principal p = LoginUtil.getLoginUser();
		MetaSalesman entity=vo.getEntity();
		if(entity==null)throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.setSalesmanStatus(Constant.STATUS_CANCEL);
		entity.setUpdatePerson(p.getUserId());
		entity.setUpdateTime(new Date());
		int n=dao.updateByExample(entity, new Example(MetaDepartment.class));
		if(n==0)throw new DaoException(ErrorCode.DB_EXCEPTION);
		ResultModel m = new ResultModel();
		return m;
	}
	/**
	 * 分页条件查询
	 * @param vo
	 * @param p
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	public ResultModel pageList(MetaSalesmanVo vo) throws ServiceException, DaoException {
		if(vo == null) return new ResultModel();
		Page<MetaSalesmanVo> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<MetaSalesman> list = dao.selectByExample(vo.getExampleLike());
		List<MetaSalesmanVo> r = new ArrayList<MetaSalesmanVo>();
		if(list != null){
			for(MetaSalesman d:list){
				MetaSalesmanVo dvo=new MetaSalesmanVo();
				dvo.setEntity(d);
				r.add(dvo);
			}
		}
		return new ResultModel().setPage(page).setList(r);
	}
	/**
	 * 条件查询
	 * @param vo
	 * @param p
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	public ResultModel selectList(MetaSalesmanVo vo) throws ServiceException, DaoException {
		if(vo == null) return new ResultModel();
		//Page<MetaSalesmanVo> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<MetaSalesman> list = dao.selectByExample(vo.getExampleLike());
		List<MetaSalesmanVo> r = new ArrayList<MetaSalesmanVo>();
		if(list != null){
			for(MetaSalesman d:list){
				MetaSalesmanVo dvo=new MetaSalesmanVo();
				dvo.setEntity(d);
				r.add(dvo);
			}
		}
		return new ResultModel().setList(r);
	}
	/**
	 * 查看单条记录内容
	 * @param vo
	 * @param p
	 * @return
	 * @throws ServiceException
	 */
	public ResultModel view(MetaSalesmanVo vo) throws ServiceException{
		if(vo==null||vo.getEntity()==null)throw new ServiceException(ErrorCode.DATA_EMPTY);
		//Department entity =  dao.selectByPrimaryKey(id);
		//DepartmentVo vo = new DepartmentVo();
		//vo.setEntity(entity);
		return new ResultModel().setObj(vo);
	}

}