package risk.Players;

import risk.Action.*;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {
    private Random random;
    private double attackThreshold = 0.4;
    private double fortifyThreshold = 0.8;

    public RandomPlayer(String name, int index, GameModel gameModel) {
        super(name, index, PlayerType.RANDOM_PLAYER, gameModel);

        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void startTurn(int continentBonus) {
        super.startTurn(continentBonus);
        attackThreshold = 0.4;
    }

    @Override
    public void inputTroopCount(String msg, int min, int max) {
        int val = random.nextInt(max) + 1;
        setNumTroopsOfAction(val);
    }


    public Action getAction(){
        switch (gameModel.gameStatus){
            case TROOP_PLACEMENT_PHASE:
                return getDeployCommand().buildDeploy();
            case SELECT_ATTACKING_PHASE:
            case SELECT_DEFENDING_PHASE:
                if (random.nextDouble() > attackThreshold) {
                    attackThreshold *= 1.2;
                    return getAttackCommand();//.buildAttack();
                } else {
                    return getEndCommand().buildEnd();
                }
            case SELECT_TROOP_MOVING_FROM_PHASE:
            case SELECT_TROOP_MOVING_TO_PHASE:
                if (random.nextDouble() > fortifyThreshold)
                    return getFortifyCommand().buildFortify();
                else
                    return getEndCommand().buildEnd();
        }

        return null;
    }

    /**
     * Choose a random country to place armies in.
     * Also chooses a random number of armies to place in the country
     * between 1 and getPlaceableArmies().
     *
     * @return a placement action
     */
    private ActionBuilder getDeployCommand() {
        return new ActionBuilder(
                gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size()))),
                random.nextInt(getPlaceableArmies()) + 1
        );
    }


    /**
     * Find a random country to attack from and a random country to attack from attacking country.
     * Needs to ensure that the attacking country has to have at least 2 army.
     *
     * @return an attack action.
     */
    private Action getAttackCommand() {
        // Start in random place in countries and add
        for (int i = random.nextInt(countriesOwned.size()); i < countriesOwned.size(); i++) {
            Country attackCountry = gameModel.getCountry(countriesOwned.get(i));

            if (attackCountry.getArmies() <= 1) {
                continue;
            }

            // Can shuffle neighbours with no effect on how game is run
            List<String> neighbours = attackCountry.getNeighbours();
            Collections.shuffle(neighbours, random);

            for (String neighbour : neighbours) {
                Country defendingCountry = gameModel.getCountry(neighbour);

                if (defendingCountry.getPlayer() != this) {
                    actionBuilder.setFirstCountry(attackCountry);
                    actionBuilder.setSecondCountry(defendingCountry);
                    actionBuilder.setNumTroops(Math.min(attackCountry.getArmies(), 3));
                    return actionBuilder.buildAttack();
                }
            }
        }

        return actionBuilder.buildEnd();
    }


    /**
     * Choose two countries to move armies from and to.
     * Make sure that the countries have a path between them.
     * The number of armies is between 1 and first country armies minus 1.
     *
     * @return a movement action
     */
    private ActionBuilder getFortifyCommand() {
        Country firstCountry;
        Country secondCountry;

        // TODO: Make the country not just neighbour, but have a path between the two.
        do {
            firstCountry = gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size())));
            secondCountry = gameModel.getCountry(firstCountry.getNeighbours().get(random.nextInt(firstCountry.getNeighbours().size())));
        } while (firstCountry.getArmies() == 1 || firstCountry.getPlayer().getIndex() != secondCountry.getPlayer().getIndex());


        return new ActionBuilder(firstCountry, secondCountry, random.nextInt(firstCountry.getArmies() - 1) + 1);
    }


    /**
     * Choose two countries to move armies from and to.
     * Make sure that the countries have a path between them.
     * The number of armies is between 1 and first country armies minus 1.
     *
     * @return a movement action
     */
    private ActionBuilder getFortifyCommand(Country firstCountry, Country secondCountry) {
        return new ActionBuilder(firstCountry, secondCountry, random.nextInt(firstCountry.getArmies() - 1) + 1);
    }

    /**
     * Choose two countries to move armies from and to.
     * Make sure that the countries have a path between them.
     * The number of armies is between 1 and first country armies minus 1.
     *
     * @return a movement action
     */
    private ActionBuilder getEndCommand() {
        return new ActionBuilder();
    }
}
