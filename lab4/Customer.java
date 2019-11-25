import java.util.ArrayList;

public class Customer extends Person{
	  // private variables
	  private Person person;
	  private Checking checking;
	  private Savings savings;
	  private Credit credit;
	  private ArrayList<String> logs = new ArrayList<String>();
	  private int logIndex;

	  /**
	  * Constructor
	  * @param p
	  * @param c
	  * @param s
	  * @param cr
	  * @return
	  */
	 Customer(){}

	  /**
	  * Constructor
	  * @param p
	  * @param c
	  * @param s
	  * @param cr
	  * @return
	  */
	  Customer(Person p, Checking c, Savings s, Credit cr){
	        this.person = p;
	        this.checking = c;
	        this.savings = s;
	        this.credit = cr;
	        this.logIndex = 0;
	    }
	  
	    Customer(String fn, String ln, String dob, String eml, String id, String pwd, String addr, String phnum){
	    	super(fn,ln,dob,eml,id,pwd,addr,phnum);
	    }

	    /**
	    * printLogs: gets log at a specific index
	    * @param  i int: what index to get from logs
	    * @return   String: log
	    */
	    public String printLogs(int i){
	      return this.logs.get(i);
	    }

	    /**
	    * getLogLength: how many log we have per users
	    * @return int: length if array of logs
	    */
	    public int getLogLength(){
	      return this.logs.size();
	    }

	    /**
	     * addLog: adds log what the user has done
	     * @param l String: user action
	     */
	    public void addLog(String l){
	        // add to log
	        this.logs.add(l);
	        this.logIndex++;
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
	     * @return Account: all current user checking info
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
	    * @return Account: all current users credit info
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
	     * getMax: max amount from credit class
	     * @return double: credit max
	     */
		public double getMax() {
			return this.credit.getMax();
		}
}
