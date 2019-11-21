/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */

//TODO re-implement to write log file 
public interface Log {

	void withdraw(double amount);
	double inquiry();
	void deposit(double amount);
	void transfer(double amount, Account transferToAccount);
	void payment(double amount, Account payToName);
	boolean checkBalance(double toCheck);
	
}
