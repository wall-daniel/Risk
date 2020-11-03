package risk.Map;

import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

public class CustomCountry extends JPanel implements MouseListener {
    private Polygon countryPolygon;
    private Color color;
    Graphics2D graphics2D;

    public CustomCountry(Polygon polygon, String name) {
        setOpaque(false);
        this.countryPolygon = polygon;
        this.setName(name);
        this.color = PlayerColor.PLAYER_1_COLOR.getRandomPlayerColor();
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2D = (Graphics2D) g.create();

        //draw border polygon
        graphics2D.setColor(MapColor.BORDER_COLOR.getColor());
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawPolygon(countryPolygon);

        //fill polygon
        graphics2D.setColor(color);
        graphics2D.fillPolygon(countryPolygon);

    }

    public boolean isPointInPolygon(int x, int y){
        return countryPolygon.contains(x, y);
    }


    public void setFillColor(Color fillColor){
        color = fillColor;
        repaint();
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



    public Polygon getCountryPolygon(){
        return countryPolygon;
    }
}
