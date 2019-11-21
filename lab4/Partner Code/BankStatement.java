import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class BankStatement implements Log{
	Customer currentCustomer;
	File statement;
	
	BankStatement(String name, Customer customerIn){
		statement = new File(name +"Statement.txt");
		currentCustomer = customerIn;
	}
	
	public void createStatement(File logFile) throws IOException {
		FileWriter writer = new FileWriter(statement);
		FileReader reader = new FileReader(logFile);
		BufferedReader filereader = new BufferedReader(reader);
		String line= filereader.readLine();
		
		DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime current = LocalDateTime.now();  

		writer.write(currentCustomer.toString());
		writer.write(dateTime.format(current));
		
		while(!(line == null)) {
			writer.write(line);
			line = filereader.readLine();
		}
	} 
	@Override
	public void withdraw(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double inquiry() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deposit(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transfer(double amount, Account transferToAccount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payment(double amount, Account payToName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkBalance(double toCheck) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
