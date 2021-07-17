package com.andreas.oa.dao;

import com.andreas.oa.pojo.ProcessFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessFlowMapper {
    int deleteByPrimaryKey(Long processId);

    int insert(ProcessFlow record);

    int insertSelective(ProcessFlow record);

    ProcessFlow selectByPrimaryKey(Long processId);

    int updateByPrimaryKeySelective(ProcessFlow record);

    int updateByPrimaryKey(ProcessFlow record);

    List<ProcessFlow> selectByFormId(@Param("formId") Long formId);
}