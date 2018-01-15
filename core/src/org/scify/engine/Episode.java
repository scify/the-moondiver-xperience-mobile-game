package org.scify.engine;

public abstract class Episode {

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

    public void initialize(GameProps props) {
        this.gameEngine.initialize(props);
    }

    public void execute() {
        System.out.println("execute");
    }
}
