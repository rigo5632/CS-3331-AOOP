/*******************************************************************************
    Author: Rigoberto Quiroz, Denise Castro
    Alias: Rigo, Watch12
    Date: 11/26/19
    Course: CS3331
    Lab: Programming Assignment 4
    Lab Description:
      Since we have the base of our bank from our preveious lab (lab3). For this
      lab we have to refactor our code to our partners code. The only new feature
       is the login function that checks the customers name with a pasword. 
    Honesty Statement:
        I confirm that the work of this assignment is completely my own.
        By turning in this assignment. I declare that I did not receive
        unauthorized assistance. Moreover, all deliverables including,
        but not limited to the source code, lab report and output files were
        written and produced by me alone.
    Assumptions:
      1. manager should be the only one that should print BankStatements or create new users
      2. people can have same last or first name but not both
*******************************************************************************/


// libaries
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        HashMap<String, Customer> database = createBank(fields);
        Customer user = userLogin(database);
        if(user == null)System.exit(0);
        createLogFile();
        boolean keepGoing = userMenu(user, database);

        while(keepGoing){
          user = userLogin(database);
          if(user == null) System.exit(0);
          keepGoing = userMenu(user, database);
        }
    }

    /** TAKEN FROM RIGOBERTO
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

    /**TAKEN FROM DENISE
     * collision: This method will check if new users do not use the same key. if
     * they have the same key then they are already in the database
     * @param  database  HashMap: All users
     * @param  firstName String: new users name
     * @param  lastName  String: new users name
     * @return           String: new hash map insert key
     */
    public static String collision(HashMap<String, Customer> database, String firstName, String lastName){
      String newHashKey = firstName + " " + lastName;
      newHashKey = newHashKey.toLowerCase();

      for(String keys: database.keySet()){
        if(keys.compareTo(newHashKey) == 0)return null;
      }
      return newHashKey;
    }

    /** TAKEN FROM RIGO
     * createUser: send all the information of the user to be checked. Sets default
     * values for empty fields user might leave
     * @param  database hashMap: all users
     * @return          boolean: user added
     */
    public static boolean createUser(HashMap<String, Customer> database){
      // all fields that a new user will need
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

      // checking if new data is correct
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

      // IS USER NEW?
      String hashKey = collision(database, firstName, lastName);
      // add new user
      if(hashKey != null){
        Person person = new Customer(firstName, lastName, dateOfBirth, email, identification, password, address, phone);
        Checking checking = new Checking(Integer.parseInt(checkingAccountNum), Double.parseDouble(checkingBalance));
        Savings savings = new Savings(Integer.parseInt(savingsAccountNum), Double.parseDouble(savingsBalance));
        Credit credit = new Credit(Integer.parseInt(creditAccountNum), Double.parseDouble(creditBalance), Integer.parseInt(creditMax));
        Customer newCustomer = new Customer(person, checking, savings, credit);
        database.put(hashKey, newCustomer);
        System.out.println("\nNew User added!\n");
        return false;
      }
      System.out.println("\nUSER WITH NAME: " + firstName + " " + lastName + " ALREADY FOUND IN DATABASE\n");
      return false;
    }

    /**Taken from Rigoberto
     * printUpdatedBank: This method will be accessed once the user decides to
     * terminate the program (not when they decide to log out).
     * Record all the new accounts in Bank Users Updated.txt file.
     *
     * @param database Bank: all users
     */
    public static void printUpdatedBank(HashMap<String, Customer> database){
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
              update.print(database.get(customerName).getChecking().getAccountNumber() + "\t");
              update.print(database.get(customerName).getSavings().getAccountNumber() + "\t");
              update.print(database.get(customerName).getCredit().getAccountNumber() + "\t");
              update.print(database.get(customerName).getChecking().getBalance() + "\t");
              update.print(database.get(customerName).getSavings().getBalance() + "\t");
              update.print(database.get(customerName).getCredit().getBalance() + "\t");
              update.print(database.get(customerName).getMax() + "\n");
            }
            update.close();
        }catch(IOException IOE){
            System.out.println("Error File!");
            System.exit(0);
        }
    }
    
    /**Taken from Rigoberto, Modified by Denise
    * transfer: This method will make a transfer. The user will be able
    * to transfer money from their account or to other customers accounts.
    * The user can choose to which account they want to transfer to
    * @param  user                Bank: user logged in
    * @param  database            HashMap: contains all users
    * @param  isTransactionFile   boolean: using transaction file
    * @param  senderName          String: sender name
    * @param  senderSource        String: sender source
    * @param  recieverName        String: receiver name
    * @param  recieverDestination String: recieverDestination
    * @param  amount              String: how much to transfer
    * @return                     boolean: successful
    */
    public static boolean transfer(Customer user, HashMap<String, Customer> database, boolean isTransactionFile, String senderName, String senderSource, Customer recieverName, String recieverDestination, double amount){
        boolean transfer = true;
        String log = "";
        Account tempAccount = null;
        String type = "";
        String type2 = "";
        Account recieverAccount = null;
        // using transaction file
        if(isTransactionFile){
        	
        	if(senderSource.toLowerCase().compareTo("checking") == 0) tempAccount = user.getChecking();
        	else if (senderSource.toLowerCase().compareTo("savings") == 0) tempAccount = user.getSavings();
        	else if (senderSource.toLowerCase().compareTo("credit") == 0) tempAccount = user.getCredit();
        	
          if((recieverDestination.toLowerCase().compareTo("checking") == 0))
        	  recieverAccount = recieverName.getChecking();
          else if(recieverDestination.toLowerCase().compareTo("savings") == 0)
        	  recieverAccount = recieverName.getSavings();
          else if(user.getPersonName().toLowerCase().compareTo(recieverName.getPersonName().toLowerCase()) == 0)
        	  recieverAccount = user.getCredit();

          // successful
          if(tempAccount.transfer(recieverAccount, amount)){
            // log
            log = getTime() + ": " + user.getPersonName() + " made a transfer of $" + amount + " to " + recieverName.getPersonName();
            user.addLog(log);
            userAction(log);
            return true;
          }
          // unsuccessful
          return false;
        }
        // not using transaction file
        if(!isTransactionFile){
          Scanner scnr = new Scanner(System.in);
          System.out.println("\nTransfer Money");
          while(transfer){
            try{
              // transfer menu
              System.out.println("Select Transfer Process");
              System.out.println("1. Your Acounts");
              System.out.println("2. Other users Account");
              System.out.print("> ");
              int userChoice = scnr.nextInt();

              switch(userChoice){
                // user transfer within their account
                case 1:
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
	                switch(transferChoice){
	                case 1: 
	                	tempAccount = user.getSavings();
	                	type = "savings ";
	                	recieverAccount = user.getChecking();
	                	type2 = "checking ";
	                break;
	                // savings transfer
	                case 2:
	                	tempAccount = user.getChecking();
	                	type = "checking ";
	                	recieverAccount = user.getSavings();
	                	type2 = "savings ";
	                break;
	                // credit transfer
	                case 3:
	                tempAccount = user.getChecking();
	                type = "checking ";
	                recieverAccount = user.getCredit();
	                type2 = "credit ";
	                break;
	                default:
	                  System.out.println("Invalid Option. Try Again!");
	                  transfer = true;
	              }
	                  // updates account
	                  transfer = !(tempAccount.transfer(user.getChecking(), transferAmount));
	                  // log
	                  if(!transfer){
	                    log = getTime() + ": " + user.getPersonName() + " transfered money from " + type+ "to " + type2 +"\b."+ type.replaceFirst("c", "C") +": $" + tempAccount.getBalance() + type2.replaceFirst("c","C") +": $" +recieverAccount.getBalance();
	                    user.addLog(log);
	                    userAction(log);
	                  }
              break;
              // transfer to other customers account
                case 2:
                	// transfer menu
                System.out.println("Enter name of Person");
                System.out.print("> ");
                scnr.nextLine();
                String otherUser = scnr.nextLine();
                otherUser = otherUser.toLowerCase();

                if(database.get(otherUser) != null){
                  Customer userToTransfer = database.get(otherUser);
                  System.out.println("Select Account to transfer");
                  System.out.println("1. Checking");
                  System.out.println("2. Savings");
                  System.out.print("> ");
                  userChoice = scnr.nextInt();

                  // amount they will transfer
                  System.out.println("Amount to transfer");
                  System.out.print("> ");
                  double transferAmount1 = scnr.nextDouble();

                  // transfer to checking account
                  switch(userChoice){
                  case 1:
                	tempAccount = user.getChecking();
                	recieverAccount = userToTransfer.getChecking();
                  break;
                  case 2:
                	  tempAccount = user.getSavings();
                	  recieverAccount = userToTransfer.getSavings();
                  break;
                 default:
                    System.out.println("Invalid Option. Try Again!");
                    transfer = true;
                  break;
                }
                  transfer = !(tempAccount.transfer(recieverAccount, transferAmount1));
                  if(!transfer){
                    log = getTime() + ": " + user.getPersonName() + " made a transfer of $" +transferAmount1 + " to " + userToTransfer.getPersonName();
                    user.addLog(log);
                    userAction(log);
                  }
                break;
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
    
    /**Taken from Rigoberto, Modified by Denise
    * withdraw: This method will get the users account an will withdraw money from it.
    * The only account the user can withdraw from would be from their checkings account.
    * @param  user              Bank: user logged in
    * @param  isTransactionFile boolean: using transaction file
    * @param  source            String which account to use
    * @param  amount            String: how much to withdraw
    * @return                   boolean: successful
    */
    public static boolean withdraw(Customer user, boolean isTransactionFile, String source, double amount){
        boolean withdraw = true;
        String log = "";
        String type = "";
        Account tempAccount = null;
        double withdrawAmount = 0;
        // using transaction file
        if(isTransactionFile){
          // withdraw using checking
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
            tempAccount = user.getChecking();
            type = "Checking ";
          }
          // withdraw savings
          if(source.toLowerCase().compareTo("savings") == 0){
        	  tempAccount = user.getSavings();
        	  type = "Savings ";
          }          
      	// withdraw successful
          if(tempAccount.withdraw(amount)){
            // log
            log = getTime() + ": " + user.getPersonName() + " made a withdraw in "+type+ "account of $" + amount + " " + tempAccount.getAccountNumber() + " Current Balance: $" + tempAccount.getBalance();
            user.addLog(log);
            userAction(log);
            return true;
          }
          // unsuccessful
          return false;
        }
        // not using transaction file
        if(!isTransactionFile){
          // withdraw menu
          System.out.println("\nWithdraw Money");
          while(withdraw){
              try{
                Scanner scnr = new Scanner(System.in);
                System.out.println("Select account to withdraw: ");
                System.out.println("1. Checking");
                System.out.println("2. Savings");
                System.out.print("> ");
                int userChoice = scnr.nextInt();

                switch(userChoice){
                case 1: tempAccount = user.getChecking();
                  type = "Checking ";

                  break;
                case 2: tempAccount = user.getSavings();
                	type = "Savings ";
                  break;
                 default:
                  System.out.println("Invalid option. Please Try again!");
                  withdraw = true;
                  
                }
                  
                  if(userChoice == 1 || userChoice == 2) {
                      System.out.println("Amount to Withdraw: ");
                      System.out.print("> $");
                      withdrawAmount = scnr.nextDouble();
                      withdraw = !(tempAccount.withdraw(withdrawAmount));
                  if(!withdraw){
                    log = getTime() + ": " + user.getPersonName() + " made a withdraw in "+type+ "account of $" + withdrawAmount + " " + tempAccount.getAccountNumber() + " Current Balance: $" + tempAccount.getBalance();
                    user.addLog(log);
                    userAction(log);
                  }
                }
              }catch(InputMismatchException IME){
                System.out.println("Please enter a valid withdraw amount");
                withdraw = true;
              }
          }
        return true;
      }
      return false;
    }

    //Changed by Denise
    /**Taken from Rigoberto, Modified by Denise
    * deposit: This method will get the users account and deposit the
    * amount they entered. User decides which account the money
    * will be added to.
    * @param  user              Bank: user logged in
    * @param  isTransactionFile Boolean: using transaction file
    * @param  source            StringL what account to use
    * @param  amount            Double: how much to deposit
    * @return                   boolean: successful
    */
    public static boolean deposit(Customer user, boolean isTransactionFile, String source, double amount){
        boolean deposit = true;
        String log = "";
        String type = "";
        Account tempAccount = null;
        double depositAmount;
        // transaction file is being used
        if(isTransactionFile){
          // enter money into checking
          if(source.toLowerCase().compareTo("checking") == 0 || source == ""){
        	  tempAccount =user.getChecking();
        	  type = "Checking ";
          } else if(source.toLowerCase().compareTo("savings") == 0){
              // enter money into savings
        	  tempAccount = user.getSavings();
        	  type = "Savings ";
          }
          // successful deposit
          if(tempAccount.deposit(amount)){
            // log
            log = getTime() + ": " + user.getPersonName() + " made a deposit in "+ type + "Account of $" + amount + " " + tempAccount.getAccountNumber() + " Curent Balance: $" + tempAccount.getBalance();
            user.addLog(log);
            userAction(log);
            return true;
          }
          // unsuccessful
          return false;
        }
        
        // not using transaction file
        if(isTransactionFile == false){
          System.out.println("\nDeposit Money");
          while(deposit){
            try{
              // deposit menu
              Scanner scnr = new Scanner(System.in);
              System.out.println("Select account to deposit: ");
              System.out.println("1. Checking");
              System.out.println("2. Savings");
              System.out.print("> ");
              int userChoice = scnr.nextInt();

              // add money to their checking account
              switch(userChoice){
              case 1:
            	tempAccount = user.getChecking();
            	type = "Checking ";
              break;
                // add money to their savings account
              case 2:
            	tempAccount = user.getSavings();
            	type = "Savings ";
              break;
              // invalid options
              default:
                System.out.println("Invalid Option. Try Again!");
                deposit = true;
              }
              if(!type.equals("")){
                  System.out.println("Amount to Deposit: ");
                  System.out.print("> $");
                  depositAmount = scnr.nextDouble();
                  // updates account
                  deposit = !(tempAccount.deposit(depositAmount));
                  // make log if deposit was successful
                  if(!deposit){
                    log = getTime() + ": " + user.getPersonName() + " made a deposit in "+ type +"Account of $" + depositAmount+ " " + tempAccount.getAccountNumber() + " Cuurent Balance: $" + tempAccount.getBalance();
                    userAction(log);
                    user.addLog(log);
                  }
              }
            }catch(InputMismatchException IME){
              System.out.println("Invalid Input");
              deposit = true;
              }
        }
        return true;
      }
      return false;
    }

    //Changed by Denise
    
    /**Taken from Rigoberto, Modified by Denise
    * inquireBalance: This method will display the account details of the user logged in.
    * Whether it be checking, savings, or credit, the user will be able to
    * see their account
    * @param  user              Bank: user using account
    * @param  isTransactionFile boolean: using transaction file
    * @param  view              view: which account to view
    * @return                   boolean: user was successful in getting account information
    */
    public static boolean inquireBalance(Customer user, boolean isTransactionFile, String view){
        boolean inquire = true;
        String logs = "";
        int userChoice = 0;
        String type = "";
        Account tempAccount = null;

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
                switch(userChoice){
                case 1: tempAccount = user.getChecking();
                		type = "Checking ";
                  inquire = false;
                break;
                //savings account data
                case 2: tempAccount = user.getSavings();
                		type = "Savings ";
                  inquire = false;
                break;
                // credit account data
                case 3: tempAccount = user.getCredit();
                		type = "Credit ";
                  inquire = false;
                break;
                // all accounts data
                case 4:
                  System.out.println("*****************************************");
                    System.out.println("Checking Account Number: " + user.getChecking().getAccountNumber());
                    System.out.println("Checking Balance: " + user.getChecking().getBalance());
                    System.out.println("Savings Account Number: " + user.getSavings().getAccountNumber());
                    System.out.println("Savings Balance: " + user.getSavings().getBalance());
                    System.out.println("Credit Account Number: " + user.getCredit().getAccountNumber());
                    System.out.println("Credit Balance: " + user.getCredit().getBalance());
                    System.out.println("Credit Max: " + user.getMax());
                    System.out.println("*****************************************");
                    // log
                    logs = getTime() + ": " + user.getPersonName() + " inquire All Accounts Balance ";
                    user.addLog(logs);
                    userAction(logs);
                    inquire = false;
                break;
                // user exits menu
                case 5: inquire = false; break;
                default: 
                  // user enters invalid menu option
                  System.out.println("Invalid Option Try again!");
                  inquire = true;
                break;
                }
                if(userChoice<4) {
                	System.out.println("*****************************************");
                	System.out.println(type + "Account Number: " + tempAccount.getAccountNumber());
                	System.out.println(type +"Balance: " + tempAccount.getBalance());
                	System.out.println("*****************************************\n");
                	// log
	                logs = getTime() + ": " + user.getPersonName() + " inquire "+ type +"Balance " + tempAccount.getAccountNumber() + ": $" + tempAccount.getBalance();
	                user.addLog(logs);
	                userAction(logs);
                }
            }catch(InputMismatchException IME){
              // user enters value that is not a character
              System.out.println("Please enter a valid menu option [1 - 5]");
              inquire = true;
            }
        }
        return true;
    }

    
    /**Taken From Rigoberto, Modified by Denise
     * userMenu: This method will display the user menu. The user will be
     * able to do various things like deposit or withdraw.
     *
     * @param user Bank: user using account
     * @param database bank: all users
     * @return boolean: user want to logout
     */
    public static boolean userMenu(Customer user, HashMap<String, Customer> database){
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
                
                // functionalities
                switch(userChoice) {
                case 1: menu = inquireBalance(user, false, ""); break;
                case 2: menu = deposit(user, false, "", 0); break;
                case 3: menu = withdraw(user, false, "", 0); break;
                case 4: menu = transfer(user, database, false, "", "", null, "", 0); break;
                case 5: scnr.close(); return true;
                default:
                  System.out.println("Invalid Menu Choice. Try Again!");
                  menu = true;
                break;
                }
            }catch(InputMismatchException IME){
                System.out.println("Please enter a valid menu option [1 - 5]");
                menu = true;
            }
        }
        return true;
    }

    
    /**Taken from Rigoberto
     * 
     * @param userToLogin
     * @return
     */
    public static boolean userAndPasswordMatch(Customer userToLogin){
      Scanner scnr = new Scanner(System.in);
      //System.out.print("{This is the PassWord:"+userToLogin.getPersonPassword() + "}\n");
      System.out.print("Password: ");
      String password = scnr.nextLine();

      if(password.compareTo(userToLogin.getPersonPassword()) == 0) return true;
      return false;
    }


        /**Taken from Rigoberto
         * transactionBuffer: Depending on what the header action request, the buffer
         * will call different methods and complete the task.
         * @param  db HashMap: contains all users
         * @param  sn String: Sender name
         * @param  ss String: Sender source
         * @param  a  String: Action
         * @param  rn String: reciever name
         * @param  rd String: receiver destination
         * @param  sa String: sender amount
         * @return    Boolean: transaction was complete? User found?
         *
         * IMPPORTANT:
         * Test if the user that does not exists can still perform task
         */
        public static boolean transactionBuffer(HashMap<String, Customer> database, String sn, String ss, String a, String rn, String rd, String sa){
          // checks if sender amount is a double
          try{
            Double.parseDouble(sa);
          }catch(NumberFormatException NFE){
            System.out.println("Not a number found!");
            return false;
          }
          Customer senderName = database.get(sn.toLowerCase());
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
            Customer recieverName = database.get(rn.toLowerCase());
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
    /**Taken from Rigoberto
     * Transaction: This method will get the array of strings that contain the
     * header of the .txt file read. it will then look through the txt file to
     * find the data that corresponds to it. It will then send that information
     * to a buffer to be completed. Prints out if action was completed or not
     * @param  db       HashMap: contains all users
     * @param  fileName String: Name of file
     * @return          Boolean: when all the tasks have been finished
     */
    public static boolean transactions(HashMap<String, Customer> database, String fileName){
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

    /**Taken from Rigoberto
     * makeStatement: asks manager to enter name of customer to make bank statement for
     * repeats until we have found a customer
     * @param database HashMap: contains all users
     */
    public static void makeStatement(HashMap<String, Customer> database){
      Scanner scnr = new Scanner(System.in);
      System.out.println("Customer Name: ");
      String managerUser = scnr.nextLine();
      Customer userToStatement = database.get(managerUser.toLowerCase());
      while(userToStatement == null){
        System.out.print("Customer Name: ");
        managerUser = scnr.nextLine();
        userToStatement = database.get(managerUser);
      }
      BankStatements userStatement = new BankStatements(userToStatement);
      userStatement.statement();
    }

    /**Taken from Rigoberto, Modified by Denise
     * managerMenu: This method will display the manager menu. The manager
     * will be able to see other users accounts
     *
     * @param database Bank:all users ()
     * @return boolean: keep using program
     */
    public static boolean managerMenu(HashMap<String, Customer> database){
        Scanner scnr = new Scanner(System.in);
        boolean manager = true;
        String log = "";

        // keep going until manager does not want to uise account anymore
        while(manager != false){
            try{
                // menu
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
                    Customer userToInquire = database.get(userName.toLowerCase());
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
                    System.out.println("Checking Account Number: " + database.get(customer).getChecking().getAccountNumber());
                    System.out.println("Savings Account Number: " + database.get(customer).getSavings().getAccountNumber());
                    System.out.println("Credit Account Number: " + database.get(customer).getCredit().getAccountNumber());
                    System.out.println("Checking Balance: " + database.get(customer).getChecking().getAccountNumber());
                    System.out.println("Savings Balance: " + database.get(customer).getSavings().getAccountNumber());
                    System.out.println("Credit Balance: " + database.get(customer).getCredit().getAccountNumber());
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

    //Scanner Skipping believed happening here, Denise
    /**Taken from Rigoberto
     * userLogin: This method will find the user from the database and login
     * into their account, or their manager account.
     * @param database Bank: all users
     * @return Bank: user was found or null if not
     */
    public static Customer userLogin(HashMap<String, Customer> database){
        boolean login = false;
        int attempts = 3;
        String loginName = "";
        // keep going while we want to login
        while(!login){
            Scanner rescnr = new Scanner(System.in);
            Scanner scnr = new Scanner(System.in);
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
            try {
            loginName = rescnr.nextLine();
            loginName = loginName.toLowerCase();
            }catch(NoSuchElementException e) {
            	System.out.println("!Q");
            	loginName = "!q";
            }

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

    
    /**Taken from Rigoberto
    * createBank: This method will get the txt which contains all the user
    * data and will take certain data fields and initialize them to their
    * appropriate class. Once all data has been organized, the instances will be set
    * in a HashMap
    * @param  fields[] String: header array
    * @return          HashMap: all users
    */
    public static HashMap<String, Customer> createBank(String fields[]){
      // creates hashmap with String insert key, and value of type Bank
      HashMap<String, Customer> bankDataBase = new HashMap<>();
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
                // gets user data according to what we are looking for
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
                //String hashMapInsertKey = firstName.toLowerCase() + " " + lastName.toLowerCase();
                String hashMapInsertKey = collision(bankDataBase, firstName, lastName);
                if(hashMapInsertKey != null){
                  // creates objects of each type, Customer, Checking, Savings, and credit
                  Person person = new Customer(firstName, lastName, dateOfBirth, email, idNumber, password, address, phoneNumber);
                  Checking checking = new Checking(Integer.parseInt(checkingAccountNumber), Double.parseDouble(checkingStartingBalance));
                  Savings savings = new Savings(Integer.parseInt(savingAccountNumber), Double.parseDouble(savingStartingBalance));
                  Credit credit = new Credit(Integer.parseInt(creditAccountNumber), Double.parseDouble(creditStartingBalance), Integer.parseInt(creditMax));
                  // adds user to hashMap based on key
                  Customer newUser = new Customer(person, checking, savings, credit);
                  bankDataBase.put(hashMapInsertKey, newUser);
                }
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

    /**Taken from Rigoberto
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

    /**Taken from Rigoberto
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
