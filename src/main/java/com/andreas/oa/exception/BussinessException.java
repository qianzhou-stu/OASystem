package com.andreas.oa.exception;

/**
 * 描述：自定义业务的处理异常类
 */
public class BussinessException extends RuntimeException{
    private Integer code;
    private String msg;

    public BussinessException(Integer code, String msg) {
        super(code+":"+msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
