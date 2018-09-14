package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.yunkouan.wms.modules.meta.dao.IDepartmentDao;
import com.yunkouan.wms.modules.meta.entity.MetaDepartment;
import com.yunkouan.wms.modules.meta.service.IDepartmentService;
import com.yunkouan.wms.modules.meta.vo.MetaDepartmentVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 货品服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class DepartmentServiceImpl extends BaseService implements IDepartmentService {
	@Autowired
	private IDepartmentDao dao;
	/**
	 * 部门列表(分页)
	 * 默认为生效状态的
	 * @param vo
	 * @param p
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	public ResultModel pageList(MetaDepartmentVo vo) throws DaoException, ServiceException {
		//Principal p=LoginUtil.getLoginUser();
		Page<MetaDepartment> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<MetaDepartment> list = dao.selectByExample(vo.getExampleLike());
		List<MetaDepartmentVo> r = new ArrayList<MetaDepartmentVo>();
		if(list != null)
			for(int i=0; i<list.size(); ++i) {
				MetaDepartment entity = list.get(i);
				MetaDepartmentVo dvo=new MetaDepartmentVo();
				dvo.setEntity(entity);
				r.add(dvo);
			}
		return new ResultModel().setPage(page).setList(r);
	}


	/**
	 * 添加部门
	 * @param entity 
	 * @return
	 * @throws DaoException 
	 * @throws ServiceException 
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel add(MetaDepartmentVo vo) throws DaoException{
		Principal p=LoginUtil.getLoginUser();
		MetaDepartment entity=vo.getEntity();
		if(entity==null){
			throw new BizException();
		}
		List<MetaDepartment>list;
		list=dao.selectByExample(vo.getExample("departmentNo",entity.getDepartmentNo()));
		if(list.size()>0)throw new BizException("");
		list=dao.selectByExample(vo.getExample("departmentName",entity.getDepartmentName()));
		if(list.size()>0)throw new BizException("");
		//补充系统生成的字段
		entity.setDepartmentStatus(Constant.STATUS_OPEN);
		String id = IdUtil.getUUID();
		entity.setDepartmentId(id);
		entity.setCreatePerson(p.getUserId());
		int count=dao.insert(entity);
		if(count==0){
			throw new DaoException(ErrorCode.DB_EXCEPTION);
		}
		return view(entity.getDepartmentId());
	}
	/**
	 * 生效
	 * @param idList
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel enable(MetaDepartmentVo vo) throws DaoException, ServiceException {
		Principal p = LoginUtil.getLoginUser();
		MetaDepartment entity=vo.getEntity();
		if(entity==null)throw new BizException("");
		entity.setDepartmentStatus(Constant.STATUS_ACTIVE);
		entity.setCreatePerson(p.getUserId());
		dao.updateByExample(entity, new Example(MetaDepartment.class));
		ResultModel m = new ResultModel();
		return m;
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
	public ResultModel update(MetaDepartmentVo vo) throws DaoException, ServiceException {
		Principal p=LoginUtil.getLoginUser();
		MetaDepartment entity = vo.getEntity();
		if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
		entity.clear();
		//检查编号是否唯一
		List<MetaDepartment>list;
		list=dao.selectByExample(vo.getExample("departmentNo", vo.getEntity().getDepartmentNo()));
		if(list.size()>0)throw new BizException("");
		list=dao.selectByExample(vo.getExample("departmentName", vo.getEntity().getDepartmentName()));
		if(list.size()>0)throw new BizException("");	//检查货品类型跟上级货品类型是否相同货主
		//TODO
		//entity.setUpdatePerson(p.getUserId());
		entity.setUpdateTime(new Date());
		int r = dao.updateByPrimaryKeySelective(entity);
		if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
		return view(entity.getDepartmentId());
	}

	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel disable(MetaDepartmentVo vo) throws DaoException, ServiceException {
		Principal p = LoginUtil.getLoginUser();
		MetaDepartment entity=vo.getEntity();
		if(entity==null)throw new BizException("");
		entity.setDepartmentStatus(Constant.STATUS_OPEN);
		entity.setCreatePerson(p.getUserId());
		dao.updateByExample(entity, new Example(MetaDepartment.class));
		ResultModel m = new ResultModel();
		return m;
	}

	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(MetaDepartmentVo vo) throws ServiceException, DaoException {
		Principal p = LoginUtil.getLoginUser();
		MetaDepartment entity=vo.getEntity();
		if(entity==null)throw new BizException("");
		//entity.setCreatePerson(p.getUserId());
		entity=dao.selectByExample(vo.getExample("departmentNo", vo.getEntity().getDepartmentNo())).get(0);
		if(entity.getDepartmentStatus()!=Constant.STATUS_OPEN)throw new BizException();
		entity.setDepartmentStatus(Constant.STATUS_CANCEL);
		dao.updateByExample(entity, vo.getExample("departmentNo", vo.getEntity().getDepartmentNo()));
		
		ResultModel m = new ResultModel();
		return m;
	}
	/**
	 * 条件查询
	 * @param vo
	 * @param p
	 * @return
	 * @throws ServiceException
	 * @throws DaoException
	 */
	public ResultModel selectList(MetaDepartmentVo vo) throws ServiceException, DaoException {
		//Principal p=LoginUtil.getLoginUser();
		//if(vo == null || vo.getEntity() == null) return new ResultModel();
		//vo.setStatusNo(Constant.STATUS_CANCEL);
		List<MetaDepartment> list = dao.selectByExample(vo.getExampleLike());
		List<MetaDepartmentVo> r = new ArrayList<MetaDepartmentVo>();
		if(list != null){
			for(MetaDepartment d:list){
				MetaDepartmentVo dvo=new MetaDepartmentVo();
				dvo.setEntity(d);
				r.add(dvo);
			}
		}
		return new ResultModel().setList(r);
	}
	  public ResultModel view(String id){
	    	MetaDepartment entity =  dao.selectByPrimaryKey(id);
	    	MetaDepartmentVo vo = new MetaDepartmentVo();
	    	vo.setEntity(entity);
	        return new ResultModel().setObj(vo);
	    }

}