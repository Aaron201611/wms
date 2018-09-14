package com.yunkouan.wms.modules.meta.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.DaoException;
import com.yunkouan.exception.ServiceException;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.modules.meta.entity.MetaSku;
import com.yunkouan.wms.modules.meta.entity.MetaSkuPack;
import com.yunkouan.wms.modules.meta.vo.SkuVo;

/**
 * 货品服务接口
 * @author tphe06 2017年2月14日
 */
public interface ISkuService {
    /**
     * 货品列表数据查询（带分页，分页参数从1开始）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list4Page(SkuVo vo) throws DaoException, ServiceException;
    /**
     * 货品列表数据查询（分页）
     * @param vo 
     * @param page 
     * @return
     */
    public ResultModel list(SkuVo vo, Principal p) throws DaoException, ServiceException;
    
    /**
     * 查询所有货品列表
     * @param vo
     * @param p
     * @return
     * @throws Exception
     */
    public List<MetaSku> qryAllList(SkuVo vo,Principal p) throws Exception;
    /**
    * @Description: 分页查询安全库存信息（限设置了最大安全库存或者最小安全库存记录）
    * @param vo：查询参数，可选：
    * 货主名称：merchant.merchantName
    * 货品代码：entity.skuNo
    * 当前页：currentPage
    * 每页显示数量：pageSize
    * @throws DaoException
    * @throws ServiceException
    * @return SkuVo
    * @throws
    */
    public Page<MetaSku> list4stock(SkuVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 查询货品详情
     * @param id 
     * @return
     */
    public ResultModel view(String id, Principal p) throws DaoException, ServiceException;
    /**
     * 查询货品详情
     * @param id 
     * @return
     */
    public MetaSku get(String id);
    /**
     * 根据货品属性查询单个货品信息
     */
    public MetaSku query(MetaSku entity);

    /**
     * 添加货品
     * @param vo 
     * @return
     */
    public ResultModel add(SkuVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 修改货品
     * @param vo 
     * @return
     */
    public ResultModel update(SkuVo vo, Principal p) throws DaoException, ServiceException;

    /**
     * 生效货品
     * @param idList 
     * @return
     */
    public ResultModel enable(List<String> idList) throws DaoException, ServiceException;
    
//    /**
//     * 同步更新erp商品信息
//     * @param idList
//     * @throws Exception
//     */
//    public void updateErpSku(int status,List<String> idList) throws Exception;

    /**
     * 失效货品
     * @param id 
     * @return
     * @throws Exception 
     */
    public ResultModel disable(List<String> idList) throws DaoException, ServiceException, Exception;

    /**
     * 取消货品
     * @param idList 
     * @return
     */
    public ResultModel cancel(List<String> idList) throws DaoException, ServiceException;

    /**
     * 根据货品包装实体类属性查询一个货品信息
     */
    public MetaSkuPack querySkuPack(MetaSkuPack entity);
	/**
	 * @param vo
	 * @param p
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月19日 下午2:55:57<br/>
	 * @author 王通<br/>
	 */
	public ResultModel saveAndEnable(SkuVo vo) throws DaoException, ServiceException;

    /**
     * 修改货品，生效状态
     * @param vo 
     * @return
     */
    public ResultModel change(SkuVo vo, Principal p) throws DaoException, ServiceException;
	/**
	 * @param merchantIdList
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月11日 下午7:52:58<br/>
	 * @author 王通<br/>
	 */
	public List<String> getSkuIdList(List<String> merchantIdList);
	/**
	 * @param fileLicense
	 * @throws Exception
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 上午9:06:08<br/>
	 * @author 王通<br/>
	 */
	void importSku(MultipartFile fileLicense) throws Exception;
    /**
     * 从外部接口导入货品
     * @param skuVoList
     * @throws Exception
     */
    public List<SkuVo> importSkuForExtInface(List<SkuVo> skuVoList) throws Exception;
	/**
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月16日 下午2:57:21<br/>
	 * @author 王通<br/>
	 * @throws Exception 
	 */
	public ResponseEntity<byte[]> downloadSkuDemo() throws Exception;
	/**
	 * @param idList
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年9月22日 上午11:09:59<br/>
	 * @author 王通<br/>
	 */
	public ResultModel recovery(List<String> idList) throws DaoException, ServiceException;
	
	/**
	 * 批量调整货品
	 * @param fileLicense
	 * @throws Exception
	 */
	void batchAdjustmentSku(MultipartFile fileLicense) throws Exception;
	
	public ResponseEntity<byte[]> downloadBatchAjdustmentDemo() throws Exception;
	public MetaSku getBySkuNo(String skuNo);
	ResponseEntity<byte[]> skuExport(List<String> list) throws Exception;
}