package nl.sanderhautvast.sqlighter.fileviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteriorCell extends Cell{
    @JsonIgnore
    private long pageReference;
    @JsonIgnore
    private long lastRowId;


}
