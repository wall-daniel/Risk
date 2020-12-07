package risk.View.Map;

import risk.Controller.Controller;
import risk.Controller.PlayerController;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;

public class CountryPanel extends JPanel {
    protected Country country;

    protected CountryLabel countryLabel;

    protected Color color;


    public CountryPanel(Country country, Dimension dimension, Controller controller) {
        super();
        this.country = country;
        setLayout(null);
        setSize(dimension);
        setOpaque(false);
        setName(country.getName());
        if (country.getPlayer() == null)
            color = PlayerColor.getRandomPlayerColor();
        else
            color = country.getPlayer().getPlayerColor();


        addLabel(controller);

        addMouseListener(controller);
    }

    protected void addLabel(Controller controller) {
        countryLabel = new CountryLabel(country.getName(), country.getArmies(), controller);
        Point labelPoint = country.getLabelPoint();
        countryLabel.setBounds(labelPoint.x, labelPoint.y, 200, 30);
        add(countryLabel);
    }

    public void updateCountry(){
        color = country.getPlayer().getPlayerColor();
        countryLabel.updateLabel(country.getName(), country.getArmies());
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
        graphics2D.setColor(color);
        graphics2D.fillPolygon(translatedPolygon);
    }

    public void setColorClickable(){
        color = MapColor.CLICKABLE_COLOR.getColor();
    }

    public void resetColor(){
        if (country.getPlayer() == null)
            color = PlayerColor.getRandomPlayerColor();
        else
            color = country.getPlayer().getPlayerColor();
    }


}
