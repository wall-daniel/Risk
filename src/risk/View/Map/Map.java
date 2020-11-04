package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Listener.GameModelListener;
import risk.Listener.OneCountryEvent;
import risk.Listener.TwoCountryEvent;
import risk.Model.Country;
import risk.Players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Map extends JLayeredPane implements GameModelListener, MouseListener {
    Controller controller;

    HashMap<String, Polygon> polygonList = new HashMap<String, Polygon>();
    OneCountryEvent countryEvent = null;

    public Map(Controller controller){
        this.controller = controller;
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (countryEvent!=null){
            Graphics2D g2d = (Graphics2D) g.create();
            drawPolygon(g2d, countryEvent.getFirstCountry().getPolygon(), countryEvent.getFirstCountry().getPlayer().getPlayerColor().getColor()); //demeter? i dont even know her
            if (countryEvent instanceof TwoCountryEvent){
                drawPolygon(g2d, ((TwoCountryEvent) countryEvent).getSecondCountry().getPolygon(), ((TwoCountryEvent) countryEvent).getSecondCountry().getPlayer().getPlayerColor().getColor());
            }
        }
    }

    public void drawPolygon(Graphics2D g2d, Polygon polygon, Color color){
        g2d.setColor(MapColor.BORDER_COLOR.getColor());
        g2d.setStroke(new BasicStroke(10));
        g2d.drawPolygon(polygon);

        g2d.setColor(color);
        g2d.fillPolygon(polygon);
    }

    //put update
    @Override
    public void onNewCountry(OneCountryEvent oce) {

    }

    @Override
    public void onPlaceTroops(OneCountryEvent oce) {
        countryEvent = oce;
        repaint();
    }

    @Override
    public void onAttack(TwoCountryEvent tce) {
        countryEvent = tce;
        repaint();
    }

    @Override
    public void onTroopMovement(TwoCountryEvent tce) {
        countryEvent = tce;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (String countryName : polygonList.keySet()) {
            if (polygonList.get(countryName).contains(e.getX(), e.getY())) {
                //TODO once gamestatus gets implemented in controller, then logic can be done
                /*
                if (Controller.getGameStatus() == GameStatusEnum.TROOP_PLACEMENT_PHASE){
                    selectAttackingCountry(countryName);
                } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_ATTACKING_COUNTRY_PHASE){
                    selectAttackingCountry(countryName);
                } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_DEFENDING_COUNTRY_PHASE){
                    selectDefendingCountry(countryName);
                } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_TROOP_MOVING_FROM_PHASE){
                    selectTroopMovingFrom(countryName);
                } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_TROOP_MOVING_TO_PHASE){
                    selectTroopMovingTo(countryName);
                }
                */

                System.out.println("Clicked in poly " + countryName);
            }
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
