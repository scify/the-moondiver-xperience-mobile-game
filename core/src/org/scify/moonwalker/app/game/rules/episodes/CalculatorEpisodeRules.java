package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.EpisodeEndStateCode;
import org.scify.moonwalker.app.ui.actors.ActionButton;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class CalculatorEpisodeRules extends BaseEpisodeRules {

    public CalculatorEpisodeRules(GameState gsCurrent) {
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
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this)) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));
            addEpisodeBackgroundImage(gsCurrent, "img/calculator_episode/bg.jpg");
            Renderable calculator = new Renderable("calculator", "calculator_button");
            gsCurrent.addRenderable(calculator);
            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            gsCurrent.addRenderable(escape);
            addRenderableEntry("calculator_finished_button", escape);
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
