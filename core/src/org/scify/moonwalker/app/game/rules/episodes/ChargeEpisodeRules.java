package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.calculator.CalculatorController;
import org.scify.moonwalker.app.ui.renderables.ChargeEpisodeRenderable;
import java.util.ArrayList;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class ChargeEpisodeRules extends FadingEpisodeRules<ChargeEpisodeRenderable> {
    protected static final String RENDERABLE_ID = "charge";
    protected boolean outroInitiated;
    protected boolean introComplete;
    protected CalculatorController calculatorController;

    public ChargeEpisodeRules () {
        super();
        renderable = null;
        outroInitiated = false;
        introComplete = false;
        calculatorController = new CalculatorController();
        calculatorController.resetCalculator();
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new ChargeEpisodeRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            renderable.setDistanceFromDestinationLabel(gameInfo.getNextLocationDistance() + " Km");
            renderable.setDistancePerUnitLabel(gameInfo.getMotorEfficiency() + " Km/Unit");
            renderable.setEnergyLabel(gameInfo.getRemainingEnergy() + " Units");
            renderable.setCurrentMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()), gameInfo.getCurrentMoonPhase().getImgPath());
            renderable.setNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getNextMoonPhase()), gameInfo.getNextMoonPhase().getImgPath());
            renderable.setPostNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getPostNextMoonPhase()), gameInfo.getPostNextMoonPhase().getImgPath());
            renderable.setCalculatorLabel(calculatorController.getCurrentΡrepresentation());
            renderable.setZIndex(0);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    introComplete = true;
                }
            });
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            super.episodeStartedEvents(gameState);
        }
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CHARGE_SPACESHIP_PASS_DAY: {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.POWER_UP_AUDIO_PATH));
                gameInfo.addEnergy(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()));
                gameInfo.dayPassed();
                renderable.setCurrentMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()), gameInfo.getCurrentMoonPhase().getImgPath());
                renderable.setNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getNextMoonPhase()), gameInfo.getNextMoonPhase().getImgPath());
                renderable.setPostNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getPostNextMoonPhase()), gameInfo.getPostNextMoonPhase().getImgPath());
                renderable.setEnergyLabel(gameInfo.getRemainingEnergy() + " Units");
                break;
            }
            case UserActionCode.CALCULATOR_OPERATION: {
                String payload = (String)userAction.getActionPayload();
                String existingValue = renderable.getCalculatorValue();
                String newValue = calculatorController.calculate(payload);
                if (newValue.equals(existingValue)) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI,renderable.WRONG_BUTTON_AUDIO_PATH));
                }else {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI,renderable.CLICK_AUDIO_PATH));
                    renderable.setCalculatorLabel(newValue);
                }
                break;
            }
            case UserActionCode.QUIT: {
                if (gameInfo.getRemainingEnergy()  > 0)
                    gameInfo.setChargeRequestFlag(false);
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.POWER_UP_AUDIO_PATH));
                endEpisodeAndAddEventWithType(gameState, "");
                break;
            }
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, currentState);
    }

}
