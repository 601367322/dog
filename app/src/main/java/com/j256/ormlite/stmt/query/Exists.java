package com.j256.ormlite.stmt.query;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.stmt.ArgumentHolder;
import com.j256.ormlite.stmt.QueryBuilder.InternalQueryBuilderWrapper;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal class handling the SQL 'EXISTS' query part. Used by {@link Where#exists}.
 *
 * @author graywatson
 */
public class Exists implements Clause {

    private final InternalQueryBuilderWrapper subQueryBuilder;

    public Exists(InternalQueryBuilderWrapper subQueryBuilder) {
        this.subQueryBuilder = subQueryBuilder;
    }

    public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList)
            throws SQLException {
        sb.append("EXISTS (");
        subQueryBuilder.appendStatementString(sb, argList);
        sb.append(") ");
    }
}
