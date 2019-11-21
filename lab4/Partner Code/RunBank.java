/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;

public class RunBank{
	static HashMap<String, Customer> users = new HashMap();
	static Customer currentUser = new Customer();
	static Scanner keyboard = new Scanner(System.in);

	static File logFile = new File("LogFile.txt");
	static 		String standardHeader="First Name, Last Name, Date of Birth, ID Number, Address, Checking Account Number, Checking Balance,"
			+ "Savings Account Number, Savings Balance, Credit Account Number, Credit Balance";
	public static void main (String[] args) throws IOException {
		createBank("Bank Users 4.csv");
		boolean exit = false;
		int verificationNumber;
		
		do {
			System.out.println("Hello! Please select one.\n[1]Customer user\n[2]Bank Manager user\n[3]Add New Customer\n[4]Parse Transaction File\n[0]Exit\n");
			int choice = keyboard.nextInt();
			
			switch(choice) {
				case 1: userMenu(); break;
				case 2: managerMenu();	break;
				case 3: newUser();	break;
				//Doesn't work
				case 4: transactionFile("Transaction Actions"); break;
				//Not functional
				case 5: writeStatement(currentUser); break;
				case 0:	exit = true; break;
			
			}
		}while(!exit);
		keyboard.close();

		loadFile(standardHeader);
	}	
	public static void newUser() {	
		Customer tempNewCustomer= new Customer();
		
		System.out.println("Enter the first name of the new customer: ");
		String tempString = keyboard.nextLine();
		tempNewCustomer.setFirstName(tempString);
		System.out.println("Enter the last name of the new customer: ");
		tempString = keyboard.nextLine();
		tempNewCustomer.setLastName(tempString);
		users.put(tempNewCustomer.getFirstName()+" "+tempNewCustomer.getLastName(), tempNewCustomer);
		
		System.out.println("Enter the date of birth of the new customer: ");
		tempString = keyboard.nextLine();
		tempNewCustomer.setDateOfBirth(tempString);
		System.out.println("Enter the address of the new customer: ");
		tempString = keyboard.nextLine();
		tempNewCustomer.setAddress(tempString);
		
		tempNewCustomer.setIDNumber(String.valueOf(users.size() + 1));
		tempNewCustomer.savings.setAccountNumber(String.valueOf(2000+ tempNewCustomer.getIDNumber()));
		System.out.println("Enter the savings starting balance of the new customer: ");
		tempString = keyboard.nextLine();
		tempNewCustomer.savings.setBalance(tempString);
		
		System.out.println("Would you like to start a checkings account? [y/n]");
		String option = keyboard.nextLine();
		if(option.equalsIgnoreCase("y")) {
			System.out.println("Enter the checkings starting balance of the new customer: ");
			tempString = keyboard.nextLine();
			tempNewCustomer.check.setBalance(tempString);
			tempNewCustomer.check.setAccountNumber(String.valueOf(1000 + tempNewCustomer.getIDNumber()));
		}
		System.out.println("Would you like to start a credit account? [y/n]");
		option = keyboard.nextLine();
		if(option.equalsIgnoreCase("y")){
			System.out.println("Enter the date of birth of the new customer: ");
			tempString = keyboard.nextLine();
			tempNewCustomer.credit.setBalance(tempString);
			tempNewCustomer.credit.setAccountNumber(String.valueOf(3000+ tempNewCustomer.getIDNumber()));
		}
	}
	public static void managerMenu() {
		System.out.println("\n[1]Print all Accounts \n[2]Inquire about a customer\n");
		int choice = keyboard.nextInt();
		keyboard.nextLine();
		
		switch(choice) {
		case 1: printBank();break;
		case 2:
				System.out.println("Enter the First and Last name of the Customer.\n");
				String name = keyboard.nextLine();
				
				if(users.containsKey(name))
					System.out.print(users.get(name));
				else
					System.out.print("That customer does not have an account at this bank.");
				break;
		}
	}
	public static void userMenu() throws IOException {
		int accountNumber = 0;
		boolean exit = false;
		while(currentUser.getFirstName().equalsIgnoreCase("Uninitialized")) {
			System.out.print("\nPlease enter your full name: ");
			String name = keyboard.nextLine();
			setUser(name);
		}

		System.out.print("Welcome " + currentUser.getFirstName()+ " " + currentUser.getLastName());

		try {
			while(!exit) {
				System.out.print("\nManage your: \n[1]Checking\n[2]Saving \n[3]Credit\n[0]Exit\n");
				accountNumber = keyboard.nextInt();
				
				if(accountNumber == 1)
					menu(currentUser.check);
				else if(accountNumber == 2)
					menu(currentUser.savings);
				else if(accountNumber == 3)
					menu(currentUser.credit);
				else
					System.out.print("\nThe number you entered is not an account in your file.");
			}
		} catch(InputMismatchException e) {
			
		}
	}
	/**
	 * Prints out all the users in bank when called
	 */
	public static void printBank() {
	    Iterator iterator = users.entrySet().iterator();
        
	    while (iterator.hasNext()) {
	        HashMap.Entry input = (HashMap.Entry)iterator.next();
	        Customer tempUser = (Customer) input.getValue();
	        
	        System.out.println("----------------------------------");
	        System.out.print(tempUser+"\n");
	        System.out.println("----------------------------------");
	    }
	}
	/**
	 * Writes csv file from class HashMap
	 * @throws IOException
	 */
	public static void loadFile(String header) throws IOException{
		FileWriter writer = new FileWriter("NewBankUsers.csv");
		PrintWriter filewriter = new PrintWriter(writer); 
		String[] comparing = header.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
		String[] organizer = new String[comparing.length];
		filewriter.write(header);
		filewriter.write("\n");
		
	    Iterator iterator = users.entrySet().iterator();
	    while (iterator.hasNext()) {
	        HashMap.Entry input = (HashMap.Entry)iterator.next();
	        Customer tempUser = (Customer) input.getValue();
	        
	         organizer[compareField("First Name",comparing)]= tempUser.getFirstName();
	         organizer[compareField("Last Name",comparing)]= tempUser.getLastName();
	         organizer[compareField("Date of Birth",comparing)]= tempUser.getDateOfBirth();
	         organizer[compareField("ID Number",comparing)]= String.valueOf(tempUser.getIDNumber());
	         organizer[compareField("Address", comparing)]= tempUser.getAddress();
	         organizer[compareField("Checking Account Number",comparing)]=String.valueOf(tempUser.check.getAccountNumber());
	         organizer[compareField("Checking Balance",comparing)]=String.valueOf(tempUser.check.getBalance());
	         organizer[compareField("Savings Account Number",comparing)]=String.valueOf(tempUser.savings.getAccountNumber());	         
	         organizer[compareField("Savings Balance",comparing)]=String.valueOf(tempUser.savings.getBalance());	
	         organizer[compareField("Credit Account Number",comparing)]=String.valueOf(tempUser.credit.getAccountNumber());
	         organizer[compareField("Credit Balance",comparing)]=String.valueOf(tempUser.credit.getBalance());
	         
	         for(String s: organizer)
	        	 filewriter.write(s+",");
	         filewriter.write("\n");
	   }
		filewriter.flush();
		filewriter.close();
	}
	/**
	 * Compares a heading value to a possible known string found in the header
	 * @param comparing
	 * @param header
	 * @return
	 */
	public static int compareField(String comparing, String[] header) {
		for(int i = 0; i < header.length; i++) {
			header[i] = header[i].replaceAll("[^\\x00-\\x7F]", "");
			header[i] = header[i].replaceAll("\\s","");
			comparing = comparing.replaceAll("\\s", "");
			if(header[i].compareTo(comparing)==0)
				return i;
		}
		return -1;
	}    
	/**
	 * 
	 * @param file File that information is being taken from
	 * @throws IOException 
	 * @catch FileNotFoundException
	 */
    public static String createBank(String file) throws IOException{
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
        String header = "";
        String line = "";

        // creates users
        try {
		FileReader reader = new FileReader(file);
		BufferedReader fileReader = new BufferedReader(reader);
			line = fileReader.readLine();
            header = line;
            String[] fields = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

            while(line != null){
                String[] splitter = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                
                        firstName = splitter[compareField("First Name",fields)];
                        lastName = splitter[compareField("Last Name", fields)];
                        dateOfBirth = splitter[compareField("Date of Birth", fields)];
                        idNumber = splitter[compareField("Identification Number", fields)];
                        address = splitter[compareField("Address", fields)];
                        phoneNumber = splitter[compareField("Phone Number", fields)];
                        checkingAccountNumber = splitter[compareField("Checking Account Number", fields)];
                        savingAccountNumber = splitter[compareField("Savings Account Number",fields)];
                        creditAccountNumber = splitter[compareField("Credit Account Number", fields)];
                        checkingStartingBalance = splitter[compareField("Checking Starting Balance", fields)];
                        savingStartingBalance = splitter[compareField("Savings Starting Balance", fields)];
                        creditStartingBalance = splitter[compareField("Credit Starting Balance", fields)];
                        
                        line = fileReader.readLine();
                        
                       if(!(idNumber.equalsIgnoreCase("Identification Number"))) {
			                Customer person = new Customer(firstName, lastName);
			               	person.setAddress(address);
			                person.setDateOfBirth(dateOfBirth);
			                person.setPhoneNumber(phoneNumber);
			                person.setIDNumber(idNumber);
			                person.check.setAccountNumber(checkingAccountNumber);
			                person.check.setBalance(checkingStartingBalance);
			                person.savings.setAccountNumber(savingAccountNumber);
			                person.savings.setBalance(savingStartingBalance);
			                person.credit.setAccountNumber(creditAccountNumber);
			                person.credit.setBalance(creditStartingBalance);
			                
			                users.put(person.firstName+" "+person.lastName, person);
                       }
            }
            fileReader.close();
        }catch(FileNotFoundException e) {
            System.out.println("Error creating bank users");
            System.out.println("Possible Errors: ");
            System.out.println("1. Bank Users.csv does not exists");
            System.out.println("2. Bank Users.csv does not contain user information or is empty");
            System.exit(0);
        }

        return header;
    }
    /**
	 * 
	 * @param nameIn String that sets currentUser Customer
	 */
	public static void setUser(String nameIn) {
		String[] tempName = nameIn.split("\\s,");
			if(users.containsKey(nameIn))
				currentUser = users.get(nameIn);
			else if(tempName.length <= 1)
				System.out.println("Please enter both the First AND Last name.");
			if(!(users.containsKey(nameIn))) 
				System.out.println("The name you entered was not found or was spelled incorrectly. \nPlease re-enter the information.");
	}
	/**
	 * Menu of options for current Account that goes through switch from analog options
	 * @param currentAccount Account to be manipulated
	 * @throws IOException
	 */
	public static void menu(Account currentAccount) throws IOException {
		int choice;
		do {
			System.out.print("\nPlease choose an option by entering the corresponding number.");
			System.out.print("\n[1] Print my balance.\n[2] Deposit\n[3] Withdraw\n[4]Transfer\n[5]Make a payment."
					+ "\n[0] EXIT ");
			choice = keyboard.nextInt();
			double amount = 0.0;

			switch(choice) {
			case 1:	System.out.println(currentAccount.getBalance());
			writeLogFile(currentAccount,amount,"Inquiry");
			break;
			case 2: System.out.print("Please enter how much you would like to deposit.");
			amount = keyboard.nextDouble();
			currentAccount.deposit(amount);
			writeLogFile(currentAccount,amount,"Deposit");
			break;
			case 3:System.out.print("Please enter how much you would like to withdraw.");
			amount = keyboard.nextDouble(); 
			currentAccount.withdraw(amount);
			writeLogFile(currentAccount,amount,"Withdrawal");
			break;
			case 4: System.out.println("[1]Transfer to checking\n[2]Transfer to savings\n[3]Transfer to Credit");
					choice = keyboard.nextInt();
					Account tempAccount = null;
					switch(choice) {
					case 1: tempAccount = findAccount("checking", currentUser); break;
					case 2: tempAccount = findAccount("savings", currentUser); break;
					case 3: tempAccount = findAccount("credit", currentUser); break;
					}
					if(choice > 0) {
						System.out.println("How much would you like to transfer: ");
						amount = keyboard.nextDouble();
						currentAccount.transfer(amount, tempAccount);
					}
					writeLogFile(currentAccount,amount,"Transfer");
				break;
			case 5:
				Customer tempCustomer= new Customer();
				do {
					System.out.print("Please enter the account name of whom you would like to pay.");
					String accountIn = keyboard.next();
					if(users.containsKey(accountIn))
						tempCustomer = users.get(accountIn);
					else {
						System.out.println("Account specified was not found");
						System.out.println("[1]Try entering the account again\n[0]Exit");
						choice = keyboard.nextInt();
						if(choice == 2 )
							tempCustomer.setFirstName("IgnoreAndContinue");
					}
				}while(tempCustomer.getFirstName().equalsIgnoreCase("Uninitialized"));
				
				System.out.print("Please enter how much you would like to make the payment.");
				amount = keyboard.nextDouble();
				System.out.print("If the information entered above is correct please enter 0.");
				choice = keyboard.nextInt();
				if(choice == 0)
					currentAccount.payment(amount, tempCustomer.check);
				writeLogFile(currentAccount,amount,"Payment");
				break;
			}

		}while(choice >0);


	}
	public static void transactionFile(String file) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader fileReader = new BufferedReader(reader);
		String header[] = {"From First Name", "From Last Name, From Where", "Action", "To First Name", "To Last Name", "To Where", "Action Amount"};
		String line = fileReader.readLine();
		String comparing[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		int[] organizer = new int[comparing.length];
		
		for(int i = 0; i < organizer.length; i++) 
			organizer[i] = compareField(comparing[i],header);
		
		line = fileReader.readLine();
		while(!(line == null)) {
			Customer tempCustomer = users.get((comparing[organizer[0]] + " " + comparing[organizer[1]]));
			Account tempAccount = findAccount(comparing[organizer[2]],tempCustomer);
			
			if(comparing[organizer[3]].equalsIgnoreCase("pays")) {
				Account secondTemp = findAccount(comparing[organizer[6]],users.get(comparing[organizer[4]]+" "+comparing[organizer[5]]));
				tempAccount.payment(Double.valueOf(comparing[organizer[7]]), secondTemp);
			}else if(comparing[organizer[3]].equalsIgnoreCase("transfers"))
				tempAccount.transfer(Double.valueOf(comparing[organizer[7]]), findAccount(comparing[organizer[6]],tempCustomer));
			else if(comparing[organizer[3]].equalsIgnoreCase("inquires"))
				tempAccount.getBalance();
			else if(comparing[organizer[3]].equalsIgnoreCase("withdraws"))
				tempAccount.withdraw(Double.valueOf(comparing[organizer[7]]));
			else if(comparing[organizer[3]].equalsIgnoreCase("deposits"))
				tempAccount.deposit(Double.valueOf(comparing[organizer[7]]));
			
		}
	}
	public static Account findAccount(String compare, Customer tempCustomer) {
		if(compare.equalsIgnoreCase("checking"))
			return tempCustomer.check;
		else if(compare.equalsIgnoreCase("savings"))
			return tempCustomer.savings;
		else if(compare.equalsIgnoreCase("credit"));
			return tempCustomer.credit;
	}
	public static void writeStatement(Customer statementUser) throws IOException {
		BankStatement statement = new BankStatement(statementUser.getFirstName()+" "+statementUser.getLastName(),statementUser);
		statement.createStatement(logFile);
	}
	
	/**
	 * Writes to log file even if process failed to record attempt
	 * @param process Account that transaction took place from 
	 * @param moneyOrder amount that transaction demanded
	 * @throws IOException
	 */
	public static void writeLogFile(Account process,double moneyOrder,String transaction) throws IOException {
		String format = "|%1$11s| |%2$20s| |%3$12s| |%4$10s| |%5$10s|";
		String out = String.format(format, "Account", "Name of Account   ", "Money In/Out", "Balance  ","Transaction");
		FileWriter filewriter = new FileWriter(logFile, true);
		PrintWriter writer = new PrintWriter(filewriter);


		if(logFile.length()==0) 		
			writer.println(out); 			

		String money = Double.toString(moneyOrder); 
		out = String.format(format, process.type, currentUser.getFirstName()+currentUser.getLastName(),money, process.getBalance(),transaction);
		writer.println(out);
		writer.flush();
		writer.close();

	}
}
