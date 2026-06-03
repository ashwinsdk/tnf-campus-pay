package main;

import config.DatabaseConfig;
import model.CampusPayment;
import model.Hackathon;
import model.Hostel;
import model.InstituteMoney;
import service.CampusPaymentService;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) throws  Exception{

        Connection connection = DatabaseConfig.getConnection();
        InstituteMoney im = new InstituteMoney();
        CampusPayment cm = new Hackathon();
        CampusPaymentService cp = new CampusPaymentService();
        cp.payFine(im,cm,connection,2,"WITHDRAW");
        System.out.println("payFine called");
    }
}