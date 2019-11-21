/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */
import java.util.ArrayList;

public class Customer extends Person {
	Savings savings = new Savings();
	Checking check = new Checking();
	Credit credit = new Credit();
	
	/**
	 * Customer without parameter initializes name to Uninitialized and leaves the rest of the data points empty.
	 */
	Customer(){
		firstName = "Uninitialized";
		lastName = "Unknown";
	}
	/**
	 * 
	 * @param firstName allows to enter first name
	 * @param lastName and enter last name 
	 * Leaves the rest of the data points empty
	 */
	Customer(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName =  lastName;
	}
	
	public String toString() {
		return "Name: " + this.firstName + " " + this.lastName
				+"\nId Number: " + this.idNumber + "\nDate of Birth: " + this.dateOfBirth
				+"\nAddress: " + this.address + "\nPhone Number: "+ this.phoneNumber
				+"\n" + this.check.toString() + "\n" + this.savings.toString() + "\n" + this.credit.toString();
	}
	
}