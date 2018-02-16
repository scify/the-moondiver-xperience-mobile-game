package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.engine.Player;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

import java.util.HashMap;

public class RoomEpisodeRules extends SinglePlayerRules {
    protected Renderable messagesLabel;
    protected String mainLabelText = "Επεισόδιο 0: Το δωμάτιο.";

    public RoomEpisodeRules() {
        pPlayer = new Player(gameInfo.getScreenWidth() / 2f,
                gameInfo.getScreenHeight() / 2f - 80,  gameInfo.getScreenWidth() * 0.3f, gameInfo.getScreenWidth() * 0.3f,
                "boy", "player");
        pPlayer.setLives(5);
        pPlayer.setScore(0);
        addRenderableEntry("player", pPlayer);
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        gameResumedEvents(gsCurrent);
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return gsCurrent;
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/episode_0/bg.jpg"));
            float labelWidth = gameInfo.getScreenWidth() * 0.2f;
            float labelHeight = gameInfo.getScreenHeight()* 0.5f;
            messagesLabel = new Renderable(gameInfo.getScreenWidth() - labelWidth - 20, gameInfo.getScreenHeight() / 2f - 100, labelWidth, labelHeight, "label", "messagesLabel");
            gsCurrent.addRenderable(messagesLabel);
            gsCurrent.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
        }
    }

    @Override
    public void gameResumedEvents(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_RESUMED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_RESUMED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/episode_0/bg.jpg"));
        }
    }


    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        // begin conversation with Yoda
        // if the conversation has not started and has not finished too
        // TODO add conversation id in case we have multiple conversations in an episode
        if(!gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED") && !gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_0.json");
        }
        // if the conversation has already started, but has not finished yet
        if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED") && !gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }

        if(gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED"))
            gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED"));

        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {

        }
    }

    /**
     * This method is similar to isGameFinished
     * However, it is used internally by the getNextState method
     * in order to decide whether the ending Game Events should be added
     * to the current game state, before the episode finishes.
     * @param gsCurrent the current {@link GameState}
     * @return true if the episode is finished
     */
    protected boolean episodeFinished(GameState gsCurrent) {
        // this episode is considered finished either
        // when the player has reached the top border of the screen
        // or the base rules class decides that should finish
        return gsCurrent.eventsQueueContainsEvent("CALCULATOR_STARTED");
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return episodeFinished(gsCurrent);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if(gsCurrent.eventsQueueContainsEvent("CALCULATOR_STARTED"))
            code = EpisodeEndStateCode.CALCULATOR_STARTED;
        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        // TODO: Remove all events where I am the owner
        currentState.removeGameEventsWithType("EPISODE_RESUMED");
        currentState.removeGameEventsWithType("CALCULATOR_STARTED");
        currentState.removeGameEventsWithType("EPISODE_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        return currentState;
    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }
}
