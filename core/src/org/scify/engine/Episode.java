package org.scify.engine;

import java.util.concurrent.Callable;

public abstract class Episode<T> implements Callable<T> {

    // each episode uses a game engine when executing
    protected GameEngine gameEngine;
    protected RenderingEngine renderingEngine;
    protected UserInputHandler userInputHandler;

    public Episode(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    public boolean isAccessible(ScenarioState state){
        return true;
    }

    public void execute() {
        System.out.println("execute");
    }

    public void disposeResources() {
        renderingEngine.disposeResources();
        gameEngine.getRules().disposeResources();
    }
}
