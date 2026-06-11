import Arvore.AVL;
import tools.Document;

public class Main {
    public static void main(String[] args) throws Exception {
        Document planilha = new Document("input/FC26_20250921.csv");
        planilha.readLine();

        Player a = new Player(planilha.readLine());
        Player b = new Player(planilha.readLine());
        Player c = new Player(planilha.readLine());

        AVL<Player> porId = new AVL<>(
                Player.comparandoPor(Atributos.PLAYER_ID),
                a,
                b,
                c
        );

        AVL<Player> porNomeDepoisId = new AVL<>(
                Player.comparandoPor(Atributos.SHORT_NAME, Atributos.PLAYER_ID),
                a,
                b,
                c
        );

        AVL<Player> porOverallDepoisPotencialDepoisNome = new AVL<>(
                new Player[]{a, b, c},
                Player.comparandoPor(Atributos.OVERALL, Atributos.POTENTIAL, Atributos.SHORT_NAME)
        );

        System.out.println("Ordenado por PLAYER_ID:");
        System.out.println(porId);

        System.out.println("\nOrdenado por SHORT_NAME e, em caso de empate, PLAYER_ID:");
        System.out.println(porNomeDepoisId);

        System.out.println("\nOrdenado por OVERALL, depois POTENTIAL, depois SHORT_NAME:");
        System.out.println(porOverallDepoisPotencialDepoisNome);

        planilha.setOutputFile("");
        planilha.writeLine(a);
        planilha.writeLine(b);
        planilha.writeLine(c);

    }
}
