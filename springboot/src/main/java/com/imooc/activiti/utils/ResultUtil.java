package com.imooc.activiti.utils;

import com.imooc.activiti.entity.Msg;

public class ResultUtil {

    /**
     * 请求成功返回
     * @param object
     * @return
     */
    public static Msg success(Object object){
        Msg msg=new Msg();
        msg.setState(1);
        msg.setMsg("请求成功");
        msg.setData(object);
        return msg;
    }
    public static Msg success(){
        return success(null);
    }

    public static Msg error(String resultmsg){
        Msg msg=new Msg();
        msg.setState(0);
        msg.setMsg(resultmsg);
        return msg;
    }

}
