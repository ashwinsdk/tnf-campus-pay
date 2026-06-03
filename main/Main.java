package main;

import config.DatabaseConfig;
import model.*;
import service.CampusPaymentService;
import service.StudentServiceImplementation;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws  Exception{

        Connection connection = DatabaseConfig.getConnection();
        InstituteMoney im = new InstituteMoney();
        CampusPayment cm = new WorkShop();
        CampusPaymentService cp = new CampusPaymentService();
      //  cp.payFine(im,cm,connection,1,"TRANSFER");
        StudentServiceImplementation st = new StudentServiceImplementation();
        Student stud = st.getStudentById(connection,2);
        cp.payFee(stud,cm,connection);
        System.out.println("payFine called");
    }
}