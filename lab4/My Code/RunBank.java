/*******************************************************************************
    Author: Rigoberto Quiroz
    Alias: Rigo
    Date: 11/07/19
    Course: CS3331
    Lab: Programming Assignment 3
    Lab Description:
      Since we have the base of our bank from our preveious lab (lab2). For this
      lab we have to refactor our code to have extra features. Some of the new
      features are adding new users, handling users with the same name, read
      transaction from transaction file, new class implementation BankStatements,
      and allow manager to create BankStatements for users.
        -new users:
          new users must have a first name, last name, date of birth, address,
          phoneNumber, and at least a savings account
        -names:
          customers with the same name should not appear in our database
        - transaction file
          program should be able to read from a transaction file and execute the
          task it has. As well as printing out errors if they might happen
        -BankStatements:
          Should get any users and only print out what they have done while
          logged in with time stamps
        -Manager feature
          Manager should be able to make bank statements for users
    Honesty Statement:
        I confirm that the work of this assignment is completely my own.
        By turning in this assignment. I declare that I did not receive
        unauthorized assistance. Moreover, all deliverables including,
        but not limited to the source code, lab report and output files were
        written and produced by me alone.
    Assumptions:
      1. manager should be the only one that should print BankStatements
      2. people can have same last or first name but not both
*******************************************************************************/


