package org.scify.engine;

public class EpisodeWithEndState extends Episode<EpisodeEndState> {
    public EpisodeWithEndState(RenderingEngine renderingEngine, UserInputHandler userInputHandler, String name) {
        super(renderingEngine, userInputHandler, name);
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        return endState;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
