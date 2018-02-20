package org.scify.engine;

import com.badlogic.gdx.Gdx;
import org.scify.engine.rules.Rules;

public class EpisodeWithEndState extends Episode<EpisodeEndState> {
    protected Rules rules;

    public EpisodeWithEndState(RenderingEngine renderingEngine, UserInputHandler userInputHandler, String name, Rules rules) {
        super(renderingEngine, userInputHandler, name);
        this.rules = rules;
        gameEngine.initialize(renderingEngine, userInputHandler, rules);
    }

    @Override
    public EpisodeEndState call() {
        EpisodeEndState endState = gameEngine.execute();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                disposeResources();
            }
        });

        return endState;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Only keeps basic params
        return new EpisodeWithEndState(renderingEngine, userInputHandler, name, rules);
    }

    public void disposeResources() {
        rules.disposeResources();
    }
}
