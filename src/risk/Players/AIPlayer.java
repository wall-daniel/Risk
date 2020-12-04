package risk.Players;

import com.google.gson.JsonObject;
import risk.Action.*;
import risk.Enums.PlayerType;
import risk.Model.Continent;
import risk.Model.Country;
import risk.Model.GameModel;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {

    //CONSTANTS
    private static int LOW_ARMIES_THRESHOLD = 10;
    private static int HIGH_ARMIES_THRESHOLD = 40;

    //DEPLOYING WEIGHTS
    private static double COUNTRY_WILL_LIKELY_BE_CONQUERED = 3;                 // if the country has very few armies
    private static double COUNTRY_IS_ALREADY_POWERFUL = 4;                      // if the country has many armies, and should be reinforced
    private static double COUNTRY_IS_SAFE = 2;                                  // if the country has a similar number of armies to the enemy countries around it
    //more to be added if needed


    //ATTACKING WEIGHTS
    private static double LAST_COUNTRY_TO_CAPTURE_WEIGHT = 5;                   // if attacking this country would conquer the region
    private static double HIGH_ARMY_COUNT_WEIGHT = 4;                           // if the attacking country is very likely to win (has significantly more armies)
    private static double COUNTRY_BELONGS_TO_STRONG_PLAYER_WEIGHT = 2;          // if attacking this country could weaken a player
    private static double COUNTRY_BELONGS_TO_WEAK_PLAYER_WEIGHT = 3;            // if attacking this country could eliminate a player

    public AIPlayer(String name, int index, PlayerType playerType, GameModel gameModel) {
        super(name, index, playerType, gameModel);
    }

    public AIPlayer(GameModel gameModel, JsonObject state) {
        super(state, gameModel);
    }

    /**
     * used to set the num troops to move in actionBuilder if a country is won in an attack
     * @param msg
     * @param min
     * @param max
     */
    @Override
    public void inputTroopCount(String msg, int min, int max) {
        if (countryIsSafe(getActionBuilder().getFirstCountry())){
            setNumTroopsOfAction(max);
        } else {
            setNumTroopsOfAction(Math.floorDiv(max, 2) + 1);
        }

    }
    /**
     * Logic for ai to choose action depending on game phase
     * @return
     */
    @Override
    public Action getAction(){
        switch (gameModel.gameStatus){
            case TROOP_PLACEMENT_PHASE:
                return getBestDeployCommand().buildDeploy();
            case SELECT_ATTACKING_PHASE:
            case SELECT_DEFENDING_PHASE:
                Country c1 = getBestAttackingCountry();
                Country c2 = null;
                if (c1 != null) {
                    c2 = getBestDefendingCountry(c1);
                }
                if (c2 != null){
                    return new ActionBuilder(c1, c2, Math.min(c1.getArmies(), 3)).buildAttack();
                } else return getEndCommand().buildEnd();
            case SELECT_TROOP_MOVING_FROM_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                ActionBuilder fc = getBestFortifyCommand();
                if (fc != null) {
                    return fc.buildFortify();
                }
                else return getEndCommand().buildEnd();
        }

        return null;
    }


    /**
     * Checks through all the player's owned countries and determines which is the best to attack from.
     * @return the country to attack from.
     */
    private Country getBestAttackingCountry(){

        ArrayList<Country> validCountries = new ArrayList<>();
        for (String country : countriesOwned) {
            Country country1 = gameModel.getCountry(country);
            if (country1.getArmies() > 1) {
                validCountries.add(country1);
            }
        }
        if (validCountries.isEmpty()) {
            return null;
        }

        ArrayList<Country> bestCountries = new ArrayList<>();
        double bestScore = 0;

        for (Country c : validCountries){
            double score = 0;

            for (String neighbourName : c.getNeighbours()){
                Country neighbour = gameModel.getCountry(neighbourName);
                if (neighbour.getPlayer().equals(gameModel.getCurrentPlayer())){
                    continue;
                }

                int totalNeighbourArmies = neighbour.getPlayer().getTotalArmies();

                if (totalNeighbourArmies < LOW_ARMIES_THRESHOLD){           // This neighbour is close to being eliminated
                    score += COUNTRY_BELONGS_TO_WEAK_PLAYER_WEIGHT;
                }
                if (totalNeighbourArmies > HIGH_ARMIES_THRESHOLD) {         // This neighbour belongs to a strong player
                    score += COUNTRY_BELONGS_TO_STRONG_PLAYER_WEIGHT;
                }
                if (neighbour.getArmies() + 3 < c.getArmies()){
                    score += HIGH_ARMY_COUNT_WEIGHT;
                }
                if (lastCountryInContinent(neighbour.getPlayer(), neighbour.getContinent())){
                    score += LAST_COUNTRY_TO_CAPTURE_WEIGHT;
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
            return c;
        }
        return bestCountries.get(0);
    }

    private Country getBestDefendingCountry(Country attacker){
        Country weakest = null;
        for (String neighbour : attacker.getNeighbours()){
            Country n = gameModel.getCountry(neighbour);
            if (attacker.getPlayer() == n.getPlayer()){
                continue;
            }

            if (weakest == null){
                weakest = n;
            } else {
                if (n.getArmies() < weakest.getArmies()){
                    weakest = n;
                }
            }
        }
        return weakest;
    }

    private ActionBuilder getBestDeployCommand(){
        ArrayList<Country> bestCountries = new ArrayList<>();
        double bestScore = 0;
        for (String country : countriesOwned){
            Country c = gameModel.getCountry(country);
            double score = 0;

            boolean hasAlliedNeighbour = false;
            for (String neighbourName : c.getNeighbours()){
                Country neighbour = gameModel.getCountry(neighbourName);
                if (!hasAlliedNeighbour && neighbour.getPlayer().equals(gameModel.getCurrentPlayer())){
                    hasAlliedNeighbour = true;
                }
                if (neighbour.getArmies() > c.getArmies() + 3){             // This neighbour is dangerous
                    score += COUNTRY_WILL_LIKELY_BE_CONQUERED;
                } else if (neighbour.getArmies() < c.getArmies() - 3){      // This neighbour is a good target
                    score += COUNTRY_IS_ALREADY_POWERFUL;
                } else {                                                    // This neighbour is not currently a big threat
                    score += COUNTRY_IS_SAFE;
                }
            }
            if (!hasAlliedNeighbour){
                return new ActionBuilder(c, 1);
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

    private ActionBuilder getBestFortifyCommand(){

        Country firstCountry = null;
        Country secondCountry = null;

        for (String country : countriesOwned) {
            Country c = gameModel.getCountry(country);
            if (lastCountryInContinent(c.getPlayer(), c.getContinent())) { continue;}
            if (countryIsSafe(c)) {
                firstCountry = c;
            } else {
                return null;
            }
            int lowest = 1000;
            Country lowestNeighbour = null;
            for (String neighbour : c.getNeighbours()){
                Country n = gameModel.getCountry(neighbour);
                if (n.getArmies() < lowest){
                    lowest = n.getArmies();
                    lowestNeighbour = n;
                }
            }
            secondCountry = lowestNeighbour;
        }

        return new ActionBuilder(firstCountry, secondCountry, firstCountry.getArmies() - 1);
    }

    public boolean lastCountryInContinent(Player p, Continent continent){
        int count = 0;
        for (Country c : continent.getCountries()){
            if (c.getPlayer().equals(p)){
                count++;
                if (count >= 2){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean countryIsSafe(Country c){
        for (String neighbourString : c.getNeighbours()){
            Country neighbour = gameModel.getCountry(neighbourString);
            if (!neighbour.getPlayer().equals(c.getPlayer())){
                return false;
            }
        }
        return true;
    }

}
