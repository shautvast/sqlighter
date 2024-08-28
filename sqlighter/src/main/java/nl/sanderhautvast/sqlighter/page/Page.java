package nl.sanderhautvast.sqlighter.page;

import nl.sanderhautvast.sqlighter.Database;
import nl.sanderhautvast.sqlighter.SQLiteConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a SQLite page
 */
public final class Page {

    public static int POSITION_RIGHTMOST_POINTER = 8; // first position after page header
    public static int START = 12; // first position after page header

    public static final int POSITION_CELL_COUNT = 3;
    public static final int START_OF_CONTENT_AREA = 5;

    private final byte[] data;

    private long key;

    private final List<Page> children = new ArrayList<>();

    private int forwardPosition;
    private int backwardPosition;

    private final PageType type;

    static Page newLeaf() {
        return newLeaf(Database.pageSize);
    }

    static Page newInterior() {
       return newInterior(Database.pageSize);
    }

    static Page newLeaf(short pageSize) {
        Page page = new Page(PageType.TABLE_LEAF, pageSize);
        page.putU8(SQLiteConstants.TABLE_LEAF_PAGE);
        page.skipForward(2);
        return page;
    }

    static Page newInterior(short pageSize) {
        Page page = new Page(PageType.TABLE_INTERIOR, pageSize);
        page.putU8(SQLiteConstants.TABLE_INTERIOR_PAGE);
        return page;
    }


    public static Page newHeader(int size) {
        return new Page(PageType.HEADER, size);
    }

    public void addChild(Page child) {
        children.add(child);
    }

    private Page(PageType type, int size) {
        this.type = type;
        data = new byte[size];
        forwardPosition = 0;
        backwardPosition = size;
    }

    public int getForwardPosition() {
        return forwardPosition;
    }

    public int getBackwardPosition() {
        return backwardPosition;
    }

    public void setForwardPosition(int forwardPosition) {
        this.forwardPosition = forwardPosition;
    }

    public void putU16(int value) {
        data[forwardPosition] = (byte) ((value >> 8) & 0xFF);
        data[forwardPosition + 1] = (byte) (value & 0xFF);
        forwardPosition += 2;
    }

    public void putU32(long value) {
        data[forwardPosition] = (byte) ((value >> 24) & 0xFF);
        data[forwardPosition + 1] = (byte) ((value >> 16) & 0xFF);
        data[forwardPosition + 2] = (byte) ((value >> 8) & 0xFF);
        data[forwardPosition + 3] = (byte) (value & 0xFF);
        forwardPosition += 4;
    }

    public void putU8(int value) {
        data[forwardPosition] = (byte) (value & 0xFF);
        forwardPosition += 1;
    }

    public void putU8(byte[] value) {
        System.arraycopy(value, 0, data, forwardPosition, value.length);
        forwardPosition += value.length;
    }

    public int getU16() {
        return ((data[forwardPosition] & 0xFF) << 8) + (data[forwardPosition + 1] & 0xFF);
    }

    public void putBackward(byte[] value) {
        backwardPosition -= value.length;
        System.arraycopy(value, 0, data, backwardPosition, value.length);
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    public int size() {
        return data.length;
    }

    public List<Page> getChildren() {
        return children;
    }

    public byte[] getData() {
        return data;
    }

    public void skipForward(int length) {
        this.forwardPosition += length;
    }

    public boolean isInterior() {
        return type == PageType.TABLE_INTERIOR;
    }

    public PageType getType() {
        return type;
    }

    void reset() {
        this.forwardPosition = 0;
        this.backwardPosition = Database.pageSize;
        this.children.clear();
    }
}
