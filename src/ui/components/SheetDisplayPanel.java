package ui.components;

import filters.Atributes;
import model.Player;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SheetDisplayPanel extends JPanel {
    private final PlayerTableModel tableModel = new PlayerTableModel();
    private final JTable sheetTable = new JTable(tableModel);
    private final JScrollPane sheetScrollPane = new JScrollPane(sheetTable);
    private final JButton exportButton = UITheme.blueButton("Exportar");

    private Consumer<Player> playerSelectionListener;

    public SheetDisplayPanel(){
        setLayout(new BorderLayout(8, 8));
        setBorder(UITheme.simpleBorder());

        configureTable();
        add(sheetScrollPane, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    public void setPlayers(List<Player> players){
        tableModel.setPlayers(players);
        resizeColumns();
        sheetTable.clearSelection();

        SwingUtilities.invokeLater(() -> sheetScrollPane.getViewport().setViewPosition(new Point(0, 0)));
    }

    public void addPlayerSelectionListener(Consumer<Player> listener){
        this.playerSelectionListener = listener;
    }

    public void addExportListener(java.awt.event.ActionListener listener){
        exportButton.addActionListener(listener);
    }

    private void configureTable(){
        sheetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sheetTable.setFillsViewportHeight(true);
        sheetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sheetTable.setSelectionBackground(UITheme.BLUE);
        sheetTable.setSelectionForeground(Color.WHITE);
        sheetTable.setRowHeight(26);

        sheetTable.getSelectionModel().addListSelectionListener(event -> {
            if(event.getValueIsAdjusting() || playerSelectionListener == null){
                return;
            }

            int selectedRow = sheetTable.getSelectedRow();

            if(selectedRow < 0){
                playerSelectionListener.accept(null);
                return;
            }

            int modelRow = sheetTable.convertRowIndexToModel(selectedRow);
            playerSelectionListener.accept(tableModel.getPlayerAt(modelRow));
        });
    }

    private JPanel createFooter(){
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exportButton.setPreferredSize(new java.awt.Dimension(280, 44));
        footer.add(exportButton);
        return footer;
    }

    private void resizeColumns(){
        for(int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++){
            Atributes attribute = tableModel.getAttributeAt(columnIndex);
            TableColumn column = sheetTable.getColumnModel().getColumn(columnIndex);
            column.setPreferredWidth(preferredWidthFor(attribute));
        }
    }

    private int preferredWidthFor(Atributes attribute){
        switch(attribute){
            case PLAYER_ID:
            case OVERALL:
            case POTENTIAL:
            case AGE:
            case HEIGHT_CM:
            case WEIGHT_KG:
            case CLUB_TEAM_ID:
                return 90;
            case SHORT_NAME:
            case CLUB_NAME:
                return 160;
            case LONG_NAME:
                return 230;
            case PLAYER_POSITIONS:
                return 130;
            case VALUE_EUR:
            case WAGE_EUR:
                return 120;
            case PLAYER_FACE_URL:
                return 280;
            default:
                return 140;
        }
    }

    private static class PlayerTableModel extends AbstractTableModel {
        private final Atributes[] columns = Atributes.values();
        private List<Player> players = new ArrayList<>();

        public void setPlayers(List<Player> players){
            this.players = new ArrayList<>(players);
            fireTableDataChanged();
        }

        public Player getPlayerAt(int row){
            return players.get(row);
        }

        public Atributes getAttributeAt(int column){
            return columns[column];
        }

        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column].csvColumn();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columns[columnIndex].type();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Player player = players.get(rowIndex);
            Atributes attribute = columns[columnIndex];
            return player.getValue(attribute);
        }
    }
}
