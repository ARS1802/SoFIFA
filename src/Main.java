import java.io.FileInputStream;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        AVL<Player> arvore_lobotomia = new AVL<>();

        Scanner sc = new Scanner(new FileInputStream("input/players_22.csv"));
        int count = 0;
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(",");
            Player entry = new Player(line);
            arvore_lobotomia.add(entry);
            System.out.println(count++);
        }
        System.out.println(arvore_lobotomia);
    }
}