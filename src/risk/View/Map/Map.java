package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Listener.Listeners.GameActionListener;
import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class Map extends JPanel implements GameActionListener, MouseListener {
    Controller controller;

    HashMap<String, Polygon> polygonList = new HashMap<>();

    OneCountryEvent countryEvent = null;

    public Map(Controller controller){
        this.controller = controller;
        setLayout(null);
        setBackground(MapColor.BACKGROUND_COLOR.getColor());
        this.controller.addAsGameActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (countryEvent!=null){
            Graphics2D g2d = (Graphics2D) g.create();

            Country country = countryEvent.getFirstCountry();
            Polygon polygon = country.getPolygon();

            int midX = (int) (polygon.getBounds().getWidth()/2 +  polygon.getBounds().getX());
            int midY = (int) (polygon.getBounds().getHeight()/2 +  polygon.getBounds().getX());

            drawPolygon(g2d, polygon, country.getPlayer().getPlayerColor().getColor(), country.getName(), midX, midY, country.getArmies()); //demeter? i dont even know her
            if (countryEvent instanceof TwoCountryEvent) {
                country = ((TwoCountryEvent) countryEvent).getSecondCountry();
                polygon = country.getPolygon();

                midX = (int) (polygon.getBounds().getWidth() / 2 + polygon.getBounds().getX());
                midY = (int) (polygon.getBounds().getHeight() / 2 + polygon.getBounds().getX());

                drawPolygon(g2d, polygon, country.getPlayer().getPlayerColor().getColor(), country.getName(), midX, midY, country.getArmies());
            }
        }
    }

    public void drawPolygon(Graphics2D g2d, Polygon polygon, Color color, String countryName, int x, int y, int numArmies){
        //drawing polygon
        g2d.setColor(MapColor.BORDER_COLOR.getColor());
        g2d.setStroke(new BasicStroke(10));
        g2d.drawPolygon(polygon);

        //filling polygon
        g2d.setColor(color);
        g2d.fillPolygon(polygon);

        //adding countryname and armycount
        g2d.setColor(MapColor.TEXT_COLOR.getColor());
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 25));
        g2d.drawString(countryName + ": " + numArmies, x, y);

        /* //TODO will remove
        JLabel countryNameLabel = new JLabel(countryName);
        countryNameLabel.setBackground(MapColor.TRANSPARENT_COLOR.getColor());
        countryNameLabel.setForeground(MapColor.TEXT_COLOR.getColor());
        countryNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 25));
        countryNameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        countryNameLabel.setOpaque(false);
        countryNameLabel.setLocation(x, y);

        add(countryNameLabel, Integer.MAX_VALUE);
         */
    }



    @Override
    public void onPlaceTroops(OneCountryEvent oce) {
        countryEvent = oce;
        polygonList.put(oce.getFirstCountry().getName(), oce.getFirstCountry().getPolygon());
        repaint();
    }

    @Override
    public void onAttack(TwoCountryEvent tce) {
        countryEvent = tce;
        polygonList.put(tce.getFirstCountry().getName(), tce.getFirstCountry().getPolygon());
        polygonList.put(tce.getSecondCountry().getName(), tce.getSecondCountry().getPolygon());
        repaint();
    }

    @Override
    public void onTroopMovement(TwoCountryEvent tce) {
        countryEvent = tce;
        polygonList.put(tce.getFirstCountry().getName(), tce.getFirstCountry().getPolygon());
        polygonList.put(tce.getSecondCountry().getName(), tce.getSecondCountry().getPolygon());
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
