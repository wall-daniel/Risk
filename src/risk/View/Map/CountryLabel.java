package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;

public class CountryLabel extends JLabel {

    public CountryLabel(String text, int numArmies, Controller controller){
        if (numArmies != 0)
            setText(text + " : " + numArmies);
        else
            setText(text);
        setForeground(MapColor.TEXT_COLOR.getColor());
        setBackground(MapColor.TRANSPARENT_COLOR.getColor());
        setOpaque(false);
        setFont(new Font("TimesRoman", Font.BOLD, 11));
        setVisible(true);
    }

    public void updateLabel(String text, int armies) {
        setText(text + " : " + armies);
        repaint();
    }
}
