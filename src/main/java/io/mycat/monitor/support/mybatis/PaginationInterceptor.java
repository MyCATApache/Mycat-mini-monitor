package io.mycat.monitor.support.mybatis;

import io.mycat.monitor.model.result.PageableResult;
import io.mycat.monitor.model.result.PageableResult;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author maxiaoguang
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}))
public class PaginationInterceptor implements Interceptor {
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        CacheKey cacheKey = (CacheKey) args[4];
        BoundSql boundSql = (BoundSql) args[5];
        long total = getTotal(executor, ms, parameter, boundSql);
        if (total == 0) {
            return new PageableResult<>(0L, Collections.emptyList());
        } else {
            return new PageableResult<>(total, getData(executor, ms, parameter, rowBounds, resultHandler, cacheKey, boundSql));
        }
    }

    private long getTotal(Executor executor, MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) throws SQLException {
        final String countSql = "select count(*) from (" + boundSql.getSql() + ") t";
        Connection conn = executor.getTransaction().getConnection();
        PreparedStatement countStmt = conn.prepareStatement(countSql);
        new DefaultParameterHandler(mappedStatement,parameterObject,boundSql).setParameters(countStmt);
        ResultSet rs = countStmt.executeQuery();
        rs.next();
        return rs.getLong(1);
    }

    private <E> List<E> getData(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        String pageSql = boundSql.getSql() + " limit " + rowBounds.getOffset() + ", " + rowBounds.getLimit();
        BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
        return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, pageBoundSql);
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
    }
    
}
