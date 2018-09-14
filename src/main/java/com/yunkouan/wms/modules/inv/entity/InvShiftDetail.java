package com.yunkouan.wms.modules.inv.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yunkouan.base.BaseEntity;
import com.yunkouan.valid.ValidSave;
import com.yunkouan.valid.ValidUpdate;

public class InvShiftDetail  extends BaseEntity {
    /**
	 * 
	 * @version 2017年2月20日 上午9:58:29<br/>
	 * @author 王通<br/>
	 */
	private static final long serialVersionUID = 6168720963673740625L;
	
	/**
	 * 主键ID
	 */	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String shiftDetailId;
	
	/**
	 * 主移位单ID
	 */
    private String shiftId;
    
    /**
     * 货品ID
     */
    @Length(max=64,message="{valid_shift_sku_id_out_length}",groups={ValidSave.class,ValidUpdate.class})
	@NotNull(message="{valid_shift_sku_id_is_null}",groups={ValidSave.class,ValidUpdate.class})
    private String skuId;

    /**
     * 批次号
     */
    @Length(max=32,message="{valid_shift_batch_no_out_length}",groups={ValidSave.class,ValidUpdate.class})
    private String batchNo;

    /**
     * 收货单id
     */
    @Length(max=64,message="{valid_shift_asn_id_out_length}",groups={ValidSave.class,ValidUpdate.class})
    private String asnId;
    /**
     * 收货单详情id
     */
    @Length(max=64,message="{valid_shift_asn_detail_id_out_length}",groups={ValidSave.class,ValidUpdate.class})
    private String asnDetailId;

    
    /**
     * 包装ID
     */
    @Length(max=64,message="{valid_shift_batch_no_out_length}",groups={ValidSave.class,ValidUpdate.class})
     private String packId;

    /**
     * 出库库位
     */
    @Length(max=64,message="{valid_shift_out_location_out_length}",groups={ValidSave.class,ValidUpdate.class})
	@NotNull(message="{valid_shift_out_location_is_null}",groups={ValidSave.class,ValidUpdate.class})
    private String outLocation;

    /**
     * 入库库位
     */
    @Length(max=64,message="{valid_shift_in_location_out_length}",groups={ValidSave.class,ValidUpdate.class})
	@NotNull(message="{valid_shift_in_location_is_null}",groups={ValidSave.class,ValidUpdate.class})
    private String inLocation;

    /**
     * 库存数量
     */
    @Max(value=999999999,message="{valid_shift_stock_qty_max}",groups={ValidSave.class,ValidUpdate.class})
	@NotNull(message="{valid_shift_shift_qty_is_null}",groups={ValidSave.class,ValidUpdate.class})
    private Double stockQty;
    /**
     * 移位数量
     */
    @Max(value=999999999,message="{valid_shift_shift_qty_max}",groups={ValidSave.class,ValidUpdate.class})
	@NotNull(message="{valid_shift_shift_qty_is_null}",groups={ValidSave.class,ValidUpdate.class})
    private Double shiftQty;
    
    /**
     * 机构ID
     */
	private String orgId;

    /**
     * 仓库ID
     */
	private String warehouseId;

    /**
     * 实际移位数量
     */
    @Max(value=999999999,message="{valid_shift_real_shift_qty_out_length}",groups={ValidSave.class,ValidUpdate.class})
	private Double realShiftQty;

    /**
     * 入库日期
     */
   	@NotNull(message="{valid_shift_in_date_is_null}",groups={ValidUpdate.class})
    private Date inDate;

    /**
     * 自增ID
     */
    private Integer shiftDetailId2;

    public String getShiftDetailId() {
        return shiftDetailId;
    }

    public void setShiftDetailId(String shiftDetailId) {
        this.shiftDetailId = shiftDetailId == null ? null : shiftDetailId.trim();
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId == null ? null : shiftId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId == null ? null : packId.trim();
    }

    public String getOutLocation() {
        return outLocation;
    }

    public void setOutLocation(String outLocation) {
        this.outLocation = outLocation == null ? null : outLocation.trim();
    }

    public String getInLocation() {
        return inLocation;
    }

    public void setInLocation(String inLocation) {
        this.inLocation = inLocation == null ? null : inLocation.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId == null ? null : warehouseId.trim();
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public Integer getShiftDetailId2() {
        return shiftDetailId2;
    }

    public void setShiftDetailId2(Integer shiftDetailId2) {
        this.shiftDetailId2 = shiftDetailId2;
    }

	/**
	 * 属性 asnId getter方法
	 * @return 属性asnId
	 * @author 王通<br/>
	 */
	public String getAsnId() {
		return asnId;
	}

	/**
	 * 属性 asnId setter方法
	 * @param asnId 设置属性asnId的值
	 * @author 王通<br/>
	 */
	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}

	/**
	 * 属性 asnDetailId getter方法
	 * @return 属性asnDetailId
	 * @author 王通<br/>
	 */
	public String getAsnDetailId() {
		return asnDetailId;
	}

	/**
	 * 属性 asnDetailId setter方法
	 * @param asnDetailId 设置属性asnDetailId的值
	 * @author 王通<br/>
	 */
	public void setAsnDetailId(String asnDetailId) {
		this.asnDetailId = asnDetailId;
	}

	public Double getStockQty() {
		return stockQty;
	}
	

	public void setStockQty(Double stockQty) {
		this.stockQty = stockQty;
	}
	

	public Double getShiftQty() {
		return shiftQty;
	}
	

	public void setShiftQty(Double shiftQty) {
		this.shiftQty = shiftQty;
	}
	

	public Double getRealShiftQty() {
		return realShiftQty;
	}
	

	public void setRealShiftQty(Double realShiftQty) {
		this.realShiftQty = realShiftQty;
	}
	
}