package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.actors.Player;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

import java.util.Date;
import java.util.HashMap;

public class KnightRaceRules extends SinglePlayerRules {

    protected Renderable messagesLabel;
    protected String mainLabelText = "Episode goal: Help the player reach the top edge of the screen.";

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        handlePositionRules(gsCurrent);
        gsCurrent = handleConversationRules(gsCurrent, userAction);
        return gsCurrent;
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/episode_1/bg.png"));
            float labelWidth = gameInfo.getScreenWidth() * 0.2f;
            float labelHeight = gameInfo.getScreenHeight()* 0.5f;
            messagesLabel = new Renderable(gameInfo.getScreenWidth() - labelWidth - 20, gameInfo.getScreenHeight() / 2f - 100, labelWidth, labelHeight, "label", "messagesLabel");
            gsCurrent.addRenderable(messagesLabel);
            gsCurrent.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
        }
    }

    protected void handlePositionRules(GameState gameState) {
        if(gameState.eventsQueueContainsEvent("PLAYER_BORDER")) {
            // add dialog object in game event
            gameState.removeGameEventsWithType("PLAYER_BORDER");
            gameState.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, "Whoops!")));
            // label is reset to its original state after 3 seconds
            gameState.addGameEvent(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText), new Date().getTime() + 3000, false));
        }
    }

    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        Player player = gsCurrent.getPlayer();
        if(player.getxPos() < gameInfo.getScreenWidth() / 2f) {
            // begin conversation with Yoda
            // if the conversation has not started and has not finished too
            // TODO add conversation id in case we have multiple conversations in an episode
            if(!gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED") && !gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
                // call base class create method, passing the resource file for this specific conversation
                createConversation(gsCurrent, "json_DB/conversation.json");
            }
            // if the conversation has already started, but has not finished yet
            if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED") && !gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
                // ask the conversation rules to alter the current game state accordingly
                gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
            }
            handleTriggerEventForCurrentConversationLine(gsCurrent);
        }
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
     * to the current game state
     * @param gsCurrent the current {@link GameState}
     * @return
     */
    protected boolean episodeFinished(GameState gsCurrent) {
        // this episode is considered finished either
        // when the player has reached the top border of the screen
        // or the base rules class decides that should finish
        return gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER") || super.isGameFinished(gsCurrent);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return episodeFinished(gsCurrent) && gsCurrent.eventsQueueContainsEvent("EPISODE_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        if(gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER"))
            return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, gsCurrent);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, gsCurrent);
    }

    @Override
    public void cleanUpState(GameState currentState) {

    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
