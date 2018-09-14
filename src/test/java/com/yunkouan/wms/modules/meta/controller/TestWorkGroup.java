package com.yunkouan.wms.modules.meta.controller;

import org.junit.Test;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidSearch;
import com.yunkouan.valid.ValidUpdate;
import com.yunkouan.wms.common.BaseJunitTest;
import com.yunkouan.wms.modules.meta.entity.MetaWorkGroup;
import com.yunkouan.wms.modules.meta.vo.WorkGroupVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestWorkGroup extends BaseJunitTest {
	@Autowired
	private WorkGroupController c;

	@Test
//	@Ignore
	public void testList() throws ServiceException, DaoException {
		MetaWorkGroup obj = new MetaWorkGroup();
		obj.setWorkGroupName("test");
		WorkGroupVo vo  = new WorkGroupVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidSearch.class);
		ResultModel r = c.list(vo, b);
		if(r.getList() != null) System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("3F7119977360431697FB737E5B187567");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testAdd() throws ServiceException, DaoException {
		MetaWorkGroup obj = new MetaWorkGroup();
		obj.setWorkGroupName("test");
		WorkGroupVo vo = new WorkGroupVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidSave.class);
		ResultModel r = c.add(vo, b);
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws ServiceException, DaoException {
		MetaWorkGroup obj = new MetaWorkGroup();
		obj.setWorkGroupId("3F7119977360431697FB737E5B187567");
		obj.setWorkGroupName("1111");
		WorkGroupVo vo = new WorkGroupVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidUpdate.class);
		ResultModel r = c.update(vo, b);
		System.out.println(r.getObj());
	}
}