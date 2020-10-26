package LevelCreator;

import javax.swing.*;
import java.awt.*;

public class CustomContinent extends JPanel{
    private Image continentImage;

    public CustomContinent(Image continentImage) {
        setOpaque(false);
        this.continentImage = continentImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(continentImage, 0, 0, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


}
