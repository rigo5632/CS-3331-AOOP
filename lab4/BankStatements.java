import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class BankStatements implements Log{
  private Customer user;

  BankStatements(){}
  BankStatements(Customer u){
    this.user = u;
  }

  public void customerInformation(){
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

  public void accountInformation(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Account Information");
      logWriter.println("-----------------------------------------------------");
      logWriter.println("Checking Account Number: " + user.getChecking().getAccountNumber());
      logWriter.println("Savings Account Number: " + user.getSavings().getAccountNumber());
      logWriter.println("Credit Account Number: " + user.getCredit().getAccountNumber());
      logWriter.println("-----------------------------------------------------");
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  public void startingBalance(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Checking Starting Balance: $" + user.getChecking().getStartingBalance());
      logWriter.println("Savings Starting Balance:  $" + user.getSavings().getStartingBalance());
      logWriter.close();
    }catch(IOException eo){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  public void endBalance(){
    try{
      File file = new File(user.getPersonName() + "_statement.txt");
      FileWriter writer = new FileWriter(file, true);
      PrintWriter logWriter = new PrintWriter(writer);
      logWriter.println("Checking Ending Balance: $" + user.getChecking().getBalance());
      logWriter.println("Savings Ending Balance:  $" + user.getSavings().getBalance());
      logWriter.println("Credit Ending Balance:  $" + user.getCredit().getBalance());
      logWriter.close();
    }catch(IOException oe){
      System.out.println("Bank Statement was not able to record statement");
    }
  }

  public void allTransactions(){
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
