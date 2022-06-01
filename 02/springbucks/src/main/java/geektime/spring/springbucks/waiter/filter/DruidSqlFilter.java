package geektime.spring.springbucks.waiter.filter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.h2.parser.H2StatementParser;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class DruidSqlFilter extends FilterEventAdapter {

    @Override
    public PreparedStatementProxy connection_prepareStatement(FilterChain chain, ConnectionProxy connection, String sql) throws SQLException {
        // 解析sql
        log.info("正在执行sql = {}",sql);
        H2StatementParser parser = new H2StatementParser(sql);
        SQLStatement stmt = parser.parseStatement();
        if(stmt instanceof SQLSelectStatement){
            // 如果是查询语句
            SQLSelect sqlSelect = ((SQLSelectStatement)stmt).getSelect();
            SQLExpr where = sqlSelect.getQueryBlock().getWhere();
            // 检查是否包含in查询条件，若包含 in列表不能超过10个
            checkInCondition(where);
        }
        return super.connection_prepareStatement(chain,connection,sql);
    }

    private void checkInCondition(SQLExpr sqlExpr){
        if(sqlExpr == null){
            return;
        }

        if(sqlExpr instanceof SQLInListExpr){
            // 如果是in表达式 ，检查条件长度
            SQLInListExpr expr = (SQLInListExpr) sqlExpr;
            List<SQLExpr> targetList = expr.getTargetList();
            if(targetList != null && targetList.size() > 10){
                throw new RuntimeException("in 列表不能超过 10 个");
            }
        } else {
            List<SQLObject> sqlObjects = sqlExpr.getChildren();
            sqlObjects.forEach(e -> {
                if(e instanceof SQLExpr){
                    //递归检查
                    checkInCondition((SQLExpr) e);
                }
            });
        }
    }
}