// libaries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.*;

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
        // stores customers in a hash map
        HashMap<String, Bank> database = createBank(fields);
        Bank user = userLogin(database);
        if(user == null)System.exit(0);
        createLogFile();
        boolean keepGoing = userMenu(user, database);

        while(keepGoing){
          user = userLogin(database);
          if(user == null) System.exit(0);
          keepGoing = userMenu(user, database);
        }
    }

    /**
     * checkData: checks all the user enters to make a new character.
     * @param  field String: what the user is entering for. firstname, address?
     * @return       String: user input checked or fixed
     */
    public static String checkData(String field){
      // user input
      Scanner scnr = new Scanner(System.in);
      String userInput;
      // firstname or lastname field
      if(field.compareTo("firstname") == 0 || field.compareTo("lastname") == 0){
        System.out.print(field + " (required): ");
        userInput = scnr.nextLine();
        if(Pattern.matches("[a-zA-Z]*", userInput))return userInput;
        return checkData(field);
      }
      // date of birth field
      else if(field.compareTo("dob") == 0){
        System.out.print("Date of Birth (FORMAT 01/22/2998) (required): ");
        userInput = scnr.nextLine();
        if(Pattern.matches("([0-1][0-9])\\/([0-3]{2})\\/([0-9]{4})", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("email") == 0){
        System.out.print(field + " (required): ");
        userInput = scnr.nextLine();
        if(Pattern.matches("^([a-z\\d\\.-]+)@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?$", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("password") == 0){
        System.out.print(field + " (required) EX: password123: ");
        userInput = scnr.nextLine();
        if(Pattern.matches("^[\\w@_-]{8,20}$", userInput))return userInput;
        return checkData(field);
      }
      // address field
      else if(field.compareTo("address") == 0){
        System.out.print(field + " (required): ");
        userInput = scnr.nextLine();
        if(Pattern.matches("[A-Za-z0-9'\\.\\-\\s\\,]{1,}", userInput))return userInput;
        return checkData(field);
      }
      // phone field
      else if(field.compareTo("phone") == 0){
        System.out.print(field + " (required) EX 1112223333: ");
        userInput = scnr.nextLine();
        if(Pattern.matches("^\\d{10}$", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("checking account number") == 0 || field.compareTo("credit account number") == 0){
        System.out.print(field + " (optional): ");
        userInput = scnr.nextLine();
        if(userInput.compareTo("") == 0)return "0";
        if(Pattern.matches("^\\d{4}$", userInput))return userInput;
        return checkData(field);
      }
      // savings account number
      else if(field.compareTo("savings account number") == 0){
        System.out.print(field + " (required): ");
        userInput = scnr.nextLine();
        if(Pattern.matches("^\\d{4}$", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("checking balance") == 0 || field.compareTo("credit max") == 0){
        System.out.print(field + " (required): $");
        userInput = scnr.nextLine();
        if(Pattern.matches("^\\d{1,}$", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("savings balance") == 0){
        System.out.print(field + " (required): $");
        userInput = scnr.nextLine();
        if(Pattern.matches("^\\d{1,}$", userInput))return userInput;
        return checkData(field);
      }
      else if(field.compareTo("credit balance") == 0){
        System.out.print(field + " (required): -$");
        userInput = scnr.nextLine();
        if(Pattern.matches("^\\d{1,}$", userInput))return userInput;
        return checkData(field);
      }else{
        System.out.println("Field requested not found!");
      }
     return null;
    }

    public static String collision(HashMap<String, Bank> database, String firstName, String lastName){
      String newHashKey = firstName + " " + lastName;
      newHashKey = newHashKey.toLowerCase();

      for(String keys: database.keySet()){
        if(keys.compareTo(newHashKey) == 0)return null;
      }
      return newHashKey;
    }

    /**
     * createUser: send all the information of the user to be checked. Sets default
     * values for empty fields user might leave
     * @param  database Bank: all users ()
     * @return          boolean: user added
     */
    public static boolean createUser(HashMap<String, Bank> database){
      String firstName = "";
      String lastName = "";
      String dateOfBirth = "";
      String email = "";
      String identification = "";
      String password = "";
      String address = "";
      String phone = "";
      String checkingAccountNum = "0";
      String savingsAccountNum = "0";
      String creditAccountNum = "0";
      String checkingBalance = "0";
      String savingsBalance = "0";
      String creditBalance = "0";
      String creditMax = "0";

      System.out.println("Adding New User");
      firstName = checkData("firstname");
      lastName = checkData("lastname");
      dateOfBirth = checkData("dob");
      email = checkData("email");
      identification = Integer.toString(database.size()+1);
      password = checkData("password");
      address = checkData("address");
      phone = checkData("phone");
      checkingAccountNum = checkData("checking account number");
      savingsAccountNum = checkData("savings account number");
      creditAccountNum = checkData("credit account number");
      if(checkingAccountNum.compareTo("0") != 0)checkingBalance = checkData("checking balance");
      savingsBalance = checkData("savings balance");
      if(creditAccountNum.compareTo("0") != 0){
        creditBalance = checkData("credit balance");
        creditMax = checkData("credit max");
        while(Double.parseDouble(creditMax) < Double.parseDouble(creditBalance)){
          creditMax = checkData("credit max");
        }
      }

      String hashKey = collision(database, firstName, lastName);
      if(hashKey != null){
        Person person = new Customer(firstName, lastName, dateOfBirth, email, identification, password, address, phone);
        Account checking = new Checking(Integer.parseInt(checkingAccountNum), Double.parseDouble(checkingBalance));
        Account savings = new Savings(Integer.parseInt(savingsAccountNum), Double.parseDouble(savingsBalance));
        Account credit = new Credit(Integer.parseInt(creditAccountNum), Double.parseDouble(creditBalance), Integer.parseInt(creditMax));
        Bank newCustomer = new Bank(person, checking, savings, credit);
        database.put(hashKey, newCustomer);
        System.out.println("\nNew User added!\n");
        return false;
      }
      System.out.println("\nUSER WITH NAME: " + firstName + " " + lastName + " ALREADY FOUND IN DATABASE\n");
      return false;
    }

    /**
     * printUpdatedBank: This method will be accesed once the user decides to
     * terminate the program (not when they decide to log out).
     * Record all the new accounts in Bank Users Updated.txt file.
     *
     * @param database Bank: all users
     */
    public static void printUpdatedBank(HashMap<String, Bank> database){
        try{
            // name of file
            File file = new File("Bank Users Updated.txt");
            FileWriter writer = new FileWriter(file);
            PrintWriter update = new PrintWriter(writer);
            update.println("First Name\tLast Name\tDate of Birth\tEmail\tIdetification Number\tPassword\tAddres\tPhone Number\tChecking Account Number\tSavings Account Number\tCredit Account Number\tChecking Balance\tSavings Balance\tCredit Balance");

            for(String customerName: database.keySet()){
              update.print(database.get(customerName).getPersonName() + "\t");
              update.print(database.get(customerName).getPersonDateOfBirth() + "\t");
              update.print(database.get(customerName).getPersonEmail() + "\t");
              update.print(database.get(customerName).getPersonID() + "\t");
              update.print(database.get(customerName).getPersonPassword() + "\t");
              update.print(database.get(customerName).getPersonAddress() + "\t");
              update.print(database.get(customerName).getPersonPhoneNumber() + "\t");
              update.print(database.get(customerName).getCheckingAccountNumber() + "\t");
              update.print(database.get(customerName).getSavingsAccountNumber() + "\t");
              update.print(database.get(customerName).getCreditAccountNumber() + "\t");
              update.print(database.get(customerName).getCheckingBalance() + "\t");
              update.print(database.get(customerName).getSavingsBalance() + "\t");
              update.print(database.get(customerName).getCreditBalance() + "\t");
              update.print(database.get(customerName).getMax() + "\n");
            }
            update.close();
        }catch(IOException IOE){
            System.out.println("Error File!");
            System.exit(0);
        }
    }

    /**
    * transfer: This method will make a transfer. The user will be able
    * to transfer money from their account or to other customers accounts.
    * The user can choose to which account they want to transfer to
    * @param  user                Bank: user logged in
    * @param  database            Bank: all users ()
    * @param  isTransactionFile   boolean: using transaction file
    * @param  senderName          String: sender name
    * @param  senderSource        String: sender source
    * @param  recieverName        String: reciever name
    * @param  recieverDestination String: recieverDestination
    * @param  amount              String: how much to transfer
    * @return                     boolean: succesful
    */
    public static boolean transfer(Bank user, HashMap<String, Bank> database, boolean isTransactionFile, String senderName, String senderSource, Bank recieverName, String recieverDestination, double amount){
        boolean transfer = true;
        String log = "";

        // using transaction file
        if(isTransactionFile){
          // transfering from checking to checking
          if((senderSource.toLowerCase().compareTo("checking") == 0) && (recieverDestination.toLowerCase().compareTo("checking") == 0)){
            // successful
            if(user.checkingTransfer(recieverName.getChecking(), amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
          // transfering from checking to savings
          if((senderSource.toLowerCase().compareTo("checking") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            // successful
            if(user.checkingTransfer(recieverName.getSavings(), amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
          // transfering from savings to checking
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("checking") == 0)){
            // successful
            if(user.savingsTransfer(recieverName.getChecking(), amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            //unsuccessful
            return false;
          }
          // transfering from savings to savings
          if((senderSource.toLowerCase().compareTo("savings") == 0) && (recieverDestination.toLowerCase().compareTo("savings") == 0)){
            // successful
            if(user.savingsTransfer(recieverName.getSavings(), amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
          // transfering to user credit account
          if(user.getPersonName().toLowerCase().compareTo(recieverName.getPersonName().toLowerCase()) == 0){
            // successful
            if(user.creditTransfer(user.getCredit(), amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
              user.addLog(log);
              userAction(log);
              return true;
            }
            //unsuccessful
            return false;
          }
        }
        // not using transaction file
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
                otherUser = otherUser.toLowerCase();

                if(database.get(otherUser) != null){
                  Bank userToTransfer = database.get(otherUser);
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
                    if(!user.checkingTransfer(userToTransfer.getChecking(), transferAmount))transfer = true;
                    else transfer = false;
                    log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + transferAmount + " to " + userToTransfer.getPersonName();
                    user.addLog(log);
                    userAction(log);
                  }

                  else if(userChoice == 2){
                    if(!user.savingsTransfer(userToTransfer.getSavings(), transferAmount))transfer = true;
                    else transfer = false;
                    log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + transferAmount + " to " + userToTransfer.getPersonName();
                    user.addLog(log);
                    userAction(log);
                  }
                  else{
                    System.out.println("Invalid Option. Try Again!");
                    transfer = true;
                  }
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
     * checkDuplicates: goes through the bank and checks if there are any users
     * with the same name
     * @param  db Bank: all users ()
     * @param  fn String: first name of user being checked
     * @param  ln String: last name of user being checked
     * @return    Boolean: duplicate found, false if not
     */
     /*
    public static boolean checkDuplicates(Bank db, String fn, String ln){
      Bank tempDB = db;
      while(tempDB != null){
        // duplicate found
        if((db.getPersonFirstName().toLowerCase().compareTo(fn.toLowerCase()) == 0) & (db.getPersonLastName().toLowerCase().compareTo(ln.toLowerCase()) == 0)){
          return true;
        }
        tempDB = tempDB.next;
      }
      return false;
    }
    */


    /**
    * withdraw: This method will get the users account an will withdraw money from it.
    * The only account the user can withdraw from would be from their checkings account.
    * @param  user              Bank: user logged in
    * @param  isTransactionFile boolean: using transaction file
    * @param  source            String which account to use
    * @param  amount            String: how much to withdraw
    * @return                   boolean: successful
    */
    public static boolean withdraw(Bank user, boolean isTransactionFile, String source, double amount){
        boolean withdraw = true;
        String log = "";
        // using transaction file
        if(isTransactionFile){
          // withdraw using checking
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
            // withdraw successful
            if(user.checkingWithdraw(amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a withdraw in Checkings account of $" + amount + " " + user.getCheckingAccountNumber() + " Current Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
          // withdraw savings
          if(source.toLowerCase().compareTo("savings") == 0){
            // withdraw successful
            if(user.savingsWithdraw(amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a withdraw in Savings account of $" + amount + " " + user.getSavingsAccountNumber() + " Current Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
        }
        // not using transaction file
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
    * deposit: This method will get the users account and deposit the
    * amount they entered. User decides which account the money
    * will be added to.
    * @param  user              Bank: user logged in
    * @param  isTransactionFile Boolean: using transaction file
    * @param  source            StringL what account to use
    * @param  amount            Double: how much to deposit
    * @return                   boolean: successful
    */
    public static boolean deposit(Bank user, boolean isTransactionFile, String source, double amount){
        boolean deposit = true;
        String log = "";
        // transaction file is being used
        if(isTransactionFile){
          // enter money into checking
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
            // successful deposit
            if(user.checkingDeposit(amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a deposit in Checkings Account of $" + amount + " " + user.getCheckingAccountNumber() + " Curent Balance: $" + user.getCheckingBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
          // enter money into savings
          if(source.toLowerCase().compareTo("savings") == 0){
            // successful deposit
            if(user.savingsDeposit(amount)){
              // log
              log = getTime() + ": " + user.getPersonName() + " made a deposit int Savings Account of $" + amount + " " + user.getSavingsAccountNumber() + " Cuurent Balance: $" + user.getSavingsBalance();
              user.addLog(log);
              userAction(log);
              return true;
            }
            // unsuccessful
            return false;
          }
        }
        // not using transaction file
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
    * inquireBalance: This method will display the account details of the user logged in.
    * Whether it be checking, savings, or credit, the user will be able to
    * see their account
    * @param  user              Bank: user using account
    * @param  isTransactionFile boolean: using transaction file
    * @param  view              view: which account to view
    * @return                   boolean: user was successful in getting account information
    */
    public static boolean inquireBalance(Bank user, boolean isTransactionFile, String view){
        boolean inquire = true;
        String logs = "";
        int userChoice = 0;

        // user wants to acquire information
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
                  // inquire accounts through transaction file
                  if(view.toLowerCase().compareTo("checking") == 0) userChoice = 1;
                  if(view.toLowerCase().compareTo("savings") == 0) userChoice = 2;
                  if(view.toLowerCase().compareTo("credit") == 0) userChoice = 3;
                }

                // checking account data
                if(userChoice == 1){
                  System.out.println("*****************************************");
                  System.out.println("Checking Account Number: " + user.getCheckingAccountNumber());
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
                  System.out.println("*****************************************");
                  System.out.println("Credit Account Number: " + user.getCreditAccountNumber());
                  System.out.println("Credit Balance: " + user.getCreditBalance());
                  System.out.println("Credit Max: " + user.getMax());
                  System.out.println("*****************************************\n");
                  // log
                  logs = getTime() + ": " + user.getPersonName() + " inquire Credit Balance " + user.getCreditAccountNumber() + ": $" + user.getCreditBalance();
                  user.addLog(logs);
                  userAction(logs);

                  inquire = false;
                }
                // all accounts data
                else if(userChoice == 4){
                  System.out.println("*****************************************");
                    System.out.println("Checking Account Number: " + user.getCheckingAccountNumber());
                    System.out.println("Checking Balance: " + user.getCheckingBalance());
                    System.out.println("Savings Account Number: " + user.getSavingsAccountNumber());
                    System.out.println("Savings Balance: " + user.getSavingsBalance());
                    System.out.println("Credit Account Number: " + user.getCreditAccountNumber());
                    System.out.println("Credit Balance: " + user.getCreditBalance());
                    System.out.println("Credit Max: " + user.getMax());
                    System.out.println("*****************************************");
                    // log
                    logs = getTime() + ": " + user.getPersonName() + " inquire All Accounts Balance ";
                    user.addLog(logs);
                    userAction(logs);
                    inquire = false;
                }
                // user exits menu
                else if(userChoice == 5)inquire = false;
                else{
                  // user enters invalid menu option
                  System.out.println("Invalid Option Try again!");
                  inquire = true;
                }
            }catch(InputMismatchException IME){
              // user enters value that is not a character
              System.out.println("Please enter a valid menu option [1 - 5]");
              inquire = true;
            }
        }
        return true;
    }

    /**
     * userMenu: This method will display the user menu. The user will be
     * able to do various things like deposit or withdraw.
     *
     * @param user Bank: user using account
     * @param database bank: all users
     * @return boolean: user want to logout
     */
    public static boolean userMenu(Bank user, HashMap<String, Bank> database){
        boolean menu = true;
        // keep going if user until users does not want to
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

    public static boolean userAndPasswordMatch(Bank userToLogin){
      Scanner scnr = new Scanner(System.in);
      System.out.print("Password: ");
      String password = scnr.nextLine();

      if(password.compareTo(userToLogin.getPersonPassword()) == 0) return true;
      return false;
    }


        /**
         * transactionBuffer: Depending on what the header action request, the buffer
         * will call different methods and complete the task.
         * @param  db Bank: all users ()
         * @param  sn String: Sender name
         * @param  ss String: Sender source
         * @param  a  String: Action
         * @param  rn String: reciever name
         * @param  rd String: receiver destination
         * @param  sa String: sender amount
         * @return    Boolean: transaction was complete? User found?
         *
         * IMPPORTANT:
         * Test if the user that does not exists can still preform task
         */
        public static boolean transactionBuffer(HashMap<String, Bank> database, String sn, String ss, String a, String rn, String rd, String sa){
          // checks if sender amound is a double
          try{
            Double.parseDouble(sa);
          }catch(NumberFormatException NFE){
            System.out.println("Not a number found!");
            return false;
          }
          Bank senderName = database.get(sn.toLowerCase());
          if(senderName == null && a.toLowerCase().compareTo("deposits") == 0){
            senderName = database.get(rn.toLowerCase());
            ss = "checking";
          }
          if(senderName == null)return false;
          if(a.toLowerCase().compareTo("deposits") == 0){
            if(deposit(senderName, true, ss, Double.parseDouble(sa)))return true;
            return false;
          }
          if(a.toLowerCase().compareTo("withdraws") == 0){
            if(withdraw(senderName, true, ss, Double.parseDouble(sa)))return true;
            return false;
          }
          if(a.toLowerCase().compareTo("pays") == 0 || a.toLowerCase().compareTo("transfers") == 0){
            Bank recieverName = database.get(rn.toLowerCase());
            if(recieverName == null)return false;
            if(transfer(senderName, database, true, sn, ss, recieverName, rd, Double.parseDouble(sa)))return true;
            return false;
          }
          if(a.toLowerCase().compareTo("inquires") == 0){
            if(inquireBalance(senderName, true, ss))return true;
            return false;
          }
          return true;
        }
    /**
     * Transaction: This method will get the array of strings that contain the
     * header of the .txt file read. it will then look through the txt file to
     * find the data that corresponds to it. It will then send that information
     * to a buffer to be cpompleted. Prints out if action was completed or not
     * @param  db       Bank: contains all users (link list)
     * @param  fileName String: Name of file
     * @return          Boolean: when all the tasks have been finished
     */
    public static boolean transactions(HashMap<String, Bank> database, String fileName){
      // Header array
      String transactionFields[] = fileFields(fileName);
      // fields to look for
      String senderName = "";
      String senderSouce = "";
      String action = "";
      String recieverName = "";
      String recieverDestination = "";
      String senderAmount = "0";
      int transactionDone = 1;

      try{
        // open file
        File file = new File(fileName);
        Scanner scnr = new Scanner(file);
        String line = scnr.nextLine();
        while(scnr.hasNextLine()){
          line = scnr.nextLine();
          String splitter[] = line.split("\t");
          // looks for each field in the .txt file that corresponds to the header array
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
          // transaction failed
          if(!transactionBuffer(database, senderName, senderSouce, action, recieverName, recieverDestination, senderAmount)){
            System.out.println(transactionDone + ": Transaction status: FAILED");
            System.out.println(line + "\n");
          }else{
            // transaction completed
            System.out.println(transactionDone + ": Transaction status: COMPLETE");
          }
          transactionDone++;
        }
        return true;
      }catch(FileNotFoundException FNFE){
        // file not found
        System.out.println("Transaction File not found!");
        return false;
      }
    }

    /**
     * makeStatement: asks manager to enter name of customer to make bank statment for
     * repeats until we have found a customer
     * @param db Bank: all users ()
     */
    public static void makeStatement(HashMap<String, Bank> database){
      Scanner scnr = new Scanner(System.in);
      System.out.println("Customer Name: ");
      String managerUser = scnr.nextLine();
      Bank userToStatement = database.get(managerUser.toLowerCase());
      while(userToStatement == null){
        System.out.print("Customer Name\n> ");
        managerUser = scnr.nextLine();
        userToStatement = database.get(managerUser);
      }
      BankStatements userStatement = new BankStatements(userToStatement);
      userStatement.statement();
    }

    /**
     * managerMenu: This method will display the manager menu. The manager
     * will be able to see other users accounts
     *
     * @param database Bank:all users ()
     * @return boolean: keep using program
     */
    public static boolean managerMenu(HashMap<String, Bank> database){
        Scanner scnr = new Scanner(System.in);
        boolean manager = true;
        String log = "";

        // keep going until manager does not want to uise account anymore
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
                // Sees one user account
                if(userChoice == 1){
                    scnr = new Scanner(System.in);
                    System.out.println("Enter Name of User");
                    System.out.print("> ");
                    String userName = scnr.nextLine();
                    Bank userToInquire = database.get(userName.toLowerCase());
                    if(userToInquire != null){
                      log = "Manager inquired " + userToInquire.getPersonName() + " Account";
                      manager = inquireBalance(userToInquire, false, "");
                    }
                }
                // Sees all accounts
                else if(userChoice == 2){
                  log = getTime() + ": " + "Manager inquired all customers accounts!";
                  for(String customer: database.keySet()){
                    System.out.println();
                    System.out.println("Customer Name: " + database.get(customer).getPersonName());
                    System.out.println("Checking Account Number: " + database.get(customer).getCheckingAccountNumber());
                    System.out.println("Savings Account Number: " + database.get(customer).getSavingsAccountNumber());
                    System.out.println("Credit Account Number: " + database.get(customer).getCreditAccountNumber());
                    System.out.println("Checking Balance: " + database.get(customer).getCheckingAccountNumber());
                    System.out.println("Savings Balance: " + database.get(customer).getSavingsAccountNumber());
                    System.out.println("Credit Balance: " + database.get(customer).getCreditAccountNumber());
                    System.out.println();
                  }
                  //userLog(log);
                }
                // manager wants to make users statement
                else if(userChoice == 3)makeStatement(database);
                // manager wants to logout
                else if(userChoice == 4)manager = false;
                // manager cannot logout
                else if(userChoice == 5){
                  printUpdatedBank(database);
                  System.exit(0);
                }
                else{
                  // not a valid option
                  System.out.println("Invalid User menu. Try Again");
                }
            }catch(InputMismatchException IME){
              // manager enters values other than numbers
              System.out.println("Please enter a valid menu option. [1 - 5]");
              return managerMenu(database);
            }
        }
        return false;
    }

    /**
     * userLogin: This method wil find the user from the database and login
     * into their account, or their manager account.
     * @param database Bank: all users
     * @return Bank: user was found or null if not
     */
    public static Bank userLogin(HashMap<String, Bank> database){
        boolean login = false;
        int attempts = 3;
        // keep going while we want to login
        while(!login){
            Scanner scnr = new Scanner(System.in);
            //Bank tempDB = database;

            // menu
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
            loginName = loginName.toLowerCase();

            // quit
            if(loginName.toLowerCase().compareTo("!q") == 0){
                printUpdatedBank(database);
                System.exit(0);
            }
            // creates user
            else if(loginName.toLowerCase().compareTo("useradd") == 0){
              createUser(database);
            }
            // get transactions from file
            else if(loginName.toLowerCase().compareTo("transaction file") == 0){
              System.out.println("read transaction file");
              transactions(database, "Transaction Actions.txt");
            }
            // login as manager
            else if(loginName.toLowerCase().compareTo("manager") == 0){
              login = managerMenu(database);
            }
            else if(database.get(loginName) != null){
              while(attempts > 0){
                if(userAndPasswordMatch(database.get(loginName))) return database.get(loginName);
                System.out.println("User not found try again!");
                attempts--;
              }
            }
            attempts = 3;
        }
        // did not find the user account
        return null;
    }

    /**
    * createBank: This method will get the txt which contains all the user
    * data and will take certain data fields and initialize them to their
    * apporpiate class. Once all data has been organized, the instances will be set
    * in a HashMap
    * @param  fields[] String: header array
    * @return          HashMap: all users
    */
    public static HashMap<String, Bank> createBank(String fields[]){
      // creates hashmap with String insert key, and value of type Bank
      HashMap<String, Bank> bankDataBase = new HashMap<>();
      // default values for users
      String firstName = "";
      String lastName = "";
      String dateOfBirth = "";
      String email = "";
      String idNumber = "";
      String password = "";
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
                String[] splitter = line.split("\t");
                // gets user data accrding to what we are looking for
                for(int i = 0; i < fields.length; i++){
                    if(fields[i].toLowerCase().compareTo("first name") == 0)firstName = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("last name") == 0)lastName = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("date of birth") == 0)dateOfBirth = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("email") == 0)email = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("identification number") == 0)idNumber = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("password") == 0)password = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("address") == 0)address = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("phone number") == 0)phoneNumber = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("checking account number") == 0)checkingAccountNumber = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("savings account number") == 0)savingAccountNumber = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("credit account number") == 0)creditAccountNumber = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("checking starting balance") == 0)checkingStartingBalance = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("savings starting balance") == 0)savingStartingBalance = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("credit starting balance") == 0)creditStartingBalance = splitter[i];
                    else if(fields[i].toLowerCase().compareTo("credit max") == 0)creditMax = splitter[i];
                }
                // creates hashMap key, users first and last name
                String hashMapInsertKey = firstName.toLowerCase() + " " + lastName.toLowerCase();
                // creates objects of each type, Customer, Checking, Savings, and credit
                Person person = new Customer(firstName, lastName, dateOfBirth, email, idNumber, password, address, phoneNumber);
                Account checking = new Checking(Integer.parseInt(checkingAccountNumber), Double.parseDouble(checkingStartingBalance));
                Account savings = new Savings(Integer.parseInt(savingAccountNumber), Double.parseDouble(savingStartingBalance));
                Account credit = new Credit(Integer.parseInt(creditAccountNumber), Double.parseDouble(creditStartingBalance), Integer.parseInt(creditMax));
                // adds user to hashMap based on key
                Bank newUser = new Bank(person, checking, savings, credit);
                bankDataBase.put(hashMapInsertKey, newUser);
            }
        }catch(FileNotFoundException FNFE){
            System.out.println("Error creating bank users");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.txt does not exists");
            System.out.println("2. Bank Users.txt does not contain user information or is empty");
            System.exit(0);
        }
        return bankDataBase;
    }

    /**
     * createLogFile: creates the log file.
     */
    public static void createLogFile(){
      try{
        // log file
        File file = new File("logs.txt");
        FileWriter writer = new FileWriter(file);
        PrintWriter logWriter = new PrintWriter(writer);
        logWriter.close();
      }catch(IOException IOE){
        // error creating file
        System.out.println("Error File!");
        System.exit(0);
      }
    }

    /**
     * userAction: this method will record what all the users have done into a
     * .txt file
     * @param accountActions String: what did the user do while logged in
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
     * getTime: This method get the local time from the local machine and put it
     * in a format of 12Hrs.
     * @return String: current time
     */
    public static String getTime(){
      // current time
      SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
      Date date = new Date();
      // returns in format of 12 hrs
      return formatTime.format(date);
    }

    /**
     * autoLogin: Logs in into the requested user if found, otherwise it will not
     * @param  db          Bank: All users ()
     * @param  userToLogin String: user to be login
     * @return             Bank: user was found! or null if not
     */
     /*
    public static Bank autoLogin(Bank db, String userToLogin){
      while(db != null){
        // user found
        if(db.getPersonName().toLowerCase().compareTo(userToLogin.toLowerCase()) == 0)return db;
        db = db.next;
      }
      // user not found
      return null;
    }
    */


    /**
     * fileFields: will open a file and look for its header located
     * in the first line of the file. It will then store all the headers
     * into an array and return that
     * @param  fileName String, contains the name of file to opne
     * @return          returns array of string or null if not found
     */
    public static String[] fileFields(String fileName){
        try{
          // opens file
            File file = new File(fileName);
            Scanner scnr = new Scanner(file);
            // stores first line of the file into an array and returns it
            String line = scnr.nextLine();
            String splitter[] = line.split("\t");
            return splitter;
        }catch(FileNotFoundException FNFE){
            System.out.println("Error!");
            // no file found
            return null;
        }
    }


    /**
     * idGetter: gets the last user of the user list and returns the id + 1
     * @param  db Bank: all users ()
     * @return    String: id for new last user
     */
     /*
    public static String idGetter(Bank db){
      while(db.next != null){
        db = db.next;
      }
      // gets last users id and adds one
      int id = Integer.parseInt(db.getPersonID()) + 1;
      // returns in String format
      return Integer.toString(id);
    }
    */

    /**
     * addUser: adds all the checked data into
     * @param db     Bank: all users
     * @param fn     String: first name of new user
     * @param ln     String: last name of new user
     * @param dob    String: Date of birth of new user
     * @param id     String: id of new user
     * @param addr   String: address of new user
     * @param p      String: phone number of new user
     * @param can    String: checking account number of new user
     * @param san    String: savings account number of new user
     * @param crdan  String: credit account number of new user
     * @param checkB String: checking balance of new user
     * @param savB   String: savings balance of new user
     * @param crdB   String: credit balance of new user
     * @param crdMax String: credit max of new user
     */
     /*
    public static void addUser(Bank db, String fn, String ln, String dob, String id, String addr, String p, String can, String san, String crdan, String checkB, String savB, String crdB, String crdMax){
      // gets last user from database ()
      Bank tempBank = db;
      while(tempBank.next != null){
        tempBank = tempBank.next;
      }
      // adding new user
      Person person = new Customer(fn, ln, dob, id, addr, p);
      Account checking = new Checking(Integer.parseInt(can), Double.parseDouble(checkB));
      Account savings = new Savings(Integer.parseInt(san), Double.parseDouble(savB));
      Account credit = new Credit(Integer.parseInt(crdan), Double.parseDouble(crdB), Integer.parseInt(crdMax));
      tempBank.next = new Bank(person, checking, savings, credit);
    }
    */

    /**
     * createUser: send all the information of the user to be checked. Sets default
     * values for empty fields user might leave
     * @param  database Bank: all users ()
     * @return          boolean: user added
     */
     /*
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
    */



    /**
     * fileExistence: The method will check the Bank Users2.txt and make
     * sure that the file exits and that it at least contains
     * 1 line of Customer data.
     *
     * @return boolean: file exists
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
