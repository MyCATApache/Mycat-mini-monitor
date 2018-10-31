package io.mycat.monitor.mapper;

import io.mycat.monitor.entity.JmxMetric;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import java.util.List;

import static io.mycat.monitor.model.table.JmxMetricDynamicSqlSupport.JMX_METRIC_TABLE;

/**
 * @author maxiaoguang
 */
@Mapper
public interface JmxMetricMapper {

    @Insert("insert into jmx_metric (ip, process_cpu_load, system_cpu_load, system_load_average, used_memory, committed_memory, max_memory, duration, created_date, last_modified_date) values (#{ip}, #{processCpuLoad}, #{systemCpuLoad}, #{systemLoadAverage}, #{usedMemory}, #{committedMemory}, #{maxMemory}, #{duration}, #{createdDate}, #{lastModifiedDate})")
    int insert(JmxMetric jmxMetrics);

    @Insert({
        "<script>",
            "insert into jmx_metric (ip, process_cpu_load, system_cpu_load, system_load_average, used_memory, committed_memory, max_memory, duration, created_date, last_modified_date) values ",
            "<foreach collection =\"collection\" item=\"item\" separator=\", \">",
                "(#{item.ip}, #{item.processCpuLoad}, #{item.systemCpuLoad}, #{item.systemLoadAverage}, #{item.usedMemory}, #{item.committedMemory}, #{item.maxMemory}, #{item.duration}, #{item.createdDate}, #{item.lastModifiedDate}) ",
            "</foreach>",
        "</script>"
    })
    int batchInsert(List<JmxMetric> jmxMetrics);

    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(JmxMetric.class)
    JmxMetric get(SelectStatementProvider selectStatementProvider);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(JmxMetric.class)
    List<JmxMetric> list(SelectStatementProvider selectStatementProvider);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(JmxMetric.class)
    List<JmxMetric> page(SelectStatementProvider selectStatementProvider, RowBounds rowBounds);

    default int deleteByPrimaryKey(Long id) {
        return deleteByCriteria()
                .where(JMX_METRIC_TABLE.ID_COLUMN, SqlBuilder.isEqualTo(id))
                .build()
                .execute();
    }

    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByCriteria() {
        return DeleteDSL.deleteFromWithMapper(this::delete, JMX_METRIC_TABLE);
    }

    default int updateByPrimaryKeySelective(JmxMetric jmxMetric) {
        return updateByCriteriaSelective(jmxMetric)
                .where(JMX_METRIC_TABLE.ID_COLUMN, SqlBuilder.isEqualTo(jmxMetric.getId()))
                .build()
                .execute();
    }

    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByCriteriaSelective(JmxMetric jmxMetric) {
        return UpdateDSL.updateWithMapper(this::update, JMX_METRIC_TABLE)
                .set(JMX_METRIC_TABLE.IP_COLUMN).equalToWhenPresent(jmxMetric.getIp())
                .set(JMX_METRIC_TABLE.USED_MEMORY_COLUMN).equalToWhenPresent(jmxMetric.getMaxMemory())
                .set(JMX_METRIC_TABLE.COMMITTED_MEMORY_COLUMN).equalToWhenPresent(jmxMetric.getCommittedMemory())
                .set(JMX_METRIC_TABLE.MAX_MEMORY_COLUMN).equalToWhenPresent(jmxMetric.getMaxMemory())
                .set(JMX_METRIC_TABLE.PROCESS_CPU_LOAD_COLUMN).equalToWhenPresent(jmxMetric.getProcessCpuLoad())
                .set(JMX_METRIC_TABLE.SYSTEM_CPU_LOAD_COLUMN).equalToWhenPresent(jmxMetric.getSystemCpuLoad())
                .set(JMX_METRIC_TABLE.SYSTEM_LOAD_AVERAGE_COLUMN).equalToWhenPresent(jmxMetric.getSystemLoadAverage())
                .set(JMX_METRIC_TABLE.DURATION_COLUMN).equalToWhenPresent(jmxMetric.getDuration())
                .set(JMX_METRIC_TABLE.CREATED_DATE_COLUMN).equalToWhenPresent(jmxMetric.getCreatedDate())
                .set(JMX_METRIC_TABLE.LAST_MODIFIED_DATE_COLUMN).equalToWhenPresent(jmxMetric.getLastModifiedDate());
    }

    default JmxMetric getByPrimaryKey(Long id) {
        return getByCriteria()
                .where(JMX_METRIC_TABLE.ID_COLUMN, SqlBuilder.isEqualTo(id))
                .build()
                .execute();
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<JmxMetric>> getByCriteria() {
        return SelectDSL.selectWithMapper(this::get, JMX_METRIC_TABLE.ID_COLUMN, JMX_METRIC_TABLE.IP_COLUMN, JMX_METRIC_TABLE.PROCESS_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_LOAD_AVERAGE_COLUMN, JMX_METRIC_TABLE.USED_MEMORY_COLUMN, JMX_METRIC_TABLE.COMMITTED_MEMORY_COLUMN, JMX_METRIC_TABLE.MAX_MEMORY_COLUMN, JMX_METRIC_TABLE.DURATION_COLUMN, JMX_METRIC_TABLE.CREATED_DATE_COLUMN, JMX_METRIC_TABLE.LAST_MODIFIED_DATE_COLUMN)
                .from(JMX_METRIC_TABLE);
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<JmxMetric>>> listByCriteria() {
        return SelectDSL.selectWithMapper(this::list, JMX_METRIC_TABLE.ID_COLUMN, JMX_METRIC_TABLE.IP_COLUMN, JMX_METRIC_TABLE.PROCESS_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_LOAD_AVERAGE_COLUMN, JMX_METRIC_TABLE.USED_MEMORY_COLUMN, JMX_METRIC_TABLE.COMMITTED_MEMORY_COLUMN, JMX_METRIC_TABLE.MAX_MEMORY_COLUMN, JMX_METRIC_TABLE.DURATION_COLUMN, JMX_METRIC_TABLE.CREATED_DATE_COLUMN, JMX_METRIC_TABLE.LAST_MODIFIED_DATE_COLUMN)
                .from(JMX_METRIC_TABLE);
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<JmxMetric>>> pageByCriteria(RowBounds rowBounds) {
        return SelectDSL.selectWithMapper(selectStatement -> page(selectStatement, rowBounds), JMX_METRIC_TABLE.ID_COLUMN, JMX_METRIC_TABLE.IP_COLUMN, JMX_METRIC_TABLE.PROCESS_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_CPU_LOAD_COLUMN, JMX_METRIC_TABLE.SYSTEM_LOAD_AVERAGE_COLUMN, JMX_METRIC_TABLE.USED_MEMORY_COLUMN, JMX_METRIC_TABLE.COMMITTED_MEMORY_COLUMN, JMX_METRIC_TABLE.MAX_MEMORY_COLUMN, JMX_METRIC_TABLE.DURATION_COLUMN, JMX_METRIC_TABLE.CREATED_DATE_COLUMN, JMX_METRIC_TABLE.LAST_MODIFIED_DATE_COLUMN)
                .from(JMX_METRIC_TABLE);
    }

}
