package com.imooc.activiti.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigDbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDbTest.class);

    @Test
    public void testConfig1(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        LOGGER.info("configuration = {}",configuration);
        //引擎配置的时候需要把流程引擎配置出来
        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎{}", processEngine.getName());
        //正常的关闭流程引擎
        processEngine.close();
    }

    @Test
    public void testConfig2(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");
        LOGGER.info("configuration = {}",configuration);
        //引擎配置的时候需要把流程引擎配置出来
        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎{}", processEngine.getName());
        //正常的关闭流程引擎
        processEngine.close();
    }
}
