package nl.sanderhautvast.sqlighter.fileviewer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Index {
    private final String name;
    private final List<IndexCell> indexCells = new ArrayList<>();

    public void addCell(IndexCell indexCell) {
        indexCells.add(indexCell);
    }


}

