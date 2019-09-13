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
        has finished using the bank features, a file will be automatically created with all the logs/actions they
        preformed while they were logged in.
    Honesty Statement:
        I confirm that the work of this assignment is completely my own. By turning in this assignment. 
        I declare that I did not receive unauthorized assistance. Moreover, all deliverables including, 
        but not limited to the source code, lab report and output files were written and produced by me alone.
    Assumptions:
        1. "Bank users.txt" will always have the same number of columns, and the columns will always be in the 
        same order (first name, lastname, account number, checking, savings, balance and interest rate)
        2. Each User will be able to log in once, there is not feature for them to log-out of their account
        and access other accounts. (Only way of doing that would be to exit program and re-execute program)
        3. "Bank users.txt" is not being updated when user does an action EX: Balance. Each time the program 
        executes the data will be reset to its original data set.
        4. "Log.txt" will only record the actions of one users account and not record other changes that happen
        to other users. If Pluto decides to pay Micky, then the log file will only record the changes made in
        Pluo's account, but it will not display the changes made in Micky's account. 
*/
import java.text.DecimalFormat;
class Checking{
    // attributes
    private String firstName;
    private String lastName;
    private int accountNumber;
    private boolean checking;
    private boolean savings;
    private double checkingBalance;
    private String interestRate;
    // allows us to be able to move
    Checking next;

    // Contructors
    Checking(){}
    Checking(String fn, String ln, int an, boolean c, boolean s, double cb, String ir){
        this.firstName = fn;
        this.lastName = ln;
        this.accountNumber = an;
        this.checking = c;
        this.savings = s;
        this.checkingBalance = cb;
        this.interestRate = ir;
    }
    // Setters
    public void setFirstName(String fn){
        this.firstName = fn;
    }
    public void setlastName(String ln){
        this.lastName = ln;
    }
    public void setAccountNumber(int an){
        this.accountNumber = an;
    }
    public void setChecking(boolean c){
        this.checking = c;
    }
    public void setSavings(boolean s){
        this.savings = s;
    }
    public void setCheckingBalance(double cb){
        this.checkingBalance = cb;
    }
    public void setInterestRate(String ir){
        this.firstName = ir;
    }

    // Getters
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public int getAccountNumber(){
        return this.accountNumber;
    }
    public boolean getChecking(){
        return this.checking;
    }
    public boolean getSavings(){
        return this.savings;
    }
    public double getCheckingBalance(){
        return this.checkingBalance;
    }
    public String getInterestRate(){
        return this.interestRate;
    }
    // Actions

    public boolean deposit(double amount){
        DecimalFormat df = new DecimalFormat("#.##");
        // ammount is less than 0
        if(amount < 0){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // updates linked list of user1 instance
        this.setCheckingBalance(Double.parseDouble(df.format(this.getCheckingBalance() + amount)));
        return true;
    }

    public boolean withdraw(double amount){
        DecimalFormat df = new DecimalFormat("#.##");
        // if user1 does not have any money in their  account
        if(this.getCheckingBalance() <= 0){
            System.out.println("\n********************");
            System.out.println("Please Deposit");
            System.out.println("********************\n");
            return false;
        }
        // withdraw is negative or withdraw is greater than user balance
        if(amount < 0 || amount > this.getCheckingBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // updates this user's instance
        this.setCheckingBalance(Double.parseDouble(df.format(this.getCheckingBalance() - amount)));
        return true;
    }

    public boolean paySomeone(Checking transferAccount, double amount){
        DecimalFormat df = new DecimalFormat("#.##");

        // Amount is negative or greater than user's balance
        if(amount < 0 || amount > this.getCheckingBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false; 
        }
        // updates this user's instance with new balance
        this.setCheckingBalance(Double.parseDouble(df.format((this.getCheckingBalance() - amount))));
        // updates the other user, user2 with new balance.
        System.out.println(transferAccount.getCheckingBalance());
        transferAccount.setCheckingBalance(Double.parseDouble(df.format((transferAccount.getCheckingBalance() + amount))));
        System.out.println(transferAccount.getCheckingBalance());
        return true;
    }

}