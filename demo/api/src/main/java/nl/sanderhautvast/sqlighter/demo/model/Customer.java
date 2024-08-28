package nl.sanderhautvast.sqlighter.demo.model;

import lombok.*;

@Data
public class Customer {
    String name;
    String email;
    String streetname;
    int housenumber;
    String city;
    String country;
}
