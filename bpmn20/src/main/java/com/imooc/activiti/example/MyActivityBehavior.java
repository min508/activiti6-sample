package com.imooc.activiti.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyActivityBehavior implements ActivityBehavior {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("run my activity behavior");

    }
}
