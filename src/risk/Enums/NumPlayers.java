package risk.Enums;


public enum NumPlayers {
    TWO_PLAYERS(50, 2),
    THREE_PLAYERS(35, 3),
    FOUR_PLAYERS(30,4 ),
    FIVE_PLAYERS(25, 5),
    SIX_PLAYERS(20, 6);

    int initialArmySize;
    int numPlayers;

    NumPlayers(int initialArmySize, int numPlayers){
        this.initialArmySize = initialArmySize;
        this.numPlayers = numPlayers;
    }

    public int getInitialArmySize(){
        return initialArmySize;
    }


}
