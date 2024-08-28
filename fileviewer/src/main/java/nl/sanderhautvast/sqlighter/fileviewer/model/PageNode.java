package nl.sanderhautvast.sqlighter.fileviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nl.sanderhautvast.sqlighter.page.PageType;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageNode {
    private final long number;
    private PageType type;
    private long cellCount;


    @JsonIgnore
    private final List<Cell> cells = new ArrayList<>();

    private final List<PageNode> childPages = new ArrayList<>();

    @JsonIgnore
    private Long rightMostReference;

    private PageNode rightMostChildPage;

    public PageNode(long number) {
        this.number = number;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void addChildPage(PageNode page) {
        childPages.add(page);
    }

}
