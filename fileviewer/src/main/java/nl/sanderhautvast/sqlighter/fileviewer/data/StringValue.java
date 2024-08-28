package nl.sanderhautvast.sqlighter.fileviewer.data;


import nl.sanderhautvast.sqlighter.Varint;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringValue extends ReadOnlyValue<String> {
    private final String externalValue;

    public StringValue(String value) {
        super(Varint.write(value == null ? 0 : value.getBytes(StandardCharsets.UTF_8).length * 2L + 13),
                value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8));
        this.externalValue = value; /* only for reading from db. could be optimized by not storing this when writing,
                                       or create separate value classes for reading and writing */
    }

    @Override
    int getLength() {
        return type.length + value.length;
    }

    public static StringValue read(int length, ByteBuffer in, Charset charset) {
        byte[] bytes = new byte[length];
        try {
            in.get(bytes);
            return new StringValue(new String(bytes, charset));
        } catch (BufferUnderflowException e) {
            throw new IllegalStateException("should have read " + length + " bytes", e);
        }
    }

    @Override
    public String getValue() {
        return externalValue;
    }
}
