import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class checking{
    public static void main(String[] args)throws FileNotFoundException{
        String storage[][] = fileDimensions();
        /*
        for(int i = 0; i < storage.length; i++){
            for(int j = 0; j < storage[i].length; j++){
                System.out.print(storage[i][j] + " ");
            }
            System.out.println();
        }
        */
        getUsers(storage);
        for(int i = 0; i < storage.length; i++){
            for(int j = 0; j < storage[i].length; j++){
                System.out.print(storage[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void getUsers(String[][] s)throws FileNotFoundException{
        Scanner scnr = fileScnr();
        String line = scnr.nextLine();
        int index = 0;

        while(scnr.hasNextLine()){
            line = scnr.nextLine();
            String[] splitter = line.split("\t");
            for(int i = 0; i < splitter.length; i++){
                s[index][i] = splitter[i];
            }
            index++;
        }
    }

    public static Scanner fileScnr()throws FileNotFoundException{
        try{
            File file = new File("Bank Users.txt");
            Scanner scnr = new Scanner(file);
            return scnr;
        }catch(FileNotFoundException FNFE){
            System.out.println("File was not Found. Please make sure you have \"Bank Users.txt\"");
            System.exit(0);
        }
        return null;
    }

    public static String[][] fileDimensions()throws FileNotFoundException{
        Scanner scnr = fileScnr();
        int row = 0;
        int column = 0;
        int prevColumn;

        while(scnr.hasNextLine()){
            String line = scnr.nextLine();
            String splitter[] = line.split("\t");
            prevColumn = column;

            if(prevColumn != column){
                System.out.println("Error with input file. Please make sure you have the correct file format");
                System.exit(0);
            }
            column = splitter.length;
            row++;
        }
        String storage[][] = new String[row-1][column+1];
        return storage;
    }
}