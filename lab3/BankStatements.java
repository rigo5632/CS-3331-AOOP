class BankStatements{
  private Bank user;

  BankStatements(Bank u){
    this.user = u;
  }

  public void customerInformation(){
    System.out.println("Customer Information");
    System.out.println("-----------------------------------------------------");
    System.out.println("Full Name:     " + user.getPersonName());
    System.out.println("Date of Birth: " + user.getPersonDateOfBirth());
    System.out.println("ID Number:     " + user.getPersonID());
    System.out.println("Address:       " + user.getPersonAddress());
    System.out.println("Phone Number:  " + user.getPersonPhoneNumber());
    System.out.println("-----------------------------------------------------");
  }
  public void accountInformation(){
    System.out.println("Account Information");
    System.out.println("-----------------------------------------------------");
    System.out.println("Checking Account Number: " + user.getCheckingAccountNumber());
    System.out.println("Savings Account Number: " + user.getSavingsAccountNumber());
    System.out.println("Credit Account Number: " + user.getCreditAccountNumber());
    System.out.println("-----------------------------------------------------");
  }
  public void startingBalance(){
    System.out.println("Checking Starting Balance: $" + user.getCheckingStartingBalance());
    System.out.println("Savings Starting Balance:  $" + user.getSavingsStartingBalance());
  }
  public void endBalance(){
    System.out.println("Checking Ending Balance: $" + user.getCheckingBalance());
    System.out.println("Savings Ending Balance:  $" + user.getSavingsBalance());
  }
  public void allTransactions(){
    System.out.println("Transactions:");
    System.out.println("-----------------------------------------------------");
    user.printLogs();
    System.out.println("-----------------------------------------------------");
  }
  public void statement(){
    customerInformation();
    accountInformation();
    startingBalance();
    allTransactions();
    endBalance();
  }
}
