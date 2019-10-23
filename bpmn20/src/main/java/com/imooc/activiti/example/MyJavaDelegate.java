package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class MyJavaDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = -6184305077357890876L;

    public static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {
        if(name != null){
            Object nameValue = name.getValue(execution);
            LOGGER.info("name = 【{}】", nameValue);
        }

        if(desc != null){
            Object descValue = desc.getValue(execution);
            LOGGER.info("desc = 【{}】", descValue);
        }
        LOGGER.info("run my java delegate：【{}】", this);

    }
}
