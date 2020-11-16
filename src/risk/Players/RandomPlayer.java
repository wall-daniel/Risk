package risk.Players;

import risk.Action.*;
import risk.Enums.PlayerType;
import risk.Model.Country;
import risk.Model.GameModel;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPlayer extends Player {
    private Random random;

    public RandomPlayer(String name, int index, GameModel gameModel) {
        super(name, index, PlayerType.RANDOM_PLAYER, gameModel);

        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void startTurn(int continentBonus) {
        super.startTurn(continentBonus);

        // Reset some stored variables
    }

    @Override
    public Action getAction(){
        return new EndTurn();
    }

    /**
     * Find a random country to attack from and a random country to attack from attacking country.
     * Needs to ensure that the attacking country has to have at least 2 army.
     *
     * @return an attack action.
     */
    private Attack getAttackCommand() {
        // Find a country with more than one army and that has a neighbour that isn't owned by player
        Country attackingCountry;
        Country defendingCountry;
        do {
            attackingCountry = gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size())));
            defendingCountry = gameModel.getCountry(attackingCountry.getNeighbours().get(random.nextInt(countriesOwned.size())));
        } while (attackingCountry.getArmies() > 1 && defendingCountry.getPlayer() != this);

        // Attack with all armies, could change this to a random number
        return new Attack(attackingCountry.getArmies() - 1, attackingCountry, defendingCountry);
    }

    /**
     * Choose a random country to place armies in.
     * Also chooses a random number of armies to place in the country
     * between 1 and getPlaceableArmies().
     *
     * @return a placement action
     */
    private Place getPlaceCommand() {
        return new Place(
                random.nextInt(getPlaceableArmies()) + 1,
                gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size())))
        );
    }

    /**
     * Choose two countries to move armies from and to.
     * Make sure that the countries have a path between them.
     * The number of armies is between 1 and first country armies minus 1.
     *
     * @return a movement action
     */
    private Move getMoveCommand() {
        Country firstCountry;
        Country secondCountry;

        // TODO: Make the country not just neighbour, but have a path between the two.
        do {
            firstCountry = gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size())));
            secondCountry = gameModel.getCountry(countriesOwned.get(random.nextInt(countriesOwned.size())));
        } while (firstCountry != secondCountry && firstCountry.isNeighbour(secondCountry));

        return new Move(random.nextInt(firstCountry.getArmies() - 1) + 1, firstCountry, secondCountry);
    }
}
