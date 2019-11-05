abstract class Person{
    private String firstName;
    private String lastName;
    private String dateOfBith;
    private String identificationNumber;
    private String Address;
    private String phoneNumer;


    /**
     * Constructor
     * @param fn
     * @param ln
     * @param dob
     * @param id
     * @param addr
     * @param phnum
     * @return
     */
    Person(String fn, String ln, String dob, String id, String addr, String phnum){
        this.firstName = fn;
        this.lastName = ln;
        this.dateOfBith = dob;
        this.identificationNumber = id;
        this.Address = addr;
        this.phoneNumer = phnum;
    }


    /**
     * Retrieve users full name
     * @return String
     */
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public String getFirstName(){
      return this.firstName;
    }

    public String getLastName(){
      return this.lastName;
    }

    /**
     * Retrieve users date of birth
     * @return String
     */
    public String getDateOfBirth(){
        return this.dateOfBith;
    }


    /**
     * Retrieve users id
     * @return String
     */
    public String getID(){
        return this.identificationNumber;
    }


    /**
     * Retrieve users address
     * @return String
     */
    public String getAddress(){
        return this.Address;
    }


    /**
     * Retrieve users phone number
     * @return String
     */
    public String getPhoneNumber(){
        return this.phoneNumer;
    }
}
