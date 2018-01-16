package org.scify.engine;

import com.badlogic.gdx.Gdx;

import java.util.concurrent.*;

public abstract class Episode<T> implements Callable<T> {

    // each episode uses a game engine when executing
    protected GameEngine gameEngine;
    protected RenderingEngine renderingEngine;
    protected UserInputHandler userInputHandler;
    protected String name;

    public Episode(RenderingEngine renderingEngine, UserInputHandler userInputHandler, String name) {
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
        this.name = name;
    }

    public boolean isAccessible(EpisodeEndState state){
        return true;
    }

    public T play() {
        T result = null;
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<T> future = es.submit(this);
        es.shutdown();
        //this code will execute once the user exits the app
        // (either to go to next level or to exit)
        try {
            result = future.get();
            System.out.println("Game result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
                disposeResources();
            }
        });
        return result;
    }

    public void disposeResources() {
        System.out.println("disposing episode resources...");
        renderingEngine.disposeResources();
        gameEngine.getRules().disposeResources();
    }

    public String getName() {
        return name;
    }
}
