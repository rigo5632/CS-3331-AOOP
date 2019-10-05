public class Checking extends Account{
    private double balance;
    private int accountNumber;

    public Checking(){}
    public Checking(int aNum, double b){
        this.balance = b;
        this.accountNumber = aNum;
    }
    public int getAccountNumber(){
        return this.accountNumber;
    }
    public void withdraw(double amount){
        this.balance -= amount;
    }
    public void deposit(double amount){
        this.balance += amount;
    }
    public double getBalance(){
        return this.balance;
    }
}