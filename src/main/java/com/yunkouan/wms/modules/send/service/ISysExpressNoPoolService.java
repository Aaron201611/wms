package com.yunkouan.wms.modules.send.service;

import java.io.IOException;
import java.util.List;

import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.send.entity.SysExpressNoPool;
import com.yunkouan.wms.modules.send.vo.SysExpressNoPoolVo;

/** 
* @Description: 快递单号池service
* @author tphe06
* @date 2017年10月17日 下午1:55:43  
*/
public interface ISysExpressNoPoolService {
	/**
	 * 页面列表查询（分页）
	 * @param vo
	 * @param p 当前登录帐号
	 * @return
	 */
	public List<SysExpressNoPoolVo> list(SysExpressNoPoolVo vo, Principal p);
	/**
	 * 页面列表查询（不分页）
	 * @param vo
	 * @param p
	 * @return
	 */
	public List<SysExpressNoPoolVo> select(SysExpressNoPoolVo vo, Principal p);
	/**
	 * 根据属性查询实体
	 * @param entity
	 * @return
	 */
	public List<SysExpressNoPool> query(SysExpressNoPool entity);
	/**
	 * 根据属性查询单个实体
	 * @param entity
	 * @return
	 */
	public SysExpressNoPool queryOne(SysExpressNoPool entity);
	/**
	 * 根据主键获取单个实体
	 * @param id
	 * @return
	 */
	public SysExpressNoPool get(String id);
	/**
	 * 统计数量方法
	 * @param vo
	 * @param p
	 * @return
	 */
	public long count(SysExpressNoPoolVo vo, Principal p);
	/**
	 * 页面查看详情
	 * @param id
	 * @return
	 */
	public SysExpressNoPoolVo view(String id);
	/**
	 * 新增方法
	 * @param vo
	 * @param p
	 * @return
	 */
	public int insert(SysExpressNoPoolVo vo, Principal p);
	/**
	 * 修改方法
	 * @param vo
	 * @param p
	 * @return
	 */
	public int update(SysExpressNoPoolVo vo, Principal p);
	/**
	 * 删除方法
	 * @param id
	 */
	public int delete(String id);
	/** 
	* @Title: delete 
	* @Description: 批量删除方法
	* @auth tphe06
	* @time 2018 2018年4月18日 下午2:44:28
	* @param ids
	* void
	*/
	public void delete(String[] ids);
	/** 
	* @Title: queryOne 
	* @Description: 得到快递公司的一个运单号
	* @auth tphe06
	* @time 2018 2018年4月18日 下午3:14:38
	* @return
	* String
	 * @throws Exception 
	 * @throws IOException 
	*/
	public String selectOne(SysExpressNoPool pool) throws IOException, Exception;
}