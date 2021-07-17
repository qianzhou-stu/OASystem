package com.andreas.oa.service;

import com.andreas.oa.pojo.Department;
import com.andreas.oa.pojo.Employee;
import com.andreas.oa.pojo.Major;
import com.andreas.oa.pojo.User;
import sun.jvm.hotspot.oops.LongField;

import java.util.List;

/**
 * 描述：
 */
public interface UserService {
    /*查询出所有的学院*/
    List<Department> selectDepartments();

    /*查询出所有的专业*/
    List<Major> selectMajors();

    /*通过用户名查询出用户*/
    User selectUserByName(String username);

    /*检查登录*/
    User checkLogin(String username, String password, Long departmentId, Long majorId, String sex);

    /*查询出当前用户所对应的employee*/
    Employee selectEmployeeByEmployeeId(Long employeeId);


    /*查询出当前用户所在的部门*/
    Department selectDepartmentByDepartmentId(Long departmentId);

    /*查询出当前用户所在的专业*/
    Major selectMajorByMajorId(Long majorId);
}
