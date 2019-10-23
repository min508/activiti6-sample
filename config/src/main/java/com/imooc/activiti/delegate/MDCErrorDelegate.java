package com.imooc.activiti.delegate;

import com.imooc.activiti.DemoMain;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MDCErrorDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        LOGGER.info("run MDCErrorDelegate！");
        throw new RuntimeException("only test");
    }
}
