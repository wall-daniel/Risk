package risk.Players;

import risk.Action.*;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {

    //CONSTANTS
    private static int LOW_ARMIES_THRESHOLD = 10;

    //DEPLOYING WEIGHTS
    private static double COUNTRY_WILL_LIKELY_BE_CONQUERED = 3;                 // if the country has very few armies
    private static double COUNTRY_IS_ALREADY_POWERFUL = 4;                      // if the country has many armies, and should be reinforced
    private static double COUNTRY_IS_SAFE = 2;                                  // if the country has a similar number of armies to the enemy countries around it
    //more to be added if needed


    //ATTACKING WEIGHTS
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
    private Action getBestAttackCommand(){

        ArrayList<Country> validCountries = new ArrayList<>();
        for (String country : countriesOwned) {
            Country country1 = gameModel.getCountry(country);
            if (country1.getArmies() > 1) {
                validCountries.add(country1);
            }
        }
        if (validCountries.isEmpty()) {
            return new End();
        }

        ArrayList<Country> bestCountries = new ArrayList<>();
        double bestScore = 0;

        for (Country c : validCountries){
            double score = 0;

            for (String neighbourName : c.getNeighbours()){
                Country neighbour = gameModel.getCountry(neighbourName);

                int totalNeighbourArmies = neighbour.getPlayer().getTotalArmies();

                if (totalNeighbourArmies < LOW_ARMIES_THRESHOLD){           // This neighbour is close to being eliminated
                    score += COUNTRY_BELONGS_TO_WEAK_PLAYER_WEIGHT;
                } else {

                }
            }

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
            Country c = bestCountries.get(r.nextInt(bestCountries.size()));
        }
        return null;
    }

    private ActionBuilder getBestDeployCommand(){
        ArrayList<Country> bestCountries = new ArrayList<>();
        double bestScore = 0;
        for (String country : countriesOwned){
            Country c = gameModel.getCountry(country);
            double score = 0;

            for (String neighbourName : c.getNeighbours()){
                Country neighbour = gameModel.getCountry(neighbourName);
                if (neighbour.getArmies() > c.getArmies() + 3){             // This neighbour is dangerous
                    score += COUNTRY_WILL_LIKELY_BE_CONQUERED;
                } else if (neighbour.getArmies() < c.getArmies() - 3){      // This neighbour is a good target
                    score += COUNTRY_IS_ALREADY_POWERFUL;
                } else {                                                    // This neighbour is not currently a big threat
                    score += COUNTRY_IS_SAFE;
                }
            }

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
            Country c = bestCountries.get(r.nextInt(bestCountries.size()));
            return new ActionBuilder(c, 1);
        }
        return new ActionBuilder(bestCountries.get(0), 1);
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
