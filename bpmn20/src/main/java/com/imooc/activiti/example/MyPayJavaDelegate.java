package com.imooc.activiti.example;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

public class MyPayJavaDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = -6184305077357890876L;

    public static final Logger LOGGER = LoggerFactory.getLogger(MyPayJavaDelegate.class);


    @Override
    public void execute(DelegateExecution execution) {

        LOGGER.info("variables = 【{}】", execution.getVariables());

        LOGGER.info("run my pay java delegate：【{}】", this);

        execution.getParent().setVariableLocal("key2", "value2");
        execution.setVariable("key1", "key1");
        execution.setVariable("key3", "key3");

        Object errorflag = execution.getVariable("errorflag");
        if (Objects.equals(errorflag,true)){
            throw new BpmnError("bpmnError");

        }
    }
}
