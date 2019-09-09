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
        has finished using the bank features, a file will be automatically created will all the logs/actions theyr
        preformed while they were logged in.
*/


// libraries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;

class checking{
    public static void main(String[] args)throws FileNotFoundException{
        // 2-D array: row -> users | Column -> Account Info for each Users
        String storage[][] = fileDimensions();
        // Stores Users to 2-D Array
        getUsers(storage);
        menu(storage);
    }

    public static void menu(String storage[][]){
        try{
            // Prints out Menu
            Scanner scnr = new Scanner(System.in);
            System.out.println("***********************");
            System.out.println("Welcome To Your Bank");
            System.out.println("***********************");
            System.out.println("Please Select Your Account Number");

            for(int i = 0; i < storage.length; i++){
                System.out.println((i+1) + ". \t" + storage[i][2]);
            }
            System.out.print("\nAccount Number: ");
            int menuChoice = scnr.nextInt();
            menuChoice-=1;
            // User choice is not in accounts
            if(menuChoice < 0 || menuChoice >= storage.length){
                System.out.println("Invalid Account Number! Please Select 1 - " + storage.length);
                menu(storage);
            }
            // Creates/Set-up File 
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.println("User: " + storage[menuChoice][0] + " " + storage[menuChoice][1]);
            logWriter.println("Account: " + storage[menuChoice][2]);
            logWriter.println("\nDescription:\tDestination:\tAmount:\tBalance:");
            logWriter.close();
            
            userMenu(menuChoice, storage);
        // Catches if user inputs characters rather than numbers/integers and if user log file has some errors.
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input!");
            menu(storage);
        }catch(IOException eo){
            System.out.println("Error File!");
        }
    }

    public static void userMenu(int menuChoice, String storage[][]){
        try{
            // Prints out User menu: Deposit, Withdraw, etc.
            Scanner scnr = new Scanner(System.in);
            System.out.println("\n********************");
            System.out.println("* Account Overview *");
            System.out.println("********************");
            System.out.println("Welcome " + storage[menuChoice][0] + " " + storage[menuChoice][1]);
            System.out.println("Current Balance: " + storage[menuChoice][5]);
            System.out.println("=====================");
            System.out.println("1. Pay Someone");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Select: ");
            int userDo = scnr.nextInt();

            if(userDo == 1)paySomeone(menuChoice, storage);
            else if(userDo == 2)deposit(menuChoice, storage);
            else if(userDo == 3)withdraw(menuChoice, storage);
            else if(userDo == 4)System.exit(0);
            // User selects a non-existent menu choice
            else{
                System.out.println("Invalid Account Action");
                userMenu(menuChoice, storage);
            }
        // User enters charatcers or special characters rather than integers.
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input!");
            userMenu(menuChoice, storage);
        }
    }

