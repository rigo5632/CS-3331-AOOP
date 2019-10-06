public class Checking extends Account{
    
    /** 
     * Constructor
     * @param accNum
     * @param b
     * @return 
     */
    public Checking(int accNum, double b){
        super(accNum, b);
    }

    
    /** 
     * Overides the withdraw method. User is able 
     * to take money from the checking account.
     * @param amount
     * @return boolean
     */
    @Override
    public boolean withdraw(double amount){
        // if balance of checking is less than or equal than 0
        if(super.getBalance() <= 0){
            System.out.println("\n********************");
            System.out.println("Please Deposit");
            System.out.println("********************\n");
            return false;
        }
        //amount is less than or greater than the current balance the user has
        if(amount < 0 || amount >= super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // updates the balance
        super.updateBalance(super.getBalance()-amount);
        return true;
    }

    
    /** 
     * Overrides the transfer method. This method will allow the user 
     * to transfer money from within their account or to other accounts
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    @Override
    public boolean transfer(Account transferAccount, double amount){
        //amount is less than or amount is greater than the balance of account
        if(amount < 0 || amount > super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // udpates the account balance
        super.updateBalance(super.getBalance()-amount);
        System.out.println("Account: " + super.getBalance());
        transferAccount.updateBalance(transferAccount.getBalance() + amount);
        System.out.println("Transfer Account: " + transferAccount.getBalance());
        return true;
    }
}