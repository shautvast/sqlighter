package nl.sanderhautvast.sqlighter.fileviewer.page;

import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.RecordReader;
import nl.sanderhautvast.sqlighter.fileviewer.model.PageNode;
import nl.sanderhautvast.sqlighter.page.PageType;
import nl.sanderhautvast.sqlighter.fileviewer.model.Table;
import nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader;

import java.nio.ByteBuffer;

public class TableLeafPageReader {
    private final Metadatabase metadatabase;

    public TableLeafPageReader(Metadatabase metadatabase) {
        this.metadatabase = metadatabase;
    }

    @SuppressWarnings("unused") // leaving bits in, without using them, because it works as documentation
    public void readPage(ByteBuffer buffer, long pageNumber) {
        int freeblockStartLocation = UnsignedIntReader.readU16(buffer);
        int nrCells = UnsignedIntReader.readU16(buffer);
        int startOfContentArea = UnsignedIntReader.readU16(buffer);
        int nrFragmentedFreeBytes = UnsignedIntReader.readU8(buffer);
        RecordReader recordReader = new RecordReader(metadatabase);

        PageNode newPage = new PageNode(pageNumber);
        newPage.setType(PageType.TABLE_LEAF);
        newPage.setCellCount(nrCells);
        metadatabase.addPage(pageNumber, newPage);
    }


    // reads the actual records, want we that?
    // make cmdline arg
    private static void readPage(ByteBuffer buffer, RecordReader recordReader, Table table) {
        int cellOffset = UnsignedIntReader.readU16(buffer);
        int mark = buffer.position();

        buffer.position(cellOffset);
        table.addRecord(recordReader.read(buffer));
        buffer.position(mark);
    }
}
