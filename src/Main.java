import arvore.AVL;
import filters.Atributes;
import filters.Position;
import tools.Document;

public class Main {
    public static void main(String[] args) throws Exception {
        Document spreadsheet = new Document("input/FC26_20250921.csv");
        spreadsheet.readLine();

        AVL<Player> avlTest = new AVL<>(Player.filters(Position.GK, Atributes.POTENTIAL));
        while(spreadsheet.readerHasNextLine()){
            avlTest.add(new Player(spreadsheet.readLine()));
        }
        System.out.println("AVL TESTE (golero com maior potencial):");

        System.out.println(avlTest);
        spreadsheet.setOutputFile("");
        spreadsheet.writeLine(avlTest.toString());

        //Jogador com >menor< potencial de todos:
        //76599,Li Biao,Biao Li,"RM, RW",49,>49<,50000.0,950.0,27,182,72,131531,Yunnan Yukun

        //Jogador com <maior> potencial de todos:
        //277643,Lamine Yamal,Lamine Yamal Nasraoui Ebanaلامين يامال نصراوي إبانا,"RM, RW",89,>95<,1.47E8,100000.0,17,180,72,241,FC Barcelona

    }
}
