package nl.sanderhautvast.sqlighter.data;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FloatValueTest {
    @Test
    public void test() {
        LtValue floatValue = LtValue.of(1.5D);
        ByteBuffer floatValueBytes = ByteBuffer.allocate(8).putDouble(1.5D);
        assertArrayEquals(
                floatValueBytes.array(),
                floatValue.getValue());
        assertArrayEquals(new byte[]{7}, floatValue.getType());
    }
}