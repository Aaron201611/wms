package com.yunkouan.wms.modules.report.service;

import java.util.*;
import com.yunkouan.wms.modules.report.dao.IReportDao;
import com.yunkouan.wms.modules.report.entity.InboundVo;
import com.yunkouan.wms.modules.report.entity.OutboundVo;
import com.yunkouan.wms.modules.report.entity.StockVo;

/**
 * 报表服务实现类
 */
public class ReportServiceImpl implements IReportService {

    /**
     * Default constructor
     */
    public ReportServiceImpl() {
    }

    /**
     * 
     */
    private IReportDao dao;

    /**
     * 入库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<InboundVo> inboundList(InboundVo vo) {
        // TODO implement here
        return null;
    }

    /**
     * 出库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<OutboundVo> outboundList(OutboundVo vo) {
        // TODO implement here
        return null;
    }

    /**
     * 在库列表数据查询
     * @param vo 
     * @param page 
     * @return
     */
    public Set<StockVo> stockList(StockVo vo) {
        // TODO implement here
        return null;
    }

}