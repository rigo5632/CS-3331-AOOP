/**
 * @author Denise Castro 
 * (Watch12)
 * November 8,2019
 * Advanced Object Oriented Programming
 * Professor Daniel Mejia
 * Programming Assignment 3
 * 
 */

public abstract class Account implements Log {
		double balance;
		String type;
		int accountNumber;
		
		/**
		 * 
		 * @return double the balance of the account
		 */
		public double getBalance() {
			return  balance;
		}
		/**
		 * 
		 * @param balance sets account balance to parameter
		 */
		public void setBalance(String balance) {
			this.balance = Double.parseDouble(balance);
		}
		
		/**
		 * 
		 * @return int the account number
		 */
		public int getAccountNumber() {
			return accountNumber;
		}
		public void setAccountNumber(String accountNumber) {
			this.accountNumber = Integer.valueOf(accountNumber);
		}
		/**
		 * @param double amount which is then subtracted from the balance of the account
		 * The method checks if their is enough in the balance to withdraw, prints statement if amount too high
		 */
		@Override
		public void withdraw(double amount) {
			if(amount < 0)
				System.out.println("The amount entered is negative!");
			else if(checkBalance(amount))
				this.balance -= amount;
			else
				System.out.print("\nYour balance is too low to complete this transaction.");
		}

		
		@Override
		public double inquiry() {
			return this.balance;
		}

		/**
		 * @param double amount adds amount to balance of account
		 */
		@Override
		public void deposit(double amount) {
			if(amount > 0)
				this.balance += amount;
			else
				System.out.println("The amount you entered is negative.");
		}
		
		/**
		 * @param amount will be withdrawn from the balance of this account
		 * @param toAccount amount will be deposited toAccount 
		 */
		@Override
		public void transfer(double amount, Account toAccount) {
			withdraw(amount);	
			toAccount.deposit(amount);
		}
		
		/**
		 * @param amount will be withdrawn from the balance of this account
		 * @param payToName amount will be deposited to different user in Account payToName
		 */
		@Override
		public void payment(double amount, Account payToName) {
			withdraw(amount);
			payToName.deposit(amount);
		}
		
		/**
		 * @param toCheck amount to be compared against balance
		 * @return boolean if amount is greater than toCheck
		 */
		@Override
		public boolean checkBalance(double toCheck) {
			if(this.balance >= toCheck)
				return true;
			return false;
		}

		public String toString() {
			return this.type + " Account Number: "+ this.accountNumber +"\n"+this.type + " Balance: "+
					this.balance;
		}
}
