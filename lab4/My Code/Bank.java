import java.io.*;

class Bank{
  // private varibales
  private Person person;
  private Account checking;
  private Account savings;
  private Account credit;
  private String logs[] = new String[10];
  private int logIndex;

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

    /**
    * printLogs: gets log at a specific index
    * @param  i int: what index to get from logs
    * @return   String: log
    */
    public String printLogs(int i){
      return this.logs[i];
    }

    /**
    * getLogLength: how many log we have per users
    * @return int: length if array of logs
    */
    public int getLogLength(){
      return this.logs.length;
    }

    /**
     * addLog: adds log what the user has done
     * @param l String: user action
     */
    public void addLog(String l){
      // checks if we have filled the array of logs
      if(limitLog()){
        // increase size
        this.logs = increaseLogSize();
      }else{
        // add to log
        this.logs[logIndex] = l;
        this.logIndex++;
      }
    }

    /**
     * increaseLogSize: increases log array by double
     * @return String[]: new array
     */
    private String[] increaseLogSize(){
      // temp array
      String tempLog[] = new String[this.logs.length*2];
      for(int i = 0; i < this.logs.length; i++){
        tempLog[i] = this.logs[i];
      }
      // makes orginal array bigger and retores info
      this.logs = new String[tempLog.length];
      for(int i = 0; i < tempLog.length; i++){
        logs[i] = tempLog[i];
      }
      return logs;
    }

    /**
     * limitLog: checks size of array
     * @return Boolean: we have reached array limit
     */
    private boolean limitLog(){
      if(logIndex == this.logs.length)return true;
      return false;
    }


    /**
     * getPerson: Returns the Person Object
     * @return Person: all current user persons info
     */
    public Person getPerson(){
        return this.person;
    }


    /**
     * getChecking: Returns the Checking Account
     * @return Account: all cuurent user checking info
     */
    public Account getChecking(){
        return this.checking;
    }

    /**
    * getSavings: Returns the Savings Account
    * @return Account: all current users savings info
    */
    public Account getSavings(){
        return this.savings;
    }

    /**
    * getCredit: Returns the Credit Account
    * @return Account: all cuurent users credit info
    */
    public Account getCredit(){
        return this.credit;
    }

    /**
    * getPersonName: Gets the user name from the person class
    * @return String: current users name
    */
    public String getPersonName(){
        return this.person.getFullName();
    }

    /**
     * getPersonFirstName: gets user First name
     * @return String: First name
     */
    public String getPersonFirstName(){
      return this.person.getFirstName();
    }

    /**
     * getPersonLastName: gets user last name
     * @return StringL last name
     */
    public String getPersonLastName(){
      return this.person.getLastName();
    }

    /**
     * getPersonDateOfBirth: gets the user date of birth from the person class
     * @return String: date of birth
     */
    public String getPersonDateOfBirth(){
        return this.person.getDateOfBirth();
    }

    /**
     * getPersonEmail: gets users email from the person class
     * @return String: email
     */
    public String getPersonEmail(){
      return this.person.getEmail();
    }

    /**
     * getPersonID: gets the user id from the person class
     * @return String: user id
     */
    public String getPersonID(){
        return this.person.getID();
    }

    /**
     * getPersonPassword: gets the users password from the person class
     * @return String: password
     */
    public String getPersonPassword(){
      return this.person.getPassword();
    }

    /**
     * getPersonAddress: gets the user address from the person class
     * @return String: user address
     */
    public String getPersonAddress(){
        return this.person.getAddress();
    }

    /**
     * getPersonPhoneNumber: phone number from the person class
     * @return String: user phone number
     */
    public String getPersonPhoneNumber(){
        return this.person.getPhoneNumber();
    }

    /**
     * updateCheckingBalanace: allows user to udpate the balance
     * @param amount: new checking balance
     */
    public void updateCheckingBalanace(double amount){
        this.checking.updateBalance(amount);
    }


    /**
     * getCheckingAccountNumber: allows user to get the account number(checking)
     * @return int: checking account number
     */
    public int getCheckingAccountNumber(){
        return this.checking.getAccountNumber();
    }


    /**
     * getCheckingBalance: User is able to see their balance
     * @return double: checking balance
     */
    public double getCheckingBalance(){
        return this.checking.getBalance();
    }


