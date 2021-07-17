package com.andreas.oa.controller;

import com.andreas.oa.common.Constant;
import com.andreas.oa.pojo.*;
import com.andreas.oa.service.MainService;
import com.andreas.oa.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 描述：系统主页controller
 */
@Controller
public class MainController {
    @Resource
    private MainService mainServiceImpl;
    @Resource
    private UserService userServiceImpl;

    @GetMapping("/index")
    public ModelAndView showIndex(HttpSession session) {
        /*
        * 显示主页面
        * */
        ModelAndView modelAndView = new ModelAndView("/index");
        User user = (User) session.getAttribute(Constant.USER);
        if (user==null){
            ModelAndView loginView = new ModelAndView("/login");
            List<Major> majors = userServiceImpl.selectMajors();
            List<Department> departments = userServiceImpl.selectDepartments();
            loginView.addObject("departments", departments);
            loginView.addObject("majors", majors);
            return loginView;
        }
        modelAndView.addObject("user", user);
        Long userId = user.getUserId();
        List<Node> nodes = mainServiceImpl.selectNode(userId);
        modelAndView.addObject("nodes",nodes);
        Long employeeId = user.getEmployeeId();
        Employee employee = userServiceImpl.selectEmployeeByEmployeeId(employeeId);
        Long departmentId = employee.getDepartmentId();
        Department department = userServiceImpl.selectDepartmentByDepartmentId(departmentId);
        modelAndView.addObject("department", department);
        Long majorId = employee.getMajorId();
        Major major = userServiceImpl.selectMajorByMajorId(majorId);
        modelAndView.addObject("major", major);
        return modelAndView;
    }

    @GetMapping("/forward/notice")
    public ModelAndView notice(){
        ModelAndView modelAndView = new ModelAndView("/notice");
        return modelAndView;
    }

    @GetMapping("/forward/form")
    public ModelAndView form(){
        ModelAndView modelAndView = new ModelAndView("/form");
        return modelAndView;
    }
    @GetMapping("/forward/audit")
    public ModelAndView audit(){
        ModelAndView modelAndView = new ModelAndView("/audit");
        return modelAndView;
    }
}
