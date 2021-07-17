package com.andreas.oa.dao;

import com.andreas.oa.pojo.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LeaveFormMapper {
    int deleteByPrimaryKey(Long formId);

    int insert(LeaveForm record);

    int insertSelective(LeaveForm record);

    LeaveForm selectByPrimaryKey(Long formId);

    int updateByPrimaryKeySelective(LeaveForm record);

    int updateByPrimaryKey(LeaveForm record);

    public List<Map> selectByParams(@Param("pfState") String pfState , @Param("operatorId") Long operatorId);

    LeaveForm selectByAllParams(@Param("employeeId") Long employeeId,@Param("formType") Integer formType,@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("reason") String reason, @Param("createTime") Date createTime, @Param("state") String state);

    List<LeaveForm> selectByEmployeeId(@Param("employeeId") Long employeeId);

}