    /**
     * checkingDeposit: Makes the user able to make deposits
     * @param amount double: how much to deposit
     * @return boolean: succesful
     */
    public boolean checkingDeposit(double amount){
        return this.checking.deposit(amount);
    }


    /**
     * checkingWithdraw: makes the user able to make withdraws
     * @param amount double: how much to withdraw
     * @return boolean: succesful
     */
    public boolean checkingWithdraw(double amount){
        return this.checking.withdraw(amount);
    }

    /**
     * checkingTransfer: allows the user to make transfers from checking
     * @param transferAccount Account: other user accounts
     * @param amount double: how much to transfer
     * @return boolean succesful
     */
    public boolean checkingTransfer(Account transferAccount, double amount){
        return this.checking.transfer(transferAccount, amount);
    }

    /**
     * getCheckingStartingBalance: gets user starting balance
     * @return double: starting balance
     */
    public double getCheckingStartingBalance(){
      return this.checking.getStartingBalance();
    }

    /**
     * updateSavingsBalanace: Allows user to update the savings balance
     * @param amount double: new savings balance
     */
    public void updateSavingsBalanace(double amount){
        this.savings.updateBalance(amount);
    }

    /**
     * getSavingsAccountNumber: User is able to see their savings account number
     * @return int: savings account number
     */
    public int getSavingsAccountNumber(){
        return this.savings.getAccountNumber();
    }

    /**
     * getSavingsBalance: user is able to see their savings balance
     * @return double: savings balance
     */
    public double getSavingsBalance(){
        return this.savings.getBalance();
    }

    /**
     * savingsDeposit: be able to make a deposit in the savings account
     * @param amount double: how much to deposit
     * @return boolean: succesful
     */
    public boolean savingsDeposit(double amount){
        return this.savings.deposit(amount);
    }

    /**
     * savingsWithdraw: be able to make a withdraw in the savings account
     * @param amount double: how much to withdraw
     * @return boolean: succesful
     */
    public boolean savingsWithdraw(double amount){
        return this.savings.withdraw(amount);
    }

    /**
     * savingsTransfer: Allows user to be able to transfer using their savings account
     * @param transferAccount Account: transfer to
     * @param amount double: how much to transfer
     * @return boolean: succesful
     */
    public boolean savingsTransfer(Account transferAccount, double amount){
        return this.savings.transfer(transferAccount, amount);
    }

    /**
     * getSavingsStartingBalance: savings starting balance
     * @return double: starting saving balance
     */
    public double getSavingsStartingBalance(){
      return this.savings.getStartingBalance();
    }

    /**
     * updateCreditBalanace: user is able to update their credit balance
     * @param amount double new credit balance
     */
    public void updateCreditBalanace(double amount){
        this.credit.updateBalance(amount);
    }

    /**
     * getCreditAccountNumber: see their credit account number
     * @return int: account number
     */
    public int getCreditAccountNumber(){
        return this.credit.getAccountNumber();
    }

    /**
     * getCreditBalance: see their credit account balance
     * @return double: credit balance
     */
    public double getCreditBalance(){
        return this.credit.getBalance();
    }

    /**
     * getMax: get credit max
     * @return double: credit max
     */
    public double getMax(){
      return this.credit.getMax();
    }

    /**
     * creditDeposit: allows user to make a deposit
     * @param amount double: how much to deposit
     * @return boolean: succesful
     */
    public boolean creditDeposit(double amount){
        return this.credit.deposit(amount);
    }

    /**
     * creditWithdraw: withdraw
     * @param amount double: how much to withdraw
     * @return boolean: succesful
     */
    public boolean creditWithdraw(double amount){
        return this.credit.withdraw(amount);
    }

    /**
     * creditTransfer: transfer
     * @param transferAccount Account: other user account
     * @param amount double: how much to transfer
     * @return boolean: succesful
     */
    public boolean creditTransfer(Account transferAccount, double amount){
        return this.credit.transfer(transferAccount, amount);
    }

    /**
     * getCreditStartingBalance: gets credit starting balance
     * @return double: starting Credit balance
     */
    public double getCreditStartingBalance(){
      return this.credit.getStartingBalance();
    }
}
