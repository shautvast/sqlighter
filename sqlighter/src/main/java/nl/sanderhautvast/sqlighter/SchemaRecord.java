package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;

/*
 * Is a record in the sqlites_schema table
 * and a special case of a Record
 * class is being used for both reading and writing
 *
 */
public class SchemaRecord {

    private final long rowid;
    private final String tableName;
    private final long rootpage;
    private final String sql;

    public SchemaRecord(long rowid, String tableName, long rootpage, String sql) {
        this.rowid = rowid;
        this.tableName = tableName;
        this.rootpage = rootpage;
        this.sql = sql;
    }

    public String getTableName() {
        return tableName;
    }

    public long getRootpage() {
        return rootpage;
    }

    public String getSql() {
        return sql;
    }

    public LtRecord toRecord(){
        LtRecord record = new LtRecord(rowid);
        record.addValue(LtValue.of("table"));
        record.addValue(LtValue.of(getTableName().toLowerCase()));
        record.addValue(LtValue.of(getTableName().toLowerCase()));
        record.addValue(LtValue.of(getRootpage()));
        record.addValue(LtValue.of(getSql()));
        return record;
    }
}
