package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;
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
    protected int tutorialChatPhase;

    public ChargeEpisodeRules () {
        super();
        renderable = null;
        outroInitiated = false;
        introComplete = false;
        calculatorController = new CalculatorController();
        calculatorController.resetCalculator();
        tutorialChatPhase = 0;
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (gameInfo.isTutorialMode() && renderable != null && renderable.isChatEnabled()) {
            if (tutorialChatPhase == 0) {
                tutorialChatPhase = 1;
                createConversation(gameState, "conversations/episode_charge1.json", renderable.CONVERSATION_BG_IMG_PATH);
            }
        }
        if (conversationRules != null && conversationRules.isFinished() && tutorialChatPhase == 3) {
            tutorialChatPhase = 4;
            EffectSequence showEffect = new EffectSequence();
            showEffect.addEffect(new FadeEffect(1, 0 , 0));
            showEffect.addEffect(new VisibilityEffect(true));
            showEffect.addEffect(new FadeEffect(0, 1 , 1000));
            renderable.getExitButton().addEffect(showEffect);
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new ChargeEpisodeRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            int percentageTraveled = (int)((100 - gameInfo.getNextTravelPercentagePossible()) * gameInfo.getNextLocationDistance() / 100);
            renderable.setDistanceFromDestinationLabel(percentageTraveled + " Km");
            renderable.setDistancePerUnitLabel(gameInfo.getMotorEfficiency() + " Km/Unit");
            renderable.setEnergyLabel(gameInfo.getRemainingEnergy() + " Units");
            renderable.setCurrentMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()), gameInfo.getCurrentMoonPhase().getImgPath());
            renderable.setNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getNextMoonPhase()), gameInfo.getNextMoonPhase().getImgPath());
            renderable.setPostNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getPostNextMoonPhase()), gameInfo.getPostNextMoonPhase().getImgPath());
            renderable.setCalculatorLabel(calculatorController.getCurrentΡrepresentation());
            renderable.setZIndex(0);
            if (gameInfo.isTutorialMode())
                renderable.getExitButton().setVisible(false);
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    introComplete = true;
                    renderable.enableChat();
                }
            });
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            super.episodeStartedEvents(gameState);
        }
    }

    protected void chargeOperation (GameState gameState) {
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.POWER_UP_AUDIO_PATH));
        gameInfo.addEnergy(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()));
        gameInfo.dayPassed();
        renderable.setCurrentMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getCurrentMoonPhase()), gameInfo.getCurrentMoonPhase().getImgPath());
        renderable.setNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getNextMoonPhase()), gameInfo.getNextMoonPhase().getImgPath());
        renderable.setPostNextMoonPhaseInfo(gameInfo.getUnitsOfMoonPhase(gameInfo.getPostNextMoonPhase()), gameInfo.getPostNextMoonPhase().getImgPath());
        renderable.setEnergyLabel(gameInfo.getRemainingEnergy() + " Units");
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CHARGE_SPACESHIP_PASS_DAY: {
                if (gameInfo.isTutorialMode()) {
                    if (tutorialChatPhase == 1) {
                        chargeOperation(gameState);
                        conversationRules = null;
                        tutorialChatPhase = 2;
                        createConversation(gameState, "conversations/episode_charge2.json", renderable.CONVERSATION_BG_IMG_PATH);
                    }else if (tutorialChatPhase == 2) {
                        chargeOperation(gameState);
                        conversationRules = null;
                        tutorialChatPhase = 3;
                        createConversation(gameState, "conversations/episode_charge3.json", renderable.CONVERSATION_BG_IMG_PATH);
                    }
                }else
                    chargeOperation(gameState);
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
