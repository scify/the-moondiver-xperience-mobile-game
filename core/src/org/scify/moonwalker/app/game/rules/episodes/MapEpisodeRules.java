package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MapEpisodeRules extends BaseEpisodeRules {

    public MapEpisodeRules(GameState gsCurrent) {
        this.initialGameState = gsCurrent;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BACK:
                gameEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!gameHasStarted(currentState)) {
            addGameStartedEvents(currentState);
            addPlayerAvatar(currentState);
            addEpisodeBackgroundImage(currentState, "img/map.jpg");
            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        GameState toReturn = gsCurrent;
        if(initialGameState != null) {
            toReturn = this.initialGameState;
        }
        if(gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE"))
            return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, toReturn);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, toReturn);
    }

    @Override
    public void gameEndedEvents(GameState gsCurrent) {
        gsCurrent.addGameEvent(new GameEvent("PREVIOUS_EPISODE"));
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

}
