import Arvore.AVL;
import tools.Document;

import javax.print.Doc;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Document planilha = new Document("input/FC26_20250921.csv");
        planilha.readLine();

        AVL<Player> avlteste = new AVL<>(Player.comparandoPor(Atributos.POTENTIAL));
        while(planilha.readerHasNextLine()){
            avlteste.add(new Player(planilha.readLine()));
        }
        System.out.println("AVL TESTE (maior potencial):");

        System.out.println(avlteste);
        planilha.setOutputFile("");
        planilha.writeLine(avlteste.toString());

        //Jogador com >menor< potencial de todos:
        //76599,Li Biao,Biao Li,"RM, RW",49,>49<,50000.0,950.0,27,182,72,131531,Yunnan Yukun

        //Jogador com <maior> potencial de todos:
        //277643,Lamine Yamal,Lamine Yamal Nasraoui Ebanaلامين يامال نصراوي إبانا,"RM, RW",89,>95<,1.47E8,100000.0,17,180,72,241,FC Barcelona

    }
}
