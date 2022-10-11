package com.sg.rl.framework.components.redis.config;

import com.sg.rl.common.utils.StringUtils;
import com.sg.rl.framework.components.base.ComponentInterface;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@ConditionalOnProperty(name = "spring.redis.pool.max-active")
@Configuration
public class RedisComponent implements ComponentInterface {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.redis.host:127.0.0.1}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private String port;

    @Value("${spring.redis.timeout:-1}")
    private int redisTimeout;

    @Value("${spring.redis.cluster.nodes:1}")
    private String nodes;


    @Value("${spring.redis.password:}")
    private String password;

    @Value("${redis.server.database:1}")
    private Integer database;



    @Bean
    @ConditionalOnProperty(name = "spring.redis.cluster.nodes")
    public RedissonClient redissonClient() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();

        if (!StringUtils.isBlank(password)) {
            clusterServersConfig.setPassword(password);
        }

        clusterServersConfig.setScanInterval(2000);
        String[] serverArray = nodes.split(",");
        for (String node : serverArray) {
            String url = "redis://" + node;
            clusterServersConfig.addNodeAddress(url);
        }
        RedissonClient redissonClient = Redisson.create(config);
        log.info("load component redissonClient");
        return redissonClient;
    }


    @Bean
    @ConditionalOnProperty(name = "spring.redis.host")
    public RedissonClient singleRedissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();

        if (!StringUtils.isBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        String url = "redis://" + host + ":" + port;
        singleServerConfig.setAddress(url);
        singleServerConfig.setDatabase(database);
        RedissonClient redissonClient = Redisson.create(config);

        log.info("load component singleRedissonClient");
        return redissonClient;
    }


    /**
     * @return
     */
    @Bean
    public RedisTemplate redisTemplateInit() {
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        log.info("load component redisTemplate !!!!!!!!!!!!!!!!!!!!!");
        return redisTemplate;
    }




}
