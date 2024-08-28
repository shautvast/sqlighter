package nl.sanderhautvast.sqlighter.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class IntegerValueTest {
    @Test
    public void testInteger0() {
        // 0 and 1 are special values that are encoded in the type itself, which saves 8 bits
        LtValue integerValue = LtValue.of(0);
        assertArrayEquals(new byte[0], integerValue.getValue());
        assertArrayEquals(new byte[]{8}, integerValue.getType());
    }

    @Test
    public void testInteger1() {
        // 0 and 1 are special values that are encoded in the type itself, which saves 8 bits
        LtValue integerValue = LtValue.of(1);
        assertArrayEquals(new byte[]{}, integerValue.getValue());
        assertArrayEquals(new byte[]{9}, integerValue.getType());
    }

    @Test
    public void testInteger2() {
        LtValue integerValue = LtValue.of(2);
        assertArrayEquals(new byte[]{0x02}, integerValue.getValue());
        assertArrayEquals(new byte[]{1}, integerValue.getType());
    }

    @Test
    public void testInteger132() {
        LtValue integerValue = LtValue.of(132);
        assertArrayEquals(new byte[]{0,-124}, integerValue.getValue()); //0x80 as signed byte
        assertArrayEquals(new byte[]{2}, integerValue.getType());
    }

}