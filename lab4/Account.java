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
     * @return double: returns current balance
     */
    public double getBalance(){
        return this.balance;
    }
    /**
     * withdraw: abstract withdraw method, allows customer to witdraw from their
     * accounts
     * @param  amount double: how much to withdraw
     * @return        boolean: successful
     */
    public boolean withdraw(double amount){
        // if balance of checking is less than or equal than 0
        if(this.balance <= 0){
            System.out.println("\n********************");
            System.out.println("Please Deposit");
            System.out.println("********************\n");
            return false;
        }
        //amount is less than or greater than the current balance the user has
        if(amount < 0 || amount >= this.balance){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // updates the balance
        updateBalance(this.balance -amount);
        return true;
      }

    /**
     * transfer: method. Since not all accounts will be able
     * to transfer (credit). This method can be abstract and override
     * it to no do nothing in certain circumstances
     * @param transferAccount Account: other user account
     * @param amount double: how much to transfer
     * @return boolean: successful
     */
    public boolean transfer(Account transferAccount, double amount){
        // checks if the amount is lesss than 0 or greater than the balance the user currently has
        if(amount < 0 || amount > this.balance){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // makes update
        updateBalance(this.balance - amount);
        transferAccount.updateBalance(transferAccount.getBalance() + amount);
        return true;
    }

    /**
     * deposit: This method will allow the user to make a deposit to all their accounts
     * @param amount double: how much to deposit
     * @return boolean: successful
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
