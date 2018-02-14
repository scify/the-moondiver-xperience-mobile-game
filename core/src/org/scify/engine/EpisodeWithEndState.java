package org.scify.engine;

public class EpisodeWithEndState extends Episode<EpisodeEndState> {
    protected Rules rules;

    public EpisodeWithEndState(RenderingEngine renderingEngine, UserInputHandler userInputHandler, String name) {
        super(renderingEngine, userInputHandler, name);
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        disposeResources();
        return endState;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Only keeps basic params
        return new EpisodeWithEndState(renderingEngine, userInputHandler, name);
    }

    public void disposeResources() {
        rules.disposeResources();
    }
}
