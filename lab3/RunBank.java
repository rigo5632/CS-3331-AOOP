/*
    Author: Rigoberto Quiroz
    Alias: Rigo
    Date: 11/07/19
    Course: CS3331
    Lab: Programming Assignment 3
    Lab Description:
    Honesty Statement:
        I confirm that the work of this assignment is completely my own. By turning in this assignment.
        I declare that I did not receive unauthorized assistance. Moreover, all deliverables including,
        but not limited to the source code, lab report and output files were written and produced by me alone.
    Assumptions:
*/


// libaries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunBank{
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

    public static String getTime(){
      SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
      Date date = new Date();
      return formatTime.format(date);
    }

    public static boolean transactionBuffer(Bank db, String sn, String ss, String a, String rn, String rd, String sa){
      Bank user = autoLogin(db, sn);
      if(a.toLowerCase().compareTo("deposits") == 0 && user == null){
        user = autoLogin(db, rn);
        ss = "checking";
      }
      if(user == null)return false;
      if(a.toLowerCase().compareTo("deposits") == 0){
        if(deposit(user, true, ss, Double.parseDouble(sa))){
          return true;
        }
        return false;
      }
      if(a.toLowerCase().compareTo("withdraws") == 0){
        if(withdraw(user, true, ss, Double.parseDouble(sa))){
          return true;
        }
        return false;
      }
      if(a.toLowerCase().compareTo("pays") == 0 || a.toLowerCase().compareTo("transfers") == 0){
        Bank otherUser = autoLogin(db, rn);
        if(otherUser == null)return false;
        if(transfer(user, db, true, sn, ss, otherUser, rd, Double.parseDouble(sa))){
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
      int transactionDone = 2;

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
            System.out.println(transactionDone + ": Transaction status: FAILED");
            System.out.println(line + "\n");
          }else{
            System.out.println(transactionDone + ": Transaction status: COMPLETE");
          }
          transactionDone++;
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
     * This method will be accesed once the user decides to
     * terminate the program (not when they decide to log out).
     * Record all the new accounts in Bank Users Updated.txt file.
     *
     * @param database
     */
    public static void printUpdatedBank(Bank database){
        try{
            // name of file
            File file = new File("Bank Users Updated.txt");
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
            if(loginName.toLowerCase().compareTo("manager") == 0){
              login = managerMenu(database);
            }

            // searches for user or manager
            while(tempDB != null){
                if(tempDB.getPersonName().toLowerCase().compareTo(loginName.toLowerCase()) == 0){
                  login = true;
                  return tempDB;
                }
                tempDB = tempDB.next;
            }
            if(!login)System.out.println();
        }
        // did not find the user account
        return null;
    }

    public static String idGetter(Bank db){
      while(db.next != null){
        db = db.next;
      }
      int id = Integer.parseInt(db.getPersonID()) + 1;
      return Integer.toString(id);
    }

    public static boolean asciiChecker(String userString){
      int ascii;
      userString = userString.toLowerCase();
      for(int i = 0; i < userString.length(); i++){
        ascii = userString.charAt(i);
        if(ascii < 97 || ascii > 122)return false;
      }
      return true;
    }

    public static String checkData(String field){
      Scanner scnr = new Scanner(System.in);
      String userInput;
      if(field.compareTo("firstname") == 0 || field.compareTo("lastname") == 0){
        System.out.print("Please Enter " + field + "\n> ");
        userInput = scnr.nextLine();
        if(userInput.contains("\t") || userInput.contains(" ") || userInput.length() == 0)return checkData(field);
        if(!asciiChecker(userInput))return checkData(field);
        return userInput;
      }
      if(field.compareTo("dob") == 0){
        System.out.print("Please enter date of birth format: MM/DD/YYYY\n> ");
        userInput = scnr.nextLine();
        if(userInput.contains("\t") || userInput.contains(" ") || userInput.length() == 0)return checkData(field);
        if(userInput.length() != 10) return checkData(field);
        return userInput;
      }
      if(field.compareTo("address") == 0){
        System.out.print("Please enter address\n> ");
        userInput = scnr.nextLine();
        return userInput;
      }
      if(field.compareTo("phone") == 0){
        System.out.print("Please enter phone number EX: (915)1234567\n> (915)");
        userInput = scnr.nextLine();
        if(userInput.contains("\t") || userInput.contains(" ") || userInput.length() == 0)return checkData(field);
        if(userInput.length() != 7)return checkData(field);

        try{
          Integer.parseInt(userInput);
        }catch(NumberFormatException NFE){
          return checkData(field);
        }

        userInput = "(915)" + userInput;
        return userInput;
      }
      if(field.compareTo("savings account number") == 0){
        System.out.print("Please enter savings account number\n> ");
        userInput = scnr.nextLine();
        try{
          Integer.parseInt(userInput);
        }catch(NumberFormatException NFE){
          return checkData(field);
        }
        if(Integer.parseInt(userInput) < 0)return checkData(field);
        return userInput;
      }
      if(field.compareTo("savings balance") == 0){
        System.out.print("Please enter savings balance\n> ");
        userInput = scnr.nextLine();
        try{
          Double.parseDouble(userInput);
        }catch(NumberFormatException NFE){
          return checkData(field);
        }
        if(Double.parseDouble(userInput) < 0)return checkData(field);
        return userInput;
      }
      if(field.compareTo("checking account number") == 0 || field.compareTo("credit account number") == 0){
        System.out.print("Please enter " + field + " if applicable. Leave blank if you do not have one! \n> ");
        userInput = scnr.nextLine();
        if(userInput.length() == 0)return "0";
        try{
          Integer.parseInt(userInput);
        }catch(NumberFormatException NFE){
          return checkData(field);
        }
        if(Integer.parseInt(userInput) < 0)return checkData(field);
        return userInput;
      }
      else{
        System.out.print("Please enter " + field + "\n> ");
        userInput = scnr.nextLine();
        try{
          Double.parseDouble(userInput);
        }catch(NumberFormatException NFE){
          return checkData(field);
        }
        if(field.equals("credit max") || field.equals("checking balance")){
          if(Double.parseDouble(userInput) < 0)return checkData(field);
        }else{
          if(Double.parseDouble(userInput) > 0) return checkData(field);
        }
        return userInput;
      }

    }

    public static void addUser(Bank db, String fn, String ln, String dob, String id, String addr, String p, String can, String san, String crdan, String checkB, String savB, String crdB, String crdMax){
      Bank tempBank = db;
      while(tempBank.next != null){
        tempBank = tempBank.next;
      }
      Person person = new Customer(fn, ln, dob, id, addr, p);
      Account checking = new Checking(Integer.parseInt(can), Double.parseDouble(checkB));
      Account savings = new Savings(Integer.parseInt(san), Double.parseDouble(savB));
      Account credit = new Credit(Integer.parseInt(crdan), Double.parseDouble(crdB), Integer.parseInt(crdMax));
      tempBank.next = new Bank(person, checking, savings, credit);
    }

    public static boolean createUser(Bank database){
      String firstName = "";
      String lastName = "";
      String dateOfBirth = "";
      String identification = "";
      String address = "";
      String phone = "";
      String checkingAccountNum = "0";
      String savingsAccountNum = "0";
      String creditAccountNum = "0";
      String checkingBalance = "0";
      String savingsBalance = "0";
      String creditBalance = "0";
      String creditMax = "0";

      firstName = checkData("firstname");
      lastName = checkData("lastname");
      dateOfBirth = checkData("dob");
      identification = idGetter(database);
      address = checkData("address");
      phone = checkData("phone");
      checkingAccountNum = checkData("checking account number");
      if(checkingAccountNum.length() > 1)checkingBalance = checkData("checking balance");
      savingsAccountNum = checkData("savings account number");
      savingsBalance = checkData("savings balance");
      creditAccountNum = checkData("credit account number");
      if(creditAccountNum.length() > 1){
        creditBalance = checkData("credit balance");
        creditMax = checkData("credit max");
      }
      addUser(database, firstName, lastName, dateOfBirth, identification, address, phone, checkingAccountNum, savingsAccountNum, creditAccountNum, checkingBalance, savingsBalance, creditBalance, creditMax);
      return false;
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

        while(manager != false){
            try{
                // meun
                System.out.println("Manager Menu");
                System.out.println("1. Inquire User Account");
                System.out.println("2. Inquire All Accounts");
                System.out.println("3. Statements");
                System.out.println("4. Logout");
                System.out.println("5. Exit");


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
                            userAction(log);
                            // record
                            manager = inquireBalance(tempDB, false, "");
                        }
                        tempDB = tempDB.next;
                    }
                }
                // Sees all accounts
                else if(userChoice == 2){
                    Bank tempDB = database;
                    // manager log
                    log = getTime() + ": " + "Manager inquired all customers accounts!";
                    while(tempDB != null){
                        System.out.println();
                        System.out.println("Customer Name: " + tempDB.getPersonName());
                        System.out.println("Checking Account: " + tempDB.getCheckingAccountNumber() + " Checking Balance: " + tempDB.getCheckingBalance());
                        System.out.println("Savings Account: " + tempDB.getCheckingAccountNumber() + " Savings Balance: " + tempDB.getSavingsBalance());
                        System.out.println("Credit Account: " + tempDB.getCreditAccountNumber() + " Credit Balance: " + tempDB.getCreditBalance());
                        System.out.println();
                        tempDB = tempDB.next;
                    }
                    userAction(log);
                }
                // manager cannot logout
                else if(userChoice == 3)makeStatement(database);
                else if(userChoice == 4)manager = false;//userLogin(database);
                else if(userChoice == 5)System.exit(0);
                else{
                    System.out.println("Invalid User menu. Try Again");
                }
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option. [1 - 3]");
                return managerMenu(database);
            }
        }
        return false;
    }

    public static void makeStatement(Bank db){
      Scanner scnr = new Scanner(System.in);
      System.out.print("Customer Name\n> ");
      String managerUser = scnr.nextLine();
      Bank userToStatement = autoLogin(db, managerUser);
      while(userToStatement == null){
        System.out.print("Customer Name\n> ");
        managerUser = scnr.nextLine();
        userToStatement = autoLogin(db, managerUser);
      }
      BankStatements userStatement = new BankStatements(userToStatement);
      userStatement.statement();
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
                  System.out.println("*****************************************");
                  System.out.println("\nChecking Account Number: " + user.getCheckingAccountNumber());
                  System.out.println("Checking Balance: " + user.getCheckingBalance());
                  System.out.println("*****************************************\n");
                  // log
                  logs = getTime() + ": " + user.getPersonName() + " inquire Checking Balance " + user.getCheckingAccountNumber() + ": $" + user.getCheckingBalance();
                  user.addLog(logs);
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
                  logs = getTime() + ": " + user.getPersonName() + " inquire Savings Balance " + user.getSavingsAccountNumber() + ": $" + user.getSavingsBalance();
                  user.addLog(logs);
                  userAction(logs);
                  inquire = false;
                }
                // credit account data
                else if(userChoice == 3){
                  System.out.println("*****************************************\n");
                  System.out.println("\nCredit Account Number: " + user.getCreditAccountNumber());
                  System.out.println("Credit Balance: " + user.getCreditBalance());
                  System.out.println("*****************************************\n");
                  // log
                  logs = getTime() + ": " + user.getPersonName() + " inquire Credit Balance " + user.getCreditAccountNumber() + ": $" + user.getCreditBalance();
                  user.addLog(logs);
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
                    logs = getTime() + ": " + user.getPersonName() + " inquire All Accounts Balance ";
                    user.addLog(logs);
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
              log = getTime() + ": " + user.getPersonName() + " made a deposit in Checkings Account of $" + amount + " " + user.getCheckingAccountNumber() + " Curent Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if(source.toLowerCase().compareTo("savings") == 0){
            if(!user.savingsDeposit(amount)){
              log = getTime() + ": " + user.getPersonName() + " made a deposit int Savings Account of $" + amount + " " + user.getSavingsAccountNumber() + " Cuurent Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              userAction(log);
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
                log = getTime() + ": " + user.getPersonName() + " made a deposit in Checkings Account of $" + depositAmount + " " + user.getCheckingAccountNumber() + " Cuurent Balance: $" + user.getCheckingBalance();
                userAction(log);
                user.addLog(log);
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
                log = getTime() + ": " + user.getPersonName() + " made a deposit int Savings Account of $" + depositAmount + " " + user.getSavingsAccountNumber() + " Cuurent Balance: $" + user.getSavingsBalance();
                user.addLog(log);
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
              log = getTime() + ": " + user.getPersonName() + " made a withdraw in Checkings account of $" + amount + " " + user.getCheckingAccountNumber() + " Current Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if(source.toLowerCase().compareTo("savings") == 0){
            if(user.savingsWithdraw(amount)){
              log = getTime() + ": " + user.getPersonName() + " made a withdraw in Savings account of $" + amount + " " + user.getSavingsAccountNumber() + " Current Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              userAction(log);
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
                log = getTime() + ": " + user.getPersonName() + " made a withdraw in Checkings account of $" + withdrawAmount + " " + user.getCheckingAccountNumber() + " Current Balance: $" + user.getCheckingBalance();
                user.addLog(log);
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
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("checking") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            if(user.checkingTransfer(recieverName.getSavings(), amount)){
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("checking") == 0)){
            if(user.savingsTransfer(recieverName.getChecking(), amount)){
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            if(user.savingsTransfer(recieverName.getSavings(), amount)){
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            return false;
          }
          if(user.getPersonName().toLowerCase().compareTo(recieverName.getPersonName().toLowerCase()) == 0){
            if(user.creditTransfer(user.getCredit(), amount)){
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
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
                  log = getTime() + ": " + user.getPersonName() + " transfered money from savings to checkings. Checking: $" + user.getCheckingBalance() + " Savings: $" + user.getSavingsBalance();
                  user.addLog(log);
                  userAction(log);
                }
                // savings transfer
                else if(transferChoice == 2){
                  // updates account
                  if(!user.checkingTransfer(user.getSavings(), transferAmount))transfer = true;
                    else transfer = false;
                    // log
                    log = getTime() + ": " + user.getPersonName() + " transfered money from checkings to savings. Checking: $" + user.getCheckingBalance() + " Savings: $" + user.getSavingsBalance();
                    user.addLog(log);
                    userAction(log);
                }
                // credit transfer
                else if(transferChoice == 3){
                  // updates account
                  if(!user.creditTransfer(user.getCredit(), transferAmount))transfer = true;
                  else transfer = false;
                  // log
                  log = getTime() + ": " + user.getPersonName() + " transfered money from checkings to credit. Checking: $" + user.getCheckingBalance() + " Credit: $" + user.getCreditBalance();
                  user.addLog(log);
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
                      log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + transferAmount + " to " + databaseTemp.getPersonName();
                      user.addLog(log);
                      userAction(log);
                    }
                    //transfer to savings account
                    else if(userChoice == 2){
                      if(!user.savingsTransfer(databaseTemp.getSavings(), transferAmount))transfer = true;
                      else transfer = false;
                      log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + transferAmount + " to " + databaseTemp.getPersonName();
                      user.addLog(log);
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
                    if(fields[i].toLowerCase().compareTo("first name") == 0)firstName = splitter[i];
                    if(fields[i].toLowerCase().compareTo("last name") == 0)lastName = splitter[i];
                    if(fields[i].toLowerCase().compareTo("date of birth") == 0)dateOfBirth = splitter[i];
                    if(fields[i].toLowerCase().compareTo("identification number") == 0)idNumber = splitter[i];
                    if(fields[i].toLowerCase().compareTo("address") == 0)address = splitter[i];
                    if(fields[i].toLowerCase().compareTo("phone number") == 0)phoneNumber = splitter[i];
                    if(fields[i].toLowerCase().compareTo("checking account number") == 0)checkingAccountNumber = splitter[i];
                    if(fields[i].toLowerCase().compareTo("savings account number") == 0)savingAccountNumber = splitter[i];
                    if(fields[i].toLowerCase().compareTo("credit account number") == 0)creditAccountNumber = splitter[i];
                    if(fields[i].toLowerCase().compareTo("checking starting balance") == 0)checkingStartingBalance = splitter[i];
                    if(fields[i].toLowerCase().compareTo("savings starting balance") == 0)savingStartingBalance = splitter[i];
                    if(fields[i].toLowerCase().compareTo("credit starting balance") == 0)creditStartingBalance = splitter[i];
                    if(fields[i].toLowerCase().compareTo("credit max") == 0)creditMax = splitter[i];
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
