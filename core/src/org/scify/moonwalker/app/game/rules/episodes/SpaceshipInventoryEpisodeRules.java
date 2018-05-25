package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.*;
import org.scify.moonwalker.app.game.MoonPhase;
import org.scify.moonwalker.app.ui.renderables.SpaceshipInventoryRenderable;

import java.util.ArrayList;

import static org.scify.moonwalker.app.game.rules.episodes.CockpitEpisodeRules.MAX_INVENTORY_ITEMS;

public class SpaceshipInventoryEpisodeRules extends FadingEpisodeRules<SpaceshipInventoryRenderable> {

    protected static final String RENDERABLE_ID = "spaceship_inventory";
    protected boolean outroInitiated;
    protected boolean introComplete;
    protected boolean exitButtonVisibilityNotHandled;

    public SpaceshipInventoryEpisodeRules() {
        super();
        renderable = null;
        outroInitiated = false;
        introComplete = false;
        exitButtonVisibilityNotHandled = true;
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new SpaceshipInventoryRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, gameInfo.getInventoryItemsCounter());
            renderable.setDistancePerUnitValue(gameInfo.getMotorEfficiency() + " Km/Unit");
            MoonPhase moonPhase = gameInfo.getCurrentMoonPhase();
            renderable.setMoonPhase(moonPhase);
            renderable.setUnitsValue(gameInfo.getUnitsOfMoonPhase(moonPhase) + " Units");
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
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (introComplete && gameInfo.isInventoryIncreased()) {
            int inventoryItemsCounter = gameInfo.increaseInventoryItemsCounter();
            updateGameInfo(inventoryItemsCounter);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.ADD_ITEM_AUDIO_PATH));
            renderable.addNextItem(inventoryItemsCounter, gameState);
            exitButtonVisibilityNotHandled = false;
            if (inventoryItemsCounter == 7) {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.MOON_TAKE_OFF_AUDIO_PATH));
                if (gameInfo.isGameFullySuccessfullyCompleted())
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.FULL_SUCCESS_BG_AUDIO_PATH));
                else
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.SIMPLE_SUCCESS_BG_AUDIO_PATH));
            }
        }else if (introComplete && exitButtonVisibilityNotHandled) {
            exitButtonVisibilityNotHandled = false;
            renderable.showExitButton();
        }
        return super.getNextState(gameState, userAction);
    }

    protected void updateGameInfo (int inventoryItemsCounter) {
        switch (inventoryItemsCounter) {
            case 1:
                gameInfo.setMotorEfficiency(15);
                renderable.setNextDistancePerUnitValue("15 Km/Unit");
                break;
            case 2:
                gameInfo.setMotorEfficiency(20);
                renderable.setNextDistancePerUnitValue("20 Km/Unit");
                break;
            default:
                MoonPhase moonPhase = gameInfo.getCurrentMoonPhase();
                String newValue = gameInfo.getUnitsOfMoonPhase(moonPhase) + " Units";
                if (!renderable.getUnitsLabel().getLabel().equals(newValue)) {
                    renderable.setNextUnitsValue(newValue);
                }
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if (endStateFromParent != null)
            return endStateFromParent;
        else {
            String code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
            return new EpisodeEndState(code, cleanUpGameState(currentState));
        }
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.QUIT: {
                endEpisodeAndAddEventWithType(gameState, "");
                renderable.fadeOutAllExtraRenderables();
                if (gameInfo.isInventoryRequestFlag())
                    gameInfo.resetFlags();
                if (gameInfo.getInventoryItemsCounter() == MAX_INVENTORY_ITEMS) {
                    gameInfo.setLaunchRequestFlag();
                }
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.ADD_ITEM_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.UPGRADE_STATS_AUDIO_PATH));
                break;
            }
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }
}
