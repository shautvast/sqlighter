package nl.sanderhautvast.sqlighter.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class StringValueTest {

    @Test
    public void testString() {
        String helloworld = "helloworld";
        LtValue stringValue = LtValue.of(helloworld);
        assertArrayEquals(new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x77, 0x6f, 0x72, 0x6c, 0x64}, stringValue.getValue());
        assertArrayEquals(new byte[]{(byte) (helloworld.length() * 2 + 13)}, stringValue.getType());
    }

}