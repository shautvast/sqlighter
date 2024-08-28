package nl.sanderhautvast.sqlighter.fileviewer.page;

import nl.sanderhautvast.sqlighter.SQLiteConstants;
import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader;

import java.nio.ByteBuffer;

public class PageReader {

    private final Metadatabase metadatabase;

    public PageReader(Metadatabase metadatabase) {
        this.metadatabase = metadatabase;
    }

    public void readPage(ByteBuffer buffer, long pageNumber) {
        int pagetype = UnsignedIntReader.readU8(buffer);
        switch (pagetype) {
            case SQLiteConstants.TABLE_LEAF_PAGE: new TableLeafPageReader(metadatabase).readPage(buffer, pageNumber);
                break;
            case SQLiteConstants.TABLE_INTERIOR_PAGE: new TableInteriorPageReader(metadatabase).readPage(buffer, pageNumber);
                break;
            case SQLiteConstants.INDEX_LEAF_PAGE: new IndexLeafPageReader(metadatabase).readPage(buffer, pageNumber);
                break;
            case SQLiteConstants.INDEX_INTERIOR_PAGE: new IndexInteriorPageReader(metadatabase).readPage();
                break;
        }
    }
}
