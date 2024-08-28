package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ToBytesTests {

    @Test
    public void testInteger0()  {
        // 0 and 1 are special values that are encoded in the type itself, which saves 8 bits
        ByteBuffer out = ByteBuffer.allocate(0);
        LtValue.of(0).writeValue(out);
        assertArrayEquals(new byte[0], out.array());
    }

    @Test
    public void testInteger1()  {
        // 0 and 1 are special values that are encoded in the type itself, which saves 8 bits
        ByteBuffer out = ByteBuffer.allocate(0);
        LtValue.of(1).writeValue(out);
        assertArrayEquals(new byte[]{}, out.array());
    }

    @Test
    public void testInteger2()  {
        ByteBuffer out = ByteBuffer.allocate(1);
        LtValue.of(2).writeValue(out);
        assertArrayEquals(new byte[]{0x02}, out.array());
    }

    @Test
    public void testInteger128()  {
        ByteBuffer out = ByteBuffer.allocate(2);
        LtValue.of(128).writeValue(out);
        assertArrayEquals(new byte[]{0,-128}, out.array()); //0x80 as signed byte
    }

    @Test
    public void testString()  {
        // 'helloworld' is length 10 in characters
        // 10*2+13 = 33 = 0x21
        ByteBuffer out = ByteBuffer.allocate(10);
        LtValue.of("helloworld").writeValue(out);
        assertArrayEquals(new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x6f, 0x72, 0x6c, 0x64}, out.array()
        );
    }

    @Test
    public void testBlob()  {
        // 'helloworld' is length 10 in bytes
        // 10*2+12 = 32 = 0x20
        ByteBuffer out = ByteBuffer.allocate(10);
        LtValue.of("helloworld".getBytes(StandardCharsets.UTF_8)).writeValue(out);
        assertArrayEquals(new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x6f, 0x72, 0x6c, 0x64}, out.array());
    }

    @Test
    public void testFloat()  {
        // tested the expected binary representation against the actual sqlite file
        ByteBuffer out = ByteBuffer.allocate(8);
        LtValue.of(1.1).writeValue(out);
        assertArrayEquals(new byte[]{0x3f, (byte) 0xf1, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x9a}, out.array());
    }
}
