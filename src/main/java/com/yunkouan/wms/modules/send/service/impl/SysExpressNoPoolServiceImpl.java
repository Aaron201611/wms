package com.yunkouan.wms.modules.send.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.common.constant.ErrorCode;
import com.yunkouan.util.SpringContextHolder;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.send.dao.ISysExpressNoPoolDao;
import com.yunkouan.wms.modules.send.entity.SysExpressNoPool;
import com.yunkouan.wms.modules.send.service.AbstractExpressService;
import com.yunkouan.wms.modules.send.service.ISysExpressNoPoolService;
import com.yunkouan.wms.modules.send.vo.SysExpressNoPoolVo;

/** 
* @Description: 快递单号池service implement
* @author tphe06
* @date 2017年10月17日 下午1:57:08  
*/
@Service
@Transactional(readOnly=true, rollbackFor=Exception.class)
public class SysExpressNoPoolServiceImpl implements ISysExpressNoPoolService {
	@Autowired
	private ISysExpressNoPoolDao dao;

	/** 
	* @Title: list 
	* @Description: 页面列表查询（分页）
	* @param @param vo
	* @param @param p
	* @param @return
	* @throws 
	*/
	@Override
	public List<SysExpressNoPoolVo> list(SysExpressNoPoolVo vo, Principal p) {
		Page<SysExpressNoPool> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<SysExpressNoPoolVo> list = select(vo, p);
		vo.setPageCount(page.getPages());//返回分页参数
		vo.setTotalCount(page.getTotal());
		return list;
	}

	/** 
	* @Title: list 
	* @Description: 页面列表查询（不分页）
	* @param @param vo
	* @param @param p
	* @param @return
	* @throws 
	*/
	@Override
	public List<SysExpressNoPoolVo> select(SysExpressNoPoolVo vo, Principal p) {
		List<SysExpressNoPool> list = dao.selectByExample(vo.getExample());
		List<SysExpressNoPoolVo> voList = chg(list);
		return voList;
	}

	/**
	 * 批量把实体类转化成VO类
	 * @param list
	 * @return
	 */
	private List<SysExpressNoPoolVo> chg(List<SysExpressNoPool> list) {
		if(list == null) return null;
		List<SysExpressNoPoolVo> voList = new ArrayList<SysExpressNoPoolVo>();
		for(SysExpressNoPool c : list) {
			voList.add(chg(c));
		}
		return voList;
	}
	/**
	 * 把实体类转化成VO类，补充扩展属性
	 * @param c
	 * @return
	 */
	private SysExpressNoPoolVo chg(SysExpressNoPool c) {
		if(c == null) return null;
		SysExpressNoPoolVo v = new SysExpressNoPoolVo(c);
		v.getEntity().clear();
		v.retset();
		return v;
	}

	/** 
	* @Title: query 
	* @Description: 根据属性查询实体
	* @param @param entity
	* @param @return
	* @throws 
	*/
	@Override
	public List<SysExpressNoPool> query(SysExpressNoPool entity) {
		return dao.select(entity);
	}

	/** 
	* @Title: get 
	* @Description: 根据主键获取单个实体
	* @param @param id
	* @param @return
	* @throws 
	*/
	@Override
	public SysExpressNoPool get(String id) {
		return dao.selectByPrimaryKey(id);
	}

	/** 
	* @Title: view 
	* @Description: 页面查看详情
	* @param @param id
	* @param @return
	* @throws 
	*/
	@Override
	public SysExpressNoPoolVo view(String id) {
		SysExpressNoPool entity = dao.selectByPrimaryKey(id);
		return chg(entity);
	}

