package com.yunkouan.wms.modules.send.vo;

import org.apache.commons.lang3.StringUtils;

import com.yunkouan.base.BaseVO;
import com.yunkouan.util.StringUtil;
import com.yunkouan.wms.modules.meta.vo.MaterialVo;
import com.yunkouan.wms.modules.send.entity.SendDeliveryDetail;
import com.yunkouan.wms.modules.send.entity.SendDeliveryMaterial;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;



public class SendDeliveryMaterialVo extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7516518895921074411L;

	private SendDeliveryMaterial sendDeliveryMaterial = new SendDeliveryMaterial();
	
	private MaterialVo materialVo = new MaterialVo();
	
	private Example example;
	
	public SendDeliveryMaterialVo(){
		this.example = new Example(SendDeliveryMaterial.class);
		this.example.createCriteria();
		this.sendDeliveryMaterial = new SendDeliveryMaterial();
	}

	public SendDeliveryMaterial getSendDeliveryMaterial() {
		return sendDeliveryMaterial;
	}

	public void setSendDeliveryMaterial(SendDeliveryMaterial sendDeliveryMaterial) {
		this.sendDeliveryMaterial = sendDeliveryMaterial;
	}
	

	public MaterialVo getMaterialVo() {
		return materialVo;
	}

	public void setMaterialVo(MaterialVo materialVo) {
		this.materialVo = materialVo;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}
	
	public Example getConditionExample(){
		Criteria c = example.getOredCriteria().get(0);
		if(!StringUtil.isTrimEmpty(sendDeliveryMaterial.getDeliveryId())){
			c.andEqualTo("deliveryId",sendDeliveryMaterial.getDeliveryId());
		}
		if(!StringUtil.isTrimEmpty(sendDeliveryMaterial.getMaterialId())){
			c.andEqualTo("materialId",sendDeliveryMaterial.getMaterialId());
		}
		if(StringUtils.isNotEmpty(sendDeliveryMaterial.getOrgId())){
			c.andEqualTo("orgId",sendDeliveryMaterial.getOrgId());
		}
		if(StringUtils.isNotEmpty(sendDeliveryMaterial.getWarehouseId())){
			c.andEqualTo("warehouseId",sendDeliveryMaterial.getWarehouseId());
		}
		return example;
	}
	
	
}
