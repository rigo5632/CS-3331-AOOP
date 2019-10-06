/*
    Author: Rigoberto Quiroz
    Alias: Rigo
    Date: 10/06/19
    Course: CS3331
    Lab: Programming Assignment 2
    Lab Description:
        For this lab I had to create various classes (Checking, Savings, Credit, Customer, Person, Account and Log) and implement them together to create a Bank. 
        This classes combined will replicate some of the features bank offer to their customers such as deposit or withdraw. We will get user information from a 
        txt file, which contains firstname, lastname, date of birth, identification number, checking, savings and credit information. Once the information has been
        stored the into a data strcuture (linked list), I created some methods that would handle the Bank Features. After the user has finished using the bank 
        features, a file will be automatically created with all the logs/actions they preformed while they were logged in, and a new bank file will be created
        with all the updated account balances the user did while logged in.
    Honesty Statement:
        I confirm that the work of this assignment is completely my own. By turning in this assignment. 
        I declare that I did not receive unauthorized assistance. Moreover, all deliverables including, 
        but not limited to the source code, lab report and output files were written and produced by me alone.
    Assumptions:
        1. "Bank users.txt" will always have the same number of columns, and the columns will always be in the 
        same order.
        2. "Bank Users2.txt" will always have the first row of data (firstname, lastname, account info, etc.) and
        Bank Users.txt will always have more than 1 user.
        3. Manager accounts will not be able to log-out and access user accounts.
*/


// libaries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.text.DecimalFormat;

