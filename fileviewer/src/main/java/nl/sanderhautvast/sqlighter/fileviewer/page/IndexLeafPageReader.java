package nl.sanderhautvast.sqlighter.fileviewer.page;

import nl.sanderhautvast.sqlighter.Varint;
import nl.sanderhautvast.sqlighter.fileviewer.data.ReadOnlyValue;
import nl.sanderhautvast.sqlighter.fileviewer.model.*;
import nl.sanderhautvast.sqlighter.page.PageType;
import nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/*
 * WIP
 */
public class IndexLeafPageReader {
    private final Metadatabase metadatabase;
    private final Charset charset; //stored so that it doesn't have to be retrieved every time

    public IndexLeafPageReader(Metadatabase metadatabase) {
        this.metadatabase = metadatabase;
        this.charset = metadatabase.getEncoding().toCharset();
    }

    @SuppressWarnings("unused") // leaving bits in, without using them, because it works as documentation
    public void readPage(ByteBuffer buffer, long pageNumber) {
        int freeblockStartLocation = UnsignedIntReader.readU16(buffer);
        int nrCells = UnsignedIntReader.readU16(buffer);
        int startOfContentArea = UnsignedIntReader.readU16(buffer);
        int nrFragmentedFreeBytes = UnsignedIntReader.readU8(buffer);

        PageNode newPage=new PageNode(pageNumber);
        newPage.setType(PageType.INDEX_LEAF);
        newPage.setCellCount(nrCells);

        for (int i = 0; i < nrCells; i++) {
            int cellOffset = UnsignedIntReader.readU16(buffer);
            int mark = buffer.position();

            buffer.position(cellOffset);
            newPage.addCell(read(buffer));
            buffer.position(mark);
        }
    }

    /*
     * has a similar structure as RecordReader.read, but the rowId needs to be handled differently
     */
    @SuppressWarnings("unused") // leaving bits in, without using them, because it works as documentation
    public IndexCell read(ByteBuffer buffer) {
        IndexCell indexCell = new IndexCell();
        long payloadLength = Varint.read(buffer);
        long startOfValues = Varint.read(buffer);
        int mark = buffer.position();

        ArrayList<Integer> columnTypes = new ArrayList<>();
        while (buffer.position() < mark + startOfValues -1) {
            columnTypes.add((int) Varint.read(buffer));
        }

        for (int columnType : columnTypes) {
            indexCell.addIndexValue(ReadOnlyValue.read(buffer, columnType, charset));
        }

        @SuppressWarnings({"unchecked", "ConstantConditions"})
        Long rowid = ((ReadOnlyValue<Long>) ReadOnlyValue.read(buffer, columnTypes.get(columnTypes.size() - 1), charset)).getValue();
        indexCell.setRowid(rowid);

        return indexCell;
    }
}