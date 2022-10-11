package com.sg.rl.framework.components.spring;

import com.sg.rl.framework.components.base.ComponentInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnBean(SpringContextComponent.class)
@Slf4j
public class ComponentsPostProcessor implements BeanPostProcessor {


    private Map<String,Object> componentsMap = new HashMap<>();


    @PostConstruct
    public void init(){

        String[] beanNames = SpringContextComponent.getApplicationContext().getBeanNamesForType(ComponentInterface.class);

        for (String beanName:beanNames) {
            componentsMap.put(beanName,new Object());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(componentsMap.containsKey(beanName)){
            log.info("postProcessAfterInitialization component {} load success",beanName);
        }

        return bean;
    }

}
