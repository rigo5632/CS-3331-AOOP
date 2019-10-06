// Extends the features of account
public class Savings extends Account{
    
    /** 
     * @param accNum
     * @param b
     * @return 
     */
    // contsructor
    Savings(int accNum, double b){
        // calls the Account Constructor
        super(accNum, b);
    }

    
    /** 
     * This Method overides the funtionality of withdraw 
     * method in account class. User will not be able to withdraw
     * money from their savings account
     * @param amount
     * @return boolean
     */
    @Override
    public boolean withdraw(double amount){
        System.out.println("Cannot withdraw from savings");
        return true;
    }

    
    /** 
     * This method will override the transfer method in account class.
     * Will transfer money to the transfer account the user selected
     * 
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    @Override
    public boolean transfer(Account transferAccount, double amount){
        // checks if the amount is lesss than 0 or greater than the balance the user currently has
        if(amount < 0 || amount > super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        // makes update
        super.updateBalance(super.getBalance() - amount);
        transferAccount.updateBalance(transferAccount.getBalance() + amount);
        return true;
    }
}