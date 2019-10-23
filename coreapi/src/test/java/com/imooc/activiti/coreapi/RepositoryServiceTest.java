package com.imooc.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RepositoryServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRespository(){

        RepositoryService repositoryService = activitiRule.getRepositoryService();

        //创建流程部署对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源1")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");

        //执行对应的发布操作,发布完成以后把部署对象和两个流程定义文件都会部署到数据库中
        Deployment deploy = deploymentBuilder.deploy();
        LOGGER.info("deploy= 【{}】", deploy);

        //创建流程部署对象
        DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment();
        deploymentBuilder1.name("测试部署资源2")
                .addClasspathResource("my-process.bpmn20.xml")
                .addClasspathResource("second_approve.bpmn20.xml");

        deploymentBuilder1.deploy();

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deploymentList = deploymentQuery
                /*.deploymentId(deploy.getId())*/
                .orderByDeploymenTime()
                .asc()
                .listPage(0, 100);

        deploymentList.forEach(deployment -> LOGGER.info("deployment = 【{}】",deployment));
        LOGGER.info("deploymentList.size = 【{}】", deploymentList.size());

        List<ProcessDefinition> processDefinitionList = repositoryService
                .createProcessDefinitionQuery()
                /*.deploymentId(deployment.getId())*/
                .orderByProcessDefinitionKey()
                .asc()
                .listPage(0, 100);

        processDefinitionList.forEach(processDefinition ->
                LOGGER.info("processDefinition = 【{}】, version = 【{}】, key = 【{}】, id = 【{}】",
                        processDefinition,
                        processDefinition.getVersion(),
                        processDefinition.getKey(),
                        processDefinition.getId()));

    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testSuspend(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = 【{}】",processDefinition.getId());

        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        try {
            LOGGER.info("开始启动");
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
            LOGGER.info("启动成功");
        }catch (Exception e){
            LOGGER.info("启动失败");
            LOGGER.info(e.getMessage(),e);
        }

        repositoryService.activateProcessDefinitionById(processDefinition.getId());

        LOGGER.info("开始启动");
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动成功");
    }

    @Test
    @org.activiti.engine.test.Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCandidateStarter(){
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id = 【{}】",processDefinition.getId());

        repositoryService.addCandidateStarterUser(processDefinition.getId(),"user");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(),"groupM");

        List<IdentityLink> identityLinksForProcessDefinition = repositoryService
                .getIdentityLinksForProcessDefinition(processDefinition.getId());

        identityLinksForProcessDefinition.forEach(identityLink -> LOGGER.info("identityLink = 【{}】",identityLink));

        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "groupM");
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(),"user");

    }

}
