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
        Bank users = createBank();
        userMenu(userLogin(users), users);
    }

    public static Bank userLogin(Bank database){
        boolean login = false;
        while(!login){
            Scanner scnr = new Scanner(System.in);
            Bank tempDB = database;

            System.out.println("Enter Your Full Name: EX: Mickey Mouse");
            System.out.print("> ");
            String loginName = scnr.nextLine();

            if(loginName.compareTo("Manager") == 0){
                managerMenu(database);
            }
            while(tempDB != null){
                if(tempDB.getPersonName().compareTo(loginName) == 0){
                    login = true;
                    return tempDB;
                }
                tempDB = tempDB.next;
            }
            if(!login)System.out.println("Please try again!");
        }
        return null;
    }

    public static boolean managerMenu(Bank database){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Manager Menu");
        System.out.println("1. Inquire User Account");
        System.out.println("2. Inquire All Accounts");

        System.out.println("> ");
        int userChoice = scnr.nextInt();

        if(userChoice == 1){
            System.out.println("Enter Name of User");
            String userName = scnr.nextLine();
            Bank tempDB = database;

            while(tempDB != null){
                if(tempDB.getPersonName().compareTo(userName) == 0){
                    
                }
            }
        }



    }
    public static boolean userMenu(Bank user, Bank database){
        Scanner scnr = new Scanner(System.in);
        boolean menu = true;
        while(menu){
            System.out.println("\nWelcome: " + user.getPersonName());
            System.out.println("1. Inquire Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money/Pay Someone");
            System.out.println("5. Logout");
            System.out.print("> ");
            int userChoice = scnr.nextInt();

            if(userChoice == 1)menu = inquireBalance(user);
            else if(userChoice == 2)menu = deposit(user);
            else if(userChoice == 3)menu = withdraw(user);
            else if(userChoice == 4)menu = transfer(user, database);
            else if(userChoice == 5)menu = false;
            else{
                System.out.println("Invalid Menu Choice. Try Again!");
                menu = true;
            }
        }
        return menu;
    }

    public static boolean inquireBalance(Bank user){
        Scanner scnr = new Scanner(System.in);
        boolean inquire = true;
        
        while(inquire){
            System.out.println("\nSelect Account to inquire: ");
            System.out.println("1. Checking");
            System.out.println("2. Savings");
            System.out.println("3. Credit");
            System.out.println("4. All");
            System.out.println("5. Exit");
            System.out.print("> ");
            int userChoice = scnr.nextInt();

            if(userChoice == 1){
                System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                System.out.println("Checking Balance: " + user.getCheckingBalance());
                inquire = false;
            }
            else if(userChoice == 2){
                System.out.println("\nSavings Account Number: " + user.getSavingsAccountNumber());
                System.out.println("Savings Balance: " + user.getSavingsBalance());
                inquire = false;
            }
            else if(userChoice == 3){
                System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                System.out.println("Credit Balance: " + user.getCreditBalance());
                inquire = false;      
            }
            else if(userChoice == 4){
                System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                System.out.println("Checking Balance: " + user.getCheckingBalance());
                System.out.println("\nSavings Account Number: " + user.getSavingsAccountNumber());
                System.out.println("Savings Balance: " + user.getSavingsBalance());
                System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                System.out.println("Credit Balance: " + user.getCreditBalance());
                inquire = false;  
            }
            else if(userChoice == 5)inquire = false;
            else{
                System.out.println("Invalid Option Try again!");
                inquire = true;
            }
        }
        return true;
    }

    public static boolean deposit(Bank user){
        Scanner scnr = new Scanner(System.in);
        boolean deposit = true;

        System.out.println("\nDeposit Moeny");
        while(deposit){
            System.out.println("Select account to deposit: ");
            System.out.println("1. Checking");
            System.out.println("2. Savings");
            System.out.println("3. Credit");
            System.out.println("> ");
            int userChoice = scnr.nextInt();

            System.out.println("Amount to Deposit: ");
            double depositAmount = scnr.nextDouble();

            if(userChoice == 1){
                if(!user.checkingDeposit(depositAmount))deposit = true;
                else deposit = false;
            }
            else if(userChoice == 2){
                if(!user.savingsDeposit(depositAmount))deposit = true;
                else deposit = false;
            }
            else if(userChoice == 3){
                if(!user.creditDeposit(depositAmount))deposit = true;
                else deposit = false;
            }
            else{
                System.out.println("Invalid Option. Try Again!");
                deposit = true;
            }
        }
        return true;
    }
    public static boolean withdraw(Bank user){
        Scanner scnr = new Scanner(System.in);
        boolean withdraw = true;

        System.out.println("\nWithdraw Money");
        while(withdraw){
            System.out.println("Amount to Withdraw: ");
            double withdrawAmount = scnr.nextDouble();

            if(!user.checkingWithdraw(withdrawAmount))withdraw = true;
            else withdraw = false;
        }
        return true;
    }

    public static boolean transfer(Bank user, Bank database){
        Scanner scnr = new Scanner(System.in);
        boolean transfer = true;

        System.out.println("\nTransfer Money");
        while(transfer){
            System.out.println("Select Transfer Process");
            System.out.println("1. Your Acounts");
            System.out.println("2. Other users Account");
            int userChoice = scnr.nextInt();

            if(userChoice == 1){
                System.out.println("Select Account to transfer to:");
                System.out.println("1. Checking");
                System.out.println("2. Savings");
                userChoice = scnr.nextInt();

                System.out.println("Amount to Transfer");
                System.out.print("> ");
                double transferAmount = scnr.nextDouble();

                if(userChoice == 1){
                    if(!user.savingsTransfer(user.getChecking(), transferAmount))transfer = true;
                    else transfer = false;
                }
                else if(userChoice == 2){
                    if(!user.checkingTransfer(user.getSavings(), transferAmount))transfer = true;
                    else transfer = false;
                }else{
                    System.out.println("Invalid Option. Try Again!");
                    transfer = true;
                }
            }
            if(userChoice == 2){
                Scanner newScnr = new Scanner(System.in);
                System.out.println("Enter name of Person");
                System.out.print("> ");
                String otherUser = newScnr.nextLine();
                Bank databaseTemp = database;

                while(databaseTemp != null){
                    if(databaseTemp.getPersonName().compareTo(otherUser) == 0){
                        System.out.println("Select Account to transfer");
                        System.out.println("1. Checking");
                        System.out.println("2. Savings");
                        System.out.print("> ");
                        userChoice = newScnr.nextInt();

                        System.out.println("Amount to transfer");
                        System.out.print("> ");
                        double transferAmount = scnr.nextDouble();

                        if(userChoice == 1){
                            if(!user.checkingTransfer(databaseTemp.getChecking(), transferAmount))transfer = true;
                            else transfer = false;
                        }
                        else if(userChoice == 2){
                            if(!user.savingsTransfer(databaseTemp.getSavings(), transferAmount))transfer = true;
                            else transfer = true;
                        }
                        else{
                            System.out.println("Invalid Option. Try Again!");
                            transfer = true;
                        }
                    }
                    databaseTemp = databaseTemp.next;
                }
            }
        }
        return true;
    }

    public static Bank createBank(){
        System.out.println("Creating Users...");
        Bank bank = new Bank();
        Bank tempBank = bank;
        
        // creates users
        try{
            File file = new File("Bank Users2.txt");
            Scanner scnr = new Scanner(file);
            String line = scnr.nextLine();

            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                //System.out.println(line);
                String[] splitter = line.split("\t");
                // adds nodes to linked list as long as we have info in our Bank Users file
                Person person = new Customer(splitter[0], splitter[1], splitter[2], splitter[3], splitter[4], splitter[5]);
                Account checking = new Checking(Integer.parseInt(splitter[6]), Double.parseDouble(splitter[9]));
                Account savings = new Savings(Integer.parseInt(splitter[7]), Double.parseDouble(splitter[10]));
                Account credit = new Credit(Integer.parseInt(splitter[8]), Double.parseDouble(splitter[11]));
                tempBank.next = new Bank(person, checking, savings, credit);
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