package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.GameStatusEnum;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Model.GameModel;
import risk.View.MapCreator.EditableCustomCountry;
import risk.View.MapCreator.MapEditorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

    public CustomCountry getCountryClicked(int x, int y){
        int highestLayer = -1;
        CustomCountry editableCustomCountry = null;

        for (Component component : MapEditorGUI.layeredPane.getComponents()){
            if (component instanceof EditableCustomCountry){
                EditableCustomCountry ecc = (EditableCustomCountry) component;
                int layer = MapEditorGUI.layeredPane.getLayer(ecc);
                int xLocation = ecc.getLocationOnScreen().x;
                int yLocation = ecc.getLocationOnScreen().y;

                if (layer > highestLayer && ecc.getCountryPolygon().contains(x - xLocation, y - yLocation)){
                    highestLayer = layer;
                    editableCustomCountry = ecc;
                }
            }
        }

        return editableCustomCountry;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof EditableCustomCountry))
            return;

        int x = e.getXOnScreen(), y = e.getYOnScreen();

        CustomCountry countryClicked;

        if (((CustomCountry) e.getSource()).isPointInPolygon(x, y))
            countryClicked = (CustomCountry) e.getSource();
        else
            countryClicked = getCountryClicked(x, y);

        if (countryClicked == null)
            return;

        String countryName = countryClicked.getName();


        if (Controller.getGameStatus() == GameStatusEnum.TROOP_PLACEMENT_PHASE){

        } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_ATTACKING_COUNTRY_PHASE){

        } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_DEFENDING_COUNTRY_PHASE){

        } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_TROOP_MOVING_FROM_PHASE){

        } else if (Controller.getGameStatus() == GameStatusEnum.SELECT_TROOP_MOVING_TO_PHASE){

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

    public Polygon getCountryPolygon(){
        return countryPolygon;
    }
}
