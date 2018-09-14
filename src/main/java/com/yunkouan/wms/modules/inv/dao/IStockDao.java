package com.yunkouan.wms.modules.inv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import com.yunkouan.wms.modules.inv.entity.InvLog;
import com.yunkouan.wms.modules.inv.entity.InvStock;
import com.yunkouan.wms.modules.inv.vo.InStockVO;
import com.yunkouan.wms.modules.inv.vo.InvCountVO;
import com.yunkouan.wms.modules.inv.vo.InvLogVO;
import com.yunkouan.wms.modules.inv.vo.InvStockVO;
import com.yunkouan.wms.modules.inv.vo.InvWarnVO;
import com.yunkouan.wms.modules.send.vo.TotalVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.provider.ExampleProvider;

/**
 * 库存数据层接口
 */
public interface IStockDao extends Mapper<InvStock> {
	@Options(useCache = false)
    @SelectProvider(type = ExampleProvider.class, method = "dynamicSQL")
    public List<InvStock> selectByExample(Object example);

	/**
	 * @param skuVo
	 * @return
	 * @Description 
	 * @version 2017年2月14日 下午5:47:14<br/>
	 * @author 王通<br/>
	 */
	public List<InvStock> findStockList(InvStockVO stockVo) throws Exception;

	/**
	 * @param stock
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月24日 下午2:49:00<br/>
	 * @author 王通<br/>
	 */
	public InvStock view(InvStock stock);

	/**
	 * @param stockVo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月27日 下午1:12:17<br/>
	 * @author 王通<br/>
	 */
	public int lockOutStock(InvStockVO stockVo);

	/**
	 * @param stockVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年2月28日 下午2:11:14<br/>
	 * @author 王通<br/>
	 */
	public int unlockOutStock(InvStockVO stockVo);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月2日 下午2:47:10<br/>
	 * @author 王通<br/>
	 */
	public List<InvLog> listLog(InvLogVO vo);

	/**
	 * @param warnVo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年3月13日 下午5:59:12<br/>
	 * @author 王通<br/>
	 */
	public List<InvWarnVO> selectWarnCount(InvWarnVO warnVo);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年4月1日 上午9:57:16<br/>
	 * @author 王通<br/>
	 */
	public List<InvStock> findStockByCount(InvCountVO vo);

	/**
	 * @param warnVo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月9日 下午5:22:15<br/>
	 * @author 王通<br/>
	 */
	public List<InvWarnVO> selectWarnCountAll(InvWarnVO warnVo);

	/**
	 * @param warnVo
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年5月21日 下午4:54:37<br/>
	 * @author 王通<br/>
	 */
	public List<InvWarnVO> selectWarnFromSku(InvWarnVO warnVo);

	/**
	 * @param vo
	 * @return
	 * @required 
	 * @optional  
	 * @Description 
	 * @version 2017年6月28日 下午2:06:44<br/>
	 * @author 王通<br/>
	 */
	public List<InvStock> selectStockSkuBatch(InvStockVO vo);


	List<InStockVO> selectInStock(InStockVO inStockVO);

	public TotalVo selectTotalInStock(InStockVO inStockVO);

}