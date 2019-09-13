/*
    Author: Rigoberto Quiroz
    Alias: Rigo
    Date: 09/08/19
    Course: CS3331
    Lab: Programming Assignment 1
    Lab Description:
        For this lab I had to create a Checking Class. This Checking class will replicate some Bank Features such as
        get balance, deposit, withdraw, and pay someone. We will get user information from a txt file, which contains
        firstname, lastname, account number, checking, savings, balance, and interest rate. Once the information has been
        stored the into a data strcuture, I created some methods that would handle the Bank Features. After the user
        has finished using the bank features, a file will be automatically created with all the logs/actions they
        preformed while they were logged in.
    Honesty Statement:
        I confirm that the work of this assignment is completely my own. By turning in this assignment. 
        I declare that I did not receive unauthorized assistance. Moreover, all deliverables including, 
        but not limited to the source code, lab report and output files were written and produced by me alone.
    Assumptions:
        1. "Bank users.txt" will always have the same number of columns, and the columns will always be in the 
        same order (first name, lastname, account number, checking, savings, balance and interest rate)
        2. Each User will be able to log in once, there is not feature for them to log-out of their account
        and access other accounts. (Only way of doing that would be to exit program and re-execute program)
        3. "Bank users.txt" is not being updated when user does an action EX: Balance. Each time the program 
        executes the data will be reset to its original data set.
        3. "Bank Users.txt" will always have the first row of data (firstname, lastname, account info, etc.) and
        Bank Users.txt will always have more than 1 user.
        4. "Log.txt" will only record the actions of one users account and not record other changes that happen
        to other users. If Pluto decides to pay Micky, then the log file will only record the changes made in
        Pluo's account, but it will not display the changes made in Micky's account. 
*/


// libraries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.text.DecimalFormat;

public class main{
    public static void main(String[] args){
        // makes sure the file exists and has at least 1 line of information
        if(!fileExistence()){
            System.out.println("Error reading file");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }
        // Creates a Linked-List from 'User-Bank.txt'
        Checking bank = createBank();
        // Creates temp bank for us to traverse
        Checking bankTemp = bank;
        // user is not logged in
        boolean login = false;
        Scanner scnr = new Scanner(System.in);


        // user can keep trying logging in until they get it.
        while(login == false){
            try{
                System.out.println("Welcome.\nEnter Account Number to Start");
                System.out.print("> ");
                int userAccountNumber = scnr.nextInt();

                // traverse linked list
                while(bankTemp != null){
                    // found account!
                    if(bankTemp.getAccountNumber() == userAccountNumber){
                        login = true;
                        break;
                    }
                    bankTemp = bankTemp.next;
                }
                // reset banktemp to head if user did not find account
                if(!login)bankTemp = bank;
                // Catches errors made if user inputs wrong data type
            }catch(InputMismatchException IME){
                System.out.println("Please enter your account number.");
                System.out.println("Do not inlcude any characters or special characters.");
                System.exit(0);
            }
        }
        // creates log file
        try{
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.println("User: " + bankTemp.getFirstName() + " " + bankTemp.getLastName());
            logWriter.println("Account: " + bankTemp.getAccountNumber());
            logWriter.println("Starting Balance: " + bankTemp.getCheckingBalance());
            logWriter.println("\nDescription:\tDestination:\tAmount:\tBalance:");
            logWriter.close();
            // catches error from log txt files
        }catch(IOException IOE){
            System.out.println("Error File!");
            System.exit(0);
        }
        userLoginMenu(bankTemp, bank);
    }

