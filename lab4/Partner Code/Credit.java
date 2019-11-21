/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */
public class Credit extends Account{
	/**
	 * Constructor sets Credit type to "Credit"
	 * Credit withdraw() and deposit() method are inverted from account parent
	 * This is because credit balance is always negative 
	 */
	Credit(){
		this.type = "Credit";	
	}
	/**
	 * @param amount to be added to credit balance
	 * Checks amount before addition
	 * prints amount is too high if it fails check
	 */
	public void withdraw(double amount) {
		if(checkBalance(amount))
			this.balance += amount;
		else 
			System.out.print("The amount you entered is too high. It surpasses your outstanding balance.");
	}	
	/**
	 * @param amount to be subtracted to credit balance
	 */
	public void deposit(double amount) {
		this.balance -= amount;
	}
	/**
	 * 
	 * prints "Cannot make payment account"
	 * As this function has not been created.
	 */
	public void payment(double amount, String payToName) {
		System.out.print("You cannot make a payment from the credit account at this time.");
	}
	/**
	 * 
	 * prints "Cannot transfer from credit"
	 */
	public void transfer(double amount, String transferToAccount) {
		System.out.print("You cannot transfer from credit to a different account.");
	}
	/**
	 * @param amount to be compared against balance
	 * @return boolean if amount is less than or equal to outstanding balance
	 */
	public boolean checkBalance(double amount) {
		if(amount <= this.balance)
			return true;
		return false;
	}

}