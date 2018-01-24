package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

import java.util.Date;
import java.util.HashMap;

public class KnightRaceRules extends SinglePlayerRules {

    Renderable messagesLabel;
    String mainLabelText = "Episode goal: Help the player reach the top edge of the screen.";

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        handleGameStartingRules(gsCurrent);
        handlePositionEvents(gsCurrent);
        if(rulesFinished(gsCurrent)) {
            super.handleGameFinishedEvents(gsCurrent);
            this.handleGameFinishedEvents(gsCurrent);
        }
        return gsCurrent;
    }

    protected void handleGameStartingRules(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.getEventQueue().add(new GameEvent("EPISODE_STARTED"));
            gsCurrent.getEventQueue().add(new GameEvent("BACKGROUND_IMG_UI", "img/episode_1/mushroom.jpg"));
            messagesLabel = new Renderable(gameInfo.getScreenWidth() - 200, gameInfo.getScreenHeight() / 2f, 200, 200, "label", "messagesLabel");
            gsCurrent.addRenderable(messagesLabel);
            gsCurrent.getEventQueue().add(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
        }
    }

    protected void handlePositionEvents(GameState gameState) {
        if(gameState.eventsQueueContainsEvent("PLAYER_BORDER")) {
            // add dialog object in game event
            gameState.removeGameEventsWithType("PLAYER_BORDER");
            gameState.getEventQueue().add(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, "Whoops!")));
            gameState.getEventQueue().add(new GameEvent("RESET_LABEL_TEXT", null, new Date().getTime() + 3000, false));
        }
        if(gameState.eventsQueueContainsEvent("RESET_LABEL_TEXT")) {
            if (new Date().getTime() > gameState.getEventWithType("RESET_LABEL_TEXT").delay) {
                gameState.getEventQueue().add(new GameEvent("UPDATE_LABEL_TEXT_UI", new HashMap.SimpleEntry<>(messagesLabel, mainLabelText)));
                gameState.removeGameEventsWithType("RESET_LABEL_TEXT");
            }
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
