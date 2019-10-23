package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class HistoryServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_history.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testHistory(){
        HistoryService historyService = activitiRule.getHistoryService();
        ProcessInstanceBuilder processInstanceBuilder = activitiRule
                .getRuntimeService()
                .createProcessInstanceBuilder();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0","value0");
        variables.put("key1","value1");
        variables.put("key2","value2");

        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("tKey1","tValue1");

        //使用processInstanceBuilder开启流程可以创建持久化变量和瞬时变量
        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("my-process")
                .variables(variables)
                //瞬时变量不会存在历史库中
                .transientVariables(transientVariables)
                .start();

        activitiRule.getRuntimeService()
                .setVariable(processInstance.getId(),"key1","value1_1");

        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //activitiRule.getTaskService().complete(task.getId(),variables);
        Map<String, String> properties = Maps.newHashMap();
        properties.put("fkey1","fvalue");
        properties.put("key2","value2_2");

        //通过表单提交的数据会放到historicDetail里面
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);


        //查询历史流程实例对象
        List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery()
                .listPage(0, 100);
        historicProcessInstances.forEach(historicProcessInstance ->
                LOGGER.info("historicProcessInstance = {}", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE)));

        //查询单个活动节点执行信息
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().listPage(0, 100);
        historicActivityInstances.forEach(historicActivityInstance ->
                LOGGER.info("historicActivityInstance = {}",historicActivityInstance));

        //查询用户任务实例信息
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().listPage(0, 100);
        historicTaskInstances.forEach(historicTaskInstance ->
                LOGGER.info("historicTaskInstance = {}", ToStringBuilder.reflectionToString(historicTaskInstance, ToStringStyle.JSON_STYLE)));

        //查询流程或任务变量值的实体
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().listPage(0, 100);
        historicVariableInstances.forEach(historicVariableInstance ->
                LOGGER.info("historicVariableInstance = {}",historicVariableInstance));

        //查询历史流程活动任务详细信息
        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().listPage(0, 100);
        historicDetails.forEach(historicDetail ->
                LOGGER.info("historicDetail = {}",historicDetail));

        //查询流程实例历史日志
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeActivities()
                .includeComments()
                .includeFormProperties()
                .includeTasks()
                .includeFormProperties()
                .includeVariableUpdates()
                .singleResult();

        List<HistoricData> historicDataList = processInstanceHistoryLog.getHistoricData();
        historicDataList.forEach(historicData ->
                LOGGER.info("historicDataList = {}",historicData));

        //通过流程实例ID删除历史操作
        historyService.deleteHistoricProcessInstance(processInstance.getId());
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        LOGGER.info("historicProcessInstance = {}" , historicProcessInstance);
    }
}
