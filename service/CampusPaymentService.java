package service;

import exception.StudentNotFoundException;
import model.CampusPayment;
import model.InstituteMoney;
import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.StudentRepositoryImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CampusPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(CampusPaymentService.class);

    public void payFine(InstituteMoney im, CampusPayment campusPayment, Connection con, int fromId, String type) throws Exception {
        logger.info("payFine called");
        WalletService wl = new WalletService();
        int amount = campusPayment.getFee();
        logger.debug("payFine amount = {}", amount);
        String fineType = campusPayment.getTypeOfFine();
        StudentRepositoryImplementation studentRepository = new StudentRepositoryImplementation();
        System.out.println(fineType);
        Student institute = studentRepository.findByName(fineType);
        System.out.println(institute);
       // wl.withdraw(con,amount,fromId,type);
        wl.transfer(con,fromId,institute.getId(),amount,type);
        logger.debug("Amount Withdrawn Successfully");

        logger.info("Amount debited to Institute treasury successfully");
        im.setFineFromStudent(fineType,amount);


        String query = "UPDATE campus_payment SET " + fineType + " = true WHERE id = ?";
        try(PreparedStatement ps2 = con.prepareStatement(query)){
            ps2.setInt(1,fromId);
            int rows = ps2.executeUpdate();
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
