package nl.sanderhautvast.sqlighter.demo.rest;

import nl.sanderhautvast.sqlighter.Database;
import nl.sanderhautvast.sqlighter.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class DemoRestApi {

    private final CustomerRepository customerRepository;

    @Autowired
    public DemoRestApi(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(value = "/api/db/customers", produces = "application/octet-stream")
    public void getDataAsSqliteFile(HttpServletResponse response) throws IOException {
        Database customers = customerRepository.getAllCustomersAsSQLite();
        customers.write(response.getOutputStream());
    }
}
