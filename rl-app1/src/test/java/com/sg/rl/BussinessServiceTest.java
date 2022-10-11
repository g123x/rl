package com.sg.rl;


import com.alibaba.fastjson.JSON;
import com.sg.rl.dto.GetSysConfReq;
import com.sg.rl.entity.SysConfig;
import com.sg.rl.framework.components.db.annotation.DataSource;
import com.sg.rl.framework.components.http.RestTemplateComponent;
import com.sg.rl.framework.components.redis.RedisUtil;
import com.sg.rl.framework.components.spring.SpringContextComponent;
import com.sg.rl.framework.components.threadpool.AsyncManager;
import com.sg.rl.mapper.SysConfigMapper;
import com.sg.rl.service.IBussinessService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import static com.sg.rl.common.constants.Constants.DataSourceType.MASTER;
import static com.sg.rl.common.utils.Threads.sleep;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BussinessServiceTest {

    @Autowired
    private IBussinessService bService;

    @Autowired
    private RestTemplateComponent restTemplateUtils;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test(){
        SysConfig sysConfig = (SysConfig)bService.getSysConfig();
        log.info("getSysConfig = {}",sysConfig);
    }

    @Test
    public void testIoc(){
        IBussinessService bean1 = (IBussinessService) SpringContextComponent.getBean(IBussinessService.class);
        IBussinessService bean2 = (IBussinessService) SpringContextComponent.getBean("IBussinessServiceImpl");

        if(bean2 == bean1){
            System.out.println("getBean suc");
        }

        String[] allBeanNames = SpringContextComponent.getApplicationContext().getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
        }

    }

    @Test
    public void testAsync(){

        for(int i=0;i<5;i++){
            bService.sendMsg();
        }

    }



    @Test
    @DataSource(value = MASTER)
    @Transactional
    public void insertBatch(){
        SysConfig config = new SysConfig();
        for(int i=0;i<10;i++){
            config.setConfigKey("sys.index.skinName");
            config.setConfigName("i"+i);
            config.setConfigValue("xasdxasdsad");
            config.setConfigType("Y");
            config.setCreateTime(new Date());
            sysConfigMapper.insertConfig(config);
        }


    }


    @Test
    public void testRestTemplate(){

        String rsp = "";

        rsp = restTemplateUtils.sendGet("http://www.baidu.com");

        String getUrl = "http://localhost:8020/bussiness/testGet";

        GetSysConfReq req = GetSysConfReq.builder()
                .account("123").userId("888").channel(3).build();

        Map urlParam = JSON.parseObject(JSON.toJSONString(req),Map.class);


        rsp = restTemplateUtils.sendGetWithParms(getUrl,urlParam);

        Map<String, Object> header = new HashMap<>();
        urlParam.put("RL","123");
        urlParam.put("RL111",123123213);


        HttpHeaders headers = new HttpHeaders();
        headers.add("RL","123");
        headers.add("RL1111","1121x1x2e123");
        rsp = restTemplateUtils.sendGetWithParamsHeaders(getUrl,urlParam,headers);

        String postUrl = "http://localhost:8020/bussiness/testPost";

        rsp = restTemplateUtils.sendPostJson(postUrl,urlParam);
        rsp = restTemplateUtils.sendPostWithJsonHeaders(postUrl,urlParam,headers);

    }


    @Test
    public void testRedisUtil(){
        redisUtil.set("test","123");
        log.info("after set redisUtil.get {}  !!!!!!",redisUtil.get("test"));

        redisUtil.delete("test");
        log.info("after delete redisUtil.get {}  !!!!!!",redisUtil.get("test"));
    }


    @Test
    public void testRedisLock(){
        TimerTask task1 =  new TimerTask()
        {
            @Override
            public void run()
            {
                RLock redisLock = redissonClient.getLock("lock1");
                redisLock.lock();
                log.info("task1 lock suc!!!");

                sleep(3000);


                redisLock.unlock();
                log.info("task1 unlock suc!!!");
            }
        };


        TimerTask task2 =  new TimerTask()
        {
            @Override
            public void run()
            {
                RLock redisLock = redissonClient.getLock("lock1");
                redisLock.lock();
                log.info("task2 lock  suc!!!");

                sleep(3000);

                redisLock.unlock();
                log.info("task2 unlock suc!!!");

            }
        };

        AsyncManager.me().execute(task1);
        AsyncManager.me().execute(task2);



        sleep(10000);
    }

}
