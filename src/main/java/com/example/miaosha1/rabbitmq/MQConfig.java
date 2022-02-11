package com.example.miaosha1.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
    public static final String MIAOSHA_QUEUE = "miaosha_queue";
    @Bean
    public Queue queue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }
}
