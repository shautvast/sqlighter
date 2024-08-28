package nl.sanderhautvast.sqlighter.fileviewer.data;


import java.nio.ByteBuffer;

/**
 * 64-bit float type
 */
public class FloatValue extends ReadOnlyValue<Double> {

    final private double externalValue;
    private static final byte FLOAT_TYPE = 7;


    public FloatValue(double value) {
        super(new byte[]{FLOAT_TYPE}, ByteBuffer.wrap(new byte[8]).putDouble(0, value).array());
        this.externalValue = value;
    }

    @Override
    public Double getValue() {
        return externalValue;
    }

    @Override
    int getLength() {
        return 9; // 1 for type, 8 for 64 bits
    }


}
