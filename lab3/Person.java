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
     * getFullName: Retrieve users full name
     * @return String: full name
     */
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    /**
     * getFirstName: gets first name
     * @return String: first name
     */
    public String getFirstName(){
      return this.firstName;
    }

    /**
     * getLastName: gets last name
     * @return StringL last name
     */
    public String getLastName(){
      return this.lastName;
    }

    /**
     * getDateOfBirth: Retrieve users date of birth
     * @return String: date of birth
     */
    public String getDateOfBirth(){
        return this.dateOfBith;
    }

    /**
     * getID: Retrieve users id
     * @return String: id
     */
    public String getID(){
        return this.identificationNumber;
    }

    /**
     * getAddress: Retrieve users address
     * @return String: address
     */
    public String getAddress(){
        return this.Address;
    }


    /**
     * getPhoneNumber: Retrieve users phone number
     * @return String: phone number
     */
    public String getPhoneNumber(){
        return this.phoneNumer;
    }
}
