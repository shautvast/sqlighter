package nl.sanderhautvast.sqlighter.fileviewer;

import nl.sanderhautvast.sqlighter.SchemaRecord;
import nl.sanderhautvast.sqlighter.fileviewer.data.ReadOnlyRecord;
import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.model.Table;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader.readU16;
import static nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader.readU8;

public class SchemaReader {
    private final Metadatabase meta;

    public SchemaReader(Metadatabase meta) {
        this.meta = meta;
    }


    @SuppressWarnings("unused")
        // leaving bits in, without using them, because it may work as documentation
    List<SchemaRecord> readSchema(ByteBuffer buffer) {
        // skip page type 0x0D at #100
        buffer.position(101);
        buffer.limit(meta.getPagesize());

        long freeBlockStart = readU16(buffer);
        long nrCells = readU16(buffer);
        int startOfContentArea = readU16(buffer);
        long nrFragmentedFreeBytes = readU8(buffer);

        buffer.position(startOfContentArea);
        List<SchemaRecord> records = new ArrayList<>();
        RecordReader recordReader = new RecordReader(meta);

        for (int i = 0; i < nrCells; i++) {
            ReadOnlyRecord record = recordReader.read(buffer);
            String name = record.getStringValue(1).getValue();
            long rootPage = record.getIntegerValue(3).getValue().intValue();
            String sqlObjectType = record.getStringValue(0).getValue();

            String sql = record.getStringValue(4).getValue();
            records.add(new SchemaRecord(record.getRowId(), name, rootPage, sql));
            meta.addTable(new Table(name, rootPage));
        }

        return records;
    }
}
