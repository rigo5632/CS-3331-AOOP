class Bank{
    private Person person;
    private Account checking;
    private Account savings;
    private Account credit;
    Bank next;

    Bank(){}
    Bank(Person p, Account c, Account s, Account cr){
        this.person = p;
        this.checking = c;
        this.savings = s;
        this.credit = cr;
    }

    // Getters for Objects
    public Person getPerson(){
        return this.person;
    }

    public Account getChecking(){
        return this.checking;
    }

    public Account getSavings(){
        return this.savings;
    }

    public Account getCredit(){
        return this.credit;
    }

    // Getters for Person Object's methods
    public String getPersonName(){
        return this.person.getFullName();
    }

    public String getPersonDateOfBirth(){
        return this.person.getDateOfBirth();
    }
    public String getPersonID(){
        return this.person.getID();
    }

    public String getPersonAddress(){
        return this.person.getAddress();
    }
    
    public String getPersonPhoneNumber(){
        return this.person.getPhoneNumber();
    } 

    // Getters for Checking Object's methods
    public void updateCheckingBalanace(double amount){
        this.checking.updateBalance(amount);
    }

    public int getCheckingAccountNumber(){
        return this.checking.getAccountNumber();
    }

    public double getCheckingBalance(){
        return this.checking.getBalance();
    }

    public boolean checkingDeposit(double amount){
        return this.checking.deposit(amount);
    }

    public boolean checkingWithdraw(double amount){
        return this.checking.withdraw(amount);
    }

    public boolean checkingTransfer(Account transferAccount, double amount){
        return this.checking.transfer(transferAccount, amount);
    }

    // Getters for Savings Object's methods

    public void updateSavingsBalanace(double amount){
        this.savings.updateBalance(amount);
    }

    public int getSavingsAccountNumber(){
        return this.savings.getAccountNumber();
    }

    public double getSavingsBalance(){
        return this.savings.getBalance();
    }

    public boolean savingsDeposit(double amount){
        return this.savings.deposit(amount);
    }

    public boolean savingsWithdraw(double amount){
        return this.savings.withdraw(amount);
    }

    public boolean savingsTransfer(Account transferAccount, double amount){
        return this.savings.transfer(transferAccount, amount);
    }

    // Getters for Credit Object's methods
    public void updateCreditBalanace(double amount){
        this.credit.updateBalance(amount);
    }

    public int getCreditAccountNumber(){
        return this.credit.getAccountNumber();
    }

    public double getCreditBalance(){
        return this.credit.getBalance();
    }

    public boolean creditDeposit(double amount){
        return this.credit.deposit(amount);
    }

    public boolean creditWithdraw(double amount){
        return this.credit.withdraw(amount);
    }

    public boolean creditTransfer(Account transferAccount, double amount){
        return this.credit.transfer(transferAccount, amount);
    }
}