package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.EpisodeEndState;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.ui.renderables.SpaceshipInventoryRenderable;

import java.util.ArrayList;

public class SpaceshipInventoryEpisodeRules extends FadingEpisodeRules<SpaceshipInventoryRenderable> {

    protected static final String RENDERABLE_ID = "spaceship_inventory";
    protected boolean outroInitiated;
    protected boolean introComplete;

    public SpaceshipInventoryEpisodeRules() {
        super();
        renderable = null;
        outroInitiated = false;
        introComplete = false;
    }

    @Override
    public void episodeStartedEvents(final GameState gameState) {
        if (!isEpisodeStarted(gameState)) {
            renderable = new SpaceshipInventoryRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, gameInfo.getInventoryItemsCounter());
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    introComplete = true;
                }
            });
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            gameState.addRenderable(renderable);
            super.episodeStartedEvents(gameState);
        }
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (introComplete && gameInfo.isInventoryIncreased()) {
            int inventoryItemsCounter = gameInfo.increaseInventoryItemsCounter();
            renderable.addNextItem(inventoryItemsCounter);
        }
        return super.getNextState(gameState, userAction);
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
            case EPISODE_BACK: {
                endEpisodeAndAddEventWithType(gameState, "");
                break;
            }
            default:
                super.handleUserAction(gameState, userAction);
                break;
        }
    }
}
