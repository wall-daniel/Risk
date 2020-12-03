package risk.View.MapCreator;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.View.Map.CountryLabel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MoveableCountryLabel extends CountryLabel implements MouseListener, MouseMotionListener {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    public MoveableCountryLabel(String text, Controller controller){
        super(text, 0, controller);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public void updateLabel(String text, int armies){
        setText(text);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
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
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        setLocation(myX + deltaX, myY + deltaY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
