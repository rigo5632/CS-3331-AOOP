abstract class Account{
    private int accountNumber;
    private double startingBalance;
    private double balance;


    /**
     * constructor
     * @param accNum
     * @param b
     * @return
     */
    Account(int accNum, double b){
        this.accountNumber = accNum;
        this.startingBalance = b;
        this.balance = b;
    }

    /**
     * getStartingBalance: gets current user starting account balance
     * @return double: account balance
     */
    public double getStartingBalance(){
      return this.startingBalance;
    }
    /**
     * updateBalance: This method will allow us the update the private attribute of balance
     * @param amount double: new account balance
     */
    // setters
    public void updateBalance(double amount){
        this.balance = amount;
    }

    /**
     * getAccountNumber: This method will allow you to retrieve the private
     * attribute account number
     * @return int: account number
     */
    // getters
    public int getAccountNumber(){
        return this.accountNumber;
    }

    /**
     * getBalance: This method will allow you to retrieve the private balance
     * @return double: retuns current balance
     */
    public double getBalance(){
        return this.balance;
    }
    /**
     * withdraw: abstract withdraw method, allows customer to witdraw from their
     * accounts
     * @param  amount double: how much to withdraw
     * @return        boolean: succesful
     */
    public abstract boolean withdraw(double amount);

    /**
     * getMax: gets the credit max
     * @return double: credit max value
     */
    public abstract double getMax();

    /**
     * transfer: Abstract transfer method. Since not all accounts will be able
     * to transfer (credit). This method can be abstract and override
     * it to no do nothing in certain circumstances
     * @param transferAccount Account: other user account
     * @param amount double: how much to transfer
     * @return boolean: succesful
     */
    public abstract boolean transfer(Account transferAccount, double amount);


    /**
     * deposit: This method will allow the user to make a deposit to all their accounts
     * @param amount double: how much to deposit
     * @return boolean: succesful
     */
    public boolean deposit(double amount){
        // checks if amount is negative
        if(amount < 0){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // updates balance
        this.balance += amount;
        return true;
    }
}
