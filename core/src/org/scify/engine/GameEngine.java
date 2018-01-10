package org.scify.engine;

import org.scify.moonwalker.app.MoonWalker;
import org.scify.moonwalker.app.game.GameEndState;
import org.scify.moonwalker.app.game.GameProps;
import org.scify.moonwalker.app.rules.RulesFactory;
import org.scify.moonwalker.app.helpers.UnsupportedGameTypeException;

public class GameEngine implements Game<GameEndState> {

    protected RenderingEngine renderingEngine;
    protected RulesFactory rulesFactory;
    protected Rules rules;
    protected UserInputHandler inputHandler;
    protected GameState currentGameState;
    protected MoonWalker app;

    public GameEngine(MoonWalker app) {
        this.app = app;
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

    @Override
    public void initialize(GameProps props) {
        final GameState initialState;
        rulesFactory = new RulesFactory();
        renderingEngine = props.renderingEngine;
        inputHandler = props.userInputHandler;
        try {
            rules = rulesFactory.createRules(props.gameType);
        } catch (UnsupportedGameTypeException e) {
            e.printStackTrace();
        }
        initialState = rules.getInitialState();
        renderingEngine.setGameState(initialState);
        renderingEngine.initializeGameState(initialState);
        app.setScreen(renderingEngine);
        currentGameState = initialState;
    }

    @Override
    public GameEndState call() {
        while (!rules.isGameFinished(currentGameState)) {
            doGameLoop();
            try {
                Thread.sleep(20L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // here the game has ended
        // TODO change
        return GameEndState.GAME_FINISHED;
    }

    @Override
    public void finalize() {

    }
}
