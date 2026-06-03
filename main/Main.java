package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws  Exception{

        Scanner sc = new Scanner(System.in);
        Boolean ch = true;

        // Keep showing the menu until the user chooses to exit
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
            switch (choice){}
        }
    }
}