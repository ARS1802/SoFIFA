import java.io.FileInputStream;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(new FileInputStream("input/players_22.csv"));
        System.out.println(input.nextLine());
        System.out.println(input.nextLine());
    }
}