    public static void userLoginMenu(Checking user, Checking bank){
        try{
            // User menu
            Scanner scnr = new Scanner(System.in);
            System.out.println("***********************");
            System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName());
            System.out.println("***********************");
            System.out.println("1. See Account Information");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Pay Someone");
            System.out.println("5. Exit");
            System.out.print("> ");

            int userAction = scnr.nextInt();

            // user enters input that is not in menu
            if(userAction > 5 || userAction < 1){
                System.out.println("\n********************");
                System.out.println("Invalid menu choice...");
                System.out.println("********************\n");
                userLoginMenu(user, bank);
            }

            // Checking Features
            if(userAction == 1)userAccountInfo(user, bank);
            if(userAction == 2)deposit(user, bank);
            if(userAction == 3)withdraw(user, bank);
            if(userAction == 4)paySomeOne(user, bank);
            else{
                printUserAccounts(bank);
                System.exit(0);
            }
            // user inputs wrong data type
        }catch(InputMismatchException IME){
            System.out.println("\n********************");
            System.out.println("Falied to do request.");
            System.out.println("Do not inlcude any characters or special characters.");
            System.out.println("********************\n");
            userLoginMenu(user, bank);
        }
    }

    public static void printUserAccounts(Checking bank){
        try{
            File file = new File("User Bank Accounts.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter updateUserAccounts = new PrintWriter(writer);

            while(bank != null){
                updateUserAccounts.print(bank.getFirstName() + "\t");
                updateUserAccounts.print(bank.getLastName() + "\t");
                updateUserAccounts.print(bank.getAccountNumber() + "\t");
                updateUserAccounts.print(bank.getChecking() + "\t");
                updateUserAccounts.print(bank.getSavings() + "\t");
                updateUserAccounts.print(bank.getCheckingBalance() + "\t");
                updateUserAccounts.print(bank.getInterestRate() + "\n");
                bank = bank.next;
            }
            updateUserAccounts.close();
        }catch(IOException IOE){
            System.out.println("Cannot write updated Bank Users file");
            System.exit(0);
        }
    }

    public static void userAccountInfo(Checking user, Checking bank){
        // prints user information
        System.out.println("\n********************");
        System.out.println("Name: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Account Number: " + user.getAccountNumber());
        System.out.println("Checking: " + user.getChecking());
        System.out.println("Savings: " + user.getSavings());
        System.out.println("Balance: " + user.getCheckingBalance());
        System.out.println("Interest Rate: " + user.getInterestRate());
        System.out.println("********************\n");
        userLoginMenu(user, bank);
    }

    public static void deposit(Checking user, Checking bank){
        // prints Deposit menu
        Scanner scnr = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("*******");
        System.out.println("Deposit");
        System.out.println("*******");
        System.out.print("Amount: $");

        // user input amount
        double depositAmount = scnr.nextDouble();
        // Access Checking Class and computes...
        boolean success = user.deposit(depositAmount);
        // if action is unsccessful then we will retry
        if(!success)deposit(user,bank);
        // records action
        String action = "Deposit: " + user.getFirstName() + " " + user.getLastName() + "\tAmount: $" + df.format(depositAmount) + "\t Current Balance: $" + user.getCheckingBalance();
        // creates log
        userLogs(action);
        // goes back to the menu
        userLoginMenu(user, bank);
    }

    public static void withdraw(Checking user, Checking bank){
        // print withdraw menu
        Scanner scnr = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("*******");
        System.out.println("Withdraw");
        System.out.println("*******");
        System.out.print("Amount: $");
        
        // user input amount
        double withdrawAmount = scnr.nextDouble(); 
        // Access Checking Class and computes...
        boolean success = user.withdraw(withdrawAmount);
        // if action is unsccessful then we will be sent back to menu
        if(!success)userLoginMenu(user, bank);
        // records action
        String action = "Withdrew: " + user.getFirstName() + " " + user.getLastName() + "\tAmount: $" + df.format(withdrawAmount) + "\t Current Balance: $" + user.getCheckingBalance();
        // creates log
        userLogs(action);
        // menu
        userLoginMenu(user, bank);
    }
    public static void paySomeOne(Checking user, Checking bank){
        // print Pay someone menu
        DecimalFormat df = new DecimalFormat("#.##");
        Scanner scnr = new Scanner(System.in);
        boolean found = false;
        Checking bankTemp = bank;
        System.out.println("\n********************");
        System.out.println("Pay Someone");
        System.out.println("********************\n");
        System.out.println("Enter Account Number");
        System.out.print("> ");
        
        // user input account
        int targetAccount = scnr.nextInt();

        // invalid account
        if(targetAccount < 0){
            System.out.println("\n********************");
            System.out.println("Invalid Account Number...");
            System.out.println("********************\n");
            paySomeOne(user, bank);
        }
        
        // checks linked list
        while(bankTemp != null){
            if(bankTemp.getAccountNumber() == targetAccount){
                found = true;
                break;
            }
            bankTemp = bankTemp.next;
        }
        // invalid account
        if(!found){
            System.out.println("\n********************");
            System.out.println("Invalid Account Number...");
            System.out.println("********************\n");
            paySomeOne(user, bank);
        }

        // user input amount
        System.out.print("Amount: $");
        double amount = scnr.nextDouble();

        // // Access Checking Class and computes...
        boolean success = user.paySomeone(bankTemp, amount);
        // if action failed then try again
        if(!success)paySomeOne(user, bank);
        // records action
        String action = "Payed: " + bankTemp.getFirstName() + " " + bankTemp.getLastName() + "\tAmount: $" + df.format(amount) + "\t Current Balance: $" + user.getCheckingBalance();
        // creates logs
        userLogs(action);
        // menu
        userLoginMenu(user, bank);
    }

    public static void userLogs(String accountActions){
        try{
            // Appends user actions to log files
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file, true);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.println(accountActions);
            logWriter.close();
        // catches if log file has some issues
        }catch(IOException eo){
            System.out.println("Log File has some Issues. Please check.");
            userLogs(accountActions);
        }
    }


    public static Checking createBank(){
        // creates users
        System.out.println("Creating Users...");
        Checking bank = new Checking();
        Checking tempBank = bank;
        try{
            File file = new File("Bank Users.txt");
            Scanner scnr = new Scanner(file);
            String line = scnr.nextLine();

            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                String[] splitter = line.split("\t");
                // adds nodes to linked list as long as we have info in our Bank Users file
                tempBank.next = new Checking(splitter[0], splitter[1], Integer.parseInt(splitter[2]), 
                Boolean.parseBoolean(splitter[3]), Boolean.parseBoolean(splitter[4]),
                Double.parseDouble(splitter[5]), splitter[6]);
                // moves to next node
                tempBank = tempBank.next;
            }
            // Bank Users.txt was not found
        }catch(FileNotFoundException FNFE){
            System.out.println("Error creating bank users");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }
        System.out.println("Bank Created");
        // return next b/c first node is null(no info)
        return bank.next;
    }
    public static boolean fileExistence(){
        // checks if file exists
        try{
            File file = new File("Bank Users.txt");
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