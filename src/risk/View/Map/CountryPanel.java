package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;

public class CountryPanel extends JPanel {
    private Country country;

    //public boolean twicePainted = false;

    public CountryPanel(Country country, Dimension dimension, Controller controller) {
        super();
        this.country = country;
        setSize(dimension);
        setOpaque(false);
        addMouseListener(controller);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        Polygon polygon = country.getPolygon();

        System.out.println("draw polygon: " + country.getName());
        //draw border polygon
        graphics2D.setColor(MapColor.BORDER_COLOR.getColor());
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawPolygon(polygon);

        //fill polygon
        graphics2D.setColor(country.getPlayer().getPlayerColor());
        graphics2D.fillPolygon(polygon);

        System.out.println("draw label: " + country.getName());
        graphics2D.setColor(MapColor.TEXT_COLOR.getColor());
        graphics2D.setFont(new Font("TimesRoman", Font.BOLD, 25));
        graphics2D.drawString(
                country.getName() + ": " + country.getArmies(),
                (int) (polygon.getBounds().getWidth() / 2 + polygon.getBounds().getX()),
                (int) (polygon.getBounds().getHeight() / 2 + polygon.getBounds().getY())
        );
    }
}
