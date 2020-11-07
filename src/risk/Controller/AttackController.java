package risk.Controller;

import jdk.nashorn.internal.scripts.JO;
import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AttackController {

    private Random random;

    private GameModel gameModel;
    private JFrame window;

    private Country attackingCountry;
    private Country defendingCountry;
    private int attackingArmies = 0;

    public AttackController(GameModel gameModel, JFrame frame) {
        this.gameModel = gameModel;
        this.window = frame;
        this.random = new Random(System.currentTimeMillis());
    }

    public void resetController() {
        attackingCountry = null;
        defendingCountry = null;
    }

    /**
     * Starts the dice rolling phase.
     * This ends when either someone has won or the attacking player types end.
     */
    private void attack() {
        // Continue until either one player has no armies, that player loses
        while (true) {
            // Get attacking rolls
            Integer[] attackingDice = new Integer[Math.min(attackingArmies, 3)];
            for (int i = 0; i < attackingDice.length; i++) {
                attackingDice[i] = random.nextInt(6) + 1;
            }

            // Get defending rolls
            Integer[] defendingDice = new Integer[Math.min(defendingCountry.getArmies(), 2)];
            for (int i = 0; i < defendingDice.length; i++) {
                defendingDice[i] = random.nextInt(6) + 1;
            }

            // Print out the result
            Arrays.sort(attackingDice, Collections.reverseOrder());
            Arrays.sort(defendingDice, Collections.reverseOrder());

            // Find outcome of dice roll
            // Sort dice and then compare the highest dice of each player until
            for (int i = 0; i < attackingDice.length && i < defendingDice.length; i++) {
                if (attackingDice[i] > defendingDice[i]) {  // Then the attacker won this set
                    defendingCountry.removeArmies(1);
                } else {                                    // If attacker doesn't win, then the defender wins
                    attackingArmies--;
                }
            }



            String message = "Attacking player roled: " + Arrays.toString(attackingDice) +
                    "\nDefending player roled: " + Arrays.toString(defendingDice) + "" +
                    "\n\nAttacker has: " + attackingArmies + " armies\n" +
                    "Defender has: " + defendingCountry.getArmies() + " armies.\n";

            // Check if there is a winner
            if (attackingArmies < 1 || defendingCountry.getArmies() < 1) {
                JOptionPane.showMessageDialog(window, message);
                break;
            }

            int retreat = JOptionPane.showConfirmDialog(window, message +  "\nDo you want to continue?", "Roled dice", JOptionPane.YES_NO_OPTION);

            if (retreat != JOptionPane.YES_OPTION) break;

            gameModel.updateGame();
        }

        gameModel.updateGame();
        // Figuer out who won, or if it is a tie
        if (attackingArmies <= 0) {
            // If the defender won then remove the lost armies from the defender and don't add anything to what the
            JOptionPane.showMessageDialog(window, "The attacker lost.");
        } else if (defendingCountry.getArmies() <= 0) {
            JOptionPane.showMessageDialog(window, "The attacker won.");
            attackerWon();
        } else {
            // If it is a tie then move the armies back to where they are supposed to be.
            attackingCountry.addArmies(attackingArmies);

            JOptionPane.showMessageDialog(window, "It's a tie!.");
        }
    }

    /**
     * When the attacker wins then they can move 1-attacking_armies_left to the new country,
     * and make the country theirs.
     */
    private void attackerWon() {
        while (true) {
            try {
                int armies = Integer.parseInt(
                        JOptionPane.showInputDialog(
                                window,
                                "How many armies do you want to move?",
                                JOptionPane.INFORMATION_MESSAGE
                        )
                );

                // Move armies, has to be at least 1
                if (attackingArmies >= armies && armies > 0) {
                    defendingCountry.setPlayer(gameModel.getCurrentPlayer(), armies);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAttackingCountry(Country country) {
        if (gameModel.getCurrentPlayer() == country.getPlayer()) {

            // Get how many armies
            while (true) {
                try {
                    String response = JOptionPane.showInputDialog(
                            window,
                            "How many armies do you want to attack with?",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // If null then exited
                    if (response == null) {
                        return;
                    }

                    int armies = Integer.parseInt(response);
                    if (country.getArmies() > armies && armies > 0) {
                        this.attackingCountry = country;
                        System.out.println("Attacking from " + country);

                        attackingCountry.removeArmies(armies);
                        attackingArmies = armies;
                        gameModel.nextPhase();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setDefendingCountry(Country country) {
        if (gameModel.getCurrentPlayer() != country.getPlayer() && attackingCountry.isNeighbour(country)) {
            this.defendingCountry = country;
            System.out.println("Defending from " + country);

            // Attack country
            attack();
            resetController();
            gameModel.nextPhase();
        }
    }
}
