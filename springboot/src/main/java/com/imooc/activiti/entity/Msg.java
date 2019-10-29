package com.imooc.activiti.entity;

/**
 * 返回数据结构实体
 */

public class Msg<T> {

    /*错误码*/
    private Integer state;

    /*提示信息 */
    private String msg;

    /*具体内容*/
    private T data;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
}
