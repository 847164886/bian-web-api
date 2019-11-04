package com.che.maintenance.service;

import com.che.pay.mq.AbstractMqSendService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xiaole on 2016/7/5.
 */
@Service
public class MainPayMqService extends AbstractMqSendService {
    @Resource
    private JmsTemplate cheSearchJmsTemplate;

    @Override
    protected JmsTemplate jmsTemplate() {
        return this.cheSearchJmsTemplate;
    }
}
