public class Credit extends Account{
  private int account_credit_max;

    /**
     * Constructor
     * @param accNum
     * @param b
     * @return
     */
    Credit(int accNum, double b, int creditMax){
        super(accNum, b);
        this.account_credit_max = creditMax;
    }

    /**
     * getMax: gets credit limit
     * @return double: credit max
     */
    public double getMax(){
      return this.account_credit_max;
    }

    /**
     * deposit: In credit account, the user is not able to make a deposit
     * @param amount double: how much to deposit
     * @return boolean: succesful
     */
    @Override
    public boolean deposit(double amount){
        System.out.println("Cannot deposit to Credit Account");
        return false;
    }

    /**
     * withdraw: In credit account, the user is not able to make a withdraw
     * @param amount double: how much to withdraw
     * @return boolean: succesful
     */
    @Override
    public boolean withdraw(double amount){
        System.out.println("Cannot withdraw from Credit Account");
        return false;
    }

    /**
     * transfer: The user will be able to transfer money into the credit account
     * to pay their debt.
     * @param creditAccount Account: other user account
     * @param amount double: amount to transfer
     * @return boolean: succesful
     */
    @Override
    public boolean transfer(Account creditAccount, double amount){
        // amount is less than 0
        if(amount < 0){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // amount is greater than current balance. User should only pay dept, not more
        if(amount > -super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Do not over pay!");
            System.out.println("Current Balance: " + super.getBalance());
            System.out.println("********************\n");
            return false;
        }
        // updates balance
        super.updateBalance(super.getBalance() + amount);
        return true;
    }
}
