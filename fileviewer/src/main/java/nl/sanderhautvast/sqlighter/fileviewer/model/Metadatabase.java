package nl.sanderhautvast.sqlighter.fileviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import nl.sanderhautvast.sqlighter.SchemaRecord;
import nl.sanderhautvast.sqlighter.fileviewer.util.Encoding;
import nl.sanderhautvast.sqlighter.fileviewer.util.Printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.sanderhautvast.sqlighter.fileviewer.validation.Validator.validate;

@Data
public class Metadatabase {

    public static final String HEADER = "53 51 4C 69 74 65 20 66 6F 72 6D 61 74 20 33 00";
    public static final int MAX_EMBEDDED_PAYLOAD_FRACTION = 64;
    public static final int MIN_EMBEDDED_PAYLOAD_FRACTION = 32;
    public static final int LEAF_PAYLOAD_FRACTION = 32;

    private String magicHeader;
    private int pagesize;
    private int writeversion;
    private int readversion;
    private int unusedPerPageSize;
    private int maxEmbeddedPayloadFraction;
    private int minEmbeddedPayloadFraction;
    private int leafPayloadFraction;
    private long fileChangeCounter;
    private long sizeInPages;
    private long pageNrFirstFreelistTrunkpage;
    private long totalNrOfFreelistPages;
    private long schemaCookie;
    private long schemaFormatNumber;
    private int defaultPageCachesize;
    private long pageNrLargestRootBtreePage;
    private Encoding encoding;
    private long userVersion;
    private boolean incrementalVacuumMode;
    private long applicationID;
    private String expansion;
    private long versionValidFor;
    private long sqliteVersion;

    private final List<SchemaRecord> schemaRecords = new ArrayList<>();

    @JsonIgnore
    private HashMap<Long, PageNode> pages = new HashMap<>();

    public void addPage(long nr, PageNode page) {
        pages.put(nr, page);
    }

    //    @JsonIgnore
    private final List<Table> tables = new ArrayList<>();

    public void addTable(Table table) {
        tables.add(table);
    }

    public void setMagicHeader(byte[] magicHeader) {
        this.magicHeader = Printer.printHex(magicHeader);
        validate("magic header", HEADER, this.magicHeader);
    }

    public void setMaxEmbeddedPayloadFraction(int maxEmbeddedPayloadFraction) {
        this.maxEmbeddedPayloadFraction = maxEmbeddedPayloadFraction;
        validate("max embedded payload fraction", MAX_EMBEDDED_PAYLOAD_FRACTION, maxEmbeddedPayloadFraction);
    }

    public void setMinEmbeddedPayloadFraction(int minEmbeddedPayloadFraction) {
        validate("min embedded payload fraction", MIN_EMBEDDED_PAYLOAD_FRACTION, minEmbeddedPayloadFraction);
        this.minEmbeddedPayloadFraction = minEmbeddedPayloadFraction;
    }

    public void setLeafPayloadFraction(int leafPayloadFraction) {
        this.leafPayloadFraction = leafPayloadFraction;
        validate("leaf payload fraction", LEAF_PAYLOAD_FRACTION, leafPayloadFraction);
    }

    public String report() throws IOException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public void setExpansion(byte[] expansion) {
        this.expansion = Printer.printHex(expansion);
    }

}
