import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Bank{
    Checking test;

    public static void main(String[] args)throws FileNotFoundException{
        Scanner file = fileExists();
        if(file == null)System.exit(0);

        Checking users = createUsers(file);
        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter Account Number");
        int userAccountNum = scnr.nextInt();
    }

    public static Checking createUsers(Scanner scnr){
        Checking head = new Checking();
        Checking temp = head;
        try{
            String line = scnr.nextLine();
            while(scnr.hasNextLine()){
                line = scnr.nextLine();
                String splitter[] = line.split("\t");
                String transferData[] = new String[splitter.length];
            
                for(int i = 0; i < splitter.length; i++){
                    transferData[i] = splitter[i];
                }
                
                temp.next = new Checking(transferData[0],transferData[1], Integer.parseInt(transferData[2]), 
                Boolean.parseBoolean(transferData[3]), Boolean.parseBoolean(transferData[4]), 
                Double.parseDouble(transferData[5]), transferData[6]);
                temp = temp.next;
            }
            head = head.next;
        }catch(NumberFormatException NFE){
            System.out.println("One of the fields in wrong. Please Revise!");
        }
        return head;
    }

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
    Checking next;

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

    public int getAccount(){
        return this.accountNumber;
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