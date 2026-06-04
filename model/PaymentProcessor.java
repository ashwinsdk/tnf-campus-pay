package model;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface PaymentProcessor {
    void processPayment(Student student, CampusPayment campusPayment, Connection con) throws SQLException;

}
