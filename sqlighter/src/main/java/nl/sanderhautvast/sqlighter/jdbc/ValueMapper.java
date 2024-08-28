package nl.sanderhautvast.sqlighter.jdbc;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

class ValueMapper {

    private final List<Integer> types;

    public ValueMapper(List<Integer> types) {
        this.types = types;
    }

    void addValueFromJdbcResult(ResultSet result, LtRecord record, int columnIndex) throws SQLException {
        switch (types.get(columnIndex - 1)) { // index in metadata starts with 1, index in list starts with 0
            case Types.BLOB:
            case Types.CLOB: // handle as string?
            case Types.NCLOB: // handle as string?
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.JAVA_OBJECT:
                record.addValue(LtValue.of(result.getBytes(columnIndex)));
                break;
            case Types.NUMERIC:
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.DECIMAL:
            case Types.TINYINT:
                record.addValue(LtValue.of(result.getLong(columnIndex)));
                break;
            case Types.BOOLEAN:
            case Types.BIT: //?
                record.addValue(LtValue.of(result.getBoolean(columnIndex) ? 1 : 0));
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                record.addValue(LtValue.of(result.getDate(columnIndex).getTime()));
                break;
            case Types.FLOAT:
            case Types.DOUBLE:
                record.addValue(LtValue.of(result.getDouble(columnIndex)));
                break;
            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
                record.addValue(LtValue.of(result.getString(columnIndex)));
                break;
        }
    }
}
