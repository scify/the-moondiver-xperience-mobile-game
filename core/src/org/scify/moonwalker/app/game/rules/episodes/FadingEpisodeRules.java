package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;

public class FadingEpisodeRules<T extends FadingTableRenderable> extends BaseEpisodeRules {
    protected T renderable;

    public FadingEpisodeRules () {
        super();
        renderable = null;
    }

    public void setRenderable(T mainRenderable) {
        this.renderable = mainRenderable;
    }

    @Override
    protected void episodeStartedEvents(GameState currentState) {
        // If first time started
        if (!episodeStarted) {
            // Add to current state
            currentState.addRenderable(renderable);
            // Apply fade in
            renderable.fadeIn();

        }
        super.episodeStartedEvents(currentState);

    }

    // Usable in the episode ended events function
    private void superEpisodeEndedEvents(GameState currentState) {
        super.episodeEndedEvents(currentState);
    }

    @Override
    protected void episodeEndedEvents(final GameState currentState) {
        // Make sure we end the episode AFTER the fade

        renderable.addAfterFadeOut(new Runnable() {
            @Override
            public void run() {
                superEpisodeEndedEvents(currentState);
            }
        });

        // Perform fade out
        renderable.fadeOut();
    }
}