	/** 
	* @Title: insert 
	* @Description: 新增方法
	* @param @param vo
	* @param @param p
	* @param @return
	* @throws 
	*/
	@Override
	@Transactional(readOnly = false)
	public int insert(SysExpressNoPoolVo vo, Principal p) {
		vo.getEntity().setId(IdUtil.getUUID());
		vo.getEntity().setOrgId(p.getOrgId());
		vo.getEntity().setCreatePerson(p.getUserId());
		vo.getEntity().setCreateTime(new Date());
		int result = dao.insertSelective(vo.getEntity());
		if(result == 0) throw new BizException(ErrorCode.DB_EXCEPTION);
		return result;
	}

	/** 
	* @Title: update 
	* @Description: 修改方法
	* @param @param vo
	* @param @param p
	* @param @return
	* @throws 
	*/
	@Override
	@Transactional(readOnly = false)
	public int update(SysExpressNoPoolVo vo, Principal p) {
		vo.getEntity().setUpdatePerson(p.getUserId());
		vo.getEntity().setUpdateTime(new Date());
		int result = dao.updateByPrimaryKeySelective(vo.getEntity());
		if(result == 0) throw new BizException(ErrorCode.DB_EXCEPTION);
		return result;
	}

	/** 
	* @Title: delete 
	* @Description: 删除方法
	* @param @param id
	* @throws 
	*/
	@Override
	@Transactional(readOnly = false)
	public int delete(String id) {
		int result = dao.deleteByPrimaryKey(id);
		if(result == 0) throw new BizException(ErrorCode.DB_EXCEPTION);
		return result;
	}

	/** 
	* @Title: queryOne 
	* @Description: 根据属性查询单个实体
	* @auth tphe06
	* @time 2018 2018年4月18日 下午2:56:30
	* @param entity
	* @return
	*/
	@Override
	public SysExpressNoPool queryOne(SysExpressNoPool entity) {
		return dao.selectOne(entity);
	}

	/** 
	* @Title: count 
	* @Description: 统计数量方法
	* @auth tphe06
	* @time 2018 2018年4月18日 下午2:56:44
	* @param vo
	* @param p
	* @return
	*/
	@Override
	public long count(SysExpressNoPoolVo vo, Principal p) {
		return dao.selectCountByExample(vo.getExample());
	}

	/** 
	* @Title: delete 
	* @Description: 批量删除方法
	* @auth tphe06
	* @time 2018 2018年4月18日 下午2:56:55
	* @param ids
	*/
	@Override
	@Transactional(readOnly = false)
	public void delete(String[] ids) {
		if(ids == null) return;
		for(String id : ids) {
			this.delete(id);
		}
	}

	/** 
	* @Title: queryOne 
	* @Description: 得到快递公司的一个运单号
	* @auth tphe06
	* @time 2018 2018年4月18日 下午3:23:04
	* @param pool
	* @return
	 * @throws Exception 
	 * @throws IOException 
	*/
	@Override
	@Transactional(readOnly = false)
	public String selectOne(SysExpressNoPool pool) throws IOException, Exception {
		synchronized(LOCK) {
			AbstractExpressService service = SpringContextHolder.getBean(new StringBuilder(pool.getExpressServiceCode()).append("ServiceImpl").toString());
			if(service == null) return null;
			// 1.如果数据库中存在则直接返回
			Principal p = LoginUtil.getLoginUser();
			List<SysExpressNoPool> list = this.query(pool);
			if(list != null && !list.isEmpty()) {
				// 2.如果少于指定数量则获取一批并入库
				service.get(pool.getExpressServiceCode(), list.size()-1, p);
				SysExpressNoPool express = list.get(0);
				express.setIsUsed(true);
				express.setUseTime(new Date());
				this.update(new SysExpressNoPoolVo(express), p);
				return express.getExpressBillNo();
			}
			// 3.如果数据库中不存在则获取一批，并返回一个运单号，其余入库
			return service.getAndReturnOne(pool.getExpressServiceCode(), p);
			// 4.如果数据库中不存在并且从快递公司获取运单号失败则返回空
		}
	}
	private static final byte[] LOCK = new byte[1];
}