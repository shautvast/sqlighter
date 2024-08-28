package nl.sanderhautvast.sqlighter.fileviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import nl.sanderhautvast.sqlighter.fileviewer.data.ReadOnlyRecord;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private final String name;
    private final long rootPage;
    @JsonIgnore
    private final List<ReadOnlyRecord> records = new ArrayList<>();
    private int nRecords = 0;

    @JsonIgnore
    private final List<InteriorCell> interiorCells = new ArrayList<>();

    public void addRecord(ReadOnlyRecord record) {
        records.add(record);
        nRecords += 1;
    }

}
