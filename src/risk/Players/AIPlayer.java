package risk.Players;

import risk.Action.*;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {

    private static double REGION_WILL_LIKELY_BE_DIVIDED_WEIGHT = 0;             // if attacking this country would likely divide the region
    private static double REGION_WILL_LIKELY_BE_CONQUERED_WEIGHT = 0;           // if attacking this country would make it significantly easier to capture the region
    private static double LAST_COUNTRY_TO_CAPTURE_WEIGHT = 0;                   // if attacking this country would conquer the region
    private static double HIGH_ARMY_COUNT_WEIGHT = 0;                           // if the attacking country is very likely to win (has significantly more armies)
    private static double COUNTRY_BELONGS_TO_STRONG_PLAYER_WEIGHT = 0;          // if attacking this country could weaken a player
    private static double COUNTRY_BELONGS_TO_WEAK_PLAYER_WEIGHT = 0;            // if attacking this country could eliminate a player

    public AIPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }


    /**
     * used to set the num troops to move in actionBuilder if a country is won in an attack
     * @param msg
     * @param min
     * @param max
     */
    @Override
    public void inputTroopCount(String msg, int min, int max) {

    }

    /**
     * Checks through all the player's owned countries and determines which is the best to attack from.
     * @return the country to attack from.
     */
    private Country getBestAttackingCountry(){
        ArrayList<Country> bestCountries = new ArrayList<>();
        double bestScore = 0;
        for (String country : countriesOwned){
            double score = 0;
            Country c = gameModel.getCountry(country);

            if (score == bestScore){
                bestCountries.add(c);
            }
            if (score > bestScore){
                bestScore = score;
                bestCountries = new ArrayList<>();
                bestCountries.add(c);
            }
        }
        if (bestCountries.size() > 1){ // if multiple countries end up with the same score, randomly pick one
            Random r = new Random();
            return bestCountries.get(r.nextInt(bestCountries.size()));
        }
        return null;
    }

    /**
     * Logic for ai to choose action depending on game phase
     * @return
     */
    @Override
    public Action getAction(){
        return null;
    }
}
