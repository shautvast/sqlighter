package nl.sanderhautvast.sqlighter.fileviewer.util;

import java.nio.ByteBuffer;

/**
 * reads  big-endian unsigned integers from bytebuffer
 */
public class UnsignedIntReader {
    public static long readU32(ByteBuffer buffer) {
        return buffer.getInt() & 0xFFFFFFFFL;
    }

    public static int readU16(ByteBuffer buffer) {
        return buffer.getShort() & 0xFFFF;
    }

    public static int readU8(ByteBuffer buffer) {
        return buffer.get() & 0xFF;
    }


}
