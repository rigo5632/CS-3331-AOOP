public class Customer extends Person{
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String idNumber;
    private String address;
    private String phoneNumber;


    public Customer(){}
    public Customer(String fn, String ln, String dob, String id, String addr, String phn){
        this.firstName = fn;
        this.lastName = ln;
        this.dateOfBirth = dob;
        this.idNumber = id;
        this.address = addr;
        this.phoneNumber = phn;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getDateOfBirth(){
        return this.dateOfBirth;
    }
    public String getIDNumber(){
        return this.idNumber;
    }
    public String getAddress(){
        return this.address;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }

}