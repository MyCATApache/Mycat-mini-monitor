package io.mycat.monitor.thread;

import com.sun.management.OperatingSystemMXBean;
import io.mycat.monitor.entity.JmxMetric;
import io.mycat.monitor.service.JmxMetricService;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maxiaoguang
 */
@Slf4j
public class JmxMetricFetcherThread implements Runnable {

    private JmxMetricService jmxMetricService;

    private String[] ips;
    private String[] escapeIps;
    private Map<String, MemoryMXBean> ipMemoryMXBeanMap = new HashMap<>();
    private Map<String, OperatingSystemMXBean> ipOperatingSystemMXBeanMap = new HashMap<>();

    public JmxMetricFetcherThread(JmxMetricService jmxMetricService) {
        this.jmxMetricService = jmxMetricService;
        this.ips = jmxMetricService.getIps();
        this.escapeIps = jmxMetricService.getEscapeIps();
    }

    @Override
    public void run() {
        try {
            List<JmxMetric> jmxMetrics = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < ips.length; i++) {
                long startTimeMillis = System.currentTimeMillis();
                MemoryMXBean memoryMXBean = ipMemoryMXBeanMap.get(ips[i]);
                OperatingSystemMXBean operatingSystemMXBean = ipOperatingSystemMXBeanMap.get(ips[i]);
                if (memoryMXBean == null || operatingSystemMXBean == null) {
                    try {
                        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + ips[i] + ":1984/jmxrmi");
                        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL);
                        MBeanServerConnection mbeanServerConnection = jmxConnector.getMBeanServerConnection();
                        memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
                        operatingSystemMXBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServerConnection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
                        ipMemoryMXBeanMap.put(ips[i], memoryMXBean);
                        ipOperatingSystemMXBeanMap.put(ips[i], operatingSystemMXBean);
                    } catch (IOException e) {
                        LOGGER.warn("未知错误", e);
                        continue;
                    }
                }
                try {
                    MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
                    long usedMemory = memoryUsage.getUsed();
                    long committedMemory = memoryUsage.getCommitted();
                    long maxMemory = memoryUsage.getMax();
                    BigDecimal processCpuLoad = new BigDecimal(Double.toString(operatingSystemMXBean.getProcessCpuLoad()));
                    BigDecimal systemCpuLoad = new BigDecimal(Double.toString(operatingSystemMXBean.getProcessCpuLoad()));
                    BigDecimal systemLoadAverage = new BigDecimal(Double.toString(operatingSystemMXBean.getProcessCpuLoad()));
                    int duration = (int) (System.currentTimeMillis() - startTimeMillis);
                    JmxMetric jmxMetric = new JmxMetric()
                            .setIp(escapeIps[i])
                            .setUsedMemory(usedMemory)
                            .setCommittedMemory(committedMemory)
                            .setMaxMemory(maxMemory)
                            .setProcessCpuLoad(processCpuLoad)
                            .setSystemCpuLoad(systemCpuLoad)
                            .setSystemLoadAverage(systemLoadAverage)
                            .setDuration(duration)
                            .setCreatedDate(now)
                            .setLastModifiedDate(now);
                    jmxMetrics.add(jmxMetric);
                } catch (Exception e) {
                    LOGGER.warn("未知错误", e);
                    ipMemoryMXBeanMap.remove(ips[i]);
                    ipOperatingSystemMXBeanMap.remove(ips[i]);
                    continue;
                }
            }
            if (jmxMetrics.size() > 0) {
                jmxMetricService.batchInsert(jmxMetrics);
            }
        } catch (Exception e) {
            LOGGER.error("未知错误", e);
        }
    }

}
