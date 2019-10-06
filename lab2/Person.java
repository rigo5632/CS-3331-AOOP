abstract class Person{
    private String firstName;
    private String lastName;
    private String dateOfBith;
    private String identificationNumber;
    private String Address;
    private String phoneNumer;

    Person(String fn, String ln, String dob, String id, String addr, String phnum){
        this.firstName = fn;
        this.lastName = ln;
        this.dateOfBith = dob;
        this.identificationNumber = id;
        this.Address = addr;
        this.phoneNumer = phnum;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
    
    public String getDateOfBirth(){
        return this.dateOfBith;
    }

    public String getID(){
        return this.identificationNumber;
    }

    public String getAddress(){
        return this.Address;
    }

    public String getPhoneNumber(){
        return this.phoneNumer;
    }
}