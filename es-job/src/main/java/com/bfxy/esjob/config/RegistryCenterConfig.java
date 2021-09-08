package com.bfxy.esjob.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

//@Configuration
//@ConditionalOnExpression("'${zookeeper.address}'.length() > 0")
public class RegistryCenterConfig {

    /**
     *把Zookeeper注册中心加载到spring容器中
     * @param serverLists 连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181
     * @param namespace 命名空间
     * @param connectionTimeout 连接超时时间. 单位毫秒.
     * @param sessionTimeout 会话超时时间. 单位毫秒.
     * @param maxRetries 最大重试次数.
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(@Value("${zookeeper.address}") final String serverLists,
                                                  @Value("${zookeeper.namespace}") final String namespace,
                                                  @Value("${zookeeper.connectionTimeout}") final int connectionTimeout,
                                                  @Value("${zookeeper.sessionTimeout}") final int sessionTimeout,
                                                  @Value("${zookeeper.maxRetries}") final int maxRetries) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, namespace);
        zookeeperConfiguration.setConnectionTimeoutMilliseconds(connectionTimeout);
        zookeeperConfiguration.setSessionTimeoutMilliseconds(sessionTimeout);
        zookeeperConfiguration.setMaxRetries(maxRetries);
        return new ZookeeperRegistryCenter(zookeeperConfiguration);

    }


}