package com.yunkouan.wms.modules.inv.dao;

import com.yunkouan.wms.modules.inv.entity.InvRejectsDestory;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryDetail;
import com.yunkouan.wms.modules.inv.entity.InvRejectsDestoryExample;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InvRejectsDestoryDao  extends Mapper<InvRejectsDestory> {
    int countByExample(InvRejectsDestoryExample example);

    int deleteByExample(InvRejectsDestoryExample example);

    int deleteByPrimaryKey(String rejectsDestoryId);

    int insert(InvRejectsDestory record);

    int insertSelective(InvRejectsDestory record);

    List<InvRejectsDestory> selectByExample(InvRejectsDestoryExample example);

    InvRejectsDestory selectByPrimaryKey(String rejectsDestoryId);

    int updateByExampleSelective(@Param("record") InvRejectsDestory record, @Param("example") InvRejectsDestoryExample example);

    int updateByExample(@Param("record") InvRejectsDestory record, @Param("example") InvRejectsDestoryExample example);

    int updateByPrimaryKeySelective(InvRejectsDestory record);

    int updateByPrimaryKey(InvRejectsDestory record);
}