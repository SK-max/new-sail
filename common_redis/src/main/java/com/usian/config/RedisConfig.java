package com.usian.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 设置通用序列化器
 */
@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String,Object> getRedisTemplate(RedisConnectionFactory factory){
        //创建redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //创建redis中的key序列化器
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        //创建Redis中的value序列化器
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        //指定要序列化的field get和set 以及修饰符范围，ANY 是都有包括private 和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类型必须是非final修饰的，final修饰的类会抛出异常比如String，Integer
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(om);

        //设置key value 和hashkey：hashVakye的序列化方式
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        //初始实例化redisTemplate
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
