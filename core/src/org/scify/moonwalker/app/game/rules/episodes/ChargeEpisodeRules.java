package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorController;
import org.scify.moonwalker.app.ui.renderables.ChargeEpisodeRenderable;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class ChargeEpisodeRules extends TemporaryEpisodeRules {
    public static final String IMG_EPISODE_CHARGE_BG_PATH = "img/episode_charge/bg.png";
    public static final String CALCULATOR_BUTTON_ID = "calculator_button";

    ChargeEpisodeRenderable calculator;
    CalculatorController controller;

    public ChargeEpisodeRules() {
        this.controller = new CalculatorController();
    }

    @Override
    public void episodeStartedEvents(GameState gsCurrent) {
        if (!isEpisodeStarted(gsCurrent)) {
            super.episodeStartedEvents(gsCurrent);
            addEpisodeBackgroundImage(gsCurrent, IMG_EPISODE_CHARGE_BG_PATH);

            calculator =  new ChargeEpisodeRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), Renderable.ACTOR_EPISODE_CALCULATOR, CALCULATOR_BUTTON_ID,
                    IMG_EPISODE_CHARGE_BG_PATH);
            gsCurrent.addRenderables(calculator.getAllRenderables());
            gsCurrent.addRenderable(calculator);
        }
    }

//    protected void parseButtonClick(String buttonValue) {
//        // if operator clicked, parse the string to number and append to main Label
//        if(buttonValue.equals(CalculatorController.RESULT)) {
//            mainLabelRenderable.setText(calculator.parseResult());
//            calculator.resetCalculator();
//        }
//        else {
//            if (Arrays.asList(operatorIds).contains(buttonValue)) {
//                calculator.addOperator(buttonValue);
//            } else if (Arrays.asList(operationIds).contains(buttonValue)) {
//                calculator.setOperation(buttonValue);
//            }
//            updateMainLabel();
//        }
//    }
//
//    protected void updateMainLabel() {
//        String value = calculator.getCurrentΡrepresentation();
//        if(value.equals(""))
//            value = MAIN_LABEL_INITIAL_TEXT;
//        mainLabel.setText(value);
//    }

}
