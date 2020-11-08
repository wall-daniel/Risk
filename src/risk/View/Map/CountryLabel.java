package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;

public class CountryLabel extends JLabel {

    public CountryLabel(String text, Controller controller){
        setText(text);
        setForeground(MapColor.TEXT_COLOR.getColor());
        setBackground(MapColor.TRANSPARENT_COLOR.getColor());
        setOpaque(false);
        setFont(new Font("TimesRoman", Font.BOLD, 25));
        setVisible(true);
    }
}
