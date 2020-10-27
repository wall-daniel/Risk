package risk.LevelCreator;


import java.awt.*;

public enum MapColor {
    BACKGROUND_COLOR(Color.CYAN), BORDER_COLOR(Color.BLACK);

    private Color color;

    MapColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

}
