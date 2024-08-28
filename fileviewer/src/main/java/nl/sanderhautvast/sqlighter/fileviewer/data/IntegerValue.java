package nl.sanderhautvast.sqlighter.fileviewer.data;

import nl.sanderhautvast.sqlighter.data.LtValue;

/*
 * Uses long (s64) as standard integer representation
 */
public class IntegerValue extends ReadOnlyValue<Long> {

    private final long externalValue;

    public IntegerValue(long value) {
        super(ReadOnlyValue.getIntegerType(value), LtValue.getValueAsBytes(value));
        this.externalValue = value; //See StringValue
    }

    public IntegerValue(byte[] typeByteRep, byte[] valueByteRep) {
        super(typeByteRep, valueByteRep);
        this.externalValue = bytesToLong(valueByteRep);
    }

    @Override
    int getLength() {
        return type.length + value.length;
    }



    @Override
    public Long getValue() {
        return externalValue;
    }

    public static long bytesToLong(final byte[] b) {
        long n = 0;
        for (int i = 0; i < b.length; i++) {
            byte v = b[i];
            int shift = ((b.length - i - 1) * 8);
            if (i == 0 && (v & 0x80) != 0) {
                n -= (0x80L << shift);
                v &= 0x7f;
            }
            n += ((long)(v&0xFF)) << shift;
        }
        return n;
    }
}