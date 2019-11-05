class Bank{
    private Person person;
    private Account checking;
    private Account savings;
    private Account credit;
    private String logs[] = new String[10];
    private int logIndex;
    Bank next;


    /**
     * Constructor
     * @param p
     * @param c
     * @param s
     * @param cr
     * @return
     */
    Bank(){}

    /**
     * Constructor
     * @param p
     * @param c
     * @param s
     * @param cr
     * @return
     */
    Bank(Person p, Account c, Account s, Account cr){
        this.person = p;
        this.checking = c;
        this.savings = s;
        this.credit = cr;
        this.logIndex = 0;
    }

    // Logs functionality
    public void printLogs(){
      for(int i = 0; i < this.logs.length; i++){
        if(this.logs[i] != null)System.out.println(this.logs[i]);
      }
    }

    public void addLog(String l){
      if(limitLog()){
        this.logs = increaseLogSize();
      }else{
        this.logs[logIndex] = l;
        this.logIndex++;
      }
    }

    private String[] increaseLogSize(){
      String tempLog[] = new String[this.logs.length*2];
      for(int i = 0; i < this.logs.length; i++){
        tempLog[i] = this.logs[i];
      }
      this.logs = new String[tempLog.length];
      for(int i = 0; i < tempLog.length; i++){
        logs[i] = tempLog[i];
      }
      return logs;
    }

    private boolean limitLog(){
      if(logIndex == this.logs.length)return true;
      return false;
    }


    /**
     * Returns the Person Object
     * @return Person
     */
    // Getters for Objects
    public Person getPerson(){
        return this.person;
    }


    /**
     * Returns the Checking Account
     * @return Account
     */
    public Account getChecking(){
        return this.checking;
    }


    /**
     * Returns the Savings Account
     * @return Account
     */
    public Account getSavings(){
        return this.savings;
    }


    /**
     * Returns the Credit Account
     * @return Account
     */
    public Account getCredit(){
        return this.credit;
    }


    /**
     * Gets the user name from the person class
     * @return String
     */
    // Getters for Person Object's methods
    public String getPersonName(){
        return this.person.getFullName();
    }

    public String getPersonFirstName(){
      return this.person.getFirstName();
    }

    public String getPersonLastName(){
      return this.person.getLastName();
    }

    /**
     * gets the user date of birth from the person class
     * @return String
     */
    public String getPersonDateOfBirth(){
        return this.person.getDateOfBirth();
    }

    /**
     * gets the user id from the person class
     * @return String
     */
    public String getPersonID(){
        return this.person.getID();
    }


    /**
     * gets the user address from the person class
     * @return String
     */
    public String getPersonAddress(){
        return this.person.getAddress();
    }


    /**
     * phone number from the person class
     * @return String
     */
    public String getPersonPhoneNumber(){
        return this.person.getPhoneNumber();
    }


    /**
     * allows user to udpate the balance
     * @param amount
     */
    // Getters for Checking Object's methods
    public void updateCheckingBalanace(double amount){
        this.checking.updateBalance(amount);
    }


    /**
     * allows user to get the account number(checking)
     * @return int
     */
    public int getCheckingAccountNumber(){
        return this.checking.getAccountNumber();
    }


    /**
     * User is able to see their balance
     * @return double
     */
    public double getCheckingBalance(){
        return this.checking.getBalance();
    }


    /**
     * Makes the user able to make deposits
     * @param amount
     * @return boolean
     */
    public boolean checkingDeposit(double amount){
        return this.checking.deposit(amount);
    }


    /**
     * makes the user able to make withdraws
     * @param amount
     * @return boolean
     */
    public boolean checkingWithdraw(double amount){
        return this.checking.withdraw(amount);
    }


    /**
     * allows the user to make transfers from checking
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    public boolean checkingTransfer(Account transferAccount, double amount){
        return this.checking.transfer(transferAccount, amount);
    }

    public double getCheckingStartingBalance(){
      return this.checking.getStartingBalance();
    }


    /**
     * Allows user to update the savings balance
     * @param amount
     */
    // Getters for Savings Object's methods

    public void updateSavingsBalanace(double amount){
        this.savings.updateBalance(amount);
    }


    /**
     * User is able to see their savings account number
     * @return int
     */
    public int getSavingsAccountNumber(){
        return this.savings.getAccountNumber();
    }


    /**
     * user is able to see their savings balance
     * @return double
     */
    public double getSavingsBalance(){
        return this.savings.getBalance();
    }


    /**
     * be able to make a deposit in the savings account
     * @param amount
     * @return boolean
     */
    public boolean savingsDeposit(double amount){
        return this.savings.deposit(amount);
    }


    /**
     * be able to make a withdraw in the savings account
     * @param amount
     * @return boolean
     */
    public boolean savingsWithdraw(double amount){
        return this.savings.withdraw(amount);
    }


    /**
     * Allows user to be able to transfer using their savings account
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    public boolean savingsTransfer(Account transferAccount, double amount){
        return this.savings.transfer(transferAccount, amount);
    }

    public double getSavingsStartingBalance(){
      return this.savings.getStartingBalance();
    }


    /**
     * user is able to update their credit balance
     * @param amount
     */
    // Getters for Credit Object's methods
    public void updateCreditBalanace(double amount){
        this.credit.updateBalance(amount);
    }


    /**
     * see their credit account number
     * @return int
     */
    public int getCreditAccountNumber(){
        return this.credit.getAccountNumber();
    }


    /**
     * see their credit account balance
     * @return double
     */
    public double getCreditBalance(){
        return this.credit.getBalance();
    }

    public int getMax(){
      return this.credit.getMax();
    }

    /**
     * allows user to make a deposit
     * @param amount
     * @return boolean
     */
    public boolean creditDeposit(double amount){
        return this.credit.deposit(amount);
    }


    /**
     * withdraw
     * @param amount
     * @return boolean
     */
    public boolean creditWithdraw(double amount){
        return this.credit.withdraw(amount);
    }


    /**
     * // transfer
     * @param transferAccount
     * @param amount
     * @return boolean
     */
    public boolean creditTransfer(Account transferAccount, double amount){
        return this.credit.transfer(transferAccount, amount);
    }

    public double getCreditStartingBalance(){
      return this.credit.getStartingBalance();
    }
}
