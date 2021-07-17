package com.andreas.oa.controller;

import com.andreas.oa.common.Constant;
import com.andreas.oa.exception.BussinessException;
import com.andreas.oa.pojo.LeaveForm;
import com.andreas.oa.pojo.User;
import com.andreas.oa.service.LeaveFormService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：请假单controller
 */
@Controller
public class LeaveFormController {
    private Logger logger = LoggerFactory.getLogger(LeaveFormController.class);
    @Resource
    private LeaveFormService leaveFormServiceImpl;

    /*
     * 创建请假单 前端传入数据
     * */
    @PostMapping("/leave/create")
    @ResponseBody
    public Map create(@RequestParam("formType") String formType, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("reason") String reason, HttpSession session) {
        Map map = new HashMap();
        /*对参数进行校验，这个参数进行校验，校验参数的值是不是没有传入,对前端传入的数据进行校验。
         * 如果是不合法的校验的数据，那么请求到的是错误的数据。下面是参数进行一个校验的过程。
         * */
        if (formType == null) {
            map.put("code", 3002);
            map.put("msg", "请填写请假类型");
            return map;
        }
        if (startTime == null) {
            map.put("code", 3003);
            map.put("msg", "请填写请假开始时间");
            return map;
        }
        if (endTime == null) {
            map.put("code", 3004);
            map.put("msg", "请填写请假结束时间");
            return map;
        }
        if (reason == null) {
            map.put("code", 3005);
            map.put("msg", "请填写请假理由");
            return map;
        }
        User user = (User) session.getAttribute(Constant.USER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        try {
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(user.getEmployeeId());
            form.setStartTime(sdf.parse(startTime));
            form.setEndTime(sdf.parse(endTime));
            form.setFormType(Integer.parseInt(formType));
            form.setReason(reason);
            form.setCreateTime(new Date());
            //2. 调用业务逻辑方法
            leaveFormServiceImpl.createLeaveForm(form);
            map.put("code", 1000);
            map.put("msg", "success");
        } catch (BussinessException e) {
            logger.error("请假申请异常", e);
            map.put("code", e.getCode());
            map.put("msg", e.getMsg());
        } catch (ParseException e) {
            logger.error("请假申请异常", e);
            map.put("code", 3006);
            map.put("msg", "parse error");
        }
        return map;
    }

    /*查询需要审核的请假单列表*/
    @GetMapping("/leave/list")
    @ResponseBody
    public Map getLeaveFormList(HttpSession session) {
        User user = (User) session.getAttribute(Constant.USER);
        List<Map> formList = leaveFormServiceImpl.getLeaveFormList("process", user.getEmployeeId());
        Map map = new HashMap();
        map.put("code", "0");
        map.put("msg", "");
        map.put("count", formList.size());
        map.put("data", formList);
        return map;
    }

    /*
     * 处理审批操作
     * */
    @PostMapping("/leave/audit")
    @ResponseBody
    public Map audit(@RequestParam("formId") Long formId, @RequestParam("result") String result, @RequestParam("reason") String reason, HttpSession session) {
        System.out.println(formId+result+reason);
        Map map = new HashMap();
        User user = (User) session.getAttribute(Constant.USER);
        try {
            leaveFormServiceImpl.audit(formId, user.getEmployeeId(), result, reason);
            map.put("code", "0");
            map.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            logger.error("请假单审核失败", e);
            map.put("code", e.getCode());
            map.put("msg", e.getMsg());
        }
        return map;
    }
}
