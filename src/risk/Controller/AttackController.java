package risk.Controller;

import risk.Model.Country;
import risk.Model.Countries;
import risk.Parser;
import risk.Players.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AttackController {

    private Random random;
    private Parser parser;

    private Player attackingPlayer;
    private Player defendingPlayer;
    private Country attackingCountry;
    private Country defendingCountry;
    private int attackingArmies = 0;

    public AttackController(Player player, Parser parser, Random random) {
        this.attackingPlayer = player;
        this.parser = parser;
        this.random = random;
    }

    /**
     * Starts the sequence for a player to attack another country
     *
     * @return returns true if the defending player loses last country.
     */
    public boolean startAttackSequence() {
        return getAttackingCountry();
    }

    /**
     * Gets the country the attacker wants to attack from.
     * Includes error checking and all that.
     * Player can end this by typing end.
     */
    private boolean getAttackingCountry() {
        while (true) {
            System.out.println("Your countries are: \n" + attackingPlayer.getCountriesAsStringWithArmies());
            String attackingCountry = parser.getInput("What country do you want to attack from(End)?:");

            // If the player types in 'end' then stop the attacking
            if (attackingCountry.equalsIgnoreCase("END")) {
                return false;
            }



            if (Countries.countryExists(attackingCountry)) {
                System.out.println("That is not a country. Try again.");
            } else if (attackingPlayer.ownsCountry(attackingCountry)){
                this.attackingCountry = Countries.getCountry(attackingCountry);

                if (this.attackingCountry.getArmies() > 1) {
                    return getDefendingCountry();
                } else {
                    System.out.println("You don't have enough armies to attack from " + attackingCountry);
                }
            } else {
                System.out.println("You do not own that country, try again.");
            }
        }
    }

    /**
     * Gets the country that is being attacked.
     * Cannot be there own country and has to be a country near by.
     * Player can end this by typing end.
     */
    private boolean getDefendingCountry() {
        while (true) {
            System.out.println("The countries you can attack are: " + getAttackableCountries());
            String defendingCountry = parser.getInput("What country do you want to attack (End)?:");

            // If the player types in 'end' then stop the attacking
            if (defendingCountry.equalsIgnoreCase("END")) {
                return false;
            }

            if (Countries.countryExists(defendingCountry)) {
                System.out.println("That is not a country. Try again.");
            } else if (attackingPlayer.ownsCountry(defendingCountry)) {
                System.out.println("You cannot attack your own country silly");
            } else {
                this.defendingCountry = Countries.getCountry(defendingCountry);
                this.defendingPlayer = this.defendingCountry.getPlayer();
                return getAttackingArmies();
            }
        }
    }

    /**
     * Get how many armies user wants to attack with.
     * They have to attack with at least 1 and can attack with up to 1 less than armies in the country.
     */
    private boolean getAttackingArmies() {
        while (true) {
            attackingArmies = parser.getInt("How many armies do you want to attack with? (0-" + (attackingCountry.getArmies() - 1) + ", End=-1)?:", 0, attackingCountry.getArmies() - 1);

            // If the user enters -1 then return
            if (attackingArmies == -1) {
                return false;
            }

            // The user has to enter a number between 1 and 1 less than the armies they have.
            if (attackingArmies > 0 && attackingArmies < attackingCountry.getArmies()) {
                attackingCountry.removeArmies(attackingArmies);
                return attack();
            } else {
                System.out.println("You can't enter that many armies.");
            }
        }
    }

    /**
     * Starts the dice rolling phase.
     * This ends when either someone has won or the attacking player types end.
     */
    private boolean attack() {
        int defendingArmies = defendingCountry.getArmies();

        // Continue until either one player has no armies, that player loses
        while (true) {
            // Get attacking rolls
            Integer[] attackingDice = new Integer[Math.min(attackingArmies, 3)];
            for (int i = 0; i < attackingDice.length; i++) {
                attackingDice[i] = random.nextInt(6) + 1;
            }

            // Get defending rolls
            Integer[] defendingDice = new Integer[Math.min(defendingArmies, 2)];
            for (int i = 0; i < defendingDice.length; i++) {
                defendingDice[i] = random.nextInt(6) + 1;
            }

            // Print out the result
            Arrays.sort(attackingDice, Collections.reverseOrder());
            Arrays.sort(defendingDice, Collections.reverseOrder());
            System.out.println(attackingPlayer.getName() + " roled: " + Arrays.toString(attackingDice));
            System.out.println(defendingPlayer.getName() + " roled: " + Arrays.toString(defendingDice));

            // Find outcome of dice roll
            // Sort dice and then compare the highest dice of each player until
            for (int i = 0; i < attackingDice.length && i < defendingDice.length; i++) {
                if (attackingDice[i] > defendingDice[i]) {  // Then the attacker won this set
                    defendingArmies--;
                } else {                                    // If attacker doesn't win, then the defender wins
                    attackingArmies--;
                }
            }

            // Check if there is a winner
            if (attackingArmies < 1 || defendingArmies < 1) {
                break;
            }

            // Print out the result
            System.out.println("The attacker now has " + attackingArmies + " armies, and the defender now has " + defendingArmies + " armies left.");

            // Ask player if they want to continue the attack
            String input = parser.getInput("Do you want to continue attacking? Type end if you don't: ");
            if (input.equalsIgnoreCase("end")) {
                break;
            }
        }

        // Figuer out who won, or if it is a tie
        if (attackingArmies <= 0) {
            // If the defender won then remove the lost armies from the defender and don't add anything to what the
            defendingCountry.removeArmies(defendingCountry.getArmies() - defendingArmies);

            System.out.println(attackingPlayer.getName() + " lost.");
        } else if (defendingArmies <= 0) {
            // If attacker wins then...
            System.out.println(attackingPlayer.getName() + " won, " + defendingCountry.getName() + " is now your country.");
            return attackerWon();
        } else {
            // If it is a tie then move the armies back to where they are supposed to be.
            defendingCountry.removeArmies(defendingCountry.getArmies() - defendingArmies);
            attackingCountry.addArmies(attackingArmies);

            System.out.println("Nobody won");
        }

        return false;
    }

    /**
     * When the attacker wins then they can move 1-attacking_armies_left to the new country,
     * and make the country theirs.
     */
    private boolean attackerWon() {
        while (true) {
            int moveArmies = parser.getInt("How many armies do you want to move (1-" + attackingArmies + ")?: ", 1, attackingArmies);

            if (moveArmies < 1 || moveArmies > attackingArmies) {
                System.out.println("You cannot move that many armies, try again.");
            } else {
                attackingCountry.addArmies(attackingArmies - moveArmies);
                defendingCountry.setPlayer(attackingPlayer, moveArmies);

                // Check if the player has lost, e.g. has no more countries.
                if (defendingPlayer.getCountries().isEmpty()) {
                    defendingPlayer.setLost();

                    // Check if game has been won.
                    return true;
                }

                return false;
            }
        }
    }

    public String getAttackableCountries() {
        StringBuilder sb = new StringBuilder();

//        for (String countryName : attackingCountry.getNeighbours()) {
//            if (Countries.getCountry(countryName).getPlayer() != attackingPlayer) {
//                sb.append(countryName).append(", ");
//            }
//        }

        return sb.toString();
    }
}
