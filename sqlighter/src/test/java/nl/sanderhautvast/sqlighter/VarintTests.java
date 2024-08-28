package nl.sanderhautvast.sqlighter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VarintTests {

    @Test
    public void testWriteMin1() {
        assertArrayEquals(new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1}, Varint.write(0xffffffffffffffffL));
    }

    @Test
    public void testWrite1() {
        Varint.write(220);
        assertArrayEquals(new byte[]{1}, Varint.write(0x01));
    }

    @Test
    public void testWrite() {
        assertArrayEquals(new byte[]{-1, -1, -1, -1, -1, -1, -1, 127}, Varint.write(0xffffffffffffffL));
    }

    @Test
    public void testReadMin1() {
        assertEquals(0xffffffffffffffffL, Varint.read(new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1}));
    }

    @Test
    public void testRead1() {
        assertEquals(1, Varint.read(new byte[]{1}));
    }

    @Test
    public void testRead() {
        assertEquals(0xffffffffffffffL, Varint.read(new byte[]{-1, -1, -1, -1, -1, -1, -1, 127}));
    }

    @Test
    public void testRead2() {
        assertEquals(562949953421311L, Varint.read(new byte[]{-1, -1, -1, -1, -1, -1, 127}));
    }

    @Test
    public void testRead3() {
        assertEquals(4398046511103L, Varint.read(new byte[]{-1, -1, -1, -1, -1, 127}));
    }

    @Test
    public void testRead4() {
        assertEquals(34359738367L, Varint.read(new byte[]{-1, -1, -1, -1, 127}));
    }

    @Test
    public void testRead5() {
        assertEquals(268435455, Varint.read(new byte[]{-1, -1, -1, 127}));
    }

    @Test
    public void testRead6() {
        assertEquals(2097151, Varint.read(new byte[]{-1, -1, 127}));
    }

    @Test
    public void testRead7() {
        assertEquals( 16383, Varint.read(new byte[]{-1, 127}));
    }
}
