import java.util.HashMap;

public class HashMaps{
  public static void main(String[] args){
    HashMap<String, String> cities = new HashMap<String, String>();

    // adding stuff
    cities.put("England", "London");
    cities.put("Germany", "Berlin");
    cities.put("Norway", "Oslo");
    cities.put("USA", "Washinston DC");
    System.out.println(cities);
    System.out.println(cities.get("England"));

  }
}
