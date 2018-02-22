package org.scify.engine;

import org.scify.engine.rules.Rules;

/**
 * The Game Engine class describes the logic of running the game, until it's ending point.
 * It contains the tasks of getting any user actions, passing them to the game rules
 * so that the current state of the game can be computed.
 * This loop happens until the rules decide that the game has ended.
 */
public class GameEngine {

    /**
     * The rules take into consideration any user action and compute the next game state
     * until the game ends. In such event, an episode end state is produced and returned.
     */
    protected Rules<GameState, UserAction, EpisodeEndState> rules;
    protected RenderingEngine renderingEngine;
    /**
     * The class responsible of handling the user actions
     */
    protected UserInputHandler inputHandler;
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

    /**
     * Executes the game, until the rules decide that the game is over.
     * @return an end state that describes how the game ended and contains
     * the last game state
     */
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
