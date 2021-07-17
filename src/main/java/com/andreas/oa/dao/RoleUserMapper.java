package com.andreas.oa.dao;

import com.andreas.oa.pojo.RoleUser;

public interface RoleUserMapper {
    int deleteByPrimaryKey(Long ruId);

    int insert(RoleUser record);

    int insertSelective(RoleUser record);

    RoleUser selectByPrimaryKey(Long ruId);

    int updateByPrimaryKeySelective(RoleUser record);

    int updateByPrimaryKey(RoleUser record);
}