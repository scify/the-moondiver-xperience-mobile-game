package org.scify.engine;

import com.badlogic.gdx.Gdx;
import org.scify.engine.rules.Rules;

public class EpisodeWithEndState extends Episode<EpisodeEndState> {


    public EpisodeWithEndState(Rules rules) {
        this.rules = rules;
    }

    @Override
    public void init() {
        gameEngine.initialize(rules);
    }

    @Override
    public EpisodeEndState call() {

        EpisodeEndState endState = gameEngine.execute();
//        Gdx.app.postRunnable(new Runnable() {
//            @Override
//            public void run() {
//                disposeEpisodeResources();
//            }
//        });

        return endState;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // Only keeps basic params
        return new EpisodeWithEndState(rules);
    }

}
