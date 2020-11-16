package com.xhs.addressbook.bean;

/**
 * Created by zhf on 2018/10/19 0019.
 */

public class BaseJavaEntry<T> {

    private boolean success;


    private String msg;
    private String message;


    private int code;


    private T data;


    public BaseJavaEntry() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
