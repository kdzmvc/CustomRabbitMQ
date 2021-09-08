package com.bfxy.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bfxy.rabbit.task.annotation.EnableElasticJob;


/**
 * @author kongdz
 * @notes $RabbitProducerAutoConfiguration 自动装配
 * @create 2021/5/31 14:14
 **/
@EnableElasticJob
@Configuration
@ComponentScan({"com.bfxy.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {


}
