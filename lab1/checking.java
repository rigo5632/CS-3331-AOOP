import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.io.*;

class checking{
    public static void main(String[] args)throws FileNotFoundException{
        String storage[][] = fileDimensions();
        String logs[]= new String[5];
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
            userMenu(menuChoice, storage, logs);
        }catch(InputMismatchException IME){
            System.out.println("Invalid Input!");
            menu(storage, logs);
        }

    }

    public static void userMenu(int menuChoice, String storage[][], String logs[]){
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

        if(userDo == 1)paySomeone(menuChoice, storage, logs);
        else if(userDo == 2)deposit(menuChoice, storage, logs);
        else if(userDo == 3)withdraw(menuChoice, storage, logs);
        else if(userDo == 4){
            try{
                PrintWriter logFile = new PrintWriter(new FileWriter("User Bank Logs.txt"));
                for(int i = 0; i < logs.length; i++){
                    if(logs[i] != null)logFile.println(logs[i]);
                }
                logFile.close();
                System.exit(0);
            }catch(IOException ex){
                System.out.println("jnfkdsfhsadjkl");
            }
        }
        else{
            System.out.println("Invalid Account Action");
            userMenu(menuChoice, storage, logs);
        }
    }

    public static void paySomeone(int userAccount, String db[][], String logs[]){
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
                System.exit(0);
        }

        System.out.print("Amount: $");
        double amount = scnr.nextDouble();

        if(amount > Double.parseDouble(db[userAccount][5])){
            System.out.println("Please select an amount lower than $" + db[userAccount][5]);
            paySomeone(userAccount, db, logs);
        }
        if(amount < Double.parseDouble(db[userAccount][5])){
            db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - amount);
            db[payChoice][5] = Double.toString(Double.parseDouble(db[payChoice][5]) - amount);
            String userAction = "Payed: " + db[payChoice][0] + " " + db[payChoice][1] + " Amount: " + amount;
            userLogs(userAction, userAccount, logs);
        }
        userMenu(userAccount, db, logs);
    }
    
    public static void deposit(int userAccount, String db[][], String logs[]){
        Scanner scnr = new Scanner(System.in);
        System.out.println("\n********************");
        System.out.println("* Deposit *");
        System.out.println("********************");
        System.out.print("Deposit Amount: $");
        double depositAmount = scnr.nextDouble();
        db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) + depositAmount);
        String userAction = "Deposit made of $" + depositAmount + " to " + db[userAccount][2];
        userLogs(userAction, userAccount, logs);
        userMenu(userAccount, db, logs);
    }

    public static void withdraw(int userAccount, String db[][], String logs[]){
        Scanner scnr = new Scanner(System.in);
        System.out.println("\n********************");
        System.out.println("* Withdraw *");
        System.out.println("********************");
        System.out.print("Withdraw Amount: $");
        double withdrawAmount = scnr.nextDouble();

        if(withdrawAmount > Double.parseDouble(db[userAccount][5])){
            System.out.println("Please select an amount lower than $" + db[userAccount][5]);
            withdraw(userAccount, db, logs);
        }
        if(withdrawAmount <= Double.parseDouble(db[userAccount][5])){
            db[userAccount][5] = Double.toString(Double.parseDouble(db[userAccount][5]) - withdrawAmount);
            String userAction = "Withdraw made of $" + withdrawAmount + " to " + db[userAccount][2];
            userLogs(userAction, userAccount, logs);
        }
        userMenu(userAccount, db, logs);
    }

    public static void userLogs(String accountActions, int userAccount, String logs[]){
        if(logs[logs.length-1] != null){
            resizeLogs(logs, userAccount);
        }
        
        for(int i = 0; i < logs.length; i++){
            if(logs[i] == null){
                logs[i] = accountActions;
                break;
            }
        }
    }

    public static void resizeLogs(String logs[], int userAccount){
        String temp[] = new String[logs.length];

        for(int i = 0; i < logs.length; i++){
            temp[i] = logs[i];
        }

        logs = new String[logs.length*2];
        for(int i = 0; i < temp.length; i++){
            logs[i] = temp[i];
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