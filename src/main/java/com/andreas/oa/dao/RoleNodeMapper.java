package com.andreas.oa.dao;

import com.andreas.oa.pojo.RoleNode;

public interface RoleNodeMapper {
    int deleteByPrimaryKey(Long rnId);

    int insert(RoleNode record);

    int insertSelective(RoleNode record);

    RoleNode selectByPrimaryKey(Long rnId);

    int updateByPrimaryKeySelective(RoleNode record);

    int updateByPrimaryKey(RoleNode record);
}