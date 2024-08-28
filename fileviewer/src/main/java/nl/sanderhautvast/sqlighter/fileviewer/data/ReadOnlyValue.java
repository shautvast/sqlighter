package nl.sanderhautvast.sqlighter.fileviewer.data;

import nl.sanderhautvast.sqlighter.Varint;
import nl.sanderhautvast.sqlighter.data.LtValue;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/*
 * NB Value classes derive their equality from their identity. I.e. no equals/hashcode
 */
public abstract class ReadOnlyValue<T> {

    protected final byte[] type;
    protected final byte[] value;

    protected ReadOnlyValue(byte[] type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    /**
     * returns the user representation of the value
     */
    public abstract T getValue();

    /**
     * Returns the length of serialType + the length of the value
     */
    abstract int getLength();

    /**
     * Reads a value from the buffer
     *
     * @param buffer Bytebuffer containing the database page.
     * @param columnType sqlite type representation
     * @param charset database charset
     *
     * @return the value implementation
     */
    public static ReadOnlyValue<?> read(ByteBuffer buffer, long columnType, Charset charset) {
        if (columnType == 0) {
            return null;
        } else if (columnType < 6L) {
            byte[] integerBytes = new byte[getvalueLengthForType(columnType)];
            buffer.get(integerBytes);
            //TODO columnType is first decoded, then encoded again
            return new IntegerValue(Varint.write(columnType), integerBytes);
        } else if (columnType == 7) {
            return new FloatValue(buffer.getDouble());
        } else if (columnType == 8) {
            return new IntegerValue(0);
        } else if (columnType == 9) {
            return new IntegerValue(1);
        } else if (columnType >= 12 && columnType % 2 == 0) {
            return BlobValue.read(getvalueLengthForType(columnType), buffer);
        } else if (columnType >= 13) {
            return StringValue.read(getvalueLengthForType(columnType), buffer, charset);
        } else throw new IllegalStateException("unknown column type" + columnType);
    }

    private static int getvalueLengthForType(long columnType) {
        // can't switch on long
        if (columnType == 0 || columnType == 8 || columnType == 9) {
            return 0;
        } else if (columnType < 5) {
            return (int) columnType;
        } else if (columnType == 5) {
            return 6;
        } else if (columnType == 6 || columnType == 7) {
            return 8;
        } else if (columnType < 12) {
            return -1;
        } else {
            if (columnType % 2 == 0) {
                return (int) ((columnType - 12) >> 1);
            } else {
                return (int) ((columnType - 13) >> 1);
            }
        }
    }


    public static byte[] getIntegerType(long value) {
        if (value == 0) {
            return new byte[]{8};
        } else if (value == 1) {
            return new byte[]{9};
        } else {
            int length = LtValue.getLengthOfByteEncoding(value);
            if (length < 5) {
                return Varint.write(length);
            } else if (length < 7) {
                return Varint.write(5);
            } else return Varint.write(6);
        }
    }
}
