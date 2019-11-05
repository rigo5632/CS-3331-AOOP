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

    public int getMax(){
      return this.account_credit_max;
    }

    /**
     * In credit account, the user is not able to make a deposit
     * @param amount
     * @return boolean
     */
    @Override
    public boolean deposit(double amount){
        System.out.println("Cannot deposit to Credit Account");
        return false;
    }


    /**
     * In credit account, the user is not able to make a withdraw
     * @param amount
     * @return boolean
     */
    @Override
    public boolean withdraw(double amount){
        System.out.println("Cannot withdraw from Credit Account");
        return false;
    }


    /**
     * The user will be able to transfer money into the credit account
     * to pay their debt.
     * @param creditAccount
     * @param amount
     * @return boolean
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
