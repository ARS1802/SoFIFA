import Arvore.AVL;
import tools.Document;

import java.io.FileInputStream;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        AVL<Player> arvore_lobotomia = new AVL<>();
        Document planilha = new Document("input/players_22.csv");
        planilha.readLine();
        Player a = new Player(planilha.readLine());
        Player b = new Player(planilha.readLine());
        Player c = new Player(planilha.readLine());

        arvore_lobotomia.add(a);
        arvore_lobotomia.add(b);
        arvore_lobotomia.add(c);
        System.out.println(arvore_lobotomia);

        planilha.setOutputFile("");
        planilha.writeLine(a);
        planilha.writeLine(b);
        planilha.writeLine(c);

        Document pesquisa = new Document("output/2026-06-09_16-18-07.csv");

    }
}