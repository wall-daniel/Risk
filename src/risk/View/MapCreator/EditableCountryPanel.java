package risk.View.MapCreator;


import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.OneCountryEvent;
import risk.View.Map.CountryPanel;
import risk.View.Views.GameModelListener;
import risk.Model.Continent;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class EditableCountryPanel extends CountryPanel implements MouseMotionListener, MouseListener {
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    private boolean pressed = false;;

    public EditableCountryPanel(Country country, Dimension size, Controller controller, Point polygonPoint) {
        super(country, size, controller, false);
        countryLabel = new MoveableCountryLabel(country.getName(), controller);
        Point labelPoint = country.getLabelPoint();
        countryLabel.setBounds(labelPoint.x - polygonPoint.x, labelPoint.y - polygonPoint.y, 100, 20);
        add(countryLabel);


        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g.create();
        Polygon polygon = country.getPolygon();

        //draw border polygon
        graphics2D.setColor(MapColor.BORDER_COLOR.getColor());
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawPolygon(polygon);

        //fill polygon
        graphics2D.setColor(color);
        graphics2D.fillPolygon(polygon);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (country.getPolygon().contains(e.getPoint())) {
            screenX = e.getXOnScreen();
            screenY = e.getYOnScreen();

            myX = getX();
            myY = getY();
            pressed = true;
        } else {
            pressed = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressed) {
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;

            setLocation(myX + deltaX, myY + deltaY);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
