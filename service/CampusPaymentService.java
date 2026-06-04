package service;

import exception.FeeAlreadyPaid;
import exception.StudentNotFoundException;
import model.campus_payments.PaymentProcessor;
import model.campus_payments.CampusPayment;
import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

public class CampusPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(CampusPaymentService.class);

    private final WalletService walletService = new WalletService();

    public void payFee(Student student, CampusPayment payment, Connection con) throws IllegalArgumentException, SQLException {

        logger.info("Campus payment started");


        String query1 = "SELECT * FROM campus_payment WHERE id=? ";

        try (PreparedStatement ps = con.prepareStatement(query1)) {
            ps.setInt(1,student.getId());
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                throw new StudentNotFoundException("Student not found");
            }
            if(rs.getBoolean(payment.getTypeOfFine())){
                throw new FeeAlreadyPaid("Fee already paid");
            }
        }
        catch (StudentNotFoundException e){
            logger.info("Student not found with id={} ",student.getId());
            throw e;
        }
        catch(FeeAlreadyPaid e){
            logger.info("Fee Already Paid for campus payment");
            throw e;
        }
        catch (SQLException e) {
            logger.info("Payment failed");
            throw e;
        }

        Predicate<Integer> validAmount = amount -> amount > 0;

        if(!validAmount.test(payment.getFee())) {
            throw new IllegalArgumentException("Invalid fee amount");
        }

        PaymentProcessor processor = (s, p, connection) -> {
            logger.info("Processing {} for student {}", p.getTypeOfFine(), s.getId());

            walletService.withdraw(connection, p.getFee(), s.getId(), p.getTypeOfFine());

            logger.info("{} paid successfully",p.getTypeOfFine());

            String query2 = "UPDATE campus_payment SET " + p.getTypeOfFine() + " = true WHERE id = ?";
            try(PreparedStatement ps2 = con.prepareStatement(query2)){
                ps2.setInt(1,s.getId());
                int rows = ps2.executeUpdate();
                logger.info("updating campus_payment with id={}", s.getId());
                if(rows == 0){
                    throw new StudentNotFoundException("Student whose value we are updating is not found");
                }

            }
            catch(StudentNotFoundException e){
                System.out.println(e.getMessage());
                throw e;
            }


        };



        try {
            processor.processPayment(student, payment, con);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}