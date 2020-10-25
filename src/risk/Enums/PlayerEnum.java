package risk.Enums;

public enum PlayerEnum {
    PLAYER_1 (1), PLAYER_2(2), PLAYER_3 (3), PLAYER_4(4), PLAYER_5(5), PLAYER_6(6);

    private int playerID;

    PlayerEnum(int playerID){
        this.playerID = playerID;
    }

    public int getPlayerID(){
        return playerID;
    }

    public PlayerEnum getNextPlayer(int num){
        int nextPlayerID = this.playerID==num ? 1 : this.playerID+1;
        PlayerEnum nextPlayer = PLAYER_1;

        for (PlayerEnum playerEnum : PlayerEnum.values())
            if (playerEnum.getPlayerID() == nextPlayerID) {
                nextPlayer = playerEnum;
                break;
            }

        return nextPlayer;
    }


}
