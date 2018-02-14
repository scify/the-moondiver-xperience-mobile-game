package org.scify.engine;
import java.util.concurrent.*;

public abstract class Episode<T> implements Callable<T>, Cloneable {

    // each episode uses a game engine when executing
    protected GameEngine gameEngine;
    protected RenderingEngine renderingEngine;
    protected UserInputHandler userInputHandler;
    protected String name;

    public Episode(RenderingEngine renderingEngine, UserInputHandler userInputHandler, String name) {
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
        this.name = name;
        gameEngine = new GameEngine();
    }

    public boolean isAccessible(EpisodeEndState state){
        return true;
    }

    public T play() {
        T result = null;
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<T> future = es.submit(this);
        es.shutdown();
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
