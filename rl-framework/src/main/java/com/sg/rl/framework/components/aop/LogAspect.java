package com.sg.rl.framework.components.aop;


import com.alibaba.fastjson.JSONObject;
import com.sg.rl.common.utils.IPAddressUtils;
import com.sg.rl.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


/**
 * @title : log AOP
 * @describle :
 * <p>
 * Create By gaoxing
 * @date 2021/11/25
 */
@Aspect
@Component
@Order(6)
public class LogAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static class LogLocalData{
        private String restApi;
        private Long startTime;

        public String getRestApi() {
            return restApi;
        }

        public void setRestApi(String restApi) {
            this.restApi = restApi;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }
    }

    ThreadLocal<LogLocalData> logData = new ThreadLocal<LogLocalData>();

    /**
     *
     * web log
     */
    //@Pointcut("execution(public * com.sg.rl..*.controller..*.*(..))")
    @Pointcut("execution(public * com.sg.rl.controller..*.*(..))")
    public void webLog() {
    }


    /**
     * request webLog
     *
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        LogLocalData localData = new LogLocalData();
        localData.setStartTime(System.currentTimeMillis());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(null != requestAttributes){
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            if (null != joinPoint && null != attributes) {
                HttpServletRequest request = attributes.getRequest();

                StringBuilder sb = new StringBuilder();
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()){
                    String key = headerNames.nextElement();
                    String val = request.getHeader(key);
                    sb.append(key).append("=").append(val).append("\n");
                }
                logger.debug("【before-request-headers】：{}",sb.toString());
                logger.debug("【before-request-URL】：{}", request.getRequestURL().toString());
                logger.debug("【before-request-method】:{}", request.getMethod());
                logger.debug("【before-request IP】:{}", IPAddressUtils.getIpAddress(request));
                if(null != joinPoint.getArgs()){
                    String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                    int parmNo = 0;
                    for (Object object:joinPoint.getArgs()) {
                        if(!(object instanceof HttpServletResponse)  && !(object instanceof HttpServletRequest) ){
                            logger.debug("【before-request】 【parm_name】: {} 【parm_value】: {}",parameterNames[parmNo++], JSONObject.toJSONString(object));
                        }
                    }
                }
                localData.setRestApi(request.getRequestURL().toString());
            }
        }
        else {
            if(null!= joinPoint && null != joinPoint.getSignature()){
                String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                int parmNo = 0;
                logger.debug("before-request package {} method {}",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName());
                if(!StringUtils.isEmpty(joinPoint.getSignature().getDeclaringTypeName())
                        && !StringUtils.isEmpty(joinPoint.getSignature().getName())){
                    String method = joinPoint.getSignature().getDeclaringTypeName() + joinPoint.getSignature().getName();
                    if(null != joinPoint.getArgs()){
                        for (Object object:joinPoint.getArgs()) {
                            logger.debug("【before-request】 【method】 {} 【parm_name】: {} 【parm_value】: {}",method,parameterNames[parmNo++],JSONObject.toJSONString(object));
                        }
                    }
                    localData.setRestApi(method);
                }
            }
        }
        logData.set(localData);
    }



    @AfterReturning(pointcut = "webLog()", returning = "object")
    public void doAfterReturning(Object object) {
        LogLocalData localData = logData.get();
        if(null != localData){
            logger.debug("【after-request-process】 【restApi】 {} 【return】 = {} cost {} ms",
                    localData.getRestApi(), JSONObject.toJSONString(object),
                    System.currentTimeMillis() - localData.getStartTime());

            logData.remove();
        }
    }




}
