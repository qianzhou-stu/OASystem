package com.andreas.oa.controller;

import com.andreas.oa.common.Constant;
import com.andreas.oa.exception.BussinessException;
import com.andreas.oa.pojo.Department;
import com.andreas.oa.pojo.Employee;
import com.andreas.oa.pojo.Major;
import com.andreas.oa.pojo.User;
import com.andreas.oa.pojo.request.LoginRequest;
import com.andreas.oa.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.crypto.spec.PSource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：用户controller
 */
@Controller
public class UserController {

    @Resource
    private UserService userServiceImpl;

    @GetMapping("/login")
    public ModelAndView login() {
        /*
         * 显示登录的界面
         * */
        ModelAndView modelAndView = new ModelAndView("/login");
        List<Major> majors = userServiceImpl.selectMajors();
        List<Department> departments = userServiceImpl.selectDepartments();
        modelAndView.addObject("departments", departments);
        modelAndView.addObject("majors", majors);
        return modelAndView;
    }

    @PostMapping("/checkLogin")
    @ResponseBody
    public Map checkLogin(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("departmentId") Long departmentId, @RequestParam("majorId") Long majorId, @RequestParam("sex") String sex, HttpSession session) {
        /*
         * 检测用户是否登录
         * */
        /*1.新建一个map*/
        Map map = new HashMap();
        /*2.获取前端的请求的参数，对前端的请求的参数进行校验*/
//        String username = loginRequest.getUsername();
//        String password = loginRequest.getPassword();
//        Integer departmentId = loginRequest.getDepartmentId();
//        Integer majorId = loginRequest.getMajorId();
//        String sex = loginRequest.getSex();
        if (username == null) {
            map.put("code", 1001);
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (password == null) {
            map.put("code", 1002);
            map.put("msg", "密码不能为空");
            return map;
        }
        if (departmentId == null) {
            map.put("code", 1003);
            map.put("msg", "学院名称不能为空");
            return map;
        }
        if (majorId == null) {
            map.put("code", 1004);
            map.put("msg", "专业不能为空");
            return map;
        }
        if (sex == null) {
            map.put("code", 1005);
            map.put("msg", "角色不能为空");
            return map;
        }
        User user = null;
        try {
            user = userServiceImpl.checkLogin(username, password, departmentId, majorId, sex);
            session.setAttribute(Constant.USER, user);
            Employee employee = userServiceImpl.selectEmployeeByEmployeeId(user.getEmployeeId());
            Department department = userServiceImpl.selectDepartmentByDepartmentId(departmentId);
            Major major = userServiceImpl.selectMajorByMajorId(majorId);
            session.setAttribute(Constant.EMPLOYEE,employee);
            session.setAttribute(Constant.DEPARTMENT,department);
            session.setAttribute(Constant.MAJOR,major);
            map.put("code", 1000);
            map.put("msg", "success");
        }catch (BussinessException e){
            map.put("code",e.getCode());
            map.put("msg",e.getMsg());
        }
        return map;
    }

    @GetMapping("/logout")
    @ResponseBody
    public ModelAndView logout(HttpSession session){
        /*
        * 退出登录--退出登录的本质上是清除session的值，将session的值进行清楚，将session的值清楚为null
        * */
        ModelAndView modelAndView = new ModelAndView("/login");
        session.invalidate();
        List<Major> majors = userServiceImpl.selectMajors();
        List<Department> departments = userServiceImpl.selectDepartments();
        modelAndView.addObject("departments", departments);
        modelAndView.addObject("majors", majors);
        return modelAndView;
    }
}
