import java.util.ArrayList;
import java.util.Arrays;

public class Data {
    public static ArrayList<ArrayList<String>> FRUITS = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("Mango", "2000.00", "5","false")),
            new ArrayList<>(Arrays.asList("Apple", "400.00", "5","false")),
            new ArrayList<>(Arrays.asList("Banana", "100.00", "5","false")),
            new ArrayList<>(Arrays.asList("Pineapple", "1000.00", "5","false"))
    ));

    public static ArrayList<ArrayList<String>> FRUITS_QUANT = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("Mango", "1kg", "0.00")),
            new ArrayList<>(Arrays.asList("Mango", "2kg", "2000.00")),
            new ArrayList<>(Arrays.asList("Mango", "3kg", "4000.00")),
            new ArrayList<>(Arrays.asList("Apple", "1kg", "0.00")),
            new ArrayList<>(Arrays.asList("Apple", "2kg", "400.00")),
            new ArrayList<>(Arrays.asList("Apple", "3kg", "800.00")),
            new ArrayList<>(Arrays.asList("Banana", "1kg", "0.00")),
            new ArrayList<>(Arrays.asList("Banana", "2kg", "100.00")),
            new ArrayList<>(Arrays.asList("Banana", "3kg", "200.00")),
            new ArrayList<>(Arrays.asList("Pineapple", "1kg", "0.00")),
            new ArrayList<>(Arrays.asList("Pineapple", "2kg", "1000.00")),
            new ArrayList<>(Arrays.asList("Pineapple", "3kg", "2000.00"))
    ));

    public static ArrayList<ArrayList<String>> VEG = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("Potato", "50.00","5","false")),
            new ArrayList<>(Arrays.asList("Tomato", "60.00","5","false")),
            new ArrayList<>(Arrays.asList("Carrots", "20.00","5","false"))
           
    ));

//     list of array list of string
    public static ArrayList<ArrayList<String>> VEG_QUANT  = new ArrayList<>(Arrays.asList(
        new ArrayList<>(Arrays.asList("Potato", "1kg", "0.00")),
        new ArrayList<>(Arrays.asList("Potato", "2kg", "50.00")),
        new ArrayList<>(Arrays.asList("Potato", "3kg", "100.00")),
        new ArrayList<>(Arrays.asList("Tomato", "1kg", "0.00")),
        new ArrayList<>(Arrays.asList("Tomato", "2kg", "60.00")),
        new ArrayList<>(Arrays.asList("Tomato", "3kg", "120.00")),
        new ArrayList<>(Arrays.asList("Carrots", "1kg", "0.00")),
        new ArrayList<>(Arrays.asList("Carrots", "2kg", "20.00")),
        new ArrayList<>(Arrays.asList("Carrots", "3kg", "40.00"))
    ));

   
}