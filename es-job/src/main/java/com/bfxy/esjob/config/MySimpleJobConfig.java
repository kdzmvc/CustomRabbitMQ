package com.bfxy.esjob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bfxy.esjob.listener.SimpleJobListener;
import com.bfxy.esjob.task.MySimpleJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

//@Configuration
public class MySimpleJobConfig {

    @Autowired
    private ZookeeperRegistryCenter registryCenter; //基于Zookeeper的注册中心

    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    /**
     * 	定时任务执行逻辑
     * @return
     */
    @Bean
    public SimpleJob simpleJob() {
        return new MySimpleJob();
    }

    /**
     * 根据Lite作业配置init作业调度器加载到spring容器中
     * @param simpleJob
     * @param cron 表达式
     * @param shardingTotalCount 分片总数
     * @param shardingItemParameters 分片参数
     * @param jobParameter job参数
     * @param failover 失败转移
     * @param monitorExecution 监控
     * @param monitorPort 监控端口
     * @param maxTimeDiffSeconds 最大误差时间
     * @param jobShardingStrategyClass 分片策略
     * @return 作业调度器
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final SimpleJob simpleJob,
                                           @Value("${simpleJob.cron}") final String cron,
                                           @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters,
                                           @Value("${simpleJob.jobParameter}") final String jobParameter,
                                           @Value("${simpleJob.failover}") final boolean failover,
                                           @Value("${simpleJob.monitorExecution}") final boolean monitorExecution,
                                           @Value("${simpleJob.monitorPort}") final int monitorPort,
                                           @Value("${simpleJob.maxTimeDiffSeconds}") final int maxTimeDiffSeconds,
                                           @Value("${simpleJob.jobShardingStrategyClass}") final String jobShardingStrategyClass) {

        return new SpringJobScheduler(
                simpleJob,
                registryCenter,
                getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters, jobParameter, failover, monitorExecution, monitorPort, maxTimeDiffSeconds, jobShardingStrategyClass),
                jobEventConfiguration, new SimpleJobListener());
    }

    /**
     * 根据参数配置Lite作业配置
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @param jobParameter
     * @param failover
     * @param monitorExecution
     * @param monitorPort
     * @param maxTimeDiffSeconds
     * @param jobShardingStrategyClass
     * @return Lite作业配置
     */
    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass, String cron,
                                                         int shardingTotalCount, String shardingItemParameters, String jobParameter, boolean failover,
                                                         boolean monitorExecution, int monitorPort, int maxTimeDiffSeconds, String jobShardingStrategyClass) {
        //作业核心配置
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
                .newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .misfire(true)
                .failover(failover)
                .jobParameter(jobParameter)
                .shardingItemParameters(shardingItemParameters)
                .build();
        //简单作业配置
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());
        //Lite作业配置
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                .overwrite(false)
                .build();

        return liteJobConfiguration;
    }


}
