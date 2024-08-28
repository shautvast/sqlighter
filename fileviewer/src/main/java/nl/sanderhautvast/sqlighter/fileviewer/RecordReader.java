package nl.sanderhautvast.sqlighter.fileviewer;

import nl.sanderhautvast.sqlighter.Varint;
import nl.sanderhautvast.sqlighter.fileviewer.data.*;
import nl.sanderhautvast.sqlighter.fileviewer.data.ReadOnlyRecord;
import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RecordReader {

    private final Metadatabase metadatabase;

    public RecordReader(Metadatabase metadatabase) {
        this.metadatabase = metadatabase;
    }

    @SuppressWarnings("unused") // leaving bits in, without using them, because it works as documentation
    public ReadOnlyRecord read(ByteBuffer buffer) {
        long payloadLength = Varint.read(buffer);
        long rowId = Varint.read(buffer);
        long columnLengthSum = Varint.read(buffer);

        int mark = buffer.position();

        List<Long> columnTypes = new ArrayList<>();
        while (buffer.position() < mark + columnLengthSum -1) {
            columnTypes.add(Varint.read(buffer));
        }

        ReadOnlyRecord record = new ReadOnlyRecord(rowId);
        for (long columnType : columnTypes) {
            record.addValue(ReadOnlyValue.read(buffer, columnType, metadatabase.getEncoding().toCharset()));
        }

        return record;
    }


}
