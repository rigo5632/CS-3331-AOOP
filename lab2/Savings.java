public class Savings extends Account{
    Savings(int accNum, double b){
        super(accNum, b);
    }

    @Override
    public boolean withdraw(double amount){
        System.out.println("Cannot withdraw from savings");
        return true;
    }

    @Override
    public boolean transfer(Account transferAccount, double amount){
        if(amount < 0 || amount > super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        super.updateBalance(super.getBalance() - amount);
        transferAccount.updateBalance(transferAccount.getBalance() + amount);
        return true;
    }
}