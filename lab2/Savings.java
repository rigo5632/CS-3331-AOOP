public class Savings extends Account{
    private double balance;
    private int accountNumber;

    public Savings(){}
    public Savings(int aNum, double b){
        this.balance = b;
        this.accountNumber = aNum;
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