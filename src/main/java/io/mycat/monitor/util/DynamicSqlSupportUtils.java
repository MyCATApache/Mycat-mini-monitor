package io.mycat.monitor.util;

import io.mycat.monitor.model.param.PageableParam;
import io.mycat.monitor.model.param.PageableParam;
import org.mybatis.dynamic.sql.SortSpecification;
import org.mybatis.dynamic.sql.SqlTable;

/**
 * @author maxiaoguang
 */
public class DynamicSqlSupportUtils {

    public static SortSpecification[] getSortSpecifications(PageableParam pageableParam, SqlTable sqlTable) {
        if (pageableParam.getSorts() == null) {
            return new SortSpecification[] {sqlTable.column("id")};
        }
        SortSpecification[] sortSpecifications = new SortSpecification[pageableParam.getSorts().size()];
        for (int i = 0; i < pageableParam.getSorts().size(); i++) {
            SortSpecification sortSpecification = sqlTable.column(pageableParam.getSorts().get(i));
            if ("desc".equals(pageableParam.getOrders().get(i))) {
                sortSpecification = sortSpecification.descending();
            }
            sortSpecifications[i] = sortSpecification;
        }
        return sortSpecifications;
    }

}
