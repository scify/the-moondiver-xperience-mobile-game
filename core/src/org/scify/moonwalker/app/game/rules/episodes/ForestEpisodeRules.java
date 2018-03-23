package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.Renderable;

public class ForestEpisodeRules extends BaseEpisodeRules{

    protected final int ROCKET_IMG_WIDTH_PIXELS = 200;
    protected final int ROCKET_IMG_HEIGHT_PIXELS = 200;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/forest.jpg");
            createAndAddRocket(currentState);
            addPlayerAvatar(currentState);
        }
    }

    protected void createAndAddRocket(GameState currentState) {
        final float rocketWidth = appInfo.pixelsWithDensity(ROCKET_IMG_WIDTH_PIXELS);
        final float rocketHeight = appInfo.pixelsWithDensity(ROCKET_IMG_HEIGHT_PIXELS);
        org.scify.engine.renderables.Renderable renderable = new Renderable("image", "rocket");
        renderable.setWidth(rocketWidth);
        renderable.setHeight(rocketHeight);
        renderable.setxPos(rocketWidth);
        renderable.setyPos(appInfo.getScreenHeight() / 2f - rocketHeight);
        renderable.setImgPath("img/rocket.png");
        currentState.addRenderable(renderable);
    }

    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if(conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/forest.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if(isConversationFinished(gsCurrent))
            gsCurrent.addGameEvent(new GameEvent("CONVERSATION_FINISHED", null, this));

        return gsCurrent;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if(gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED"))
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }
}