public class RunBank implements Log{
    public static void main(String[] args){
        // checks if the file exits and contains data
        if(!fileExistence()){
            System.out.println("Error reading file");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }
        // stores customers in a linked list
        Bank users = createBank();
        Bank bankUser = userLogin(users);
        if(bankUser == null)System.exit(0);
        //creates log file
        createLogFile();
        // users can logout and re-login
        boolean keepGoing = userMenu(bankUser, users);
        while(keepGoing){
            bankUser = userLogin(users);
            if(bankUser == null)System.exit(0);
            keepGoing = userMenu(bankUser, users);
        }
    }
    /**
     * This method will create logs.txt. Everytime a user
     * does an action (deposit, withdraw, etc.) We will
     * record their action in this txt file
     */
    public static void createLogFile(){
        try{
            // log file
            File file = new File("logs.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter logWriter = new PrintWriter(writer);
            logWriter.close();
        }catch(IOException IOE){
            System.out.println("Error File!");
            System.exit(0);
        }
    }
    
    /** 
     * This method will be accesed once the user decides to
     * terminate the program (not when they decide to log out).
     * Record all the new accounts in Bank Users2 Updated.txt file.
     * 
     * @param database
     */
    public static void printUpdatedBank(Bank database){
        try{
            // name of file
            File file = new File("Bank Users2 Updated.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter update = new PrintWriter(writer);
            update.println("First Name\tLast Name\tDate of Birth\tIndetification Number\tAddres\tPhone Number\tChecking Account Number\tSavings Account Number\tCredit Account Number\tChecking Balance\tSavings Balance\tCredit Balance");
            Bank tempB = database;
            
            // gets data from linked list and prints it in the txt file
            while(tempB != null){
                update.print(tempB.getPersonName() + "\t");
                update.print(tempB.getPersonDateOfBirth() + "\t");
                update.print(tempB.getPersonID() + "\t");
                update.print(tempB.getPersonAddress() + "\t");
                update.print(tempB.getPersonPhoneNumber() + "\t");
                update.print(tempB.getCheckingAccountNumber() + "\t");
                update.print(tempB.getSavingsAccountNumber() + "\t");
                update.print(tempB.getCreditAccountNumber() + "\t");
                update.print(tempB.getCheckingBalance() + "\t");
                update.print(tempB.getSavingsBalance() + "\t");
                update.print(tempB.getCreditBalance() + "\n");
                tempB = tempB.next;
            }
            update.close();
        }catch(IOException IOE){
            System.out.println("Error File!");
            System.exit(0);
        }
    }

    
    /** 
     * This method wil find the user from the database and login
     * into their account, or their manager account.
     * 
     * @param database
     * @return Bank
     */
    public static Bank userLogin(Bank database){
        boolean login = false;
        while(!login){
            Scanner scnr = new Scanner(System.in);
            Bank tempDB = database;

            System.out.println("****************************");
            System.out.println("*       How to use         *");
            System.out.println("****************************");
            System.out.println("* Enter first name last name of Bank's Customer to access account.");
            System.out.println("* Enter \"Manager\" to access the manager Account.");
            System.out.println("* !q or !Q to quit program.");
            System.out.println();
            System.out.println("Enter Your Full Name: EX: Mickey Mouse");
            System.out.print("> ");
            String loginName = scnr.nextLine();

            // quit
            if(loginName.toLowerCase().compareTo("!q") == 0){
                printUpdatedBank(database);
                System.exit(0);
            }

            // searches for user or manager
            while(tempDB != null){
                if(tempDB.getPersonName().toLowerCase().compareTo(loginName.toLowerCase()) == 0){
                    login = true;
                    return tempDB;
                }
                if(loginName.toLowerCase().compareTo("manager") == 0){
                    login = managerMenu(database);
                }
                tempDB = tempDB.next;
            }
            if(!login)System.out.println("Please try again!\n");
        }
        // did not find the user account
        return null;
    }

    
    /** 
     * This method will display the manager menu. The manager
     * will be able to see other users accounts
     * 
     * @param database
     * @return boolean
     */
    public static boolean managerMenu(Bank database){
        Scanner scnr = new Scanner(System.in);
        boolean manager = true;
        String log = "";

        while(manager){
            try{
                // meun
                System.out.println("Manager Menu");
                System.out.println("1. Inquire User Account");
                System.out.println("2. Inquire All Accounts");
                System.out.println("3. Exit");

                System.out.print("> ");
                int userChoice = scnr.nextInt();
                
                // Sees one oser account
                if(userChoice == 1){
                    scnr = new Scanner(System.in);
                    System.out.println("Enter Name of User");
                    System.out.print("> ");
                    String userName = scnr.nextLine();
                    Bank tempDB = database;

                    while(tempDB != null){
                        if(tempDB.getPersonName().compareTo(userName) == 0){
                            // Manager log
                            log = "Manager inquired " + tempDB.getPersonName() + " Account";
                            // record
                            userAction(log);
                            manager = inquireBalance(tempDB);
                        }
                        tempDB = tempDB.next;
                    }
                }
                // Sees all accounts
                else if(userChoice == 2){
                    Bank tempDB = database;
                    // manager log
                    log = "Manager inquired all customers accounts!";
                    userAction(log);
                    while(tempDB != null){
                        System.out.println();
                        System.out.println("Customer Name: " + tempDB.getPersonName());
                        System.out.println("Checking Account: " + tempDB.getCheckingAccountNumber() + " Checking Balance: " + tempDB.getCheckingBalance());
                        System.out.println("Savings Account: " + tempDB.getCheckingAccountNumber() + " Savings Balance: " + tempDB.getSavingsBalance());
                        System.out.println("Credit Account: " + tempDB.getCreditAccountNumber() + " Credit Balance: " + tempDB.getCreditBalance());
                        System.out.println();
                        tempDB = tempDB.next;
                    }
                }
                // manager cannot logout
                else if(userChoice == 3)System.exit(0);
                else{
                    System.out.println("Invalid User menu. Try Again");
                }
                return true;
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option. [1 - 3]");
                return false;
            }
        }
        return false;
    }
    
    /** 
     * This method will display the user menu. The user will be
     * able to do various things like deposit or withdraw.
     * 
     * @param user
     * @param database
     * @return boolean
     */
    public static boolean userMenu(Bank user, Bank database){
        boolean menu = true;
        while(menu){
            try{
                // User menu
                Scanner scnr = new Scanner(System.in);
                System.out.println("\nWelcome: " + user.getPersonName());
                System.out.println("1. Inquire Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Transfer Money/Pay Someone");
                System.out.println("5. Logout");
                System.out.print("> ");
                int userChoice = scnr.nextInt();
                
                // funtionalities
                if(userChoice == 1)menu = inquireBalance(user);
                else if(userChoice == 2)menu = deposit(user);
                else if(userChoice == 3)menu = withdraw(user);
                else if(userChoice == 4)menu = transfer(user, database);
                else if(userChoice == 5)return true;
                else{
                    System.out.println("Invalid Menu Choice. Try Again!");
                    menu = true;
                }
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option [1 - 5]");
                menu = true;
            }
        }
        return true;
    }

    
    /** 
     * This method will display the account details of the user logged in.
     * Whether it be checking, savings, or credit, the user will be able to
     * see their account 
     * 
     * @param user
     * @return boolean
     */
    public static boolean inquireBalance(Bank user){
        boolean inquire = true;
        String logs = "";
        
        while(inquire){
            try{
                // Inquire accounts
                Scanner scnr = new Scanner(System.in);
                System.out.println("\nSelect Account to inquire: ");
                System.out.println("1. Checking");
                System.out.println("2. Savings");
                System.out.println("3. Credit");
                System.out.println("4. All");
                System.out.println("5. Exit");
                System.out.print("> ");
                int userChoice = scnr.nextInt();

                // checking account data
                if(userChoice == 1){
                    System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                    System.out.println("Checking Balance: " + user.getCheckingBalance());
                    // log
                    logs = user.getPersonName() + " inquire Checking Balance " + user.getCheckingAccountNumber() + ": $" + user.getCheckingBalance();
                    userAction(logs);
                    inquire = false;
                }
                //savings account data
                else if(userChoice == 2){
                    System.out.println("\nSavings Account Number: " + user.getSavingsAccountNumber());
                    System.out.println("Savings Balance: " + user.getSavingsBalance());
                    // log
                    logs = user.getPersonName() + " inquire Savings Balance " + user.getSavingsAccountNumber() + ": $" + user.getSavingsBalance();
                    userAction(logs);
                    inquire = false;
                }
                // credit account data
                else if(userChoice == 3){
                    System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                    System.out.println("Credit Balance: " + user.getCreditBalance());
                    // log
                    logs = user.getPersonName() + " inquire Credit Balance " + user.getCreditAccountNumber() + ": $" + user.getCreditBalance();
                    userAction(logs);
                    inquire = false;      
                }
                // all accounts data
                else if(userChoice == 4){
                    System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                    System.out.println("Checking Balance: " + user.getCheckingBalance());
                    System.out.println("\nSavings Account Number: " + user.getSavingsAccountNumber());
                    System.out.println("Savings Balance: " + user.getSavingsBalance());
                    System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                    System.out.println("Credit Balance: " + user.getCreditBalance());
                    // log
                    logs = user.getPersonName() + " inquire All Accounts Balance ";
                    userAction(logs);
                    inquire = false;  
                }
                else if(userChoice == 5)inquire = false;
                else{
                    System.out.println("Invalid Option Try again!");
                    inquire = true;
                }
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option [1 - 5]");
                inquire = true;
            }
        }
        return true;
    }

    
    /** 
     * This method will get the users account and deposit the
     * amount they entered. User decides which account the money
     * will be added to.
     *  
     * @param user
     * @return boolean
     */
    public static boolean deposit(Bank user){
        boolean deposit = true;
        String log = "";

        System.out.println("\nDeposit Moeny");
        while(deposit){
            try{
                // deposit meun
                Scanner scnr = new Scanner(System.in);
                System.out.println("Select account to deposit: ");
                System.out.println("1. Checking");
                System.out.println("2. Savings");
                System.out.print("> ");
                int userChoice = scnr.nextInt();

                // add money to their checkings account
                if(userChoice == 1){
                    System.out.println("Amount to Deposit: ");
                    System.out.print("> $");
                    double depositAmount = scnr.nextDouble();
                    // updates account
                    if(!user.checkingDeposit(depositAmount))deposit = true;
                    else deposit = false;
                    // log
                    log = user.getPersonName() + " made a deposit in Checkings Account of $" + depositAmount + " " + user.getCheckingAccountNumber() + " Cuurent Balance: $" + user.getCheckingBalance();
                    userAction(log);
                }
                // add money to their savings account
                else if(userChoice == 2){
                    System.out.println("Amount to Deposit: ");
                    System.out.print("> $");
                    double depositAmount = scnr.nextDouble();
                    // update savings account
                    if(!user.savingsDeposit(depositAmount))deposit = true;
                    else deposit = false;
                    // log
                    log = user.getPersonName() + " made a deposit int Savings Account of $" + depositAmount + " " + user.getSavingsAccountNumber() + " Cuurent Balance: $" + user.getSavingsBalance();
                    userAction(log);
                }
                // invalid options
                else{
                    System.out.println("Invalid Option. Try Again!");
                    deposit = true;
                }
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option [1 - 2]");
                deposit = true;
            }
        }
        return true;
    }
    
    /** 
     * This method will get the users account an will withdraw money from it.
     * The only account the user can withdraw from would be from their checkings account.
     * 
     * @param user
     * @return boolean
     */
    public static boolean withdraw(Bank user){
        boolean withdraw = true;
        String log = "";
        
        // withdraw menu
        System.out.println("\nWithdraw Money");
        while(withdraw){
            try{
                Scanner scnr = new Scanner(System.in);
                System.out.println("Amount to Withdraw: ");
                System.out.print("> $");
                double withdrawAmount = scnr.nextDouble();
                
                // updates account balance
                if(!user.checkingWithdraw(withdrawAmount))withdraw = true;
                else withdraw = false;
                // log action
                log = user.getPersonName() + " made a withdraw in Checkings account of $" + withdrawAmount + " " + user.getCheckingAccountNumber() + " Current Balance: $" + user.getCheckingBalance();
                userAction(log);
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid withdraw amount");
                withdraw = true;
            }
        }
        return true;
    }

    
    /** 
     * This method will make a transfer. The user will be able
     * to transfer money from their account or to other customers accounts.
     * The user can choose to which account they want to transfer to
     * 
     * @param user
     * @param database
     * @return boolean
     */
    public static boolean transfer(Bank user, Bank database){
        boolean transfer = true;
        String log = "";

        System.out.println("\nTransfer Money");
        while(transfer){
            try{
                // transfer menu
                Scanner scnr = new Scanner(System.in);
                System.out.println("Select Transfer Process");
                System.out.println("1. Your Acounts");
                System.out.println("2. Other users Account");
                System.out.print("> ");
                int userChoice = scnr.nextInt();

                if(userChoice == 1){
                    // user transfer within their account
                    System.out.println("Select Account to transfer to:");
                    System.out.println("1. Checking");
                    System.out.println("2. Savings");
                    System.out.println("3. Credit");
                    System.out.print("> ");
                    int transferChoice = scnr.nextInt();

                    System.out.println("Amount to Transfer");
                    System.out.print("> ");
                    double transferAmount = scnr.nextDouble();

                    // checking transfer
                    if(transferChoice == 1){
                        // updates account
                        if(!user.savingsTransfer(user.getChecking(), transferAmount))transfer = true;
                        else transfer = false;
                        // log
                        log = user.getPersonName() + " transfered money from savings to checkings. Checking: $" + user.getCheckingBalance() + " Savings: $" + user.getSavingsBalance();
                        userAction(log); 
                    }
                    // savings transfer
                    else if(transferChoice == 2){
                        // updates account
                        if(!user.checkingTransfer(user.getSavings(), transferAmount))transfer = true;
                        else transfer = false;
                        // log
                        log = user.getPersonName() + " transfered money from checkings to savings. Checking: $" + user.getCheckingBalance() + " Savings: $" + user.getSavingsBalance();
                        userAction(log); 
                    }
                    // credit transfer
                    else if(transferChoice == 3){
                        // updates account
                        if(!user.creditTransfer(user.getCredit(), transferAmount))transfer = true;
                        else transfer = false;
                        // log
                        log = user.getPersonName() + " transfered money from checkings to credit. Checking: $" + user.getCheckingBalance() + " Credit: $" + user.getCreditBalance();
                        userAction(log); 
                    }else{
                        System.out.println("Invalid Option. Try Again!");
                        transfer = true;
                    }
                }
                
                // transfer to other customers account
                if(userChoice == 2){
                    // transfer menu
                    Scanner newScnr = new Scanner(System.in);
                    System.out.println("Enter name of Person");
                    System.out.print("> ");
                    String otherUser = newScnr.nextLine();
                    Bank databaseTemp = database;

                    while(databaseTemp != null){
                        // goes through the database to find requested user
                        if(databaseTemp.getPersonName().compareTo(otherUser) == 0){
                            System.out.println("Select Account to transfer");
                            System.out.println("1. Checking");
                            System.out.println("2. Savings");
                            System.out.print("> ");
                            userChoice = newScnr.nextInt();
                            
                            // amount they will transfer
                            System.out.println("Amount to transfer");
                            System.out.print("> ");
                            double transferAmount = scnr.nextDouble();
                            
                            // transfer to checkings account
                            if(userChoice == 1){
                                if(!user.checkingTransfer(databaseTemp.getChecking(), transferAmount))transfer = true;
                                else transfer = false;
                                log = user.getPersonName() + " made a transfer of $" + transferAmount + " to " + databaseTemp.getPersonName();
                                userAction(log);
                            }
                            //transfer to savings account
                            else if(userChoice == 2){
                                if(!user.savingsTransfer(databaseTemp.getSavings(), transferAmount))transfer = true;
                                else transfer = false;
                                log = user.getPersonName() + " made a transfer of $" + transferAmount + " to " + databaseTemp.getPersonName();
                                userAction(log);
                            }
                            else{
                                System.out.println("Invalid Option. Try Again!");
                                transfer = true;
                            }
                        }
                        databaseTemp = databaseTemp.next;
                    }
                }
            }catch(InputMismatchException IME){
                System.out.println("Please select a valid option!");
                transfer = true;
            }
        }
        return true;
    }

    
    /** 
     * Recieves the actions taken by the user and will
     * write them in a txt log file. As long as the program
     * is running the txt will keep updating
     * @param accountActions
     */
    public static void userAction(String accountActions){
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
        }
    }

    
    /** 
     * This method will get the txt which contains all the user
     * data and will take certain data fields and initialize them to their
     * apporpiate class. Once all data has been organized, the instances will be set
     * in a linked list as a node.
     * 
     * @return Bank
     */
    public static Bank createBank(){
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
                // creates an instance of each class we created
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
        // return next b/c first node is null(no info)
        return bank.next;
    }

    
    /** 
     * The method will check the Bank Users2.txt and make
     * sure that the file exits and that it at least contains
     * 1 line of Customer data.
     * 
     * @return boolean
     */
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