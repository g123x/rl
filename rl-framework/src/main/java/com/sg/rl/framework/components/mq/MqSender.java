package com.sg.rl.framework.components.mq;

import com.sg.rl.framework.components.base.ComponentInterface;
import com.sg.rl.framework.components.mq.config.MQConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(MQConfig.class)
public class MqSender implements ComponentInterface {
}
