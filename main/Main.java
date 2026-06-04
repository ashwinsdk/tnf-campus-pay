package main;

import config.DatabaseConfig;
import model.Student;
import service.StudentServiceImplementation;
import service.WalletService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws  Exception{

        Scanner sc = new Scanner(System.in);
        Boolean ch = true;
        StudentServiceImplementation studentService = new StudentServiceImplementation();
        WalletService wallet = new WalletService();
        Connection conn = null;
        int currentStudentId = 0;

        // Keep showing the menu until the user chooses to exit
        try {
            conn = DatabaseConfig.getConnection();
            while (ch) {
                System.out.println();
                System.out.println("------------------------");
                System.out.println("WELCOME TO TAYLOR AND FRANCIS CAMPUS PAY!");
                System.out.println("------------------------");
                System.out.println(
                        "1. Register Student \n" +
                                "2. Create Wallet \n" +
                                "3. Pay & Wallet Operations \n" +
                                "4. Split Expense\n" +
                                "5. View Transaction History \n" +
                                "6. logout\n" +
                                "7. Exit\n");
                System.out.println("------------------------");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:{

                        System.out.println("Enter id:");
                        int id = sc.nextInt();

                        if (studentService.checkDuplicateId(conn,id))
                        {
                            System.out.println("Enter name:");
                            sc.nextLine();
                            String name = sc.nextLine();
                            System.out.println("Enter course:");
                            String course= sc.nextLine();
                            System.out.println("Enter email:");
                            String email= sc.nextLine();
                            System.out.println("Enter phone:");
                            String phone= sc.nextLine();

                            studentService.registerStudent(conn,new Student(id, name, course, email, phone, true));
                        }
                        else {
                            System.out.println("[ERROR] This studentId already is already registered");
                            break;
                        }
                        break;
                    }
                    case 2:{
                        System.out.println("Enter your student id: ");
                        int studentId  = sc.nextInt();

                        if (wallet.checkStudentId(conn, studentId)){
                            System.out.println("Enter initial Balance");
                            double balance = sc.nextDouble();
                        }
                        else {
                            System.out.println("[ERROR] This studentId already has an wallet");
                            break;
                        }
                        break;
                    }
                    case 3:{}
                    case 4:{}
                    case 5:{}
                    case 6:{}
                    case 7:{}
                    default:{
                        System.out.println("See you again!");
                        System.exit(1);
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
