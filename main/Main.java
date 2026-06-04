package main;

import config.DatabaseConfig;
import exception.*;
import model.*;
import repository.jdbc.*;
import repository.*;
import service.*;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws  Exception{

        Scanner sc = new Scanner(System.in);
        Boolean ch = true;
        StudentServiceImplementation studentService = new StudentServiceImplementation();
        WalletService wallet = new WalletService();
        SplitwiseService splitwiseService = new SplitwiseService(
                new GroupExpenseRepositoryJdbc(),
                new GroupMemberRepositoryJdbc(),
                new ExpenseSplitRepositoryJdbc()
        );
        CampusPaymentService campus = new CampusPaymentService();
/*
        // campus pay
        InstituteMoney im = new InstituteMoney();
        CampusPayment cm = new WorkShop();
        CampusPaymentService cp = new CampusPaymentService();
        cp.payFine(im,cm,conn,1,"TRANSFER");
        StudentServiceImplementation st = new StudentServiceImplementation();
        Student stud = st.getStudentById(connection,2);
        cp.payFee(stud,cm,connection);
        System.out.println("payFine called");

*/
        Connection conn = null;


        // Keep showing the menu until the user chooses to exit
        try {
            conn = DatabaseConfig.getConnection();
            while (ch) {
                System.out.println();
                System.out.println("------------------------");
                System.out.println("WELCOME TO TAYLOR AND FRANCIS CAMPUS PAY!");
                System.out.println("------------------------");
                System.out.println(
                                "1. Create Student \n" +
                                "2. Pay & Wallet Operations \n" +
                                "3. Split Expense \n" +
                                "4. View Transaction History\n" +
                                "5. View Report \n" +
                                "6. Campus Pay\n" +
                                "7. Exit\n");
                System.out.println("------------------------");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:{
                        // Student Creation
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
                            break;
                        }
                        else {
                            System.out.println("[ERROR] This studentId already is already registered");
                            break;
                        }
                    }
                    case 2:{
                        // Wallet operations
                        boolean walletch = true;
                        while (walletch){
                        System.out.println("--- Wallet Operations ---");
                        System.out.println(
                                        "1. Create Wallet \t" +
                                        "2. Pay / transfer \t" +
                                        "3. Withdraw \t" +
                                        "4. back\t");
                        System.out.println("------------------------");
                        System.out.println("Enter wallet operation option: ");
                        int walletOpsChoice = sc.nextInt();
                            switch (walletOpsChoice){
                                case 1:{
                                    // Wallet Creation
                                    System.out.println("Enter your student id: ");
                                    int studentId  = sc.nextInt();

                                    if (wallet.checkStudentId(conn, studentId)){
                                        System.out.println("Enter initial Balance");
                                        double balance = sc.nextDouble();
                                        wallet.createWallet(conn,studentId,balance);
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] This studentId already has an wallet");
                                        break;
                                    }
                                }
                                case 2:{
                                    // Pay students
                                    System.out.println("Enter your wallet id: ");
                                    int fromId = sc.nextInt();

                                    System.out.println("Enter the TO wallet id: ");
                                    int toId = sc.nextInt();

                                    if(!wallet.checkWalletId(conn,fromId) && !wallet.checkWalletId(conn, toId)){
                                        System.out.println("Enter amount: ");
                                        double amount = sc.nextDouble();
                                        wallet.transfer(conn,fromId,toId,amount,"TRANSFER");
                                        break;
                                    }
                                    else {
                                        if (!wallet.checkWalletId(conn, fromId)){
                                            System.out.println("[ERROR] Invalid FROM wallet id");
                                        }
                                        else if (!wallet.checkWalletId(conn, toId)){
                                            System.out.println("[ERROR] Invalid TO wallet id");
                                        }
                                        else {
                                            System.out.println("[ERROR] Invalid walletId values");
                                        }

                                        break;
                                    }
                                }
                                case 3:{
                                    // Withdraw
                                    System.out.println("Enter your wallet id: ");
                                    int fromId = sc.nextInt();
                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to withdraw: ");
                                        double amount = sc.nextDouble();

                                        wallet.withdraw(conn, amount, fromId, "WITHDRAW");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }
                                }
                                case 4:{
                                    // Exit wallet operations
                                    System.out.println("Exited wallet operations");
                                    walletch =false;
                                    break;
                                }
                                default:{
                                    System.out.println("[ERROR Invalid choice]");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 3:{
                        boolean splitCh = true;

                        while (splitCh){
                            // --- Integrated Splitwise Interface Submenu ---
                            System.out.println("\n--- CAMPUS EXPENSE SPLITTER ---");
                            System.out.println("1. Create New Expense Group");
                            System.out.println("2. View Group Balances & Ledger");
                            System.out.println("3. Settle a Pending Split Dues");
                            System.out.println("4. back to main menu");
                            System.out.print("Select an action: ");
                            int splitChoice = sc.nextInt();
                            sc.nextLine(); // Clear scanner buffer
                            switch (splitChoice) {
                                case 1: {
                                    try {
                                        System.out.print("Enter Group Name (e.g., Canteen, Hostel Pizza): ");
                                        String groupName = sc.nextLine();

                                        System.out.print("Enter Total Bill Amount (₹): ");
                                        double totalAmount = sc.nextDouble();

                                        System.out.print("Enter Payer Student ID: ");
                                        int createdBy = sc.nextInt();

                                        System.out.print("Enter number of other students sharing this expense: ");
                                        int count = sc.nextInt();

                                        List<Integer> members = new ArrayList<>();
                                        for (int i = 0; i < count; i++) {
                                            System.out.print("Enter Student ID #" + (i + 1) + ": ");
                                            members.add(sc.nextInt());
                                        }

                                        // === CAPTURE AND PRINT THE GENERATED ID ===
                                        int newGroupId = splitwiseService.createExpenseGroup(groupName, totalAmount, createdBy, members);

                                        if (newGroupId != -1) {
                                            System.out.println("\n=======================================================");
                                            System.out.println("[SUCCESS] Group '" + groupName + "' has been successfully committed!");
                                            System.out.println("YOUR GENERATED GROUP ID IS: " + newGroupId);
                                            System.out.println("[NOTICE] Use Group ID " + newGroupId + " in Option 2 to view this ledger.");
                                            System.out.println("=======================================================");
                                        } else {
                                            System.out.println("\n[ERROR] Group could not be saved to the database.");
                                        }

                                    } catch (InvalidInputException e) {
                                        System.out.println("[VALIDATION ERROR] " + e.getMessage());
                                    }
                                    break;
                                }
                                case 2: {
                                    try {
                                        System.out.print("Enter Group ID to lookup ledger: ");
                                        int groupId = sc.nextInt();
                                        splitwiseService.viewGroupBalances(groupId);
                                    } catch (InvalidInputException e) {
                                        System.out.println("[LOOKUP ERROR] " + e.getMessage());
                                    }
                                    break;
                                }
                                case 3: {
                                    System.out.print("Enter individual Split ID to mark as PAID: ");
                                    int splitId = sc.nextInt();
                                    splitwiseService.settleExpense(splitId);
                                    break;
                                }
                                case 4:{
                                    // Exit wallet operations
                                    System.out.println("Exited wallet operations");
                                    splitCh =false;
                                    break;
                                }
                                default:
                                    System.out.println("Invalid Split wise operation selection.");
                            }
                        }
                        break; // === PREVENTS THE CRASH FALL-THROUGH INTO CASE 5 ===

                    }
                    case 4:{
                        // View transaction history
                        System.out.println("Enter your wallet id: ");
                        int walletId = sc.nextInt();
                        wallet.getTx().showTransactionHistory(conn,walletId);
                        break;
                    }
                    case 5:{
                        // View  Report
                        wallet.getTx().showReport(conn);
                        break;
                    }
                    case 6:{
                        // Campus pay
                        boolean campusCh =true;
                        while (campusCh){

                            System.out.println("--- Campus Payments ---");
                            System.out.println(
                                    "1. Canteen \t" +
                                            "2. library fine\t" +
                                            "3. hackathon fee\t" +
                                            "4. workshop fee \t" +
                                            "5. hostel fee \t" +
                                            "6. Exit Campus payment\t");
                            System.out.println("------------------------");
                            System.out.println("Enter wallet operation option: ");
                            int campusOption = sc.nextInt();
                            switch (campusOption){
                                case 1:{ // Canteen
                                    System.out.println("Enter Canteen bill: ");
                                    double amount = sc.nextDouble();

                                    System.out.println("Enter your wallet id to pay: ");
                                    int fromId = sc.nextInt();

                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to pay Canteen: ");

                                        wallet.withdraw(conn, amount, fromId, "CANTEEN");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }
                                }
                                case 2:{
                                    // Library fine
                                    System.out.println("Enter library fine: ");
                                    double amount = sc.nextDouble();

                                    System.out.println("Enter your wallet id to pay: ");
                                    int fromId = sc.nextInt();

                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to pay library fine: ");

                                        wallet.withdraw(conn, amount, fromId, "LIBRARY");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }

                                }
                                case 3:{
                                    System.out.println("Enter Hackathon fee: ");
                                    double amount = sc.nextDouble();

                                    System.out.println("Enter your wallet id to pay: ");
                                    int fromId = sc.nextInt();

                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to pay hackathon fee: ");

                                        wallet.withdraw(conn, amount, fromId, "HACKATHON");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }

                                }
                                case 4:{
                                    // WorkShop
                                    System.out.println("Enter workshop fee: ");
                                    double amount = sc.nextDouble();

                                    System.out.println("Enter your wallet id to pay: ");
                                    int fromId = sc.nextInt();

                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to pay workshop fee: ");

                                        wallet.withdraw(conn, amount, fromId, "WORKSHOP");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }

                                }
                                case 5:{
                                    System.out.println("Enter hostel fee: ");
                                    double amount = sc.nextDouble();

                                    System.out.println("Enter your wallet id to pay: ");
                                    int fromId = sc.nextInt();

                                    if (!wallet.checkWalletId(conn, fromId)){
                                        System.out.println("Enter amount to pay hostel fee: ");

                                        wallet.withdraw(conn, amount, fromId, "HOSTEL");
                                        break;
                                    }
                                    else {
                                        System.out.println("[ERROR] Invalid wallet id");
                                        break;
                                    }

                                }
                                case 6:{
                                    // Exit Campus payments
                                    System.out.println("Exited campus payments");
                                    campusCh = false;
                                    break;
                                }
                                default:{
                                    System.out.println("[ERROR Invalid choice]");
                                    break;}
                            }
                        }
                        break;
                    }
                    case 7:{
                        System.out.println("See you again!");
                        System.exit(1);
                    }
                    default:{
                        System.out.println("[ERROR Invalid choice]");
                        break;
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
