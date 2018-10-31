package io.mycat.monitor.service;

import io.mycat.monitor.entity.JmxMetric;
import io.mycat.monitor.model.param.JmxMetricParam;

import java.util.List;

/**
 * @author maxiaoguang
 */
public interface JmxMetricService {

    String[] getIps();

    String[] getEscapeIps();

    int batchInsert(List<JmxMetric> jmxMetrics);

    int cleanYestodayData();

    List<JmxMetric> recentlyJmxMetrics();

    List<JmxMetric> listJmxMetrics(JmxMetricParam jmxMetricParam);

}
