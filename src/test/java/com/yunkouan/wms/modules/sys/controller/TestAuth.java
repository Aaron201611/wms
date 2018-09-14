package com.yunkouan.wms.modules.sys.controller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.saas.modules.sys.service.IAuthService;
import com.yunkouan.saas.modules.sys.vo.AuthVo;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.util.IdUtil;

/**
 * @author tphe06 2017年2月10日
 */
public class TestAuth extends BaseJunitTest {
	@Autowired
	private AuthController c;
	@Autowired
	private IAuthService s;

	@Test
//	@Ignore
	public void testTree() throws Exception {
		AuthVo vo = new AuthVo();
		List<SysAuth> list = s.query(new SysAuth().setParentId("0"));
		vo.setList(chg(list));
		tree(vo);

		ResultModel r = new ResultModel();
		r.setList(vo.getList());
		super.toJson(r);
	}

	private void tree(AuthVo vo) {
		if(vo.getList() == null || vo.getList().size() == 0) return;
		for(int i=0; i<vo.getList().size(); ++i) {
			AuthVo c = vo.getList().get(i);
			List<SysAuth> list = s.query(new SysAuth().setParentId(c.getEntity().getAuthId()));
			c.setList(chg(list));
			tree(c);
		}
	}
	private List<AuthVo> chg(List<SysAuth> list) {
		if(list == null) return null;
		List<AuthVo> r = new ArrayList<AuthVo>();
		for(int i=0; i<list.size(); ++i) {
			r.add(new AuthVo(list.get(i)));
		}
		return r;
	}

	@Test
	@Ignore
	public void testList() throws DaoException, ServiceException {
		AuthVo vo = new AuthVo();
		ResultModel r = c.list(vo, new BeanPropertyBindingResult(vo, AuthVo.class.getName()));
		System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws DaoException, ServiceException {
//		ResultModel r = c.view("1");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testQuery() throws DaoException, ServiceException {
		List<SysAuth> list = s.query(new SysAuth().setAuthStatus(1));
		System.out.println(list.size());
	}
}