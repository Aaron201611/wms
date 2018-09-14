package com.yunkouan.wms.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.saas.modules.sys.entity.SysAuth;
import com.yunkouan.wms.modules.sys.entity.SysAccount;
import com.yunkouan.wms.modules.sys.vo.AccQueryVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 企业帐号数据层接口
 * @author tphe06
 */
public interface IAccountDao extends Mapper<SysAccount> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<SysAccount> selectByExample(Object example);

	/**
	 * 查询授权的权限
	 */
	public List<SysAuth> query(AccQueryVo vo);
	public List<SysAuth> query4park(AccQueryVo vo);
}