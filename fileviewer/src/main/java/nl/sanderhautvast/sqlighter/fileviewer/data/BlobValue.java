package nl.sanderhautvast.sqlighter.fileviewer.data;


import nl.sanderhautvast.sqlighter.Varint;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class BlobValue extends ReadOnlyValue<byte[]> {

    public BlobValue(byte[] value) {
        super(Varint.write(value.length * 2L + 12), value);
    }


    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    int getLength() {
        return type.length + value.length;
    }

    public static BlobValue read(int length, ByteBuffer in) {
        byte[] bytes = new byte[length];
        try {
            in.get(bytes);
            return new BlobValue(bytes);
        } catch (BufferUnderflowException e) {
            throw new IllegalStateException("should have read " + length + " bytes", e);
        }
    }
}
