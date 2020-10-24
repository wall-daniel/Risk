package risk;

import risk.Enums.CountryEnum;
import risk.Players.Player;

import javax.sound.midi.SysexMessage;
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

    public void startAttackSequence() {
        getAttackingCountry();
    }

    public void getAttackingCountry() {
        while (true) {
            System.out.println("Your countries are: " + attackingPlayer.getCountriesAsStringWithArmies());
            String attackingCountry = parser.getInput("What country do you want to attack from(End)?:").toUpperCase();

            // If the player types in 'end' then stop the attacking
            if (attackingCountry.equals("END")) {
                return;
            }

            CountryEnum countryEnum = CountryEnum.getEnumFromString(attackingCountry);
            if (countryEnum == null) {
                System.out.println("That is not a country. Try again.");
            } else if (attackingPlayer.ownsCountry(countryEnum)) {
                this.attackingCountry = countryEnum.country;

                if (this.attackingCountry.getArmies() > 1) {
                    getDefendingCountry();
                    return;
                } else {
                    System.out.println("You don't have enough armies to attack from " + countryEnum);
                }
            } else {
                System.out.println("You do not own that country, try again.");
            }
        }
    }

    public void getDefendingCountry() {
        while (true) {
            System.out.println("The countries you can attack are: " + getAttackableCountries());
            String defendingCountry = parser.getInput("What country do you want to attack (End)?:").toUpperCase();

            // If the player types in 'end' then stop the attacking
            if (defendingCountry.equals("END")) {
                return;
            }

            CountryEnum countryEnum = CountryEnum.getEnumFromString(defendingCountry);
            if (countryEnum == null) {
                System.out.println("That is not a country. Try again.");
            } else if (attackingPlayer.ownsCountry(countryEnum)) {
                System.out.println("You cannot attack your own country silly");
            } else {
                this.defendingCountry = countryEnum.country;
                this.defendingPlayer = this.defendingCountry.getPlayer();
                getAttackingArmies();
                return;
            }
        }
    }

    /**
     * Get how many armies user wants to attack with.
     * They have to attack with at least 1 and can attack with up to 1 less than armies in the country.
     */
    private void getAttackingArmies() {
        while (true) {
            attackingArmies = parser.getInt("How many armies do you want to attack with? (0-" + (attackingCountry.getArmies() - 1) + ", End=-1)?:");

            // If the user enters -1 then return
            if (attackingArmies == -1) {
                return;
            }

            if (attackingArmies > 0 && attackingArmies < attackingCountry.getArmies()) {
                attackingCountry.removeArmies(attackingArmies);
                attack();
                break;
            } else {
                System.out.println("You can't enter that many armies.");
            }
        }
    }

    private void attack() {
        int defendingArmies = defendingCountry.getArmies();

        // Continue until either one player has no armies, that player loses
        while (attackingArmies > 0 && defendingArmies > 0) {
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

            // Print out the result
            System.out.println("The attacker now has " + attackingArmies + " armies, and the defender now has " + defendingArmies + " armies left.");

            // Wait 1.5 seconds to simulate suspense
            try {
                Thread.sleep(1500L);
            } catch (Exception ignored) {}
        }

        if (attackingArmies <= 0) {
            defendingCountry.removeArmies(defendingCountry.getArmies() - defendingArmies);

            System.out.println(attackingPlayer.getName() + " lost.");
        } else {
            System.out.println(attackingPlayer.getName() + " won, " + defendingCountry.getName() + " is now your country.");
            attackerWon();
        }
    }

    private void attackerWon() {
        while (true) {
            int moveArmies = parser.getInt("How many armies do you want to move (1-" + attackingArmies + ")?: ");

            if (moveArmies < 1 || moveArmies > attackingArmies) {
                System.out.println("You cannot move that many armies, try again.");
            } else {
                attackingCountry.addArmies(attackingArmies - moveArmies);
                defendingCountry.setPlayer(attackingPlayer, moveArmies);
                break;
            }
        }
    }

    public String getAttackableCountries() {
        StringBuilder sb = new StringBuilder();

        for (CountryEnum countryEnum : attackingCountry.getName().getNeighbours()) {
            if (countryEnum.country.getPlayer() != attackingPlayer) {
                sb.append(countryEnum).append(", ");
            }
        }

        return sb.toString();
    }
}
