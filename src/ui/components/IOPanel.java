package ui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class IOPanel extends JPanel {
    private static final String INPUT_PLACEHOLDER = "Path to .csv";
    private static final String OUTPUT_PLACEHOLDER = "Default path is ./output";

    private final JTextField inputPathField;
    private final JTextField outputPathField;

    public IOPanel(){
        setLayout(new GridBagLayout());
        setBorder(UITheme.simpleBorder());

        inputPathField = createTextField(INPUT_PLACEHOLDER);
        outputPathField = createTextField(OUTPUT_PLACEHOLDER);

        addPathRow("input:", inputPathField, 0);
        addPathRow("output:", outputPathField, 1);
    }

    public String getInputPath(){
        return readText(inputPathField, INPUT_PLACEHOLDER);
    }

    public String getOutputPath(){
        String outputPath = readText(outputPathField, OUTPUT_PLACEHOLDER);

        if(outputPath.isBlank()){
            return "output";
        }

        return outputPath;
    }

    private JTextField createTextField(String placeholder){
        JTextField textField = new JTextField(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                if(textField.getText().equals(placeholder)){
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent event) {
                if(textField.getText().isBlank()){
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        return textField;
    }

    private String readText(JTextField textField, String placeholder){
        String text = textField.getText().trim();

        if(text.equals(placeholder)){
            return "";
        }

        return text;
    }

    private void addPathRow(String labelText, JTextField textField, int row){
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.insets = new Insets(6, 10, 6, 8);
        labelConstraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(labelText), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(6, 0, 6, 10);
        add(textField, fieldConstraints);
    }
}
