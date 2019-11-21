public class Customer extends Person{
    /**
     * Constructor
     * @param fn
     * @param ln
     * @param dob
     * @param eml
     * @param id
     * @param pwd
     * @param addr
     * @param phnum
     * @return
     */
    Customer(String fn, String ln, String dob, String eml, String id, String pwd, String addr, String phnum){
        super(fn, ln, dob, eml, id, pwd, addr, phnum);
    }
}
