package ui.screens;

import arvore.AVL;
import filters.Atributes;
import model.Player;
import tools.Document;
import ui.components.FiltersPanel;
import ui.components.IOPanel;
import ui.components.PlayerDisplayPanel;
import ui.components.SheetDisplayPanel;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreen extends JFrame {
    private final IOPanel ioPanel = new IOPanel();
    private final FiltersPanel filtersPanel = new FiltersPanel();
    private final SheetDisplayPanel sheetDisplayPanel = new SheetDisplayPanel();
    private final PlayerDisplayPanel playerDisplayPanel = new PlayerDisplayPanel();

    private Document document;
    private AVL<Player> currentPlayersTree;
    private List<Player> currentPlayers = Collections.emptyList();

    public HomeScreen(){
        super("SoFIFA - Home");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 650));
        setSize(1280, 720);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(ioPanel, BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        connectActions();
    }

    private JPanel createMainContent(){
        JPanel mainContent = new JPanel(new BorderLayout(16, 0));

        filtersPanel.setPreferredSize(new Dimension(290, 0));
        playerDisplayPanel.setPreferredSize(new Dimension(290, 0));

        mainContent.add(filtersPanel, BorderLayout.WEST);
        mainContent.add(sheetDisplayPanel, BorderLayout.CENTER);
        mainContent.add(playerDisplayPanel, BorderLayout.EAST);

        return mainContent;
    }

    private void connectActions(){
        filtersPanel.addApplyListener(event -> applyFilters());
        sheetDisplayPanel.addPlayerSelectionListener(playerDisplayPanel::showPlayer);
        sheetDisplayPanel.addExportListener(event -> exportCurrentPlayers());
    }

    private void applyFilters(){
        String inputPath = ioPanel.getInputPath();

        if(inputPath.isBlank()){
            showWarning("Informe o caminho do arquivo CSV de entrada.");
            return;
        }

        try {
            document = new Document(inputPath);

            Comparator<Player> comparator = Player.filters(filtersPanel.getSelectedFilters());
            currentPlayersTree = new AVL<>(comparator);
            currentPlayers = loadPlayers(document, currentPlayersTree, comparator);

            if(!filtersPanel.isAscendingOrder()){
                Collections.reverse(currentPlayers);
            }

            currentPlayers = limitPlayers(currentPlayers, filtersPanel.getPlayerLimit());

            sheetDisplayPanel.setPlayers(currentPlayers);
            playerDisplayPanel.showPlayer(null);

            if(currentPlayers.isEmpty()){
                showWarning("Nenhum jogador foi carregado do arquivo informado.");
            }
        }
        catch(Exception exception){
            currentPlayersTree = null;
            currentPlayers = Collections.emptyList();
            sheetDisplayPanel.setPlayers(Collections.emptyList());
            playerDisplayPanel.showPlayer(null);
            showError("Não foi possível aplicar os filtros.", exception);
        }
    }

    private List<Player> loadPlayers(
            Document sourceDocument,
            AVL<Player> playersTree,
            Comparator<Player> comparator
    ) throws IOException {
        List<Player> players = new ArrayList<>();

        sourceDocument.readLine(); // Ignora o cabeçalho do CSV.

        while(sourceDocument.readerHasNextLine()){
            String[] row = sourceDocument.readLine();

            if(row != null){
                Player player = new Player(row);
                playersTree.add(player);
                players.add(player);
            }
        }

        players.sort(comparator);
        return players;
    }

    private List<Player> limitPlayers(List<Player> players, int playerLimit){
        int lastIndex = Math.min(playerLimit, players.size());
        return new ArrayList<>(players.subList(0, lastIndex));
    }

    private void exportCurrentPlayers(){
        if(document == null || currentPlayersTree == null || currentPlayers.isEmpty()){
            showWarning("Aplique os filtros antes de exportar.");
            return;
        }

        try {
            document.setOutputFile(ioPanel.getOutputPath());
            document.writeLine(buildCsvHeader());

            for(Player player : currentPlayers){
                document.writeLine(player);
            }

            JOptionPane.showMessageDialog(this, "Jogadores exportados com sucesso.");
        }
        catch(IOException exception){
            showError("Não foi possível exportar os jogadores.", exception);
        }
    }

    private String buildCsvHeader(){
        StringBuilder header = new StringBuilder();

        for(Atributes attribute : Atributes.values()){
            if(header.length() > 0){
                header.append(",");
            }

            header.append(attribute.csvColumn());
        }

        return header.toString();
    }

    private void showWarning(String message){
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message, Exception exception){
        JOptionPane.showMessageDialog(
                this,
                message + "\n" + exception.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
