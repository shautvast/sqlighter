package nl.sanderhautvast.sqlighter;

import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ControlTest {
    public static void main(String[] args) {
        System.out.println(Long.toString(520 * 4096L, 16));
    }

    @Test
    public void createDatabaseUsingSqliteJdbc() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:lite.db")) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("drop table if exists foo");
            }
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("create table foo (bar integer, baz varchar(1002))");
                String filler = "Hhgtttg".repeat(167);

                for (int i = 0; i < 2000; i++) {
                    statement.executeUpdate("insert into foo (bar, baz) values (42, '" + filler + "')");
                }
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("vacuum");
            }
        }
    }


    @Test
    public void createDatabaseUsingSqlighter() throws IOException {
        DatabaseBuilder databaseBuilder = new DatabaseBuilder();
        databaseBuilder.addSchema("foo",
                "CREATE TABLE foo(bar integer,baz varchar(1002))");

        for (int i = 0; i < 2000; i++) {
            LtRecord record = new LtRecord(i);
            record.addValues(LtValue.of(42), LtValue.of("Hhgtttg".repeat(167)));
            databaseBuilder.addRecord(record);
        }

        databaseBuilder.build().write("light.db");
    }
}
