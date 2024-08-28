package nl.sanderhautvast.sqlighter.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BlobValueTest {

    @Test
    public void test() {
        LtValue blobValue = LtValue.of(new byte[]{1, 2, 3, 4, -128});
        assertArrayEquals(new byte[]{1, 2, 3, 4, -128}, blobValue.getValue());
        assertArrayEquals(new byte[]{22}, blobValue.getType());
    }
}