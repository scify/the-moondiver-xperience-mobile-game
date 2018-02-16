package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;
import org.scify.moonwalker.app.ui.components.ActionButton;
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

    public CalculatorEpisodeRules() {
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
            Renderable calculator = new Renderable("calculator");
            gsCurrent.addRenderable(calculator);
            float btnRealSize = gameInfo.pixelsWithDensity(ESCAPE_BUTTON_SIZE_PIXELS);
            System.out.println(btnRealSize);
            ActionButton escape = new ActionButton(0, gameInfo.getScreenHeight() - btnRealSize, btnRealSize, btnRealSize, "image_button", "escape_button");
            escape.setPadding(gameInfo.pixelsWithDensity(ESCAPE_BUTTON_PADDING_PIXELS));
            escape.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
            escape.setImgPath("img/close.png");
            gsCurrent.addRenderable(escape);
            addRenderableEntry("calculator_finished_button", escape);
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
        GameState toReturn = gsCurrent;
        if(gsPrevious != null) {
            toReturn = this.gsPrevious;
        }
        if(gsCurrent.eventsQueueContainsEvent("CALCULATOR_FINISHED"))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_FINISHED, toReturn);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, toReturn);
    }

    @Override
    public void gameEndedEvents(GameState gsCurrent) {
        //todo add temp
        gsCurrent.addGameEvent(new GameEvent("CALCULATOR_FINISHED"));
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
