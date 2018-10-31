package io.mycat.monitor.thread;

import io.mycat.monitor.service.JmxMetricService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author maxiaoguang
 */
@Slf4j
public class JmxMetricCleanerThread implements Runnable {

    private JmxMetricService jmxMetricService;

    public JmxMetricCleanerThread(JmxMetricService jmxMetricService) {
        this.jmxMetricService = jmxMetricService;
    }

    @Override
    public void run() {
        try {
            jmxMetricService.cleanYestodayData();
        } catch (Exception e) {
            LOGGER.error("未知错误", e);
        }
    }

}
