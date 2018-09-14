package com.yunkouan.wms.modules.meta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IMerchantDao;
import com.yunkouan.wms.modules.meta.entity.MetaMerchant;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.meta.vo.MerchantVo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 客商服务实现类
 * @author tphe06 2017年2月14日
 */
@Service
@Transactional(readOnly=true)
public class MerchantServiceImpl extends BaseService implements IMerchantService {
    /**客商数据层接口*/
	@Autowired
    private IMerchantDao dao;

    /**
     * 客商列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
		Page<MetaMerchant> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		Example example = vo.getExample();
		this.defWhere(example);
    	List<MetaMerchant> list = dao.selectByExample(example);
    	List<MerchantVo> r = new ArrayList<MerchantVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		r.add(new MerchantVo(list.get(i)));
    	}
    	return new ResultModel().setPage(page).setList(r);
    }
    
    /**
     * 客商列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public List<MetaMerchant> listByParam(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	vo.getEntity().setOrgId(p.getOrgId());
		Example example = vo.getExample();
		this.defWhere(example);
    	List<MetaMerchant> list = dao.selectByExample(example);
    	return list;
    }

    /**
     * 查询客商详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	MetaMerchant obj =  dao.selectByPrimaryKey(id);
        return new ResultModel().setObj(new MerchantVo(obj));
    }

    private byte[] lock = new byte[1];
    /**
     * 添加客商
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	MetaMerchant entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	MetaMerchant old = dao.selectOne(new MetaMerchant().setMerchantNo(entity.getMerchantNo()).setOrgId(p.getOrgId()));
    	if(old != null) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	//设置客商编号
    	synchronized(lock) {
	    	String no = IdUtil.addOne(dao.getMaxNo(), IdUtil.INIT);
	    	entity.setMerchantNo(no);
    	}
    	//若海关客商代码为空，则默认为客商代码 2017-11-17
    	if(StringUtil.isEmpty(entity.getHgMerchantNo())){
    		entity.setHgMerchantNo(entity.getMerchantNo());
    	}
    	//若相关代码为空，则默认为客商代码 2017-11-17
    	if(StringUtil.isEmpty(entity.getRelatedNo())){
    		entity.setRelatedNo(entity.getMerchantNo());
    	}
    	//设置其他属性
    	entity.setMerchantStatus(Constant.STATUS_OPEN);
    	String id = IdUtil.getUUID();
    	entity.setMerchantId(id);
		entity.setOrgId(p.getOrgId());
		entity.setCreatePerson(p.getUserId());
		entity.setUpdatePerson(p.getUserId());
    	entity.setCreatePerson(LoginUtil.getLoginUser().getUserId());
    	entity.setMerchantId2(context.getStrategy4Id().getMerchantSeq());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(id, p);
    }

    /**
     * 修改客商
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	MetaMerchant entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	MetaMerchant old = dao.selectOne(new MetaMerchant().setMerchantNo(entity.getMerchantNo()).setOrgId(p.getOrgId()));
    	if(old != null && !old.getMerchantId().equals(entity.getMerchantId())) throw new ServiceException(ErrorCode.NO_NOT_UNIQUE);
    	entity.setUpdatePerson(LoginUtil.getLoginUser().getUserId());
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        return view(entity.getMerchantId(), p);
    }

    /**
     * 生效客商
     * @param id 
     * @return
     */
    @Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaMerchant old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMerchantStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaMerchant obj = new MetaMerchant();
	    	obj.setMerchantId(id);
	    	obj.setMerchantStatus(Constant.STATUS_ACTIVE);
	    	obj.setUpdatePerson(LoginUtil.getLoginUser().getUserId());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
    }

    /**
     * 失效客商
     * @param id 
     * @return
     */
    @Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
    		MetaMerchant old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMerchantStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaMerchant obj = new MetaMerchant();
	    	obj.setMerchantId(id);
	    	obj.setMerchantStatus(Constant.STATUS_OPEN);
	    	obj.setUpdatePerson(LoginUtil.getLoginUser().getUserId());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
    }

    /**
     * 取消客商
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(List<String> idList) throws DaoException, ServiceException {
    	for(String id : idList) {
	    	MetaMerchant old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMerchantStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaMerchant obj = new MetaMerchant();
	    	obj.setMerchantId(id);
	    	obj.setMerchantStatus(Constant.STATUS_CANCEL);
	    	obj.setUpdatePerson(LoginUtil.getLoginUser().getUserId());
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
	}

	@Override
	public MetaMerchant get(String id) {
		MetaMerchant obj =  dao.selectByPrimaryKey(id);
		return obj;
	}

	@Override
	public List<String> list(String name) {
		Principal p = LoginUtil.getLoginUser();
		if(StringUtils.isBlank(name)) name = "";
		MetaMerchant m = new MetaMerchant();
		m.setMerchantName("%".concat(name).concat("%"));
		m.setOrgId(p.getOrgId());
		return dao.list(m);
	}
	
	
	

	/**
	* @Description: 添加并激活客商
	* @param vo
	* @return
	* @throws DaoException
	* @throws ServiceException
	 */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel addAndEnable(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	MetaMerchant entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	//设置客商编号
    	synchronized(lock) {
	    	String no = IdUtil.addOne(dao.getMaxNo(), IdUtil.INIT);
	    	entity.setMerchantNo(no);
    	}
    	//设置其他属性
    	entity.setOrgId(p.getOrgId());
    	entity.setMerchantStatus(Constant.STATUS_ACTIVE);
    	String id = IdUtil.getUUID();
    	entity.setMerchantId(id);
    	entity.setCreatePerson(LoginUtil.getLoginUser().getUserId());
    	entity.setMerchantId2(context.getStrategy4Id().getMerchantSeq());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        m.setObj(new MerchantVo(dao.selectByPrimaryKey(id)));
        return m;
    }

	/**
	* @Description: 修改并激活客商
	* @param vo
	* @return
	* @throws DaoException
	* @throws ServiceException
	 */
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel updateAndEnable(MerchantVo vo, Principal p) throws DaoException, ServiceException {
    	ResultModel m = new ResultModel();
    	//判断客商状态是否合法
    	MetaMerchant entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	MetaMerchant old =  dao.selectByPrimaryKey(entity.getMerchantId());
    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if(old.getMerchantStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
    	//修改客商
    	entity.clear();
    	entity.setOrgId(p.getOrgId());
    	entity.setUpdatePerson(LoginUtil.getLoginUser().getUserId());
    	entity.setMerchantStatus(Constant.STATUS_ACTIVE);
        int r = dao.updateByPrimaryKeySelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        m.setObj(new MerchantVo(dao.selectByPrimaryKey(entity.getMerchantId())));
        return m;
    }
	

	/**
	 * 设置默认查询条件
	 * @param example 
	 * @version 2017年3月8日 下午1:59:29<br/>
	 * @author andy wang<br/>
	 */
	private void defWhere ( Example example ) {
		if ( example == null || example.getOredCriteria() == null || example.getOredCriteria().isEmpty() ) {
			return ;
		}
		Criteria criteria = example.getOredCriteria().get(0);
		Principal loginUser = LoginUtil.getLoginUser();
		criteria.andEqualTo("orgId", loginUser.getOrgId());
	}

	/**
	 * @param merchantIdList
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月12日 上午10:06:25<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String concatMerchantShortName(List<String> merchantIdList) {
		String merchantShorName = "";
		for (String mId : merchantIdList) {
			MetaMerchant mm = this.dao.selectByPrimaryKey(mId);
			if (!StringUtil.isEmpty(mm.getMerchantShortName())) {
				merchantShorName = merchantShorName.concat(mm.getMerchantShortName()).concat(",");
			}
		}
		if (!StringUtil.isEmpty(merchantShorName)) {
			merchantShorName = merchantShorName.substring(0, merchantShorName.length() - 1);
		}
		return merchantShorName;
	}

	/**
	 * @param merchant
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午10:26:05<br/>
	 * @author 王通<br/>
	 */
	@Override
	public MetaMerchant view(MetaMerchant merchant) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param merchantName$
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午10:26:05<br/>
	 * @author 王通<br/>
	 */
	@Override
	public MetaMerchant selectOne(String merchantName) {
		MetaMerchant mm = new MetaMerchant();
		mm.setMerchantName(merchantName);
		return this.dao.selectOne(mm);
	}

	
	public static void main(String[] args) {
//		 Integer i = Integer.valueOf("50.0");
		String s = "50.0";
		int index = s.indexOf(".");
		s = s.substring(0, index);
		 Integer j = Integer.parseInt(s);
		 System.out.println(index);
		 System.out.println(j);
		 System.out.println(j);
	}
}