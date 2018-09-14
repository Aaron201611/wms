package com.yunkouan.wms.modules.meta.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.excel.ExcelUtil;
import com.yunkouan.exception.BizException;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.util.DateUtil;
import com.yunkouan.util.FileUtil;
import com.yunkouan.util.NumberUtil;

import com.yunkouan.util.PubUtil;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.CacheName;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.constant.ErrorCode;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.modules.meta.dao.IMaterialDao;
import com.yunkouan.wms.modules.meta.dao.IMaterialLogDao;
import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;
import com.yunkouan.wms.modules.meta.service.IMaterialService;
import com.yunkouan.wms.modules.meta.vo.MaterialLogVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;
import com.yunkouan.saas.modules.sys.entity.SysUser;
import com.yunkouan.saas.modules.sys.service.ISysParamService;
import com.yunkouan.saas.modules.sys.service.IUserService;

/**
 * 辅材服务实现类
 * @author 王通 2017-8-22 14:23:51
 */
@Service
@Transactional(readOnly=true)
public class MaterialServiceImpl extends BaseService implements IMaterialService {
//	private static Log log = LogFactory.getLog(MaterialServiceImpl.class);

//	/**
//	 * 外部服务-库存
//	 */
//	@Autowired
//	IStockService stockService;
	/**
	 * 外部服务-用户
	 */
	@Autowired
	IUserService userService;
	
	@Autowired
	private ISysParamService paramService;

	
	/**辅材数据层接口*/
	@Autowired
    private IMaterialDao dao;
	/**辅材数据层接口*/
	@Autowired
    private IMaterialLogDao logDao;
	/**辅材包装数据层接口**/

    /**
     * 辅材列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MaterialVo vo, Principal p) throws DaoException, ServiceException {
    	if (vo == null) {
    		vo = new MaterialVo();
    	}
    	if (vo.getEntity() == null) {
    		vo.setEntity(new MetaMaterial());
    	}
    	vo.getEntity().setOrgId(p.getOrgId());
		Page<MetaMaterial> page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		List<MetaMaterial> list = dao.selectByExample(vo.getExample());
    	List<MaterialVo> r = new ArrayList<MaterialVo>();
    	if(list != null) for(int i=0; i<list.size(); ++i) {
    		MetaMaterial entity = list.get(i);
    		MaterialVo n = new MaterialVo(entity);
    		if (entity.getMaterialTypeId() != null) {
        		n.setMaterialTypeName(paramService.getValue(CacheName.MATERIAL_TYPE, entity.getMaterialTypeId()));
        	}
    		r.add(n);
    	}
    	return new ResultModel().setPage(page).setList(r);
    }

    /**
     * 查询辅材详情
     * @param id 
     * @return
     */
	@Override
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException {
    	MaterialVo vo = new MaterialVo();
    	/**查询辅材信息**/
    	MetaMaterial entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	if (entity.getMaterialTypeId() != null) {
    		vo.setMaterialTypeName(paramService.getValue(CacheName.MATERIAL_TYPE, entity.getMaterialTypeId()));
    	}
    	vo.setEntity(entity);
        return new ResultModel().setObj(vo);
    }

