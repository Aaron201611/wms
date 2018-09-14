package com.yunkouan.wms.modules.park.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yunkouan.entity.ResultModel;
import com.yunkouan.exception.BizException;
import com.yunkouan.saas.modules.sys.service.IOrgService;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.constant.BizStatus;
import com.yunkouan.wms.common.constant.Constant;
import com.yunkouan.wms.common.service.BaseService;
import com.yunkouan.wms.common.util.FqDataUtils;
import com.yunkouan.wms.common.util.IdUtil;
import com.yunkouan.wms.modules.meta.service.IMerchantService;
import com.yunkouan.wms.modules.park.dao.IParkOrgBusiStasDao;
import com.yunkouan.wms.modules.park.entity.ParkOrgBusiStas;
import com.yunkouan.wms.modules.park.service.IParkOrgBusiStasService;
import com.yunkouan.wms.modules.park.service.IParkRentService;
import com.yunkouan.wms.modules.park.vo.ParkOrgBusiStasVo;
import com.yunkouan.wms.modules.park.vo.ParkRentVo;
import com.yunkouan.wms.modules.send.entity.SendDelivery;

/**
 * 企业业务统计服务实现类
 *@Description
 *@author Aaron
 *@date 2017年3月9日 下午7:35:48
 *version v1.0
 */
@Service
public class ParkOrgBusiStasServiceImpl extends BaseService implements IParkOrgBusiStasService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private IParkOrgBusiStasDao parkOrgBusiStasDao;
	
	@Autowired
	private IOrgService orgService;//企业服务
	
	@Autowired
	private IParkRentService parkRentService;//出租服务
	
	@Autowired
	private IMerchantService merchantService;//客商服务
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 查询分页信息
	 * @param reqParam
	 * @return
	 * @version 2017年3月9日 下午7:59:33<br/>
	 * @author Aaron He<br/>
	 */
	public ResultModel qryPageList(ParkOrgBusiStasVo reqParam){
		if(reqParam == null)
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		ResultModel result = new ResultModel();
    	//根据企业名称查询园区id列表
    	List<String> orgList = null;//orgService.list(reqParam.getOrgName());
    	if(StringUtils.isNotEmpty(reqParam.getOrgName())){
    		orgList = orgService.list(reqParam.getOrgName(),Constant.ORGTYPE_PARK);
    		if(orgList.isEmpty()) return result;
    		reqParam.setOrgIdList(orgList);
    	}
    	//根据客商名称查询企业id列表
    	List<String> merchantList = null;
    	if(StringUtils.isNotEmpty(reqParam.getMerchantName())){
    		merchantList = orgService.list(reqParam.getMerchantName(),Constant.ORGTYPE_ORG);
    		if(merchantList.isEmpty()) return result;
    		reqParam.setMerchantList(merchantList);
    	}
		
		//分页设置
		Page<SendDelivery> page = null;
		if ( reqParam.getCurrentPage() == null ) {
			reqParam.setCurrentPage(0);
		}
		if ( reqParam.getPageSize() == null ) {
			reqParam.setPageSize(DEFAULT_PAGE_SIZE);
		}
		page = PageHelper.startPage(reqParam.getCurrentPage()+1, reqParam.getPageSize());
    	
    	List<ParkOrgBusiStas> list = parkOrgBusiStasDao.selectByExample(reqParam.getConditionExample());
    	
		if ( list == null || list.isEmpty() ) {
			return result;
		}
		result.setPage(page);		
		
		FqDataUtils fdu = new FqDataUtils();
		List<ParkOrgBusiStasVo> voList = new ArrayList<ParkOrgBusiStasVo>();
		for (ParkOrgBusiStas entity : list) {
			ParkOrgBusiStasVo vo = new ParkOrgBusiStasVo();
			vo.setParkOrgBusiStas(entity);
			//查询租赁企业
			if ( !StringUtil.isTrimEmpty(entity.getOrgId()) ) {
				vo.setOrgName(fdu.getOrgNameById(orgService, entity.getOrgId()));
			}
			//查询客商
			if ( !StringUtil.isTrimEmpty(entity.getMerchantId()) ) {
				vo.setMerchantName(fdu.getOrgNameById(orgService, entity.getMerchantId()));
			}
			voList.add(vo);
		}
		result.setList(voList);
		return result;
	}
	
	/**
	 * 新增
	 * @param reqParam
	 * @return
	 * @version 2017年3月10日 上午11:54:06<br/>
	 * @author Aaron He<br/>
	 */
	public ParkOrgBusiStasVo add(ParkOrgBusiStasVo reqParam){
		if(reqParam == null)
			throw new BizException(BizStatus.PARAMETER_IS_NULL.getReasonPhrase());
		
		reqParam.getParkOrgBusiStas().setBusiStasId(IdUtil.getUUID());
		reqParam.getParkOrgBusiStas().setBusiStasId2(context.getStrategy4Id().getBusinessStaticSeq());
		parkOrgBusiStasDao.insertSelective(reqParam.getParkOrgBusiStas());
		return reqParam;	
	}
	
	/**
	 * 新增统计记录
	 * @param docType
	 * @param busiNo
	 * @param orgId
	 * @param warehouseId
	 * @param qty
	 * @param weight
	 * @param volume
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	public ParkOrgBusiStasVo newBusiStas(Integer docType,String busiNo,String orgId,String warehouseId,double qty,double weight,double volume,String operator)throws Exception{
		//查询出租企业
		ParkRentVo parkRentVo = new ParkRentVo();
		parkRentVo.getParkRent().setMerchantId(orgId);//承租方
		parkRentVo.getParkRent().setWarehouseId(warehouseId);
		parkRentVo.setOrderTime(new Date());
		List<ParkRentVo> rentList = parkRentService.qryList(parkRentVo);
		ParkRentVo rentVo = new ParkRentVo();
		if(rentList != null && !rentList.isEmpty()){
			rentVo = rentList.get(0);
		}
		//登记企业业务统计
		ParkOrgBusiStasVo stasVo = new ParkOrgBusiStasVo();
		ParkOrgBusiStas entity = new ParkOrgBusiStas();
		entity.setBusiStasId(IdUtil.getUUID());
		entity.setBusiType(docType);
		entity.setDocNo(busiNo);
		entity.setMerchantId(orgId);//承租方
		entity.setOrgId(rentVo.getParkRent().getOrgId());//出租方
		entity.setStasQty(qty);
		entity.setStasWeight(weight);
		entity.setStasVolume(volume);
		entity.setStasDate(new Date());
		entity.setCreatePerson(operator);
		entity.setUpdatePerson(operator);
		entity.setBusiStasId(IdUtil.getUUID());
		entity.setBusiStasId2(context.getStrategy4Id().getBusinessStaticSeq());
		stasVo.setParkOrgBusiStas(entity);
		parkOrgBusiStasDao.insertSelective(entity);
		
		return stasVo;
	}
	
	
}
