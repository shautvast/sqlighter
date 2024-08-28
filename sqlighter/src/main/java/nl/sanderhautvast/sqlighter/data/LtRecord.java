package nl.sanderhautvast.sqlighter.data;

import nl.sanderhautvast.sqlighter.Varint;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Record in sqlite database.
 * Used for reading and writing.
 */
public final class LtRecord {

    private final long rowId;

    private int dataTypesLength = -1;
    private int valuesLength = -1;

    private final List<LtValue> values = new ArrayList<>(10);

    public LtRecord(long rowId) {
        this.rowId = rowId;
    }

    public void addValues(LtValue... values) {
        this.values.addAll(Arrays.asList(values));
    }

    public void addValue(LtValue value) {
        this.values.add(value);
    }

    /**
     * write the record to an array of bytes
     */
    public byte[] toBytes() {
        int dataLength = getDataLength();
        byte[] lengthBytes = Varint.write(dataLength);
        byte[] rowIdBytes = Varint.write(rowId);

        ByteBuffer buffer = ByteBuffer.allocate(lengthBytes.length + rowIdBytes.length + dataLength);
        buffer.put(lengthBytes);
        buffer.put(rowIdBytes);

        buffer.put(Varint.write(dataTypesLength));

        //types
        for (LtValue value : values) {
            value.writeType(buffer);
        }

        //values
        for (LtValue value : values) {
            value.writeValue(buffer);
        }

        return buffer.array();
    }

    public int getDataLength() {
        if (dataTypesLength < 0 || valuesLength < 0) {
            dataTypesLength = 1;
            valuesLength = 0;
            for (LtValue value : values) {
                dataTypesLength += value.getDataTypeLength();
                valuesLength += value.getValueLength();
            }
        }
        return dataTypesLength + valuesLength;
    }

    public long getRowId() {
        return rowId;
    }

    @SuppressWarnings("unused")
    public List<LtValue> getValues() {
        return values;
    }

    /**
     * returns the value at the specified column index (0 based)
     */
    public LtValue getValue(int column) {
        return values.get(column);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LtRecord record = (LtRecord) o;
        return rowId == record.rowId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowId);
    }
}
