package io.mycat.monitor.model.table;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author maxiaoguang
 */
public final class JmxMetricDynamicSqlSupport {

    public static final JmxMetricTable JMX_METRIC_TABLE = new JmxMetricTable();

    public static final class JmxMetricTable extends SqlTable {

        public final SqlColumn<Long> ID_COLUMN = column("id");
        public final SqlColumn<String> IP_COLUMN = column("ip");
        public final SqlColumn<BigDecimal> PROCESS_CPU_LOAD_COLUMN = column("process_cpu_load");
        public final SqlColumn<BigDecimal> SYSTEM_CPU_LOAD_COLUMN = column("system_cpu_load");
        public final SqlColumn<BigDecimal> SYSTEM_LOAD_AVERAGE_COLUMN = column("system_load_average");
        public final SqlColumn<Long> USED_MEMORY_COLUMN = column("used_memory");
        public final SqlColumn<Long> COMMITTED_MEMORY_COLUMN = column("committed_memory");
        public final SqlColumn<Long> MAX_MEMORY_COLUMN = column("max_memory");
        public final SqlColumn<Integer> DURATION_COLUMN = column("duration");
        public final SqlColumn<LocalDateTime> CREATED_DATE_COLUMN = column("created_date");
        public final SqlColumn<LocalDateTime> LAST_MODIFIED_DATE_COLUMN = column("last_modified_date");

        public JmxMetricTable() {
            super("jmx_metric");
        }

    }

}
