package org.scify.engine;

public class GameEngine implements Game {

    protected RenderingEngine renderingEngine;
    protected Rules<GameState, UserAction, EpisodeEndState> rules;
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

    public void initialize(RenderingEngine renderingEngine, UserInputHandler userInputHandler, Rules<GameState, UserAction, EpisodeEndState> rules) {
        final GameState initialState;
        this.renderingEngine = renderingEngine;
        this.inputHandler = userInputHandler;
        this.rules = rules;
        initialState = rules.getInitialState();
        renderingEngine.setGameState(initialState);
        renderingEngine.initializeGameState(initialState);
        currentGameState = initialState;
    }

    public EpisodeEndState execute() {
        while (!rules.isGameFinished(currentGameState)) {
            doGameLoop();
            try {
                Thread.sleep(20L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new EpisodeEndState(EpisodeEndStateCode.EPISODE_INTERRUPTED,currentGameState);
            }
        }
        // here the game has ended
        // ask the rules instance to clean up the game state if needed
        rules.cleanUpState(currentGameState);
        // ask the rendering engine instance to dispose the drawables
        renderingEngine.disposeDrawables();
        // return the end state from rules
        return rules.determineEndState(currentGameState);
    }

    public Rules getRules() {
        return rules;
    }
}
