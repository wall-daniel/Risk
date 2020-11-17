package risk.Controller;

import risk.Action.Fortify;
import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.util.ArrayList;

public class FortifyController {

    private final GameModel gameModel;
    private Fortify fortify;

    public FortifyController(GameModel gameModel, Fortify fortify) {
        this.gameModel = gameModel;
        this.fortify = fortify;
    }

    public void initiateFortify(){
        fortify.getFirstCountry().removeArmies(fortify.getNumTroops());
        fortify.getSecondCountry().addArmies(fortify.getNumTroops());
    }
}
