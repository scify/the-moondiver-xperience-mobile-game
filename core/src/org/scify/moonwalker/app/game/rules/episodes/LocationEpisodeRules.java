package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.rules.QuestionConversationRules;
import org.scify.moonwalker.app.ui.renderables.LocationRenderable;

public class LocationEpisodeRules extends FadingEpisodeRules<LocationRenderable> {

    public static final String RENDERABLE_ID = "location";
    protected boolean outroInitiated;
    protected Location location;


    public LocationEpisodeRules (Location location) {
        super();
        renderable = null;
        outroInitiated = false;
        this.location = location;
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new LocationRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, location.getEpisodeBackgroundImagePath());
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderable(renderable);
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;

        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            if(conversationRules == null) {
                conversationRules = new QuestionConversationRules(location.getConversationPath(), location.getConversationBG(), location.getConversationSuccessFilePath(), location.getConversationFailureFilePath());
                conversationRules.setStarted(true);
            }
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gameState) {
        String code = "";
        // Handle failed conversation
        if (gameState.eventsQueueContainsEvent(conversationRules.CONVERSATION_FAILED))
            code = EpisodeEndStateCode.SCENARIO_NEEDS_RESTART;
        else if (gameState.eventsQueueContainsEvent(conversationRules.CONVERSATION_FINISHED))
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;

        conversationRules.cleanUpState(gameState);
        return new EpisodeEndState(code, cleanUpGameState(gameState));
    }


}
