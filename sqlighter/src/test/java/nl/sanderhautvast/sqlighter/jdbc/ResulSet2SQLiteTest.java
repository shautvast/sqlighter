package nl.sanderhautvast.sqlighter.jdbc;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResulSet2SQLiteTest {

    @Mock
    private ResultSet jdbcResult;

    @Mock
    private ResultSetMetaData metaData;

    @Test
    public void testWhatGoesInShouldComeOut() throws SQLException {
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnType(1)).thenReturn(Types.INTEGER);
        when(metaData.getColumnType(2)).thenReturn(Types.VARCHAR);

        when(jdbcResult.getMetaData()).thenReturn(metaData);

        List<List<?>> table = List.of(
                List.of(1L, "Dr. John"),
                List.of(2L, "Allen Toussaint"));
        StatefulAnswer inputRows = new StatefulAnswer(table.iterator());

        when(jdbcResult.next()).thenAnswer(invocationOnMock -> inputRows.hasNext());
        when(jdbcResult.getLong(anyInt())).thenAnswer(inputRows);
        when(jdbcResult.getString(anyInt())).thenAnswer(inputRows);

        ArrayList<LtRecord> rows = new ArrayList<>();
        ResulSet2SQLite resulSet2SQLite = new ResulSet2SQLite(jdbcResult.getMetaData());
        while (jdbcResult.next()) {
                rows.add(resulSet2SQLite.mapRow(jdbcResult));
        }
        assertEquals(2, rows.size());
        LtRecord record = rows.get(0);
        LtValue value = record.getValue(0);
        assertArrayEquals(new byte[]{9}, value.getType());

        value = record.getValue(1);
        assertEquals(8, value.getValue().length);

        record = rows.get(1);
        value = record.getValue(0);
        assertArrayEquals(new byte[]{2}, value.getValue());

        value = record.getValue(1);
        assertEquals(15, value.getValue().length);
    }

    static class StatefulAnswer implements Answer<Object> {

        private final Iterator<List<?>> records;
        private List<?> current;

        StatefulAnswer(Iterator<List<?>> records) {
            this.records = records;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) {
            int colIndex = invocationOnMock.getArgument(0);
            return current.get(colIndex - 1); // ResultSet column index starts with 1
        }

        boolean hasNext() {
            boolean hasNext = records.hasNext();
            if (hasNext) {
                current = records.next();
            }
            return hasNext;
        }
    }
}