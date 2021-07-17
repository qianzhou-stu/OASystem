package com.andreas.oa.serviceImpl;

import com.andreas.oa.common.Constant;
import com.andreas.oa.dao.NodeMapper;
import com.andreas.oa.dao.UserMapper;
import com.andreas.oa.pojo.Node;
import com.andreas.oa.pojo.User;
import com.andreas.oa.service.MainService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 描述：系统主页serviceImpl
 */
@Service
public class MainServiceImpl implements MainService {
    @Resource
    private NodeMapper nodeMapper;

    @Override
    public List<Node> selectNode(Long userId) {
        return nodeMapper.selectNode(userId);
    }
}
