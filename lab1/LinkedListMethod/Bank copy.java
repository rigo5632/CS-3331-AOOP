import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Bank{
    Checking test;

    public static void main(String[] args)throws FileNotFoundException{
        Scanner file = fileExists();
        if(file == null)System.exit(0);
        Checking a = new Checking();
        a.createUsers();

        //Checking a = createUsers(file);
        //a.printInfo();
        //System.out.println("--------------------------");
        //a = a.prev;
        //a.printInfo();
        /*
        a = a.next;
        a.printInfo();
        System.out.println("--------------------------");
        a = a.prev;
        a.printInfo();
        */

    }
    /*
    public void createUsers(Scanner scnr){
        //Checking temp = head;
        boolean h = true;
        try{

            String line = scnr.nextLine();
            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                String splitter[] = line.split("\t");
                String transferData[] = new String[splitter.length];
            
                for(int i = 0; i < splitter.length; i++){
                    transferData[i] = splitter[i];
                }
                
                if(!h){
                    temp.next = new Checking(transferData[0],transferData[1], Integer.parseInt(transferData[2]), 
                    Boolean.parseBoolean(transferData[3]), Boolean.parseBoolean(transferData[4]), 
                    Double.parseDouble(transferData[5]), transferData[6]);
                    temp.prev = temp;
                }
                if(h){
                    System.out.println("fsda");
                    temp.next = new Checking(transferData[0],transferData[1], Integer.parseInt(transferData[2]), 
                    Boolean.parseBoolean(transferData[3]), Boolean.parseBoolean(transferData[4]), 
                    Double.parseDouble(transferData[5]), transferData[6]);
                    temp.prev = null;
                    h = false;
                }
                temp = temp.next;
                
                Checking head = new Checking(transferData[0],transferData[1], Integer.parseInt(transferData[2]), 
                Boolean.parseBoolean(transferData[3]), Boolean.parseBoolean(transferData[4]), 
                Double.parseDouble(transferData[5]), transferData[6]);
                head.next = test;
                head.prev = null;

                if(test != null)test.prev = head;
                test = head;
            }
        }catch(NumberFormatException NFE){
            System.out.println("One of the fields in wrong. Please Revise!");
        }
    }
    */

    public static Scanner fileExists()throws FileNotFoundException{
        try{
            File file = new File("Bank Users.txt");
            Scanner scnr = new Scanner(file);
            return scnr;
        }catch(FileNotFoundException FNFE){
            System.out.println("File was not Found. Make sure that \"Bank Users.txt \" exists.");
            System.exit(0);
        }
        return null;
    }
}


class Checking{
    private String firstName;
    private String lastName;
    private int accountNumber;
    private boolean checking;
    private boolean savings;
    private double balance;
    private String interestRate;
    private String logs[] = new String[5];
    private int counter = 0;
    Checking next, prev;

    Checking(){}
    Checking(String fn, String ln, int an, boolean c, boolean s, double b, String ir){
        this.firstName = fn;
        this.lastName = ln;
        this.accountNumber = an;
        this.checking = c;
        this.savings = s;
        this.balance = b;
        this.interestRate = ir;
    }

    public void createUsers(Scanner scnr){
        //Checking temp = head;
        boolean h = true;
        try{

            String line = scnr.nextLine();
            while(scnr.hasNextLine()){
            line = scnr.nextLine();
            String splitter[] = line.split("\t");
            String transferData[] = new String[splitter.length];
            
            for(int i = 0; i < splitter.length; i++){
                transferData[i] = splitter[i];
            }
            Checking head = new Checking(transferData[0],transferData[1], Integer.parseInt(transferData[2]), 
            Boolean.parseBoolean(transferData[3]), Boolean.parseBoolean(transferData[4]), 
            Double.parseDouble(transferData[5]), transferData[6]);
            head.next = test;
            head.prev = null;

            if(test != null)test.prev = head;
                test = head;
            }
            }catch(NumberFormatException NFE){
                System.out.println("One of the fields in wrong. Please Revise!");
            }
    }

    public void printInfo(){
        System.out.println("firstname: " + this.firstName);
        System.out.println("lastname: " + this.lastName);
        System.out.println("account number: " + this.accountNumber);
        System.out.println("checking: " + this.checking);
        System.out.println("savings: " + this.savings);
        System.out.println("balance: " + this.balance);
        System.out.println("interest rate: " + this.interestRate);
    }
    public double getBalance(){
        return this.balance;
    }
    
    public void pay(){
        System.out.println("Pay someone");
        accountLog("Pay Someone");
    }

    public void deposit(double amount){
        System.out.println("Depositing " + amount + " to " + this.firstName + " " + this.lastName + " checking account.");
        accountLog("Depositing");
        this.balance += amount;;
    }

    public void withdraw(double amount){
        if(amount > this.balance){
            System.out.println("inefficient Funds...");
            return;
        }

        System.out.println("Withdrawing " + amount + " to " + this.firstName + " " + this.lastName + " checking account.");
        accountLog("Withdrawing");
        this.balance -= amount;
    }

    public void accountLog(String action){
        System.out.println("Logging...");
        if(counter == this.logs.length){
            String test[] = new String[logs.length];
            
            for(int i = 0; i < logs.length; i++){
                test[i] = this.logs[i];
            }

            this.logs = new String[(this.logs.length * 2)];

            for(int i = 0; i < test.length; i++){
                this.logs[i] = test[i];
            }
        }
        this.logs[counter] = action;
        counter++;
    }

    public void getLogs(){
        for(int i = 0; i < logs.length; i++){
            if(logs[i] != null){
                System.out.println(logs[i]);
            }
        }
    }
}