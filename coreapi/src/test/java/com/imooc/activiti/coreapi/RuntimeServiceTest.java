package com.imooc.activiti.coreapi;


import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RuntimeServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    //使用流程实例KEY启动流程
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcess(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = 【{}】", processInstance);
    }

    //使用流程实例ID启动流程
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessById(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule
                .getRepositoryService()
                .createProcessDefinitionQuery().singleResult();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
        LOGGER.info("processInstance = 【{}】", processInstance);
    }

    //使用ProcessInstanceBuilder启动流程
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceBuilder(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("my-process")
                .variables(variables)
                .start();
        LOGGER.info("processInstance = 【{}】", processInstance);
    }

    //对流程实例变量进行操作
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testVariables(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = 【{}】", processInstance);
        runtimeService.setVariable(processInstance.getId(),"key3","value3");
        runtimeService.setVariable(processInstance.getId(),"key2","value2-1");
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("variables1 = 【{}】", variables1);
    }

    //对流程实例进行查询
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceQuery(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance = 【{}】", processInstance);

        ProcessInstance processInstance1 = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionId(processInstance.getId())
                .singleResult();

        LOGGER.info("processInstance1 = 【{}】", processInstance1);

    }

    //对流程执行对象进行查询
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance = 【{}】", processInstance);

        List<Execution> executionList = runtimeService
                .createExecutionQuery()
                .listPage(0, 100);

        executionList.forEach(execution ->
                LOGGER.info("execution = 【{}】",execution));
    }

    //流程触发Trigger
    @Test
    @Deployment(resources = {"my-process-trigger.bpmn20.xml"})
    public void testTrigger(){
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();

        LOGGER.info("execution = 【{}】", execution);
        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = 【{}】", execution);
    }

    @Test
    @Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
    public void testSignalEventReceived(){

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = 【{}】",execution);

        runtimeService.signalEventReceived("my-signal");

        execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = 【{}】",execution);
    }

    @Test
    @Deployment(resources = {"my-process-message-received.bpmn20.xml"})
    public void testMessageEventReceived(){

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = 【{}】",execution);

        runtimeService.messageEventReceived("my-message",execution.getId());

        execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = 【{}】",execution);
    }

    @Test
    @Deployment(resources = {"my-process-message.bpmn20.xml"})
    public void testMessageStart(){

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage("my-message");
        LOGGER.info("processInstance = 【{}】", processInstance);
    }
}
