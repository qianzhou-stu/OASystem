package com.andreas.oa.service;

import com.andreas.oa.pojo.Node;
import com.andreas.oa.pojo.User;

import java.util.List;

/**
 * 描述：系统主页service
 */
public interface MainService {
    List<Node> selectNode(Long userId);
}
