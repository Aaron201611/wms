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
import com.yunkouan.wms.modules.meta.entity.MetaMachine;
import com.yunkouan.wms.modules.meta.vo.MachineVo;

/**
 * @author tphe06 2017年2月10日
 */
public class TestMachine extends BaseJunitTest {
	@Autowired
	private MachineController c;

	@Test
//	@Ignore
	public void testList() throws ServiceException, DaoException {
		MetaMachine obj = new MetaMachine();
		obj.setMachineName("test");
		MachineVo vo  = new MachineVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidSearch.class);
		ResultModel r = c.list(vo, b);
		if(r.getList() != null) System.out.println(r.getList().size());
	}

//	@Test
//	@Ignore
//	public void testView() throws ServiceException, DaoException {
//		ResultModel r = c.view("8B4AAB2CA042427B9481F7B43694F79A");
//		System.out.println(r.getObj());
//	}

	@Test
	@Ignore
	public void testAdd() throws ServiceException, DaoException {
		MetaMachine obj = new MetaMachine();
		obj.setMachineName("test");
		MachineVo vo = new MachineVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidSave.class);
		ResultModel r = c.add(vo, b);
		System.out.println(r.getObj());
	}

	@Test
	@Ignore
	public void testUpdate() throws ServiceException, DaoException {
		MetaMachine obj = new MetaMachine();
		obj.setMachineId("8B4AAB2CA042427B9481F7B43694F79A");
		obj.setMachineName("1111");
		MachineVo vo = new MachineVo(obj);
		BeanPropertyBindingResult b = super.validateEntity(vo, ValidUpdate.class);
		ResultModel r = c.update(vo, b);
		System.out.println(r.getObj());
	}
}