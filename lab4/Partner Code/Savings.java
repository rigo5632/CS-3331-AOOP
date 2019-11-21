/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */
public class Savings extends Account{
	/**
	 * Constructor sets savings type to "savings"
	 */
	Savings(){
		this.type = "Savings";
	}
	/**
	 * 
	 * Prints out "Cannot make payment from savings"
	 * Prints suggestions 
	 */
	public void payment(double amount, String payToName) {
		System.out.print("You cannot make a payment from a savings account.");
		System.out.print("\nIf you would like to make a payment please withdraw or transfer to checking.");
	}
}
