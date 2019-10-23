package com.imooc.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TaskServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message","my test message!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        //对Task设置普通变量
        taskService.setVariable(task.getId(),"key1","value1");
        //对Task设置本地变量
        taskService.setVariableLocal(task.getId(),"localkey1","localvalue1");

        //获取普通变量
        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        //输出：taskServiceVariables = {key1=value1, localkey1=localvalue1, message=my test message!}
        LOGGER.info("taskServiceVariables = {}",taskServiceVariables);

        //获取本地变量
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        //输出：taskServiceVariablesLocal = {localkey1=localvalue1}
        LOGGER.info("taskServiceVariablesLocal = {}",taskServiceVariablesLocal);

        //根据流程去获取变量
        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        //输出：processVariables = {key1=value1, message=my test message!}
        LOGGER.info("processVariables = {}",processVariables);

        //执行流程完成
        Map<String, Object> completeVar = Maps.newHashMap();
        completeVar.put("cKey1","cValue1");
        taskService.complete(task.getId(), completeVar);

        //查询是否已经销毁
        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        //输出：task1 = null
        LOGGER.info("task1 = {}",task1);
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message","my test message!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setOwner(task.getId(), "user1");

        //使用设置assignee的方式设置的话会存在权限上的问题
        /*taskService.setAssignee(task.getId(),"jimmy");*/

        //存在候选人里面，但是这个任务并没有被指定代办人
        List<Task> taskList = taskService
                .createTaskQuery()
                .taskCandidateUser("jimmy")
                .taskUnassigned().listPage(0, 100);

        taskList.forEach(task1 -> {
            //当已存在一个任务有指定的代办人使用claim会报错
            try {
                taskService.claim(task1.getId(),"jimmy");
            }catch (Exception e){
                LOGGER.error(e.getMessage(), e);
            }
        });

        //用户/组与Task之间的关系
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        identityLinksForTask.forEach(identityLink ->
                LOGGER.info("identityLink = {}",identityLink));

        List<Task> jimmys = taskService.createTaskQuery().taskAssignee("jimmy").listPage(0, 100);
        jimmys.forEach(jimmy ->{
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("cKey1","cValue1");
            taskService.complete(jimmy.getId(),vars);
        });

        jimmys = taskService.createTaskQuery().taskAssignee("jimmy").listPage(0, 100);
        LOGGER.info("是否存在 {}", CollectionUtils.isEmpty(jimmys));
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskAttachment(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message","my test message!");
        activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process",variables);
        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();

        taskService.createAttachment("url", task.getId(),
                task.getProcessInstanceId(),"name","desc","/url.test.png");

        List<Attachment> attachments = taskService.getTaskAttachments(task.getId());

        attachments.forEach(attachment ->
                LOGGER.info("taskAttachment = {}",
                        ToStringBuilder.reflectionToString(attachment, ToStringStyle.JSON_STYLE))
        );
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskComment(){
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message","my test message!");

        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceByKey("my-process", variables);

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService.createTaskQuery().singleResult();

        taskService.setOwner(task.getId(),"user1");
        taskService.setAssignee(task.getId(),"jimmy");

        taskService.addComment(task.getId(), processInstance.getId(),"record note 1");
        taskService.addComment(task.getId(), processInstance.getId(),"record note 2");


        List<Comment> taskComments = taskService.getTaskComments(task.getId());

        taskComments.forEach(taskComment ->
                LOGGER.info("taskComment = {}",
                        ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE)));

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());

        taskEvents.forEach(taskEvent ->
                LOGGER.info("taskEvent = {}", ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE)));
    }
}
