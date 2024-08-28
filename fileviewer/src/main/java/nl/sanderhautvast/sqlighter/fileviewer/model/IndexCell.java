package nl.sanderhautvast.sqlighter.fileviewer.model;

import lombok.Getter;
import lombok.Setter;
import nl.sanderhautvast.sqlighter.fileviewer.data.ReadOnlyValue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IndexCell extends Cell{
    private int valueType;
    private int rowIdType;
    private final List<ReadOnlyValue<?>> indexValues = new ArrayList<>();

    /* reference to the row in the table */
    private long rowid;

    public void addIndexValue(ReadOnlyValue<?> value) {
        this.indexValues.add(value);
    }
}
