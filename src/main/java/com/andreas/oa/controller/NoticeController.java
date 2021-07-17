package com.andreas.oa.controller;

import com.andreas.oa.common.Constant;
import com.andreas.oa.pojo.Notice;
import com.andreas.oa.pojo.User;
import com.andreas.oa.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：notice的controller
 */
@Controller
public class NoticeController {
    @Resource
    private NoticeService noticeServiceImpl;

    @GetMapping("/notice/list")
    @ResponseBody
    public Map listNotice(HttpSession session) {
        User user = (User) session.getAttribute(Constant.USER);
        List<Notice> noticeList = noticeServiceImpl.getNoticeList(user.getEmployeeId());
        Map map = new HashMap();
        map.put("code", "0");
        map.put("msg", "");
        map.put("count", noticeList.size());
        map.put("data", noticeList);
        return map;
    }
}
