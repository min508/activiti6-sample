package com.imooc.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigEventLogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDbTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlog.cfg.xml");

    @Test
    @Deployment(resources = {"com/imooc/activiti/my-process.bpmn20.xml"})
    public void test(){
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());
        //根据流程实例ID进行查询
        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService()
                .getEventLogEntriesByProcessInstanceId(
                        processInstance.getProcessInstanceId());

        eventLogEntries.forEach(eventLogEntry -> {
            LOGGER.info("eventLog.type = 【{}】,eventLog.data = 【{}】",
                    eventLogEntry.getType(),new String(eventLogEntry.getData()));
        });
        LOGGER.info("eventLog.size = 【{}】", eventLogEntries.size());
    }
}
