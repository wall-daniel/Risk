package risk.Map;

import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomCountry extends JPanel implements MouseListener {
    private Polygon countryPolygon;

    public CustomCountry(Polygon polygon, String name) {
        setOpaque(false);
        this.countryPolygon = polygon;
        this.setName(name);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawPolygon(countryPolygon);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public boolean isPointInPolygon(int x, int y){
        return countryPolygon.contains(x, y);
    }


    /**
     * Maybe change the border color if a new player conquers the country.
     */
    public void setPolygonBorder(MapColor borderColor){

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        CustomCountry countryClicked = (CustomCountry) e.getSource();
        int x = e.getX(), y = e.getY();
        String countryName = "";
        if (countryClicked.isPointInPolygon(x, y)){


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
