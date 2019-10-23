package com.imooc.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        //对流程开始进行监听
        if (ActivitiEventType.PROCESS_STARTED.equals(eventType)){
            LOGGER.info("流程启动【{}】 \t 【{}】",eventType,event.getProcessDefinitionId());
        //对流程结束进行监听
        }else if (ActivitiEventType.PROCESS_COMPLETED.equals(eventType)){
            LOGGER.info("流程结束【{}】 \t 【{}】",eventType,event.getProcessDefinitionId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
