package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyTakeJavaDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = -6184305077357890876L;

    public static final Logger LOGGER = LoggerFactory.getLogger(MyTakeJavaDelegate.class);


    @Override
    public void execute(DelegateExecution execution) {
       
        LOGGER.info("run my take java delegate：【{}】", this);

    }
}
