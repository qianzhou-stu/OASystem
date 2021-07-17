package com.andreas.oa.dao;

import com.andreas.oa.pojo.Major;
import com.andreas.oa.pojo.User;

import java.util.List;

public interface MajorMapper {
    int deleteByPrimaryKey(Long majorId);

    int insert(Major record);

    int insertSelective(Major record);

    Major selectByPrimaryKey(Long majorId);

    int updateByPrimaryKeySelective(Major record);

    int updateByPrimaryKey(Major record);

    List<Major> selectMajors();

}