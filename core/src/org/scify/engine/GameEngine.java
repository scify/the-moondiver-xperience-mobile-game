package org.scify.engine;

import org.scify.moonwalker.app.helpers.UnsupportedGameTypeException;

public class GameEngine implements Game {

    protected RenderingEngine renderingEngine;
    protected Rules rules;
    protected UserInputHandler inputHandler;
    protected GameState currentGameState;

    public GameEngine() {
    }

    private void doGameLoop() {
        final GameState toHandle = currentGameState;
        // Ask to draw the state
        renderingEngine.setGameState(toHandle);
        // and keep on doing the loop in this thread
        //get next user action
        UserAction uaToHandle = inputHandler.getNextUserAction();

        //apply it and determine the next state
        currentGameState = rules.getNextState(currentGameState, uaToHandle);
    }

    public void initialize(GameProps props) {
        final GameState initialState;
        renderingEngine = props.renderingEngine;
        inputHandler = props.userInputHandler;
        try {
            rules = props.rules;
        } catch (UnsupportedGameTypeException e) {
            e.printStackTrace();
        }
        initialState = rules.getInitialState();
        renderingEngine.setGameState(initialState);
        renderingEngine.initializeGameState(initialState);

        currentGameState = initialState;
    }

    public EpisodeEndState call() {
        while (!rules.isGameFinished(currentGameState)) {
            doGameLoop();
            try {
                Thread.sleep(20L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
                return EpisodeEndState.EPISODE_INTERRUPTED;
            }
        }
        // here the game has ended
        // TODO change
        return EpisodeEndState.EPISODE_FINISHED;
    }

    public Rules getRules() {
        return rules;
    }
}
