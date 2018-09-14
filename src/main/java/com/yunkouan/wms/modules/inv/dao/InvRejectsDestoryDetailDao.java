package com.yunkouan.wms.modules.inv.dao;

import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetailExample;
import com.yunkouan.wms.modules.inv.entity.InvShift;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InvRejectsDestoryDetailDao extends Mapper<InvRejectsDestoryDetail> {
    int countByExample(InvRejectsDestoryDetailExample example);

    int deleteByExample(InvRejectsDestoryDetailExample example);

    int deleteByPrimaryKey(String rejectsDestoryDetailId);

    int insert(InvRejectsDestoryDetail record);

    int insertSelective(InvRejectsDestoryDetail record);

    List<InvRejectsDestoryDetail> selectByExample(InvRejectsDestoryDetailExample example);

    InvRejectsDestoryDetail selectByPrimaryKey(String rejectsDestoryDetailId);

    int updateByExampleSelective(@Param("record") InvRejectsDestoryDetail record, @Param("example") InvRejectsDestoryDetailExample example);

    int updateByExample(@Param("record") InvRejectsDestoryDetail record, @Param("example") InvRejectsDestoryDetailExample example);

    int updateByPrimaryKeySelective(InvRejectsDestoryDetail record);

    int updateByPrimaryKey(InvRejectsDestoryDetail record);

	void emptyDetail(String rejectId);
}