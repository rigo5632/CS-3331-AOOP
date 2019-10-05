public class Credit{
    private double balance;
    private int accountNumber;

    public Credit(){}
    public Credit(int aNum, double b){
        this.balance = b;
        this.accountNumber = aNum;
    }
    public int getAccountNumber(){
        return this.accountNumber;
    }
    public void deposit(double amount){
        this.balance += amount;
    }
    public double getBalance(){
        return this.balance;
    }
    
}