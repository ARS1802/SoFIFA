package ui.components;

import filters.Atributes;
import filters.Filters;
import filters.Position;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FiltersPanel extends JPanel {
    private static final int MINIMUM_PLAYER_LIMIT = 1;

    private final Map<JCheckBox, Filters> filtersByCheckBox = new LinkedHashMap<>();
    private final JLabel counterLabel = new JLabel();
    private final ButtonGroup orderButtonGroup = new ButtonGroup();
    private final JToggleButton descendingOrderButton = createOrderButton(false);
    private final JToggleButton ascendingOrderButton = createOrderButton(true);
    private final JButton applyButton = UITheme.blueButton("Aplicar");

    private int playerLimit = 10;
    private boolean ascendingOrder = true;

    public FiltersPanel(){
        setLayout(new BorderLayout(8, 8));
        setBorder(UITheme.simpleBorder());

        add(createFilterCheckList(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        updateCounterLabel();
    }

    public Filters[] getSelectedFilters(){
        List<Filters> selectedPositionFilters = new ArrayList<>();
        List<Filters> selectedAttributeFilters = new ArrayList<>();

        for(Map.Entry<JCheckBox, Filters> entry : filtersByCheckBox.entrySet()){
            if(!entry.getKey().isSelected()){
                continue;
            }

            Filters filter = entry.getValue();

            if(filter instanceof Position){
                selectedPositionFilters.add(filter);
            }
            else {
                selectedAttributeFilters.add(filter);
            }
        }

        List<Filters> selectedFilters = new ArrayList<>();
        selectedFilters.addAll(selectedPositionFilters);
        selectedFilters.addAll(selectedAttributeFilters);

        return selectedFilters.toArray(new Filters[0]);
    }

    public int getPlayerLimit(){
        return playerLimit;
    }

    public boolean isAscendingOrder(){
        return ascendingOrder;
    }

    public void addApplyListener(ActionListener listener){
        applyButton.addActionListener(listener);
    }

    private JScrollPane createFilterCheckList(){
        JPanel checkListPanel = new JPanel();
        checkListPanel.setLayout(new BoxLayout(checkListPanel, BoxLayout.Y_AXIS));

        for(Atributes attribute : Atributes.values()){
            if(attribute == Atributes.PLAYER_FACE_URL){
                continue;
            }

            addCheckBox(checkListPanel, attribute.csvColumn(), attribute);
        }

        for(Position position : Position.values()){
            String label = position.name() + " (" + position.portugueseName() + ")";
            addCheckBox(checkListPanel, label, position);
        }

        JScrollPane scrollPane = new JScrollPane(checkListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(UITheme.simpleBorder());

        return scrollPane;
    }

    private JPanel createFooter(){
        JPanel footerPanel = new JPanel(new BorderLayout(0, 8));
        footerPanel.add(createOrderSwitchPanel(), BorderLayout.NORTH);
        footerPanel.add(createCounterPanel(), BorderLayout.CENTER);
        footerPanel.add(applyButton, BorderLayout.SOUTH);
        return footerPanel;
    }

    private JPanel createOrderSwitchPanel(){
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(UITheme.simpleBorder());
        orderPanel.setPreferredSize(new Dimension(0, 58));

        orderButtonGroup.add(descendingOrderButton);
        orderButtonGroup.add(ascendingOrderButton);

        descendingOrderButton.addActionListener(event -> setAscendingOrder(false));
        ascendingOrderButton.addActionListener(event -> setAscendingOrder(true));
        setAscendingOrder(true);

        orderPanel.add(descendingOrderButton, BorderLayout.WEST);
        orderPanel.add(ascendingOrderButton, BorderLayout.CENTER);

        return orderPanel;
    }

    private JPanel createCounterPanel(){
        JPanel counterPanel = new JPanel(new BorderLayout());
        counterPanel.setBorder(UITheme.simpleBorder());

        JButton minusButton = UITheme.blueButton("-");
        JButton plusButton = UITheme.blueButton("+");
        Dimension buttonSize = new Dimension(52, 42);

        minusButton.setPreferredSize(buttonSize);
        plusButton.setPreferredSize(buttonSize);
        counterLabel.setHorizontalAlignment(JLabel.CENTER);
        counterLabel.setFont(counterLabel.getFont().deriveFont(Font.PLAIN, 30f));

        minusButton.addActionListener(event -> changePlayerLimit(-1));
        plusButton.addActionListener(event -> changePlayerLimit(1));

        counterPanel.add(minusButton, BorderLayout.WEST);
        counterPanel.add(counterLabel, BorderLayout.CENTER);
        counterPanel.add(plusButton, BorderLayout.EAST);

        return counterPanel;
    }

    private JToggleButton createOrderButton(boolean ascending){
        JToggleButton button = new JToggleButton();
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setPreferredSize(new Dimension(135, 52));
        button.putClientProperty("ascending", ascending);
        return button;
    }

    private void addCheckBox(JPanel checkListPanel, String label, Filters filter){
        JCheckBox checkBox = new JCheckBox(label);
        checkBox.setFont(checkBox.getFont().deriveFont(Font.PLAIN, 18f));
        checkBox.setAlignmentX(LEFT_ALIGNMENT);

        filtersByCheckBox.put(checkBox, filter);
        checkListPanel.add(checkBox);
    }

    private void changePlayerLimit(int change){
        playerLimit += change;

        if(playerLimit < MINIMUM_PLAYER_LIMIT){
            playerLimit = MINIMUM_PLAYER_LIMIT;
        }

        updateCounterLabel();
    }

    private void updateCounterLabel(){
        counterLabel.setText(String.valueOf(playerLimit));
    }

    private void setAscendingOrder(boolean ascendingOrder){
        this.ascendingOrder = ascendingOrder;
        descendingOrderButton.setSelected(!ascendingOrder);
        ascendingOrderButton.setSelected(ascendingOrder);
        updateOrderButtonColors();
    }

    private void updateOrderButtonColors(){
        updateOrderButton(descendingOrderButton, !ascendingOrder);
        updateOrderButton(ascendingOrderButton, ascendingOrder);
    }

    private void updateOrderButton(JToggleButton button, boolean selected){
        boolean ascending = (boolean) button.getClientProperty("ascending");
        Color iconColor;

        if(selected){
            button.setBackground(UITheme.BLUE);
            button.setForeground(UITheme.WHITE);
            iconColor = UITheme.WHITE;
        }
        else {
            button.setBackground(UITheme.WHITE);
            button.setForeground(UITheme.BLUE);
            iconColor = UITheme.BLUE;
        }

        button.setIcon(new ArrowIcon(ascending, iconColor));
    }

    private static class ArrowIcon implements Icon {
        private final boolean pointsUp;
        private final Color color;

        public ArrowIcon(boolean pointsUp, Color color){
            this.pointsUp = pointsUp;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return 42;
        }

        @Override
        public int getIconHeight() {
            return 32;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(color);
            graphics2D.setStroke(new BasicStroke(6f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

            if(pointsUp){
                graphics2D.drawLine(x + 7, y + 22, x + 21, y + 8);
                graphics2D.drawLine(x + 21, y + 8, x + 35, y + 22);
            }
            else {
                graphics2D.drawLine(x + 7, y + 10, x + 21, y + 24);
                graphics2D.drawLine(x + 21, y + 24, x + 35, y + 10);
            }

            graphics2D.dispose();
        }
    }
}
