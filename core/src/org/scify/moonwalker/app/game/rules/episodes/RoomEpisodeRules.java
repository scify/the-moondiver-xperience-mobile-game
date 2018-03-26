package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable room;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/episode_0/bg.png");
            initialize(currentState);


            //messagesLabel = new Renderable(appInfo.getScreenWidth() - labelWidth - 20, appInfo.getScreenHeight() / 2f - 100, labelWidth, labelHeight, "label", "messagesLabel");
            //gsCurrent.addRenderable(messagesLabel);
           // gsCurrent.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
            //addPlayerAvatar(gsCurrent);
        }
    }

    protected void initialize(GameState currentState) {
        room = new RoomRenderable(0,0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "room", "room");
        room.setImgPath("img/episode_0/bg.png");
        currentState.addRenderable(room);
    }


    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if(conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_0.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if(isConversationFinished(gsCurrent))
            gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));

        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {

        }
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

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        return currentState;
    }
}
