package nl.sanderhautvast.sqlighter.jdbc;

import nl.sanderhautvast.sqlighter.data.LtRecord;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Use this class to generate an SQLite database from your JDBC results
 */
public class ResulSet2SQLite {

    private final ValueMapper valueMapper;
    private final int columnCount;

    private long rowid = 1;

    public ResulSet2SQLite(ResultSetMetaData metadata) throws SQLException {
        valueMapper = new ValueMapper(getSqlTypesFromJdbcResult(metadata));

        this.columnCount = getColumnCount(metadata);
    }

    public LtRecord mapRow(ResultSet result) throws SQLException {
        LtRecord record = new LtRecord(rowid++);
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            valueMapper.addValueFromJdbcResult(result, record, columnIndex);
        }

        return record;
    }

    static List<Integer> getSqlTypesFromJdbcResult(ResultSetMetaData metaData) throws SQLException {
        List<Integer> types = new ArrayList<>();
        for (int i = 1; i <= getColumnCount(metaData); i++) {
            types.add(metaData.getColumnType(i));
        }
        return types;
    }

    private static int getColumnCount(ResultSetMetaData metaData) throws SQLException {
        return metaData.getColumnCount();
    }


}
