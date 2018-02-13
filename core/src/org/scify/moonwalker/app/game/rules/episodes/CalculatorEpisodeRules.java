package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;
import org.scify.moonwalker.app.ui.input.UserActionCode;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class CalculatorEpisodeRules extends SinglePlayerRules{

    protected GameState gsPrevious;

    public CalculatorEpisodeRules(GameState gsCurrent) {
        this.gsPrevious = gsCurrent;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    private void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case FINISH_EPISODE:
                gameEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/calculator_episode/bg.jpg"));
            // TODO Create Calculator class that will extend Group and handle the calculator functionality
            // TODO refactor "magic numbers"
            ActionButton btnDone = new ActionButton(worldX / 2f - 100, worldY / 2f - 50, 100, 50, "text_button", "calculator_button");
            btnDone.setTitle("Finish");
            btnDone.setPadding(10);
            btnDone.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
            gsCurrent.addRenderable(btnDone);
            addRenderableEntry("calculator_finished_button", btnDone);
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
        return gsCurrent.eventsQueueContainsEvent("CALCULATOR_FINISHED") || super.isGameFinished(gsCurrent);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return episodeFinished(gsCurrent);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        if(gsCurrent.eventsQueueContainsEvent("CALCULATOR_FINISHED"))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_FINISHED, this.gsPrevious);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, this.gsPrevious);
    }

    @Override
    public void gameEndedEvents(GameState gsCurrent) {
        //todo add temp
        gsCurrent.addGameEvent(new GameEvent("CALCULATOR_FINISHED"));
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    @Override
    public void cleanUpState(GameState gsCurrent) {
        // from the previous game state (that the previous episode had)
        // remove the game event that caused this episode to be started
        gsPrevious.removeGameEventsWithType("CALCULATOR_STARTED");
        gsPrevious.removeGameEventsWithType("EPISODE_FINISHED");
        gsPrevious.removeGameEventsWithType("CONVERSATION_FINISHED");
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
