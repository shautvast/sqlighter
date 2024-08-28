package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * end-to-end test that creates an SQLite database and tries to read it
 */
public class SchemaCreationTests {

    @Test
    public void newDatabase() throws SQLException, IOException {
        DatabaseBuilder databaseBuilder = new DatabaseBuilder();
        databaseBuilder.addSchema("foo",
                "CREATE TABLE foo(bar integer,baz varchar(10), xeno float)");


        LtRecord record = new LtRecord(1);
        record.addValues(LtValue.of(12), LtValue.of("hello1world"), LtValue.of(1.2));
        databaseBuilder.addRecord(record);
        LtRecord record2 = new LtRecord(2);
        record2.addValues(LtValue.of(12), LtValue.of(1.2)); // just 2 values
        databaseBuilder.addRecord(record2);

        databaseBuilder.build().write("test.db");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement statement = connection.createStatement()) {
            ResultSet result1 = statement.executeQuery("select rowid, name, tbl_name, rootpage, sql from sqlite_master");
            assertTrue(result1.next());
            assertEquals("foo", result1.getString("name"));
            assertEquals("foo", result1.getString("tbl_name"));
            assertEquals(2, result1.getInt("rootpage"));
            assertEquals("CREATE TABLE foo(bar integer,baz varchar(10), xeno float)", result1.getString("sql"));

            // baz is 'defined' as varchar,
            // but it's translated to colummn #2
            // and that contains 1.2 as double! (in row 2)
            // and the query runs without errors
            ResultSet result3 = statement.executeQuery("select baz from foo where ROWID = 2");
            assertTrue(result3.next());
            assertEquals(1.2, result3.getDouble(1));
        }
    }

    @Test
    public void test100Records() throws SQLException, IOException {
        DatabaseBuilder builder = new DatabaseBuilder();
        builder.addSchema("foo", "CREATE TABLE foo(bar integer,baz varchar(2000))");

        for (int i = 0; i < 100; i++) {
            LtRecord record = new LtRecord(i);
            record.addValues(LtValue.of(12), LtValue.of("helloworld".repeat(200)));
            builder.addRecord(record);
        }

        builder.build().write("test.db");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement statement = connection.createStatement()) {
            ResultSet result1 = statement.executeQuery("select rowid, name, tbl_name, rootpage, sql from sqlite_master");
            assertTrue(result1.next());
            assertEquals("foo", result1.getString("name"));
            assertEquals("foo", result1.getString("tbl_name"));
            assertEquals(2, result1.getInt("rootpage"));
            assertEquals("CREATE TABLE foo(bar integer,baz varchar(2000))", result1.getString("sql"));


            ResultSet result2 = statement.executeQuery("select count(*) from foo");
            assertTrue(result2.next());
            assertEquals(100, result2.getInt(1));
        }
    }
}
