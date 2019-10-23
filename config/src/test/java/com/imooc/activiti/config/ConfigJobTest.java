package com.imooc.activiti.config;

import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDbTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"com/imooc/activiti/my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        LOGGER.info("start");
        List<Job> jobs = activitiRule
                .getManagementService()
                .createTimerJobQuery().listPage(0, 100);
        jobs.forEach(job -> LOGGER.info("定时任务【{}】，默认重试次数【{}】",job,job.getRetries()));
        LOGGER.info("jobs.size【{}】",jobs.size());
        Thread.sleep(1000*100);
        LOGGER.info("end");
    }
}
