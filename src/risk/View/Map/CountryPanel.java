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
            countryLabel = new CountryLabel(country.getName(), country.getArmies(), controller);
            Point labelPoint = country.getLabelPoint();
            countryLabel.setBounds(labelPoint.x, labelPoint.y, 200, 30);
            add(countryLabel);
        }
        addMouseListener(controller);
    }

    public void updateCountryLabel(){
        countryLabel.updateArmies(country.getName(), country.getArmies());
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
        Polygon translatedPolygon = new Polygon(country.getPolygon().xpoints, country.getPolygon().ypoints, country.getPolygon().npoints);
        translatedPolygon.translate(country.getPolygonPoint().x, country.getPolygonPoint().y);

        //draw border polygon
        graphics2D.setColor(MapColor.BORDER_COLOR.getColor());
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawPolygon(translatedPolygon);

        //fill polygon
        graphics2D.setColor(country.getPlayer().getPlayerColor());
        graphics2D.fillPolygon(translatedPolygon);

    }



}
