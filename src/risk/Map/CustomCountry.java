package risk.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CustomCountry extends JPanel implements MouseListener {
    private BufferedImage continentImage;

    public CustomCountry(BufferedImage continentImage, String name) {
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
        return ((continentImage.getRGB(x, y) >> 24) & 0xff) == 0;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        //Figure out which component was clicked and get the name of it
        /*
        component is clicked if:
            - is not transparent at x, y (get us all components that are not transparent at that point)
            - higher layer than all components at that point
         */
        CustomCountry countryClicked = (CustomCountry) e.getSource();
        int x = e.getX(), y = e.getY();
        String countryName = "";
        if (countryClicked.isTransparent(x, y)){

            System.out.println("is transparent");
        } else {
            countryName = e.getComponent().getName();
        }


        //Depending on phase, allow or disallow actions




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
