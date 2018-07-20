package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.renderables.CreditsRenderable;
import java.util.ArrayList;

public class CreditsEpisodeRules extends FadingEpisodeRules<CreditsRenderable> {

    public static final String RENDERABLE_ID = "credits";

    public CreditsEpisodeRules() {
        super();
    }

    @Override
    public void episodeStartedEvents(GameState gameState) {
        // If we are just starting
        if (!isEpisodeStarted(gameState)) {
            renderable = new CreditsRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            // Enable input after fade in
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.setInputEnabled(true);
                }
            });

            // Call parent starting process
            super.episodeStartedEvents(gameState);
            // Add all children
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
        }
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        if (renderable.isReadyForInput()) {
            switch (userAction.getActionCode()) {
                case UserActionCode.SCREEN_TOUCHED:
                    if (renderable.getAboutMode() == 1) {
                        renderable.showAbout2();
                    }else if (renderable.getAboutMode() == 2) {
                        renderable.setInputEnabled(false);
                        endEpisodeAndAddEventWithType(gameState, "");
                    }
                    break;
            }
        }
        super.handleUserAction(gameState, userAction);

    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        String code = EpisodeEndStateCode.SCENARIO_NEEDS_RESTART;
        return new EpisodeEndState(code, cleanUpGameState(gsCurrent));
    }
}
