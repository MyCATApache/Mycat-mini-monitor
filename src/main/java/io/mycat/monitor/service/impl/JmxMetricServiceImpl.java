package io.mycat.monitor.service.impl;

import io.mycat.monitor.entity.JmxMetric;
import io.mycat.monitor.mapper.JmxMetricMapper;
import io.mycat.monitor.model.param.JmxMetricParam;
import io.mycat.monitor.model.table.JmxMetricDynamicSqlSupport;
import io.mycat.monitor.service.JmxMetricService;
import io.mycat.monitor.thread.JmxMetricCleanerThread;
import io.mycat.monitor.thread.JmxMetricFetcherThread;
import io.mycat.monitor.util.DynamicSqlSupportUtils;
import lombok.Getter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.mycat.monitor.model.table.JmxMetricDynamicSqlSupport.JMX_METRIC_TABLE;

/**
 * @author maxiaoguang
 */
@Service
public class JmxMetricServiceImpl implements JmxMetricService, InitializingBean {

    @Autowired
    private JmxMetricMapper jmxMetricMapper;

    @Value("${monitor.ips}")
    @Getter
    private String[] ips;
    @Getter
    private String[] escapeIps;
    @Value("${monitor.cleanInteval}")
    private int cleanInteval;


    @Override
    public int batchInsert(List<JmxMetric> jmxMetrics) {
        return jmxMetricMapper.batchInsert(jmxMetrics);
    }

    @Override
    public int cleanYestodayData() {
        if (cleanInteval > 0) {
            return jmxMetricMapper.deleteByCriteria()
                    .where(JmxMetricDynamicSqlSupport.JMX_METRIC_TABLE.CREATED_DATE_COLUMN, SqlBuilder.isLessThan(LocalDateTime.now().minusSeconds(cleanInteval)))
                    .build()
                    .execute();
        } else {
            return 0;
        }
    }


    @Override
    public List<JmxMetric> recentlyJmxMetrics() {
        List<JmxMetric> jmxMetrics = jmxMetricMapper.pageByCriteria(new RowBounds(0, 720))
                .orderBy(JMX_METRIC_TABLE.ID_COLUMN.descending())
                .build()
                .execute();
        Collections.reverse(jmxMetrics);
        return jmxMetrics;
    }

    @Override
    public List<JmxMetric> listJmxMetrics(JmxMetricParam jmxMetricParam) {
        return jmxMetricMapper.pageByCriteria(new RowBounds(jmxMetricParam.getOffset(), jmxMetricParam.getLimit()))
                .where(JMX_METRIC_TABLE.ID_COLUMN, SqlBuilder.isGreaterThan(jmxMetricParam.getId()))
                .orderBy(DynamicSqlSupportUtils.getSortSpecifications(jmxMetricParam, JMX_METRIC_TABLE))
                .build()
                .execute();
    }

    @Override
    public void afterPropertiesSet() {
        escapeIps = new String[ips.length];
        for (int i = 0; i < ips.length; i++) {
            escapeIps[i] = "_" + ips[i].replaceAll("\\.", "_");
        }
    }

}
