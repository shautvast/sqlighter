package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LtRecordTest {

    @Test
    void testStringValue() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of("string"));
        assertArrayEquals(new byte[]{8, 1, 2, 25, 115, 116, 114, 105, 110, 103}, record.toBytes());
    }


    @Test
    void testIntValue()  {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of(2));
        assertArrayEquals(new byte[]{3, 1, 2, 1, 2}, record.toBytes());
    }

    @Test
    void testIntValue_0() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of(0));
        assertArrayEquals(new byte[]{2, 1, 2, 8}, record.toBytes());
    }

    @Test
    void testFloatValue() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of(2.0));
        assertArrayEquals(new byte[]{10, 1, 2, 7, 64, 0, 0, 0, 0, 0, 0, 0}, record.toBytes());
        assertEquals(10,record.getDataLength());
    }

    @Test
    void testStringIntValues() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of("string"));
        record.addValue(LtValue.of(2));
        assertArrayEquals(new byte[]{10, 1, 3, 25, 1, 115, 116, 114, 105, 110, 103, 2}, record.toBytes());
    }

    @Test
    void testStringInt_1Values() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of("string"));
        record.addValue(LtValue.of(1));
        assertArrayEquals(new byte[]{9, 1, 3, 25, 9, 115, 116, 114, 105, 110, 103}, record.toBytes());
    }

    @Test
    void testStringIntFloatValues() {
        LtRecord record = new LtRecord(1);
        record.addValue(LtValue.of("string"));
        record.addValue(LtValue.of(2));
        assertArrayEquals(new byte[]{10, 1, 3, 25, 1, 115, 116, 114, 105, 110, 103, 2}, record.toBytes());
    }

}