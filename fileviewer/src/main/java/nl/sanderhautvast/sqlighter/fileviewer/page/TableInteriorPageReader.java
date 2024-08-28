package nl.sanderhautvast.sqlighter.fileviewer.page;

import nl.sanderhautvast.sqlighter.Varint;
import nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader;
import nl.sanderhautvast.sqlighter.fileviewer.model.InteriorCell;
import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.model.PageNode;
import nl.sanderhautvast.sqlighter.page.PageType;

import java.nio.ByteBuffer;

public class TableInteriorPageReader {
    private final Metadatabase metadatabase;

    public TableInteriorPageReader(Metadatabase metadatabase) {
        this.metadatabase = metadatabase;
    }

    @SuppressWarnings("unused") // leaving bits in, without using them, because it works as documentation
    public void readPage(ByteBuffer buffer, long pageNumber) {
        int freeblockStartLocation = UnsignedIntReader.readU16(buffer);
        int nrCells = UnsignedIntReader.readU16(buffer);
        int startOfContentArea = UnsignedIntReader.readU16(buffer);
        int nrFragmentedFreeBytes = UnsignedIntReader.readU8(buffer);
        long rightMostPointer = UnsignedIntReader.readU32(buffer); //?


        try {
            PageNode newPage = new PageNode(pageNumber);
            newPage.setType(PageType.TABLE_INTERIOR);
            newPage.setCellCount(nrCells);
            newPage.setRightMostReference(rightMostPointer);
            metadatabase.addPage(pageNumber,newPage);


            for (int i = 0; i < nrCells; i++) {
                int cellOffset = UnsignedIntReader.readU16(buffer);
                int mark = buffer.position();

                InteriorCell interiorCell = new InteriorCell();
                buffer.position(cellOffset);
                long childPageNumber = UnsignedIntReader.readU32(buffer);
                interiorCell.setPageReference(childPageNumber);
                interiorCell.setLastRowId(Varint.read(buffer));

                newPage.addCell(interiorCell);
                buffer.position(mark);
            }
        } catch (Exception e) {
            System.err.printf("Error at page number %d index %d (%#06x): ", pageNumber, buffer.position(), buffer.position());
            throw e;
        }
    }
}
