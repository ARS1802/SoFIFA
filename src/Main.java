import java.io.FileInputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new FileInputStream("input/players_22.csv"));
        String[] a = sc.nextLine().split(",");
        String[] s = sc.nextLine().split(",");
        System.out.println(s[Atributos.SOFIFA_ID.indice]);
    }
}