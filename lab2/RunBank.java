// Libraries
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.text.DecimalFormat;

public class RunBank{
    public static void main(String[] args){
        if(!fileExistence()){
            System.out.println("Error reading file");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }
        ArrayList<Person> users  = new ArrayList<Person>();
        ArrayList<Account> checking = new ArrayList<Account>();
        ArrayList<Account> savings = new ArrayList<Account>();
        ArrayList<Credit> credit = new ArrayList<Credit>();
        createBank(users, checking, savings, credit);
        login(users, checking, savings, credit);
    }

    public static void login(ArrayList<Person> users, ArrayList<Account> checking, ArrayList<Account> savings, ArrayList<Credit>credit){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome");
        System.out.println("Please Enter Account Number:");
        System.out.print("> ");
        int accountNumber = scnr.nextInt();
        int i;
        for(i = 0; i < checking.size(); i++){
            if(checking.get(i).getAccountNumber() == accountNumber){
                System.out.println("Found");
                break;
            }
        }
        userMenu(i, users, checking, savings, credit);
    }

    public static void userMenu(int index, ArrayList<Person> users, ArrayList<Account> checking, ArrayList<Account> savings, ArrayList<Credit>credit){
        int exit = 0;

        while(exit != 1){
            System.out.println("**********************");
            System.out.println("* Welcome: " + users.get(index).getFirstName() + " " + users.get(index).getLastName() + " *");
            System.out.println("**********************");
            System.out.println("1. Get Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Pay Someone");
            System.out.println("6. Exit");

            Scanner scnr = new Scanner(System.in);
            int userChoice = scnr.nextInt();
        
            if(userChoice == 1)getBalance(index, checking, savings, credit);
            if(userChoice == 2)deposit(index, checking, savings);
            if(userChoice == 3)withdraw();
            if(userChoice == 4)transfer();
            if(userChoice == 5)paySomeone();
            if (userChoice == 6)exit = 1;
        }
        System.exit(0);
    }

    public static boolean getBalance(int index, ArrayList<Account> checking, ArrayList<Account> savings, ArrayList<Credit>credit){
        System.out.println("Plese choose which account to view!");
        System.out.println("1. Checking");
        System.out.println("2. Savings");
        System.out.println("3. Credit");
        System.out.println("4. Exit");

        Scanner scnr = new Scanner(System.in);
        int userChoice = scnr.nextInt();

        if(userChoice == 1){
            System.out.println("\nChecking Balance: " + checking.get(index).getBalance() + " \n");
            return true;
        }
        if(userChoice == 2){
            System.out.println("\nSavings Balance: " + savings.get(index).getBalance() + " \n");
            return true;
        }
        if(userChoice == 3){
            System.out.println("\nCredit Balance: " + credit.get(index).getBalance() + " \n");
            return true;
        }
        return true;
    }

    public static void deposit(int index, ArrayList<Account> checking, ArrayList<Account> savings){
        System.out.println("****************");
        System.out.println("*    Deposit   *");
        System.out.println("****************");
        



    }
    public static void withdraw(){}
    public static void transfer(){}
    public static void paySomeone(){}


    public static void createBank(ArrayList<Person> users, ArrayList<Account> checking, ArrayList<Account> savings, ArrayList<Credit> credit){
        try{
            File file = new File("Bank Users2.txt");
            Scanner scnr = new Scanner(file);
            String line = scnr.nextLine();
            int customerInfo = 0;

            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                String[] splitter = line.split("\t");
                Person p = new Customer(splitter[0],splitter[1], splitter[2], splitter[3], splitter[4], splitter[5]);
                Account a = new Checking(Integer.parseInt(splitter[6]), Double.parseDouble(splitter[9]));
                Account b = new Savings(Integer.parseInt(splitter[7]), Double.parseDouble(splitter[10]));
                Credit c = new Credit(Integer.parseInt(splitter[8]), Double.parseDouble(splitter[11])); 
                users.add(p);
                checking.add(a);
                savings.add(b);
                credit.add(c);
            }
        }catch(FileNotFoundException FNFE){
            System.out.println("Error creating bank users");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }

    }

    public static boolean fileExistence(){
        // checks if file exists
        try{
            File file = new File("Bank Users2.txt");
            Scanner scnr = new Scanner(file);
            int counter = 0;

            if(!scnr.hasNextLine())return false;
            scnr.nextLine();
            //makes sure that Bank Users.txt has at least 1 line of user info
            while(scnr.hasNextLine()){
                String line = scnr.nextLine();
                counter++;
            }
            if(counter <= 0)return false;
            return true;
        }catch(FileNotFoundException FNFE){
            return false;
        }
    }
}