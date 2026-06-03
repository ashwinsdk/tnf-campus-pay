package service;

import exception.StudentNotFoundException;
import model.CampusPayment;
import model.InstituteMoney;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CampusPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(CampusPaymentService.class);

    public void payFine(InstituteMoney im, CampusPayment campusPayment, Connection con, int fromId, String type) throws SQLException,StudentNotFoundException {
        logger.info("payFine called");
        WalletService wl = new WalletService();
        int amount = campusPayment.getFee();
        logger.debug("payFine amount = {}", amount);
        wl.withdraw(con,amount,fromId,type);
        logger.debug("Amount Withdrawn Successfully");
        String fineType = campusPayment.getTypeOfFine();
        logger.info("Amount debited to Institute treasury successfully");
        im.setFineFromStudent(fineType,amount);

        String query = "UPDATE campus_payment SET " + fineType + " = true WHERE id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1,fromId);
            int rows = ps.executeUpdate();
            logger.info("updating campus_payment with id={}", fromId);
            if(rows == 0){
                throw new StudentNotFoundException("Student whose value we are updating is not found");
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());

        }

        logger.info("fine paid successfully");

    }
}
