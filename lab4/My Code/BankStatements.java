import java.io.*;

class BankStatements implements Log{
  private Bank user;

  BankStatements(){}
  BankStatements(Bank u){
    this.user = u;
  }

  private void customerInformation(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Customer Information");
      logWriter.println("-----------------------------------------------------");
      logWriter.println("Full Name:     " + user.getPersonName());
      logWriter.println("Date of Birth: " + user.getPersonDateOfBirth());
      logWriter.println("ID Number:     " + user.getPersonID());
      logWriter.println("Address:       " + user.getPersonAddress());
      logWriter.println("Phone Number:  " + user.getPersonPhoneNumber());
      logWriter.println("-----------------------------------------------------");
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  private void accountInformation(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Account Information");
      logWriter.println("-----------------------------------------------------");
      logWriter.println("Checking Account Number: " + user.getCheckingAccountNumber());
      logWriter.println("Savings Account Number: " + user.getSavingsAccountNumber());
      logWriter.println("Credit Account Number: " + user.getCreditAccountNumber());
      logWriter.println("-----------------------------------------------------");
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  private void startingBalance(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Checking Starting Balance: $" + user.getCheckingStartingBalance());
      logWriter.println("Savings Starting Balance:  $" + user.getSavingsStartingBalance());
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  private void endBalance(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Checking Ending Balance: $" + user.getCheckingBalance());
      logWriter.println("Savings Ending Balance:  $" + user.getSavingsBalance());
      logWriter.println("Credit Ending Balance:  $" + user.getCreditBalance());
      logWriter.close();
    }catch(IOException oe){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  private void allTransactions(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      //int i = 0;
      logWriter.println("Transactions:");
      logWriter.println("-----------------------------------------------------");
      for(int i = 0; i < user.getLogLength(); i++){
        if(user.printLogs(i) != null)logWriter.println(user.printLogs(i));
      }
      logWriter.println("-----------------------------------------------------");
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }
  public void statement(){
    customerInformation();
    accountInformation();
    startingBalance();
    allTransactions();
    endBalance();
  }
}
