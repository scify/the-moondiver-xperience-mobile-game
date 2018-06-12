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
        }else if (introComplete && exitButtonVisibilityNotHandled) {
            exitButtonVisibilityNotHandled = false;
            renderable.showExitButton();
        }
        return super.getNextState(gameState, userAction);
    }

    protected void updateGameInfo (int inventoryItemsCounter) {
        switch (inventoryItemsCounter) {
            case 1:
                renderable.setNextDistancePerUnitValue(gameInfo.getMotorEfficiency() + " Km/Unit");
                break;
            case 2:
                renderable.setNextDistancePerUnitValue(gameInfo.getMotorEfficiency() + " Km/Unit");
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
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                endEpisodeAndAddEventWithType(gameState, "");
                renderable.fadeOutAllExtraRenderables();
                if (gameInfo.isInventoryRequestFlag())
                    gameInfo.resetFlags();
                if (gameInfo.getInventoryItemsCounter() == MAX_INVENTORY_ITEMS) {
                    gameInfo.setLaunchRequestFlag();
                }
                break;
            }
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }
}
