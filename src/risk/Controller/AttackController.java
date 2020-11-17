package risk.Controller;

//import jdk.nashorn.internal.scripts.JO;
import risk.Action.Attack;
import risk.Action.Deploy;
import risk.Action.Fortify;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.Players.Player;
import risk.View.Main.MainGUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Handles the logic of an attack
 */
public class AttackController {

    private final Random random;
    private final GameModel gameModel;
    private Attack attack;

    public AttackController(GameModel gameModel, Attack attack){
        this.gameModel = gameModel;
        this.random = new Random(System.currentTimeMillis());
        this.attack = attack;
    }

    public void initiateAttack(){
        Country attackingCountry = attack.getAttackingCountry();
        Country defendingCountry = attack.getDefendingCountry();
        int attackingArmies = attack.getAttackingArmies();

        Integer[] attackingDice = new Integer[Math.min(attackingArmies, 3)];
        for (int i = 0; i < attackingDice.length; i++) {
            attackingDice[i] = random.nextInt(6) + 1;
        }

        // Get defending rolls
        Integer[] defendingDice = new Integer[Math.min(defendingCountry.getArmies(), 2)];
        for (int i = 0; i < defendingDice.length; i++) {
            defendingDice[i] = random.nextInt(6) + 1;
        }

        Arrays.sort(attackingDice, Collections.reverseOrder());
        Arrays.sort(defendingDice, Collections.reverseOrder());

        // Find outcome of dice roll
        // Sort dice and then compare the highest dice of each player until
        for (int i = 0; i < attackingDice.length && i < defendingDice.length; i++) {
            if (attackingDice[i] > defendingDice[i]) {  // Then the attacker won this set
                defendingCountry.removeArmies(1);
            } else {                                    // If attacker doesn't win, then the defender wins
                attackingCountry.removeArmies(1);
            }
        }

        String message = "Attacking player roled: " + Arrays.toString(attackingDice) +
                "\nDefending player roled: " + Arrays.toString(defendingDice) + "" +
                "\n\nAttacker has: " + attackingArmies + " armies\n" +
                "Defender has: " + defendingCountry.getArmies() + " armies.\n";
        JOptionPane.showMessageDialog(null, message);

        if (defendingCountry.getArmies() <= 0)
            handleAttackerWon();

        gameModel.resetPhase();
        gameModel.updateGame();
    }

    /**
     * When the attacker wins then they can move 1-attacking_armies_left to the new country,
     * and make the country theirs.
     */
    private void handleAttackerWon() {
        Country attackingCountry = attack.getAttackingCountry();
        Country defendingCountry = attack.getDefendingCountry();
        Player player = attackingCountry.getPlayer();


        //handle country change
        defendingCountry.getPlayer().removeCountry(defendingCountry.getName());
        defendingCountry.setPlayer(player);
        player.addCountry(defendingCountry.getName());

        //create action to handle troop transfer from attacking to defending
        player.setFirstCountryOfAction(attackingCountry);
        player.setSecondCountryOfAction(defendingCountry);
        player.inputTroopCount("How many troops do you want to move?", 1, attackingCountry.getArmies() - 1);

        new FortifyController(gameModel, player.getActionBuilder().buildFortify()).initiateFortify();

        System.out.println(player + " conquers " + defendingCountry.getName());
    }
}
