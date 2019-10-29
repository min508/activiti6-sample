package com.imooc.activiti.controller;

import com.imooc.activiti.entity.Msg;
import com.imooc.activiti.utils.ResultUtil;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/flowable")
public class FlowableController {

    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    @Resource
    private ProcessEngine processEngine;

    /**
     * .提交采购订单的审批请求
     *
     * @param userId 用户id
     */
    @PostMapping("/start/{userId}/{purchaseOrderId}")
    public Msg startFlow(@PathVariable String userId, @PathVariable String purchaseOrderId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("purchaseOrderId", purchaseOrderId);
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("OrderApproval",map);
        String processId = processInstance.getId();
        String name = processInstance.getName();
        return ResultUtil.success(processId + ":" + name);
    }

    /**
     * .获取用户的任务
     *
     * @param userId 用户id
     */
    @GetMapping("/getTasks/{userId}")
    public Msg getTask(@PathVariable String userId){
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().desc().listPage(0, 100);
        return ResultUtil.success(tasks.toString());
    }
}
