// libraries
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.io.*;

class checking{
    public static void main(String[] args)throws FileNotFoundException{
        String storage[][] = fileDimensions();
        String logs[]= new String[10];
        getUsers(storage);
        menu(storage, logs);
    }

    public static void menu(String storage[][], String logs[]){
        try{
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

            if(menuChoice < 0 || menuChoice >= storage.length){
                System.out.println("Invalid Account Number! Please Select 1 - " + storage.length);
                System.exit(0);
            }
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.println("User: " + storage[menuChoice][0] + " " + storage[menuChoice][1]);
            logWriter.println("Account: " + storage[menuChoice][2]);
            logWriter.println("Description:\tDestination:\tAmount:\tBalance:");
            logWriter.close();
            
            userMenu(menuChoice, storage);
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input!");
            menu(storage, logs);
        }catch(IOException eo){
            System.out.println("Error File!");
        }
    }

    public static void userMenu(int menuChoice, String storage[][]){
        try{
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
            else{
                System.out.println("Invalid Account Action");
                userMenu(menuChoice, storage);
            }
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input!");
            userMenu(menuChoice, storage);
        }
    }

    public static void paySomeone(int userAccount, String db[][]){
        try{
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

            System.out.print("Amount: $");
            double amount = scnr.nextDouble();

            if(amount > Double.parseDouble(db[userAccount][5])){
                System.out.println("Please select an amount lower than $" + db[userAccount][5]);
                paySomeone(userAccount, db);
            }
            if(amount < Double.parseDouble(db[userAccount][5])){
                db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - amount);
                db[payChoice][5] = Double.toString(Double.parseDouble(db[payChoice][5]) - amount);
                String userAction = "Payed: " + db[payChoice][0] + " " + db[payChoice][1] + "\tAmount: $" + amount + "\t Current Balance: $" + db[userAccount][5];
                userLogs(userAction, userAccount, db);
            }
            userMenu(userAccount, db);
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            paySomeone(userAccount, db);   
        }
    }
    
    public static void deposit(int userAccount, String db[][]){
        try{
            Scanner scnr = new Scanner(System.in);
            System.out.println("\n********************");
            System.out.println("* Deposit *");
            System.out.println("********************");
            System.out.print("Deposit Amount: $");
            double depositAmount = scnr.nextDouble();
            db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) + depositAmount);
            String userAction = "Deposit: " + db[userAccount][0] + " " + db[userAccount][1] + "\tAmount: $" + depositAmount + "\t Current Balance: $" + db[userAccount][5];
            userLogs(userAction, userAccount, db);
            userMenu(userAccount, db);
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            deposit(userAccount, db);    
        }
    }

    public static void withdraw(int userAccount, String db[][]){
        try{
            Scanner scnr = new Scanner(System.in);
            System.out.println("\n********************");
            System.out.println("* Withdraw *");
            System.out.println("********************");
            System.out.print("Withdraw Amount: $");
            double withdrawAmount = scnr.nextDouble();

            if(withdrawAmount > Double.parseDouble(db[userAccount][5])){
                System.out.println("Please select an amount lower than $" + db[userAccount][5]);
                withdraw(userAccount, db);
            }
            if(withdrawAmount <= Double.parseDouble(db[userAccount][5])){
                db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - withdrawAmount);
                String userAction = "Withdrew: " + db[userAccount][0] + " " + db[userAccount][1] + "\tAmount: $" + withdrawAmount + "\t Current Balance: $" + db[userAccount][5];
                userLogs(userAction, userAccount, db);
            }
            userMenu(userAccount, db);
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input! Please enter an amount.");
            withdraw(userAccount, db);     
        }
    }

    public static void userLogs(String accountActions, int userAccount, String db[][]){
        try{
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file, true);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.println(accountActions);
            logWriter.close();
        }catch(IOException eo){
            System.out.println("fdsfa");
        }

    }
    public static void getUsers(String[][] s)throws FileNotFoundException{
        Scanner scnr = fileScnr();
        String line = scnr.nextLine();
        int index = 0;

        while(scnr.hasNextLine()){
            line = scnr.nextLine();
            String[] splitter = line.split("\t");
            for(int i = 0; i < splitter.length; i++){
                s[index][i] = splitter[i];
            }
            index++;
        }
    }

    public static Scanner fileScnr()throws FileNotFoundException{
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
        Scanner scnr = fileScnr();
        int row = 0;
        int column = 0;
        int prevColumn;

        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            String splitter[] = line.split("\t");
            prevColumn = column;

            if(prevColumn != column){
                System.out.println("Error with input file. Please make sure you have the correct file format");
                System.exit(0);
            }
            column = splitter.length;
            row++;
        }
        String storage[][] = new String[row-1][column+1];
        return storage;
    }
}