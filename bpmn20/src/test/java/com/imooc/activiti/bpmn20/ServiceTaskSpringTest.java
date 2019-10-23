package com.imooc.activiti.bpmn20;

import com.google.common.collect.Maps;
import com.imooc.activiti.example.MyJavaBean;
import com.imooc.activiti.example.MyJavaDelegate;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInitializingList;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti_context.xml")
public class ServiceTaskSpringTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    @Rule
    @Resource
    public ActivitiRule activitiRule;

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask1(){
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process");

        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime().asc()
                .listPage(0, 100);

        historicActivityInstances.forEach(historicActivityInstance -> {
            LOGGER.info("activity = 【{}】", historicActivityInstance);
        });
    }

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask2(){
        Map<String, Object> variables = Maps.newHashMap();

        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        LOGGER.info("myJavaDelegate = 【{}】",myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);

        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime().asc()
                .listPage(0, 100);

        historicActivityInstances.forEach(historicActivityInstance -> {
            LOGGER.info("activity = 【{}】", historicActivityInstance);
        });
    }

    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask5(){
        Map<String, Object> variables = Maps.newHashMap();

        MyJavaBean myJavaBean = new MyJavaBean("TEST");
        LOGGER.info("myJavaBean = 【{}】",myJavaBean);
        variables.put("myJavaBean", myJavaBean);

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);

        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime().asc()
                .listPage(0, 100);

        historicActivityInstances.forEach(historicActivityInstance -> {
            LOGGER.info("activity = 【{}】", historicActivityInstance);
        });
    }
}
