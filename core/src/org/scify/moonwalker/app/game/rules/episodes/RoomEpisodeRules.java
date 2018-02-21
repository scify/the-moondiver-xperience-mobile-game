package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;

import java.util.HashMap;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected Renderable messagesLabel;
    protected String mainLabelText = "Επεισόδιο 0: Το δωμάτιο.";

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this)) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/episode_0/bg.jpg"));
            float labelWidth = gameInfo.getScreenWidth() * 0.2f;
            float labelHeight = gameInfo.getScreenHeight()* 0.5f;
            messagesLabel = new Renderable(gameInfo.getScreenWidth() - labelWidth - 20, gameInfo.getScreenHeight() / 2f - 100, labelWidth, labelHeight, "label", "messagesLabel");
            gsCurrent.addRenderable(messagesLabel);
            gsCurrent.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
            addPlayerAvatar(gsCurrent);
        }
    }

    /**
     * Avatar information is stored in the game state that was delivered
     * by the Avatar selection episode.
     * So we need to search for the appropriate {@link GameEvent} in the previous GameState
     * and set the avatar according to the game event's value (boy or girl).
     */
    private void addPlayerAvatar(GameState gsCurrent) {
        GameEvent avatarSelectionEvent = initialGameState.getGameEventsWithType("AVATAR_SELECTED");
        if(avatarSelectionEvent != null) {
            String avatarIdentifier = (String) avatarSelectionEvent.parameters;
            createPlayerAvatar(avatarIdentifier, gsCurrent);
        }
    }

    private void createPlayerAvatar(String avatarIdentifier, GameState gsCurrent) {
        pPlayer = new Player(gameInfo.getScreenWidth() / 2f,
        gameInfo.getScreenHeight() / 2f - 80,  gameInfo.getScreenWidth() * 0.3f, gameInfo.getScreenWidth() * 0.3f,
                avatarIdentifier, "player");
        gsCurrent.addRenderable(pPlayer);
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

    protected boolean isConversationOngoing(GameState gsCurrent) {
        return isConversationStarted(gsCurrent) && ! isConversationFinished(gsCurrent);
    }

    protected boolean conversationHasNotStartedAndNotFinished(GameState gsCurrent) {
        return !isConversationStarted(gsCurrent) && !isConversationFinished(gsCurrent);
    }

    protected boolean isConversationStarted(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED");
    }

    protected boolean isConversationFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED");
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {

        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CALCULATOR_STARTED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if(gsCurrent.eventsQueueContainsEvent("CALCULATOR_STARTED"))
            code = EpisodeEndStateCode.CALCULATOR_STARTED;
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
