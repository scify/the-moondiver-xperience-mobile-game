package org.scify.engine;

import org.scify.engine.rules.Rules;

public class GameEngine {

    public RenderingEngine renderingEngine;
    protected Rules<GameState, UserAction, EpisodeEndState> rules;
    public UserInputHandler inputHandler;
    protected GameState currentGameState;

    public void initialize(Rules<GameState, UserAction, EpisodeEndState> rules) {
        final GameState initialState;
        this.rules = rules;
        initialState = rules.getInitialState();
        currentGameState = initialState;
    }

    public void setRenderingEngine(RenderingEngine renderingEngine) {
        this.renderingEngine = renderingEngine;
    }

    public void setInputHandler(UserInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void setInitialGameState(GameState initialGameState) {
        this.rules.setInitialState(initialGameState);
    }

    public EpisodeEndState execute() {
        while (!rules.isGameFinished(currentGameState)) {
            doGameLoop();
            try {
                Thread.sleep(100L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new EpisodeEndState(EpisodeEndStateCode.EPISODE_INTERRUPTED,currentGameState);
            }
        }
        // here the game has ended
        EpisodeEndState endState = rules.determineEndState(currentGameState);
        // ask the rendering engine instance to dispose the drawables
        renderingEngine.disposeRenderables();
        // return the end state from rules
        return endState;
    }

    private void doGameLoop() {
        final GameState toHandle = currentGameState;
        // Ask to draw the state
        renderingEngine.setGameState(toHandle);
        // and keep on doing the loop in this thread
        // get next user action
        UserAction uaToHandle = inputHandler.getNextUserAction();
        // apply it and determine the next state
        currentGameState = rules.getNextState(currentGameState, uaToHandle);
    }
}
