package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SerialTypesTests {

    @Test
    void testInteger0()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(0).writeType(out);
        assertArrayEquals(new byte[]{8}, out.array());
    }

    @Test
    void testInteger1()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(1).writeType(out);
        assertArrayEquals(new byte[]{9}, out.array());
    }

    @Test
    void testInteger2()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(2).writeType(out);
        assertArrayEquals(new byte[]{1}, out.array());
    }

    @Test
    void testInteger128()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(128).writeType(out);
        assertArrayEquals(new byte[]{2}, out.array());
    }

    @Test
    void testString()  {
        // 'helloworld' is length 10 in characters
        // 10*2+13 = 33 = 0x21
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of("helloworld").writeType(out);
        assertArrayEquals(new byte[]{0x21}, out.array());
    }

    @Test
    void testBlob()  {
        // 'helloworld' is length 10 in bytes
        // 10*2+12 = 32 = 0x20
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of("helloworld".getBytes(StandardCharsets.UTF_8)).writeType(out);
        assertArrayEquals(new byte[]{0x20}, out.array());
    }

    @Test
    void testFloat()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(1.0).writeType(out);
        assertArrayEquals(new byte[]{7}, out.array());
    }
}
