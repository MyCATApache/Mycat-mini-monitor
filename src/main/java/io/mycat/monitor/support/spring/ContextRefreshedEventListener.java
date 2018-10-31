package io.mycat.monitor.support.spring;

import io.mycat.monitor.entity.JmxMetric;
import io.mycat.monitor.service.JmxMetricService;
import io.mycat.monitor.thread.JmxMetricCleanerThread;
import io.mycat.monitor.thread.JmxMetricFetcherThread;
import lombok.Getter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author maxiaoguang
 */
@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private JmxMetricService jmxMetricService;

    @Value("${monitor.fetchInteval}")
    private int fetchInteval;
    @Value("${monitor.cleanInteval}")
    private int cleanInteval;

    @Getter
    private ScheduledExecutorService scheduledThreadPool;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        scheduledThreadPool = new ScheduledThreadPoolExecutor(
                2,
                new BasicThreadFactory.Builder()
                        .namingPattern("monitor-schedule-pool-%d")
                        .daemon(true)
                        .build());
        if (fetchInteval < 0) {
            fetchInteval = 1;
        }
        scheduledThreadPool.scheduleWithFixedDelay(new JmxMetricFetcherThread(jmxMetricService), 0, fetchInteval, TimeUnit.SECONDS);
        if (cleanInteval > 0) {
            scheduledThreadPool.scheduleWithFixedDelay(new JmxMetricCleanerThread(jmxMetricService), 0, cleanInteval, TimeUnit.SECONDS);
        }
    }

}
