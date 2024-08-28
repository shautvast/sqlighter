package nl.sanderhautvast.sqlighter.fileviewer.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Record in sqlite database.
 * Used for reading and writing.
 */
public class ReadOnlyRecord {


    private final long rowId;

    private final List<ReadOnlyValue<?>> values = new ArrayList<>();

    public ReadOnlyRecord(long rowId) {
        this.rowId = rowId;
    }

    public void addValue(ReadOnlyValue<?> value) {
        this.values.add(value);
    }

    public int length() {
        return values.stream().mapToInt(ReadOnlyValue::getLength).sum() + 1;
    }

    public long getRowId() {
        return rowId;
    }

    @SuppressWarnings("unused")
    public List<ReadOnlyValue<?>> getValues() {
        return values;
    }

    /**
     * returns the value at the specified column index (0 based)
     */
    public ReadOnlyValue<?> getValue(int column) {
        return values.get(column);
    }

    public StringValue getStringValue(int column) {
        ReadOnlyValue<?> value = getValue(column);
        if (value instanceof StringValue) {
            return (StringValue) value;
        } else {
            throw new IllegalCallerException("value is not a StringValue, but a " + value.getClass().getSimpleName());
        }
    }

    public IntegerValue getIntegerValue(int column) {
        ReadOnlyValue<?> value = getValue(column);
        if (value instanceof IntegerValue) {
            return (IntegerValue) value;
        } else {
            throw new IllegalCallerException("value is not a IntegerValue, but a " + value.getClass().getSimpleName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadOnlyRecord record = (ReadOnlyRecord) o;
        return rowId == record.rowId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowId);
    }
}
