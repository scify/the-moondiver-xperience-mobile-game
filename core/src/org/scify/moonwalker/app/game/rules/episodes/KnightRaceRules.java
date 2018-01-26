package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.actors.Player;
import org.scify.moonwalker.app.game.conversation.Conversation;
import org.scify.moonwalker.app.game.conversation.ConversationState;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

import java.util.Date;
import java.util.HashMap;

public class KnightRaceRules extends SinglePlayerRules {

    protected Renderable messagesLabel;
    protected String mainLabelText = "Episode goal: Help the player reach the top edge of the screen.";
    protected Conversation conversation;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        handleGameStartingRules(gsCurrent);
        handlePositionRules(gsCurrent);
        handleConversationRules(gsCurrent, userAction);
        if(rulesFinished(gsCurrent)) {
            super.handleGameFinishedEvents(gsCurrent);
            this.handleGameFinishedEvents(gsCurrent);
        }
        return gsCurrent;
    }

    protected void handleGameStartingRules(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/episode_1/mushroom.jpg"));
            messagesLabel = new Renderable(gameInfo.getScreenWidth() - 200, gameInfo.getScreenHeight() / 2f, 200, 200, "label", "messagesLabel");
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

    protected void handleConversationRules(GameState gsCurrent, UserAction userAction) {
        Player player = gsCurrent.getPlayer();
        if(player.getxPos() < gameInfo.getScreenWidth() / 2f) {
            System.err.println("passed " + new Date().getTime());
            if(!gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED")) {
                //create new conversation
                conversation = new Conversation("json_DB/conversation.json");
                gsCurrent.addGameEvent(new GameEvent("CONVERSATION_STARTED"));
            } else {
                if(!conversation.isConversationFinished())
                    addNextConversationEvent(gsCurrent, userAction);
                else
                    gsCurrent.removeGameEventsWithType("CONVERSATION_STARTED");
            }
        }
    }

    protected void addNextConversationEvent(GameState gsCurrent, UserAction userAction) {
        ConversationState state = conversation.getNextState(userAction);
        if(state != null) {
            gsCurrent.addGameEvent(new GameEvent("CONVERSATION_STATE", state));
            pauseGame(gsCurrent);
        }
    }

    protected boolean rulesFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER") || super.isGameFinished(gsCurrent);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return rulesFinished(gsCurrent) && gsCurrent.eventsQueueContainsEvent("EPISODE_FINISHED");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        if(gsCurrent.eventsQueueContainsEvent("PLAYER_TOP_BORDER"))
            return EpisodeEndState.EPISODE_FINISHED_SUCCESS;
        return EpisodeEndState.EPISODE_FINISHED_FAILURE;
    }
}
