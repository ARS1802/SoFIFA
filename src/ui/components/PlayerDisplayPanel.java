package ui.components;

import filters.Atributes;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URI;

public class PlayerDisplayPanel extends JPanel {
    private static final int IMAGE_BOX_SIZE = 190;
    private static final int IMAGE_MAX_SIZE = 176;

    private final JPanel contentPanel = new JPanel();
    private final JLabel imageLabel = new JLabel();
    private final JTextArea detailsTextArea = new JTextArea();

    private SwingWorker<ImageIcon, Void> imageWorker;

    public PlayerDisplayPanel(){
        setLayout(new BorderLayout());
        setBorder(UITheme.simpleBorder());

        configureContentPanel();
        configureImageLabel();
        configureDetailsTextArea();

        JPanel imageWrapper = new JPanel();
        imageWrapper.add(imageLabel);

        contentPanel.add(imageWrapper);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(detailsTextArea);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        showPlayer(null);
    }

    public void showPlayer(Player player){
        cancelImageLoad();

        if(player == null){
            showPlaceholder("Sem jogador");
            detailsTextArea.setText("Selecione uma linha na tabela.");
            return;
        }

        detailsTextArea.setText(buildDetailsText(player));
        loadPlayerImage(player.getDisplayValue(Atributes.PLAYER_FACE_URL));
    }

    private void configureContentPanel(){
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    private void configureImageLabel(){
        Dimension imageBoxDimension = new Dimension(IMAGE_BOX_SIZE, IMAGE_BOX_SIZE);

        imageLabel.setPreferredSize(imageBoxDimension);
        imageLabel.setMinimumSize(imageBoxDimension);
        imageLabel.setMaximumSize(imageBoxDimension);
        imageLabel.setBorder(UITheme.simpleBorder());
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void configureDetailsTextArea(){
        detailsTextArea.setEditable(false);
        detailsTextArea.setOpaque(false);
        detailsTextArea.setColumns(18);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setWrapStyleWord(true);
        detailsTextArea.setFont(detailsTextArea.getFont().deriveFont(18f));
        detailsTextArea.setAlignmentX(LEFT_ALIGNMENT);
    }

    private String buildDetailsText(Player player){
        StringBuilder details = new StringBuilder();

        for(Atributes attribute : Atributes.values()){
            details.append(attribute.csvColumn())
                    .append(": ")
                    .append(player.getDisplayValue(attribute))
                    .append("\n");
        }

        return details.toString();
    }

    private void loadPlayerImage(String imageUrl){
        if(imageUrl == null || imageUrl.isBlank()){
            showPlaceholder("Sem imagem");
            return;
        }

        showPlaceholder("Carregando...");

        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                BufferedImage image = ImageIO.read(URI.create(imageUrl).toURL());

                if(image == null){
                    return null;
                }

                return new ImageIcon(scaleImage(image));
            }

            @Override
            protected void done() {
                if(this != imageWorker || isCancelled()){
                    return;
                }

                try {
                    ImageIcon imageIcon = get();

                    if(imageIcon == null){
                        showPlaceholder("Sem imagem");
                        return;
                    }

                    imageLabel.setText("");
                    imageLabel.setIcon(imageIcon);
                }
                catch(Exception exception){
                    showPlaceholder("Sem imagem");
                }
            }
        };

        imageWorker = worker;
        worker.execute();
    }

    private Image scaleImage(BufferedImage image){
        double widthScale = IMAGE_MAX_SIZE / (double) image.getWidth();
        double heightScale = IMAGE_MAX_SIZE / (double) image.getHeight();
        double scale = Math.min(widthScale, heightScale);

        int newWidth = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int newHeight = Math.max(1, (int) Math.round(image.getHeight() * scale));

        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    private void showPlaceholder(String text){
        imageLabel.setIcon(null);
        imageLabel.setText(text);
    }

    private void cancelImageLoad(){
        if(imageWorker != null && !imageWorker.isDone()){
            imageWorker.cancel(true);
        }
    }
}