    public static void paySomeone(int userAccount, String db[][]){
        try{
            // Pay someone menu
            Scanner scnr = new Scanner(System.in);
            int counter = 1;
            System.out.println("\n********************");
            System.out.println("* Pay Someone *");
            System.out.println("********************");

            for(int i = 0; i < db.length; i++){
                if(i != userAccount){
                    System.out.println(counter + ". \t" + db[i][2]);
                    counter++;
                }
            }

            System.out.print("Seclect Account to Pay: ");
            int payChoice = scnr.nextInt();

            if(payChoice < 0 || payChoice >= db.length){
                    System.out.println("Invalid Account Number! Please Select 1 - " + (db.length-1));
                    paySomeone(userAccount, db);
            }
            // User Amount
            System.out.print("Amount: $");
            double amount = scnr.nextDouble();
            // If the amount is greater than the money in their balance
            if(amount > Double.parseDouble(db[userAccount][5])){
                System.out.println("Please select an amount lower than $" + db[userAccount][5]);
                paySomeone(userAccount, db);
            }
            // otherwise we will update the 2D array and start writing our logs in file
            if(amount < Double.parseDouble(db[userAccount][5])){
                // Updates 2-D Array
                db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - amount);
                db[payChoice][5] = Double.toString(Double.parseDouble(db[payChoice][5]) - amount);
                // Info that will be stored in log file
                String userAction = "Payed: " + db[payChoice][0] + " " + db[payChoice][1] + "\tAmount: $" + amount + "\t Current Balance: $" + db[userAccount][5];
                userLogs(userAction, userAccount, db);
            }
            userMenu(userAccount, db);
        // Catches if user enters something other than doubles
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            paySomeone(userAccount, db);   
        }
    }
    
    public static void deposit(int userAccount, String db[][]){
        try{
            // Deposit Menu
            Scanner scnr = new Scanner(System.in);
            System.out.println("\n********************");
            System.out.println("* Deposit *");
            System.out.println("********************");
            System.out.print("Deposit Amount: $");

            // User Amount
            double depositAmount = scnr.nextDouble();

            // Updates 2-D Array
            db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) + depositAmount);

            // Update log file
            String userAction = "Deposit: " + db[userAccount][0] + " " + db[userAccount][1] + "\tAmount: $" + depositAmount + "\t Current Balance: $" + db[userAccount][5];
            userLogs(userAction, userAccount, db);
            userMenu(userAccount, db);
        // catches if user inputs something other than doubles
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            deposit(userAccount, db);    
        }
    }

    public static void withdraw(int userAccount, String db[][]){
        try{
            // Withdraw Menu
            Scanner scnr = new Scanner(System.in);
            System.out.println("\n********************");
            System.out.println("* Withdraw *");
            System.out.println("********************");
            System.out.print("Withdraw Amount: $");
            // withdraw amount
            double withdrawAmount = scnr.nextDouble();

            // Resets if withdraw amount is greater than balance amount
            if(withdrawAmount > Double.parseDouble(db[userAccount][5])){
                System.out.println("Please select an amount lower than $" + db[userAccount][5]);
                withdraw(userAccount, db);
            }
            // Updates info if withdraw amount is greater than or equal to the user balance 
            if(withdrawAmount <= Double.parseDouble(db[userAccount][5])){
                db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - withdrawAmount);
                String userAction = "Withdrew: " + db[userAccount][0] + " " + db[userAccount][1] + "\tAmount: $" + withdrawAmount + "\t Current Balance: $" + db[userAccount][5];
                userLogs(userAction, userAccount, db);
            }
            userMenu(userAccount, db);

        // catches if user inputs something other than doubles
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            withdraw(userAccount, db);     
        }
    }

    public static void userLogs(String accountActions, int userAccount, String db[][]){
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
            userLogs(accountActions, userAccount, db);
        }

    }
    public static void getUsers(String[][] s)throws FileNotFoundException{
        // Stores 'Bank Users.txt' into 2-D array
        Scanner scnr = fileScnr();
        String line = scnr.nextLine();
        int index = 0;

        while(scnr.hasNextLine()){
            line = scnr.nextLine();
            // Splits info into tab sections
            String[] splitter = line.split("\t");
            for(int i = 0; i < splitter.length; i++){
                s[index][i] = splitter[i];
            }
            index++;
        }
    }

    public static Scanner fileScnr()throws FileNotFoundException{
        // prepares file and to be read. Throws Error if Bank Users.txt does not exists
        try{
            File file = new File("Bank Users.txt");
            Scanner scnr = new Scanner(file);
            return scnr;
        }catch(FileNotFoundException FNFE){
            System.out.println("File was not Found. Please make sure you have \"Bank Users.txt\"");
            System.exit(0);
        }
        return null;
    }

    public static String[][] fileDimensions()throws FileNotFoundException{
        // Gets File info
        Scanner scnr = fileScnr();
        int row = 0;
        int column = 0;
        int prevColumn;

        // Gets File mesurements for the 2D array to be created
        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            String splitter[] = line.split("\t");
            prevColumn = column;

            // if the amount of items in the previous row is different from the current row, then we will execute error
            if(prevColumn != column){
                System.out.println("Error with input file. Please make sure you have the correct file format");
                System.exit(0);
            }
            column = splitter.length;
            row++;
        }
        // returns 2D array Info
        String storage[][] = new String[row-1][column+1];
        return storage;
    }
}