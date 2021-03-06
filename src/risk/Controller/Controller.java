package risk.Controller;

import risk.View.Map.CountryPanel;

import risk.Model.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

abstract public class Controller implements MouseListener {

    protected JFrame gameView;


    public Controller(JFrame view) {
        this.gameView = view;
    }


    /**
     * Finds highest layered country and sends a click of that country to determine what action is to be done.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Find the thing clicked on that has the highest layer (e.g. one on top)
        if (mouseEvent.getSource() instanceof CountryPanel){
            countryClicked(mouseEvent);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) { }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) { }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

    abstract protected void countryClicked(MouseEvent mouseEvent);
}
