package risk.Enums;


import java.awt.*;
import java.util.Random;

public enum PlayerColor  {
    PLAYER_1_COLOR(new Color(255, 0, 0, 255)),
    PLAYER_2_COLOR(new Color(0, 0, 255, 255)),
    PLAYER_3_COLOR(new Color(0, 255, 0, 255)),
    PLAYER_4_COLOR(new Color(255, 255, 0, 255)),
    PLAYER_5_COLOR(new Color(255, 0, 255, 255)),
    PLAYER_6_COLOR(new Color(0, 255, 255, 255));


    private Color color;

    PlayerColor(Color color){
        this.color = color;
    }

    public static Color getRandomPlayerColor(){
        Random random = new Random();
        return values()[random.nextInt(values().length)].getColor();
    }

    public static Color getPlayerColor(int num){
        for (PlayerColor playerColor : PlayerColor.values())
            if (playerColor.ordinal() == num - 1)
                return playerColor.color;

        return PlayerColor.PLAYER_1_COLOR.color;
    }


    public Color getColor(){
        return color;
    }


}
