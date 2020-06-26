package com.usian.config;


import com.usian.factory.MyAdaptableJobFactory;
import com.usian.quartz.OrderQuartz;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {


    /**
     * 创建job对象
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {

        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(OrderQuartz.class);
        return factory;
    }

    /**
     * 设置触发时间
     *
     * @param jobDetailFactoryBean
     * @return
     */
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {

        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(jobDetailFactoryBean.getObject());
        //设置触发时间
        //factory.setCronExpression("0/2 * * * * ?");
        factory.setCronExpression("0/2 * * * * ?");
        return factory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(CronTriggerFactoryBean cronTriggerFactoryBean,
                                                     MyAdaptableJobFactory myAdaptableJobFactory) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //关联trigger
        factory.setTriggers(cronTriggerFactoryBean.getObject());
        factory.setJobFactory(myAdaptableJobFactory);

        return factory;
    }


}
