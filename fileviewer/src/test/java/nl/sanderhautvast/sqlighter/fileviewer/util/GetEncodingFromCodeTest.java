package nl.sanderhautvast.sqlighter.fileviewer.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class GetEncodingFromCodeTest {

    @Test
    public void test() {
        assertSame(Encoding.UTF_8, Encoding.fromCode(1));
        assertSame(Encoding.UTF_16LE, Encoding.fromCode(2));
        assertSame(Encoding.UTF_16BE, Encoding.fromCode(3));
    }
}
