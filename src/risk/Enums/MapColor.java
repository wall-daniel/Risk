package risk.Enums;


import java.awt.*;

public enum MapColor {
    BACKGROUND_COLOR(new Color(0,255,255, 255)),
    BORDER_COLOR(new Color(0,0,0, 255)),
    FILL_COLOR(new Color(255, 0, 0, 255));

    private Color color;

    MapColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

}
