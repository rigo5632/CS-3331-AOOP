public class Checking extends Account{
    public Checking(int accNum, double b){
        super(accNum, b);
    }

    @Override
    public boolean withdraw(double amount){
        if(super.getBalance() <= 0){
            System.out.println("\n********************");
            System.out.println("Please Deposit");
            System.out.println("********************\n");
            return false;
        }
        if(amount < 0 || amount >= super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        super.updateBalance(super.getBalance()-amount);
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
        super.updateBalance(super.getBalance()-amount);
        System.out.println("Account: " + super.getBalance());
        transferAccount.updateBalance(transferAccount.getBalance() + amount);
        System.out.println("Transfer Account: " + transferAccount.getBalance());
        return true;
    }
}