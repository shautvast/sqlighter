package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import nl.sanderhautvast.sqlighter.page.Page;
import nl.sanderhautvast.sqlighter.page.PageCacheFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LeafDbPageTest {

    @BeforeEach
    public void setup() {
        Database.pageSize=64;
        PageCacheFactory.getPageCache().clear();
    }

    @AfterEach
    public void teardown() {
        PageCacheFactory.getPageCache().clear();
        Database.pageSize=0x1000;
    }

    @Test
    void testWriteDataToLeafPage() {
        LtRecord record1 = new LtRecord(1);
        record1.addValues(LtValue.of("hello"));
        LtRecord record2 = new LtRecord(2);
        record2.addValues(LtValue.of("world"));
        DatabaseBuilder databaseBuilder = new DatabaseBuilder();
        databaseBuilder.addRecord(record1);
        databaseBuilder.addRecord(record2);

        List<Page> leafPages = databaseBuilder.build().leafPages;

        assertFalse(leafPages.isEmpty());
        assertArrayEquals(new byte[]{
                        0x0D, 0x00, 0x00, 0x00, 0x02, 0x00, 0x2E, 0x00, 0x00, 0x37, 0x00, 0x2e, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x07, 0x02,
                        0x02, 0x17, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x07, 0x01, 0x02, 0x17, 0x68, 0x65, 0x6C, 0x6C, 0x6F},
                leafPages.get(0).getData()
        );
    }
}