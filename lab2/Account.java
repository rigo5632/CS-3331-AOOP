abstract class Account{
    private int accountNumber;
    private double balance;

    
    /**
     * This method will set the account number and balance of the user account. 
     * @param accNum
     * @param b
     * @return 
     */
    // constructor
    Account(int accNum, double b){
        this.accountNumber = accNum;
        this.balance = b;
    }

    
    /** 
     * This method will allow us the update the private attribute of balance
     * @param amount
     */
    // setters
    public void updateBalance(double amount){
        this.balance = amount;
    }

    
    /** 
     * This method will allow you to retrieve the private
     * attribute account number
     * @return int
     */
    // getters
    public int getAccountNumber(){
        return this.accountNumber;
    }

    
    /** 
     * This method will allow you to retrieve the private balance
     * @return double
     */
    public double getBalance(){
        return this.balance;
    }

    
    /** 
     * Abstract withdraw method. Since not all accounts will be able 
     * to withdraw (credit). This method can be abstract and override
     * it to no do nothing in certain circumstances
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    // functions
    public abstract boolean withdraw(double amount);
    
    /** 
     * Abstract transfer method. Since not all accounts will be able
     * to transfer (credit). This method can be abstract and override
     * it to no do nothing in certain circumstances
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    public abstract boolean transfer(Account transferAccount, double amount);

    
    /** 
     * This method will allow the user to make a deposit to all their accounts
     * @param amount
     * @return boolean
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