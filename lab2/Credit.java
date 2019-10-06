public class Credit extends Account{
    Credit(int accNum, double b){
        super(accNum, b);
    }

    @Override
    public boolean transfer(Account transferAccount, double amount){
        System.out.println("Cannot transfer from Credit Account");
        return false;
    }

    @Override
    public boolean withdraw(double amount){
        System.out.println("Cannot withdraw from Credit Account");
        return false;
    }

    @Override
    public boolean deposit(double amount){
        if(amount < 0){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        if(amount > -super.getBalance()){
            System.out.println("\n********************");
            System.out.println("Do not over pay!");
            System.out.println("Current Balance: " + super.getBalance());
            System.out.println("********************\n");
            return false; 
        }
        super.updateBalance(super.getBalance() + amount);
        return true;
    }
}