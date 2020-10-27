package risk.MapCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CustomContinent extends JPanel implements MouseListener {
    private BufferedImage continentImage;

    public CustomContinent(BufferedImage continentImage, String name) {
        setOpaque(false);
        this.continentImage = continentImage;
        this.setName(name);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(continentImage, 0, 0, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public boolean isTransparent(int x, int y){
        System.out.print("ALPHA: " + ((continentImage.getRGB(x, y) >> 24) & 0xff) + "    ");
        return ((continentImage.getRGB(x, y) >> 24) & 0xff) == 0;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof CustomContinent)){
            return;
        }

        int x = e.getX(), y = e.getY();

        System.out.println("COORDS: " + x + " " + y);

        CustomContinent continentClicked = (CustomContinent) e.getSource();

        if (continentClicked.isTransparent(e.getX(), e.getY())){ //component is transparent
            JLayeredPane layeredPane = ((JLayeredPane) getParent());

           /*
            for (Component c : layeredPane.getComponentsInLayer(layeredPane.highestLayer()))
                System.out.println("component: " + c);

            System.out.println("HIGHEST: " + layeredPane.highestLayer() + " LOWEST: " + layeredPane.lowestLayer());

            System.out.println("components in 0: " + layeredPane.getComponentsInLayer(0));
            System.out.println("components in 1: " + layeredPane.getComponentsInLayer(1));

            for (Component c : layeredPane.getComponentsInLayer(layeredPane.lowestLayer()))
                System.out.println("component: " + c);
            */


            layeredPane.moveToBack(continentClicked);

            if (layeredPane.getComponentAt(x, y) instanceof CustomContinent){ //is there a component behind

                CustomContinent behind = (CustomContinent) layeredPane.getComponentAt(x, y);

                System.out.println("COMPONENT BEHIND: " + behind.getName());
                if (behind.isTransparent(x, y)){ //is that component transparent

                    System.out.println("CLICKED: transparent " +  behind.getName());
                } else {
                    System.out.println("CLICKED: non-transparent " +  behind.getName());
                }
            } else {
                System.out.println("ERROR");
            }

        } else {
            System.out.println("CLICKED: non-transparent " +  e.getComponent().getName());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
