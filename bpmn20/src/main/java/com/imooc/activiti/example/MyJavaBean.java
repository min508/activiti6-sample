package com.imooc.activiti.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyJavaBean implements Serializable {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);
    private static final long serialVersionUID = -5879525174394858911L;

    private String name;

    public String getName() {
        LOGGER.info("run getName name；【{}】",name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyJavaBean(){
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    public void sayHello(){
        LOGGER.info("run sayHello");
    }
}
