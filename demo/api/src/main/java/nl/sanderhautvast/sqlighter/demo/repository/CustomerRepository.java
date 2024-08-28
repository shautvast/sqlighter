package nl.sanderhautvast.sqlighter.demo.repository;


import nl.sanderhautvast.sqlighter.Database;
import nl.sanderhautvast.sqlighter.DatabaseBuilder;
import nl.sanderhautvast.sqlighter.data.LtRecord;
import nl.sanderhautvast.sqlighter.data.LtValue;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    public Database getAllCustomersAsSQLite() {
        DatabaseBuilder databaseBuilder = new DatabaseBuilder();
        databaseBuilder.addSchema("customers",
                "create table customers (name varchar(100), email varchar(100), streetname varchar(100), housenumber integer, city varchar(100), country varchar(100))");

        final RandomStuffGenerator generator = new RandomStuffGenerator();
        long rowid = 1;

        for (int i = 0; i < 100_000; i++) {
            LtRecord record = new LtRecord(rowid++);
            record.addValues();

            String firstName = generator.generateFirstName();
            String lastName = generator.generateLastName();
            record.addValues(LtValue.of(firstName + " " + lastName),
                    LtValue.of(firstName + "." + lastName + "@icemail.com"),
                    LtValue.of(generator.generateStreetName()),
                    LtValue.of(generator.generateSomeNumber()),
                    LtValue.of(generator.generateSomeCityInIceland()),
                    LtValue.of(generator.generateIceland()));

            databaseBuilder.addRecord(record);
        }
        return databaseBuilder.build();
    }
}
