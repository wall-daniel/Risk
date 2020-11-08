package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;

public class CountryPanel extends JPanel {
    protected Country country;

    protected CountryLabel countryLabel;


    public CountryPanel(Country country, Dimension dimension, Controller controller, boolean addLabel) {
        super();
        this.country = country;
        setLayout(null);
        setSize(dimension);
        setOpaque(false);
        setName(country.getName());

        if (addLabel) {
            countryLabel = new CountryLabel(country.getName(), controller);
            //Point labelPoint = country.getLabelPoint();
            // countryLabel.setBounds(labelPoint.x, labelPoint.y, 100, 20);
            countryLabel.setBounds((int) (Math.random() * 300), (int) (Math.random() * 300), 100, 20);

            add(countryLabel);
        }
        addMouseListener(controller);
    }


    public CountryLabel getCountryLabel(){
        return countryLabel;
    }

    public Country getCountry(){
        return country;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g.create();
        Polygon polygon = country.getPolygon();

        //draw border polygon
        graphics2D.setColor(MapColor.BORDER_COLOR.getColor());
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawPolygon(polygon);

        //fill polygon
        graphics2D.setColor(country.getPlayer().getPlayerColor());
        graphics2D.fillPolygon(polygon);

    }



}
