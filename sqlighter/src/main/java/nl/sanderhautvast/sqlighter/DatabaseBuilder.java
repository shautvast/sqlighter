package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.page.Page;
import nl.sanderhautvast.sqlighter.page.PageCacheFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The database builder is the main interface to create a database.
 */
public class DatabaseBuilder {

    private final List<Page> leafPages = new ArrayList<>();
    private Page currentPage;

    private SchemaRecord schemaRecord;

    private int nRecordsOnCurrentPage;

    public DatabaseBuilder() {
        try {
            Database.pageSize = Short.parseShort(System.getProperty(Database.PAGESIZE_PROPERTY));
        } catch (NumberFormatException ex) {
            //ignore
        }

        createPage();
    }

    public void addRecord(final LtRecord record) {
        byte[] recordBytes = record.toBytes();
        if (!newRecordFits(recordBytes)) {
            finishCurrentPage();
            createPage();
        }
        currentPage.setKey(record.getRowId()); //gets updated until page is finished
        currentPage.putBackward(recordBytes);
        currentPage.putU16(currentPage.getBackwardPosition());
        nRecordsOnCurrentPage += 1;
    }

    public void addSchema(String tableName, String ddl) {
        this.schemaRecord = new SchemaRecord(1, tableName, 2, ddl);
    }

    public Database build() {
        currentPage.setForwardPosition(Page.POSITION_CELL_COUNT);
        currentPage.putU16(nRecordsOnCurrentPage);

        if (nRecordsOnCurrentPage > 0) {
            currentPage.putU16(currentPage.getBackwardPosition());
        } else {
            currentPage.putU16(currentPage.getBackwardPosition() - 1);
        }

        return new Database(schemaRecord, leafPages);
    }

    private boolean newRecordFits(byte[] newBytes) {
        return currentPage.getBackwardPosition() - newBytes.length -2 > currentPage.getForwardPosition();
        // 2 for cell pointer length
    }

    private void finishCurrentPage() {
        currentPage.setForwardPosition(Page.POSITION_CELL_COUNT);
        currentPage.putU16(nRecordsOnCurrentPage);
        currentPage.putU16(currentPage.getBackwardPosition());
    }

    private void createPage() {
        currentPage = PageCacheFactory.getPageCache().getLeafPage();
        currentPage.setForwardPosition(8);
        leafPages.add(currentPage);
        nRecordsOnCurrentPage = 0;
    }
}
