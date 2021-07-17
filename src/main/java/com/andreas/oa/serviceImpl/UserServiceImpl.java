package com.andreas.oa.serviceImpl;

import com.andreas.oa.dao.DepartmentMapper;
import com.andreas.oa.dao.EmployeeMapper;
import com.andreas.oa.dao.MajorMapper;
import com.andreas.oa.dao.UserMapper;
import com.andreas.oa.exception.BussinessException;
import com.andreas.oa.pojo.Department;
import com.andreas.oa.pojo.Employee;
import com.andreas.oa.pojo.Major;
import com.andreas.oa.pojo.User;
import com.andreas.oa.service.UserService;
import com.andreas.oa.utils.Md5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public List<Department> selectDepartments() {
        return departmentMapper.selectDepartment();
    }

    @Override
    public List<Major> selectMajors() {
        return majorMapper.selectMajors();
    }

    @Override
    public User checkLogin(String username, String password, Long departmentId, Long majorId, String sex) {
        User user = userMapper.selectUsername(username);
        if (user == null) {
            throw new BussinessException(1006, "用户不存在");
        }
        String md5Digest = Md5Utils.md5Digest(password, user.getSalt());
        if (!user.getPassword().equals(md5Digest)) {
            throw new BussinessException(1007, "密码错误");
        }
        Long employeeId = user.getEmployeeId();
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        Long d_id = employee.getDepartmentId();
        if (!departmentId.equals(d_id)) {
            throw new BussinessException(1008, "学院名称有误");
        }
        Long m_id = employee.getMajorId();
        if (!majorId.equals(m_id)) {
            throw new BussinessException(1009, "专业名称有误");
        }
        if (!sex.equals(employee.getTitle())) {
            throw new BussinessException(10010, "角色名称有误");
        }

//        Long employeeId = user.getEmployeeId();
//        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
//        Long departmentId = employee.getDepartmentId();
//        Department department = departmentMapper.selectByPrimaryKey(departmentId);
//        Long majorId = employee.getMajorId();
//        Major major = majorMapper.selectByPrimaryKey(majorId);
//        if (!departmentName.equals(department.getDepartmentName())) {
//            throw new BussinessException(1008, "学院名称有误");
//        }
//        if (!majorName.equals(major.getMajorName())) {
//            throw new BussinessException(1009, "专业名称有误");
//        }
//        if (!sex.equals(employee.getTitle())) {
//            throw new BussinessException(10010, "角色名称有误");
//        }
        return user;
    }

    @Override
    public User selectUserByName(String username) {
        User user = userMapper.selectUsername(username);
        return user;
    }

    @Override
    public Employee selectEmployeeByEmployeeId(Long employeeId) {
        return employeeMapper.selectByPrimaryKey(employeeId);
    }

    @Override
    public Department selectDepartmentByDepartmentId(Long departmentId) {
        return departmentMapper.selectByPrimaryKey(departmentId);
    }

    @Override
    public Major selectMajorByMajorId(Long majorId) {
        return majorMapper.selectByPrimaryKey(majorId);
    }
}
