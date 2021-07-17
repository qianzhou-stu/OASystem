package com.andreas.oa.pojo.request;


/**
 * 描述：登录请求参数类
 */
public class LoginRequest {
    private String username;
    private String password;
    private Integer departmentId;

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    private Integer majorId;
    private String sex;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