    /**
     * 添加辅材
     * @param vo 
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel add(MaterialVo vo, Principal p) throws Exception {
    	/**添加辅材信息**/
    	MetaMaterial entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	MetaMaterial qryParam = new MetaMaterial();
    	qryParam.setMaterialName(entity.getMaterialName());
    	qryParam.setOrgId(p.getOrgId());
    	if (this.dao.selectOne(qryParam) != null) {
    		throw new BizException("err_material_rep");
    	}
    	if (entity.getMaterialStatus() == null) {
        	entity.setMaterialStatus(Constant.STATUS_OPEN);
    	}
    	String id = IdUtil.getUUID();
    	entity.setMaterialNo(context.getStrategy4No(p.getOrgId(), LoginUtil.getWareHouseId()).getMaterialNo(p.getOrgId()));
    	entity.setMaterialId(id);
    	entity.setOrgId(p.getOrgId());
    	entity.setCreatePerson(p.getUserId());
    	entity.setCreateTime(new Date());
        int r = dao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        if (entity.getQty() != null) {
        	/**辅材日志*/
        	MetaMaterialLog log = vo.getMaterialLog();
        	if (log == null) {
        		log = new MetaMaterialLog();
        	}
        	log.setMaterialLogType(Constant.MATERIAL_LOG_TYPE_OTHER);
    		log.setMaterialId(id);
    		log.setNote("初始导入");
	    	log.setChangeQty(entity.getQty());
	    	log.setResultQty(entity.getQty());
        	log.setCreatePerson(p.getUserId());
        	log.setWarehouseId(LoginUtil.getWareHouseId());
        	log.setOrgId(p.getOrgId());
        	log.setCreateTime(new Date());
        	/** 保存日志信息 **/
        	this.addLog(log);
        }
        return view(id, p);
    }

    /**
     * 修改辅材
     * @param vo 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel update(MaterialVo vo, Principal p) throws DaoException, ServiceException {
    	/**修改辅材信息**/
    	MetaMaterial entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	entity.setOrgId(p.getOrgId());
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(new Date());
    	int r = dao.updateByPrimaryKeySelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	/**返回辅材信息**/
        return view(entity.getMaterialId(), p);
    }

    /**
     * 生效辅材
     * @param id 
     * @return
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaMaterial old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMaterialStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaMaterial obj = new MetaMaterial();
	    	obj.setMaterialId(id);
	    	obj.setMaterialStatus(Constant.STATUS_ACTIVE);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
    }

    /**
     * 失效辅材 在有库存和未完成收货单的情况下不能失效
     * @param id 
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
    public ResultModel disable(List<String> idList) throws Exception {
    	
    	for (String id : idList) {
	    	MetaMaterial old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMaterialStatus() != Constant.STATUS_ACTIVE) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	//校验通过
	    	MetaMaterial obj = new MetaMaterial();
	    	obj.setMaterialId(id);
	    	obj.setMaterialStatus(Constant.STATUS_OPEN);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
    	ResultModel m = new ResultModel();
        return m;
    }

    /**
    * @Description: 取消辅材
    * @param id
    * @return
    * @throws DaoException
    * @throws ServiceException
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel cancel(List<String> idList) throws DaoException, ServiceException {
    	for (String id : idList) {
	    	MetaMaterial old =  dao.selectByPrimaryKey(id);
	    	if(old == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
	    	if(old.getMaterialStatus() != Constant.STATUS_OPEN) throw new ServiceException(ErrorCode.STATUS_NO_RIGHT);
	    	MetaMaterial obj = new MetaMaterial();
	    	obj.setMaterialId(id);
	    	obj.setMaterialStatus(Constant.STATUS_CANCEL);
	        int r = dao.updateByPrimaryKeySelective(obj);
	        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	}
        ResultModel m = new ResultModel();
    	return m;
	}

	@Override
	public MetaMaterial query(MetaMaterial entity) {
		return dao.selectOne(entity);
	}

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:57:02<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel saveAndEnable(MaterialVo vo) throws Exception {
    	Principal p = LoginUtil.getLoginUser();
    	ResultModel r = null;
		if (StringUtil.isTrimEmpty(vo.getEntity().getMaterialId())) {
			r = this.add(vo, p);
			String materialId = vo.getEntity().getMaterialId();
			List<String> materialList = new ArrayList<String>();
			materialList.add(materialId);
			this.enable(materialList);
		} else {
			r = this.update(vo, p);
			String materialId = vo.getEntity().getMaterialId();
			List<String> materialList = new ArrayList<String>();
			materialList.add(materialId);
			this.enable(materialList);
		}
		return r;
	}

	/**
	 * @param vo.resultQty,log.involveBill,log.materialId,
	 * log.materialLogType,log.note
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月22日 下午2:44:17<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel changeQty(MaterialVo vo) throws Exception {
		Principal p = LoginUtil.getLoginUser();
		Date now = new Date();
		/**修改辅材信息**/
    	MetaMaterial entity = vo.getEntity();
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	String materialId = entity.getMaterialId();
    	MetaMaterial old = dao.selectOne(new MetaMaterial().setMaterialId(entity.getMaterialId()).setOrgId(p.getOrgId()));
    	/**辅材日志*/
    	MetaMaterialLog log = vo.getMaterialLog();
    	if (log == null) {
    		log = new MetaMaterialLog();
    	}
		log.setMaterialId(materialId);
		int retQty = 0;
		if (vo.getChangeQty() == null || vo.getChangeQty() == 0) {
			retQty = vo.getResultQty();
	    	log.setChangeQty(vo.getResultQty() - old.getQty());
	    	log.setResultQty(vo.getResultQty());
		} else {
			retQty = old.getQty() + vo.getChangeQty();
			if (retQty < 0) {
				throw new BizException("err_material_stock");
			}
	    	log.setChangeQty(vo.getChangeQty());
	    	log.setResultQty(retQty);
		}
    	log.setCreatePerson(p.getUserId());
    	log.setWarehouseId(LoginUtil.getWareHouseId());
    	log.setOrgId(p.getOrgId());
    	log.setCreateTime(now);
    	/** 保存日志信息 **/
    	this.addLog(log);
    	entity = new MetaMaterial();
    	entity.setMaterialId(materialId);
    	entity.setQty(retQty);
    	entity.setUpdatePerson(p.getUserId());
    	entity.setUpdateTime(now);
    	int r = dao.updateByPrimaryKeySelective(entity);
    	if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
    	
    	/**返回辅材信息**/
        return view(materialId, p);
	}

	/**
	 * @param logVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月23日 上午10:56:09<br/>
	 * @author 王通<br/>
	 */
	@Override
	public Page<MaterialLogVo> logList(MaterialLogVo vo) throws Exception {
		Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
		Page<MaterialLogVo> page = null;
		page = PageHelper.startPage(vo.getCurrentPage()+1, vo.getPageSize());
		if (vo.getMaterialLog() == null) {
			MetaMaterialLog materialLog = new MetaMaterialLog();
			vo.setMaterialLog(materialLog);
		} else {
			// 查询日志列表 
			vo.setMaterialNoLike(StringUtil.likeEscapeH(vo.getMaterialNoLike()));
			vo.setInvolveBillLike(StringUtil.likeEscapeH(vo.getInvolveBillLike()));
			vo.setMaterialNameLike(StringUtil.likeEscapeH(vo.getMaterialNameLike()));
			Date startDate = vo.getStartDate();
			Date endDate = vo.getEndDate();
			if (endDate != null) {
				if (startDate != null) {
					if (startDate.after(endDate)) {
						throw new BizException("err_stock_log_time_wrong");
					}
					if (endDate.after(new Date())) {
						throw new BizException("err_stock_log_time_limit");
					}
				}
				endDate = DateUtil.setEndTime(endDate);
			}
		}
//		vo.getMaterialLog().setWarehouseId(loginUser.getWarehouseId());
		vo.getMaterialLog().setOrgId(loginUser.getOrgId());
		List<MetaMaterialLog> listLog = this.logDao.list(vo);
		if ( listLog == null || listLog.isEmpty() ) {
			return null;
		}
		// 组装信息
		List<MaterialLogVo> listVO = new ArrayList<MaterialLogVo>();
		for (int i = 0; i < listLog.size(); i++) {
			MetaMaterialLog log = listLog.get(i);
			if ( log == null ) {
				continue;
			}
			MaterialLogVo logVo = new MaterialLogVo(log).searchCache();
			listVO.add(logVo);
			// 查询辅料信息
			MetaMaterial metaMaterial = dao.selectByPrimaryKey(log.getMaterialId());
			if (metaMaterial != null) {
				logVo.setMaterialName(metaMaterial.getMaterialName());
				logVo.setMaterialNo(metaMaterial.getMaterialNo());
				logVo.setMaterialBarCode(metaMaterial.getMaterialBarCode());
				logVo.setMeasureUnit(metaMaterial.getMeasureUnit());
			}
			// 查询创建人信息
			SysUser createPerson = userService.get(log.getCreatePerson());
			if ( createPerson != null ) {
				logVo.setCreateUserName(createPerson.getUserName());
			}
			// 查询日志类型
			if (log.getMaterialLogType() != null) {
				logVo.setMaterialLogTypeName(paramService.getValue(CacheName.MATERIAL_LOG_TYPE, log.getMaterialLogType()));
			}
		}
		page.clear();
		page.addAll(listVO);
//		rm.setPage(page);
//		rm.setList(listVO);
		return page;
	}

	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月23日 上午10:56:09<br/>
	 * @author 王通<br/>
	 */
	@Override
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public ResultModel addLog(MetaMaterialLog entity) throws Exception {
		Principal p = LoginUtil.getLoginUser();
		/**添加辅材信息**/
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	entity.clear();
    	String id = IdUtil.getUUID();
    	entity.setMaterialLogId(id);
    	entity.setOrgId(p.getOrgId());
    	entity.setWarehouseId(LoginUtil.getWareHouseId());
    	entity.setCreatePerson(p.getUserId());
    	entity.setCreateTime(new Date());
        int r = logDao.insertSelective(entity);
        if(r == 0) throw new DaoException(ErrorCode.DB_EXCEPTION);
        ResultModel rm = new ResultModel();
        return rm;
	}
	
	 /**
     * 查询辅材详情
     * @param id 
     * @return
     */
	@Override
    public MaterialVo get(String id) throws DaoException, ServiceException {
//		Principal p
		MaterialVo vo = new MaterialVo();
    	/**查询辅材信息**/
    	MetaMaterial entity =  dao.selectByPrimaryKey(id);
    	if(entity == null) throw new ServiceException(ErrorCode.DATA_EMPTY);
    	vo.setEntity(entity);
        return vo;
    }

	/**
	 * @param materialNo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 下午3:31:49<br/>
	 * @author 王通<br/>
	 */
	@Override
	public String getIdByNo(String materialNo) {
		MetaMaterial material = new MetaMaterial();
		material.setMaterialNo(materialNo);
		return dao.selectOne(material).getMaterialId();
	}
	
	/**
	 * 导入辅材
	 * @param stockVo
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月27日 下午4:37:04<br/>
	 * @author 王通<br/>
	 */
    @Override
    @Transactional(readOnly=false)
	public void importMaterial(MultipartFile fileLicense) throws Exception {
    	Principal loginUser = LoginUtil.getLoginUser();
    	// 验证用户是否登录
    	if (loginUser == null || StringUtil.isEmpty(loginUser.getAccountId())) {
    		throw new BizException("valid_common_user_no_login");
    	}
    	if (fileLicense == null) {
    		throw new BizException("parameter_is_null");
    	}
    	File newFile = FileUtil.createNewFile(fileLicense);
    	List<MaterialVo> materialVoList = null;
    	if ( newFile != null ) {
    		try {
    			materialVoList = new ArrayList<MaterialVo>();
				List<Object> listImport = ExcelUtil.parseExcel("material", newFile.getAbsolutePath());
				for (int i = 0; i < listImport.size(); i++) {
					Object obj = listImport.get(i);
					if (obj != null) {
						materialVoList.add((MaterialVo) obj);
					} else {
						MaterialVo materialVo = new MaterialVo();
						materialVo.setEntity(new MetaMaterial());
						materialVoList.add(materialVo);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				// 删除文件
				newFile.delete();
			}
    	} else {
    		throw new BizException("err_stock_imp_no_file");
    	}
    	String ret = "";
    	if ( PubUtil.isEmpty(materialVoList) ) {
    		throw new BizException("err_stock_imp_no_date");
    	}
    	
    	List<Integer> repeatIndexList = new ArrayList<Integer>();
		for (int i = 0; i < materialVoList.size(); i++) {
			if (repeatIndexList.contains(i)) {
				continue;
			}
    		boolean currect = true;
    		String lineResult = "";
    		MaterialVo materialVo = materialVoList.get(i);
			MetaMaterial  material = materialVo.getEntity();
			if (material == null) {
				currect = false;
				lineResult = "行" + (i + 2) + "无数据!\r\n<br />";
    			ret += lineResult;
    			continue;
			}
			//获取当前行数据，带$为必填项
			String materialName$ = material.getMaterialName();
//			String materialBarCode = material.getMaterialBarCode();
			String materialTypeName$ = materialVo.getMaterialTypeName();
//			String measureUnit = material.getMeasureUnit();
//			Double perWeight = material.getPerWeight();
//			Double perVolume = material.getPerVolume();
			Double packLength = material.getPackLength();
			Double packWidth = material.getPackWidth();
			Double packHeight = material.getPackHeight();
//			Integer qty = material.getQty();
    		String materialStatusName = materialVo.getMaterialStatusName();
			//检测是否信息不全，即无效
    		if (StringUtil.isTrimEmpty(materialName$)) {
    			currect = false;
    			lineResult += "'辅材名称'不能为空！";
    		} else {
    			MetaMaterial qryParam = new MetaMaterial();
    	    	qryParam.setMaterialName(materialName$);
    	    	qryParam.setOrgId(loginUser.getOrgId());
    	    	if (this.dao.selectOne(qryParam) != null) {
        			currect = false;
        			lineResult += "'辅材名称'已存在！";
    	    	}
    		}
    		if (StringUtil.isTrimEmpty(materialTypeName$)) {
    			currect = false;
    			lineResult += "'辅材类型名称'不能为空！";
    		}
    		if (materialStatusName != null) {
    			String materialStatus = paramService.getValue(CacheName.STATUS_REVERSE, materialStatusName);
    			if (materialStatus == null) {
        			currect = false;
        			lineResult += "根据'状态'查询状态代码失败！";
    			} else {
            		//找到货品状态编码，根据货品状态中文
        			material.setMaterialStatus(Integer.parseInt(materialStatus));
    			}
    		}
    		String materialTypeId = paramService.getValue(CacheName.MATERIAL_TYPE_REVERSE, materialTypeName$);
    		if (materialTypeId == null) {
    			currect = false;
    			lineResult += "根据'辅材类型'查询辅材类型代码失败！";
			}
    		//判断校验结果
    		if (!currect) {
    			lineResult = "行" + (i + 2) + ":" + lineResult + "\r\n<br />";
    			ret += lineResult;
    			continue;
    		}
        	//检查是否有无效或重复数据，并提示
			//依次查找后续对象是否有重复
			for (int j = i+1; j < materialVoList.size(); j++) {
				MaterialVo materialVo2 = materialVoList.get(j);
				MetaMaterial material2 = materialVo2.getEntity();
				if (material2 == null) {
					continue;
				}
				String materialName$2 = material2.getMaterialName();
    			//判断重复
    			if (StringUtil.equals(materialName$,materialName$2)) {
        			currect = false;
        			lineResult += "与行" + (j + 2) + "数据重复！";
        			repeatIndexList.add(j);
    			}
			}
			//开始导入
    		//设置体积
			Double packVolume = NumberUtil.mul(NumberUtil.mul(packHeight, packWidth),packLength, 6);
    		material.setPackVolume(packVolume);
			material.setMaterialTypeId(materialTypeId);
			if (currect) {
				//字段校验完毕，开始导入操作
				this.add(materialVo, loginUser);
			} else {
    			lineResult = "行" + (i + 2) + "：" + lineResult + "\n\r<br />";
    			ret += lineResult;
			}
		}
		if (!StringUtil.isTrimEmpty(ret)) {
			//这里是为了回滚事物，不提交有失败的数据
			throw new BizException("err_import_stock_msg", ret);
		}
	}

	/**
	 * @param string
	 * @param file
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:14:23<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> download(String fileName, File file) throws Exception {
		return FileUtil.excel2Stream(file.getPath(), fileName);
	}
	
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月21日 下午6:20:51<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	@Override
	public ResponseEntity<byte[]> downloadMaterialDemo() throws Exception {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource("excel/template/material_template.xls").getPath());
		return download("material_template.xls", file);
	}
}