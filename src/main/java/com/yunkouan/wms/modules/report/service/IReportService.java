package com.yunkouan.wms.modules.report.service;

import java.util.*;
import com.yunkouan.wms.modules.report.entity.InboundVo;
import com.yunkouan.wms.modules.report.entity.OutboundVo;
import com.yunkouan.wms.modules.report.entity.StockVo;

/**
 * 报表服务接口
 */
public interface IReportService {

    /**
     * 入库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<InboundVo> inboundList(InboundVo vo);

    /**
     * 出库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<OutboundVo> outboundList(OutboundVo vo);

    /**
     * 在库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<StockVo> stockList(StockVo vo);

}