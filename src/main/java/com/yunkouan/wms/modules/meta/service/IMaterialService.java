package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaMaterial;
import com.yunkouan.wms.modules.meta.entity.MetaMaterialLog;
import com.yunkouan.wms.modules.meta.vo.MaterialLogVo;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;

/**
 * 辅材服务接口
 * @author tphe06 2017年2月14日
 */
public interface IMaterialService {
    /**
     * 辅材列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(MaterialVo vo, Principal p) throws DaoException, ServiceException;
    /**
     * 查询辅材详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 根据辅材属性查询单个辅材信息
     */
    public MetaMaterial query(MetaMaterial entity);

    /**
     * 添加辅材
     * @param vo 
     * @return
     * @throws Exception 
     */
    public ResultModel add(MaterialVo vo, Principal p) throws DaoException, ServiceException, Exception;

    /**
     * 修改辅材
     * @param vo 
     * @return
     */
    public ResultModel update(MaterialVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效辅材
     * @param idList 
     * @return
     */
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException;

    /**
     * 失效辅材
     * @param id 
     * @return
     * @throws Exception 
     */
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException, Exception;

    /**
     * 取消辅材
     * @param idList 
     * @return
     */
    public ResultModel cancel(List<String> idList) throws DaoException, ServiceException;
    /**
     * 修改辅材数量
     * @param idList 
     * @return
     * @throws Exception 
     */
    public ResultModel changeQty(MaterialVo vo) throws DaoException, ServiceException, Exception;

	public ResultModel saveAndEnable(MaterialVo vo) throws DaoException, ServiceException, Exception;
	/**
	 * @param logVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月23日 上午10:16:26<br/>
	 * @author 王通<br/>
	 */
	public Page<MaterialLogVo> logList(MaterialLogVo logVo) throws Exception;
	
	/**
	 * @param log
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月23日 上午11:24:15<br/>
	 * @author 王通<br/>
	 */
	public ResultModel addLog(MetaMaterialLog log) throws Exception;
	/**
	 * @param id
	 * @return
	 * @throws DaoException
	 * @throws ServiceException
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年8月23日 下午3:04:32<br/>
	 * @author 王通<br/>
	 */
	MaterialVo get(String id) throws DaoException, ServiceException;
	/**
	 * @param materialNo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月7日 下午3:31:29<br/>
	 * @author 王通<br/>
	 */
	public String getIdByNo(String materialNo);
	/**
	 * @return
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月18日 上午10:41:09<br/>
	 * @author 王通<br/>
	 */
	ResponseEntity<byte[]> downloadMaterialDemo() throws Exception;
	/**
	 * @param fileLicense
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月18日 上午10:41:20<br/>
	 * @author 王通<br/>
	 */
	void importMaterial(MultipartFile fileLicense) throws Exception;

}