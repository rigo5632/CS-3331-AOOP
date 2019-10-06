abstract class Account{
    private int accountNumber;
    private double balance;

    // constructor
    Account(int accNum, double b){
        this.accountNumber = accNum;
        this.balance = b;
    }

    // setters
    public void updateBalance(double amount){
        this.balance = amount;
    }

    // getters
    public int getAccountNumber(){
        return this.accountNumber;
    }

    public double getBalance(){
        return this.balance;
    }

    // functions
    public abstract boolean withdraw(double amount);
    public abstract boolean transfer(Account transferAccount, double amount);

    public boolean deposit(double amount){
        if(amount < 0){
            System.out.println("\n********************");
            System.out.println("Please enter a correct amount");
            System.out.println("********************\n");
            return false;
        }
        this.balance += amount;
        return true;
    }
}