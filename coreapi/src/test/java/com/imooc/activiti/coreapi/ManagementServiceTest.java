package com.imooc.activiti.coreapi;

import com.imooc.activiti.mapper.MyCustomMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ManagementServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testJobQuery(){

        ManagementService managementService = activitiRule.getManagementService();
        List<Job> jobList = managementService.createTimerJobQuery().listPage(0, 100);
        jobList.forEach(job -> LOGGER.info("job = {}", job));

        JobQuery jobQuery = managementService.createJobQuery();
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
    }

    //通用表查询
    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testTablePageQuery(){
        ManagementService managementService = activitiRule.getManagementService();
        TablePage tablePage = managementService.createTablePageQuery()
                .tableName(managementService.getTableName(ProcessDefinitionEntity.class))
                .listPage(0, 100);

        List<Map<String, Object>> rows = tablePage.getRows();
        rows.forEach(row ->
                LOGGER.info("row = {}", row));
    }

    //执行自定义的SQL查询
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCustomSql(){

        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        ManagementService managementService = activitiRule.getManagementService();
        List<Map<String, Object>> mapList = managementService.executeCustomSql(new AbstractCustomSqlExecution<MyCustomMapper, List<Map<String, Object>>>(MyCustomMapper.class) {
            @Override
            public List<Map<String, Object>> execute(MyCustomMapper o) {
                return o.findAll();
            }
        });
        mapList.forEach(map ->
                LOGGER.info("map = {}", map));

    }

    //执行流程引擎命令
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCommand(){
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        ManagementService managementService = activitiRule.getManagementService();

        ProcessDefinitionEntity processDefinitionEntity = managementService.executeCommand(new Command<ProcessDefinitionEntity>() {
            @Override
            public ProcessDefinitionEntity execute(CommandContext commandContext) {
                ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager()
                        .findLatestProcessDefinitionByKey("my-process");
                return processDefinitionEntity;
            }
        });
        LOGGER.info("processDefinitionEntity = {}", processDefinitionEntity);
    }
}
