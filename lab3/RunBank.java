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
        String fields[] = fileFields("Bank Users4.txt");
        // stores customers in a linked list
        Bank users = createBank(fields);
        //printBank(users);
        //transactions(users, "Transaction Actions.txt");
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

    public static boolean transactionBuffer(Bank db, String sn, String ss, String a, String rn, String rd, String sa){
      Bank user = autoLogin(db, sn);
      if(user == null)return false;
      //System.out.println("Transaction File:\nType:\tStatus:");
      if(a.toLowerCase().compareTo("deposits") == 0){
        if(deposit(user, true, ss, Double.parseDouble(sa))){
          //System.out.println("Deposit Transaction Complete: " + sn + " " + a + " " +sa);
          return true;
        }
        return false;
      }
      if(a.toLowerCase().compareTo("withdraws") == 0){
        if(withdraw(user, true, ss, Double.parseDouble(sa))){
          //System.out.println("Withdraw Transaction Complete: " + sn + " " + a + " " + sa);
          return true;
        }
        return false;
      }
      if(a.toLowerCase().compareTo("pays") == 0 || a.toLowerCase().compareTo("transfers") == 0){
        Bank otherUser = autoLogin(db, rn);
        if(otherUser == null)return false;
        if(transfer(user, db, true, sn, ss, otherUser, rd, Double.parseDouble(sa))){
          //System.out.println("Transfer/pay Transaction Complete: " + sn + " " + ss + " " + rn + " " + rd + " " + sa);
          return true;
        }
        return false;
      }
      if(a.toLowerCase().compareTo("inquires") == 0){
        if(inquireBalance(user, true, ss))return true;
        return false;
      }
      return true;
    }

    public static Bank autoLogin(Bank db, String userToLogin){
      while(db != null){
        if(db.getPersonName().toLowerCase().compareTo(userToLogin.toLowerCase()) == 0)return db;
        db = db.next;
      }
      return null;
    }

    public static boolean transactions(Bank db, String fileName){
      String transactionFields[] = fileFields(fileName);
      String senderName = "";
      String senderSouce = "";
      String action = "";
      String recieverName = "";
      String recieverDestination = "";
      String senderAmount = "0";

      try{
        File file = new File(fileName);
        Scanner scnr = new Scanner(file);
        String line = scnr.nextLine();

        while(scnr.hasNextLine()){
          line = scnr.nextLine();
          String splitter[] = line.split("\t");

          for(int i = 0; i < transactionFields.length; i++){
            if(i < splitter.length){
              if(transactionFields[i].toLowerCase().compareTo("from first name") == 0)senderName = splitter[i] + " ";
              if(transactionFields[i].toLowerCase().compareTo("from last name") == 0)senderName += splitter[i];
              if(transactionFields[i].toLowerCase().compareTo("from where") == 0)senderSouce = splitter[i];
              if(transactionFields[i].toLowerCase().compareTo("action") == 0)action = splitter[i];
              if(transactionFields[i].toLowerCase().compareTo("to first name") == 0)recieverName = splitter[i] + " ";
              if(transactionFields[i].toLowerCase().compareTo("to last name") == 0)recieverName += splitter[i];
              if(transactionFields[i].toLowerCase().compareTo("to where") == 0)recieverDestination = splitter[i];
              if(transactionFields[i].toLowerCase().compareTo("action amount") == 0)senderAmount = splitter[i];
            }
          }
          if(!transactionBuffer(db, senderName, senderSouce, action, recieverName, recieverDestination, senderAmount)){
            System.out.println("Transaction status: FAILED");
            System.out.println(line + "\n");
            //System.out.printf("Sender Name: %s Sender Source: %s Action: %s Reciever Name: %s Reciever Dest: %s Sender Amount: %s \n", senderName, senderSouce, action, recieverName, recieverDestination, senderAmount);
            //System.out.println("Failed to make transaction!");
          }else{
            System.out.println("Transaction status: COMPLETE");
          }
        }
        return true;
      }catch(FileNotFoundException FNFE){
        System.out.println("Transaction File not found!");
        return false;
      }
    }

    public static void printBank(Bank db){
        while(db != null){
          System.out.println(db.getPersonName());
            System.out.println();
            db = db.next;
        }
    }



    public static String[] fileFields(String fileName){
        try{
            File file = new File(fileName);
            Scanner scnr = new Scanner(file);

            String line = scnr.nextLine();
            String splitter[] = line.split("\t");
            return splitter;
        }catch(FileNotFoundException FNFE){
            System.out.println("Error!");
            return null;
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
            System.out.println("* Enter \"useradd \" to create a new Customer Account.");
            System.out.println("* Enter \"transaction file \" to read from transaction file.");
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
            if(loginName.toLowerCase().compareTo("useradd") == 0){
              createUser(database);
            }
            if(loginName.toLowerCase().compareTo("transaction file") == 0){
              transactions(database, "Transaction Actions.txt");
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

    public static boolean createUser(Bank database){
      Scanner scnr = new Scanner(System.in);
      System.out.println("Creating Users...");
      return true;
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
                System.out.println("4. Statements");
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
                            manager = inquireBalance(tempDB, false, "");
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
                else if(userChoice == 4)makeStatement(database);
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

    public static void makeStatement(Bank db){
      Bank usertotest = autoLogin(db, "Rigoberto Quiroz");
      BankStatements ass = new BankStatements(usertotest);
      ass.statement();
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
                if(userChoice == 1)menu = inquireBalance(user, false, "");
                else if(userChoice == 2)menu = deposit(user, false, "", 0);
                else if(userChoice == 3)menu = withdraw(user, false, "", 0);
                else if(userChoice == 4)menu = transfer(user, database, false, "", "", null, "", 0);
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
    public static boolean inquireBalance(Bank user, boolean isTransactionFile, String view){
        boolean inquire = true;
        String logs = "";
        int userChoice = 0;

        while(inquire){
            try{
                // Inquire accounts
                if(!isTransactionFile){
                  Scanner scnr = new Scanner(System.in);
                  System.out.println("\nSelect Account to inquire: ");
                  System.out.println("1. Checking");
                  System.out.println("2. Savings");
                  System.out.println("3. Credit");
                  System.out.println("4. All");
                  System.out.println("5. Exit");
                  System.out.print("> ");
                  userChoice = scnr.nextInt();
                }else{
                  if(view.toLowerCase().compareTo("checking") == 0) userChoice = 1;
                  if(view.toLowerCase().compareTo("savings") == 0) userChoice = 2;
                  if(view.toLowerCase().compareTo("credit") == 0) userChoice = 3;
                }

                // checking account data
                if(userChoice == 1){
                    System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                    System.out.println("Checking Balance: " + user.getCheckingBalance());
                    // log
                    logs = user.getPersonName() + " inquire Checking Balance " + user.getCheckingAccountNumber() + ": $" + user.getCheckingBalance();
                    user.addLog(logs);
                    //user.printLogs();
                    userAction(logs);
                    inquire = false;
                }
                //savings account data
                else if(userChoice == 2){
                  System.out.println("*****************************************");
                  System.out.println("Savings Account Number: " + user.getSavingsAccountNumber());
                  System.out.println("Savings Balance: " + user.getSavingsBalance());
                  System.out.println("*****************************************\n");
                  // log
                  logs = user.getPersonName() + " inquire Savings Balance " + user.getSavingsAccountNumber() + ": $" + user.getSavingsBalance();
                  user.addLog(logs);
                  //user.printLogs();
                  userAction(logs);
                  inquire = false;
                }
                // credit account data
                else if(userChoice == 3){
                    System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                    System.out.println("Credit Balance: " + user.getCreditBalance());
                    // log
                    logs = user.getPersonName() + " inquire Credit Balance " + user.getCreditAccountNumber() + ": $" + user.getCreditBalance();
                    user.addLog(logs);
                    //user.printLogs();
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
                    user.addLog(logs);
                    //user.printLogs();
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
    public static boolean deposit(Bank user, boolean isTransactionFile, String source, double amount){
        boolean deposit = true;
        String log = "";
        if(isTransactionFile){
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
            if(user.checkingDeposit(amount)){
              log = user.getPersonName() + " made a deposit in Checkings Account of $" + amount + " " + user.getCheckingAccountNumber() + " Cuurent Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if(source.toLowerCase().compareTo("savings") == 0){
            if(!user.savingsDeposit(amount)){
              log = user.getPersonName() + " made a deposit int Savings Account of $" + amount + " " + user.getSavingsAccountNumber() + " Cuurent Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
        }
        if(isTransactionFile == false){
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
                user.addLog(log);
                //user.printLogs();
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
                user.addLog(log);
                //user.printLogs();
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
      return false;
    }

    /**
     * This method will get the users account an will withdraw money from it.
     * The only account the user can withdraw from would be from their checkings account.
     *
     * @param user
     * @return boolean
     */
    public static boolean withdraw(Bank user, boolean isTransactionFile, String source, double amount){
        boolean withdraw = true;
        String log = "";
        if(isTransactionFile){
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
            if(user.checkingWithdraw(amount)){
              log = user.getPersonName() + " made a withdraw in Checkings account of $" + amount + " " + user.getCheckingAccountNumber() + " Current Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if(source.toLowerCase().compareTo("savings") == 0){
            if(user.savingsWithdraw(amount)){
              log = user.getPersonName() + " made a withdraw in Savings account of $" + amount + " " + user.getSavingsAccountNumber() + " Current Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
        }

        if(!isTransactionFile){
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
                user.addLog(log);
                //user.printLogs();
                userAction(log);
              }catch(InputMismatchException IME){
                System.out.println("Please enter a valid withdraw amount");
                withdraw = true;
              }
          }
        return true;
      }
      return false;
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
    public static boolean transfer(Bank user, Bank database, boolean isTransactionFile, String senderName, String senderSource, Bank recieverName, String recieverDestination, double amount){
        boolean transfer = true;
        String log = "";

        if(isTransactionFile){
          if((senderSource.toLowerCase().compareTo("checking") == 0) && (recieverDestination.toLowerCase().compareTo("checking") == 0)){
            if(user.checkingTransfer(recieverName.getChecking(), amount)){
              log = user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("checking") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            if(user.checkingTransfer(recieverName.getSavings(), amount)){
              log = user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("checking") == 0)){
            if(user.savingsTransfer(recieverName.getChecking(), amount)){
              log = user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            if(user.savingsTransfer(recieverName.getSavings(), amount)){
              log = user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
          if(user.getPersonName().toLowerCase().compareTo(recieverName.getPersonName().toLowerCase()) == 0){
            if(user.creditTransfer(user.getCredit(), amount)){
              log = user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              //user.printLogs();
              return true;
            }
            return false;
          }
        }

        if(!isTransactionFile){
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
                  user.addLog(log);
                  //user.printLogs();
                  userAction(log);
                }
                // savings transfer
                else if(transferChoice == 2){
                  // updates account
                  if(!user.checkingTransfer(user.getSavings(), transferAmount))transfer = true;
                    else transfer = false;
                    // log
                    log = user.getPersonName() + " transfered money from checkings to savings. Checking: $" + user.getCheckingBalance() + " Savings: $" + user.getSavingsBalance();
                    user.addLog(log);
                    //user.printLogs();
                    userAction(log);
                }
                // credit transfer
                else if(transferChoice == 3){
                  // updates account
                  if(!user.creditTransfer(user.getCredit(), transferAmount))transfer = true;
                  else transfer = false;
                  // log
                  log = user.getPersonName() + " transfered money from checkings to credit. Checking: $" + user.getCheckingBalance() + " Credit: $" + user.getCreditBalance();
                  user.addLog(log);
                  //user.printLogs();
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
                      user.addLog(log);
                      //user.printLogs();
                      userAction(log);
                    }
                    //transfer to savings account
                    else if(userChoice == 2){
                      if(!user.savingsTransfer(databaseTemp.getSavings(), transferAmount))transfer = true;
                      else transfer = false;
                      log = user.getPersonName() + " made a transfer of $" + transferAmount + " to " + databaseTemp.getPersonName();
                      user.addLog(log);
                      //user.printLogs();
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
        return false;
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
    public static Bank createBank(String fields[]){
        Bank bank = new Bank();
        Bank tempBank = bank;
        Bank head = tempBank;
        boolean firstLoop = true;
        boolean duplicate = false;
        String firstName = "";
        String lastName = "";
        String dateOfBirth = "";
        String idNumber = "";
        String address = "";
        String phoneNumber = "";
        String checkingAccountNumber = "0";
        String savingAccountNumber = "0";
        String creditAccountNumber = "0";
        String checkingStartingBalance = "0";
        String savingStartingBalance = "0";
        String creditStartingBalance = "0";
        String creditMax = "0";

        // creates users
        try{
            File file = new File("Bank Users4.txt");
            Scanner scnr = new Scanner(file);
            String line = scnr.nextLine();

            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                //System.out.println(line);
                String[] splitter = line.split("\t");
                // adds nodes to linked list as long as we have info in our Bank Users file
                // creates an instance of each class we created
                for(int i = 0; i < fields.length; i++){
                    if(fields[i].toLowerCase().compareTo("first name") == 0){
                        firstName = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("last name") == 0){
                        lastName = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("date of birth") == 0){
                        dateOfBirth = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("identification number") == 0){
                        idNumber = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("address") == 0){
                        address = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("phone number") == 0){
                        phoneNumber = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("checking account number") == 0){
                        checkingAccountNumber = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("savings account number") == 0){
                        savingAccountNumber = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("credit account number") == 0){
                        creditAccountNumber = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("checking starting balance") == 0){
                        checkingStartingBalance = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("savings starting balance") == 0){
                        savingStartingBalance = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("credit starting balance") == 0){
                        creditStartingBalance = splitter[i];
                    }
                    if(fields[i].toLowerCase().compareTo("credit max") == 0){
                      creditMax = splitter[i];
                    }
                }
                if(firstLoop == false){
                  duplicate = checkDuplicates(head.next, firstName, lastName);
                }
                if(duplicate == false){
                  Person person = new Customer(firstName, lastName, dateOfBirth, idNumber, address, phoneNumber);
                  Account checking = new Checking(Integer.parseInt(checkingAccountNumber), Double.parseDouble(checkingStartingBalance));
                  Account savings = new Savings(Integer.parseInt(savingAccountNumber), Double.parseDouble(savingStartingBalance));
                  Account credit = new Credit(Integer.parseInt(creditAccountNumber), Double.parseDouble(creditStartingBalance), Integer.parseInt(creditMax));
                  tempBank.next = new Bank(person, checking, savings, credit);
                  tempBank = tempBank.next;
                }
                // moves to next node
                firstLoop = false;
                duplicate = false;
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

    public static boolean checkDuplicates(Bank db, String fn, String ln){
      Bank tempDB = db;
      int c = 0;
      while(tempDB != null){
        if((db.getPersonFirstName().toLowerCase().compareTo(fn.toLowerCase()) == 0) & (db.getPersonLastName().toLowerCase().compareTo(ln.toLowerCase()) == 0)){
          return true;
        }
        c++;
        tempDB = tempDB.next;
      }
      return false;
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
            File file = new File("Bank Users4.txt");
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
