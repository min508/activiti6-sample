package com.imooc.activiti.dbentity;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class DbconfigTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(DbconfigTest.class);

    @Test
    public void testDbconfig(){

        //创建流程引擎对象
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();
        //获取数据库表中的数量
        Map<String, Long> tableCount = managementService.getTableCount();
        //获取数据库表名称
        ArrayList<String> tablesNames = Lists.newArrayList(tableCount.keySet());
        Collections.sort(tablesNames);
        tablesNames.forEach(tablesName -> LOGGER.info("数据库表名称： 【{}】",tablesName));
        LOGGER.info("数据库表的数量:【{}】",tablesNames.size());
    }

    @Test
    public void dropTable(){
        //创建流程引擎对象
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                LOGGER.info("删除表结构");
                return null;
            }
        });
    }

}
