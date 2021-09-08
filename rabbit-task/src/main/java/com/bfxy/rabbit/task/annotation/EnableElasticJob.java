package com.bfxy.rabbit.task.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.bfxy.rabbit.task.autoconfigure.JobParserAutoConfigurartion;

@Target(ElementType.TYPE) //允许被修饰的注解作用在类、接口和枚举上
@Retention(RetentionPolicy.RUNTIME) //注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Documented
@Inherited //标记注解，允许子类继承父类的注解
@Import(JobParserAutoConfigurartion.class)
public @interface EnableElasticJob {

}
