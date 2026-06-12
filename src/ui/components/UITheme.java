package ui.components;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;

public final class UITheme {
    public static final Color BLUE = new Color(96, 148, 246,220);
    public static final Color WHITE = Color.WHITE;
    public static final Color BORDER = new Color(110, 110, 128, 100);

    private UITheme(){
    }

    public static JButton blueButton(String text){
        JButton button = new JButton(text);
        button.setBackground(BLUE);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 20f));
        return button;
    }

    public static Border simpleBorder(){
        return BorderFactory.createLineBorder(BORDER, 2, true);
    }
}
