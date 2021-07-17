package com.andreas.oa.dao;

import com.andreas.oa.pojo.Node;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NodeMapper {
    int deleteByPrimaryKey(Long nodeId);

    int insert(Node record);

    int insertSelective(Node record);

    Node selectByPrimaryKey(Long nodeId);

    int updateByPrimaryKeySelective(Node record);

    int updateByPrimaryKey(Node record);

    List<Node> selectNode(@Param("userId") Long userId);
}