package com.yunkouan.wms.common.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunkouan.exception.BizException;
import com.yunkouan.util.RedisTool;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.common.config.RedisConfig;
import com.yunkouan.wms.common.dao.ICommonDao;
import com.yunkouan.wms.common.service.ICommonService;
import com.yunkouan.wms.common.shiro.SystemAuthorizingRealm.Principal;
import com.yunkouan.wms.common.util.LoginUtil;
import com.yunkouan.wms.common.vo.CommonVo;

/**
 * 通用服务实现类
 * <br/><br/>
 * @Description 
 * @version 2017年2月17日 下午3:00:35<br/>
 * @author 王通<br/>
 */
@Service
public class CommonServiceImpl implements ICommonService {
	/**
     * 数据层接口
     */
	@Autowired
    private ICommonDao dao;
	/**
     * Default constructor
     */
    public CommonServiceImpl() {
    }

	/**
	 * 日志对象
	 */
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 日期格式化对象
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	
    /**
     * 获取编号
     * @param vo
     * @return
     * @Description 
     * @version 2017年2月16日 下午1:24:22<br/>
     * @author 王通<br/>
     */
	@Override
    public String getNo(CommonVo vo) {
    	if ( vo == null ) {
    		return null;
		}
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		vo.setOrgId(loginUser.getOrgId());
		String fieldName = vo.getFieldName();
		String dateString = sdf.format(new Date());
		RedissonClient client = RedisTool.lock(RedisConfig.host, RedisConfig.port, RedisConfig.password, Integer.parseInt(RedisConfig.db), "lock_common_no_"+fieldName);
		try {
			Integer seq = this.dao.getSeq(vo);
			if (seq == 0) {
				this.dao.addSeq(vo);
				seq = this.dao.getSeq(vo);
			}
			String seqStr = StringUtil.getString(new BigDecimal(seq), 7);
			if (vo.getContainDate()) {
				return fieldName + dateString + seqStr;
			} else {
				return fieldName + seqStr;
			}
		} finally {
			RedisTool.unLock(client, "lock_common_no_"+fieldName);
		}
    }
	
	public String getNo(CommonVo vo,Integer len){
		if ( vo == null ) {
    		return null;
		}
		// 获取当前用户信息
		Principal loginUser = LoginUtil.getLoginUser();
		if (loginUser == null) {
			throw new BizException("valid_common_user_no_login");
		}
		vo.setWarehouseId(LoginUtil.getWareHouseId());
		vo.setOrgId(loginUser.getOrgId());
		String fieldName = vo.getFieldName();
		String dateString = sdf.format(new Date());
		Integer seq = this.dao.getSeq(vo);
		if (seq == 0) {
			this.dao.addSeq(vo);
			seq = this.dao.getSeq(vo);
		}
		String seqStr = StringUtil.getString(new BigDecimal(seq), len);
		if (vo.getContainDate()) {
			return fieldName + dateString + seqStr;
		} else {
			return fieldName + seqStr;
		}
	}
}