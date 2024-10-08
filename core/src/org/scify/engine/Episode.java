package org.scify.engine;

import com.badlogic.gdx.Gdx;
import org.scify.engine.rules.Rules;

import java.util.concurrent.*;

/**
 * An Episode represents an interactive scene, where things happen, like in a mini-game.
 * @param <T> A class which contains info regarding how an Episode ended.
 */
public abstract class Episode<T> implements Callable<T>, Cloneable {
    protected Rules rules;
    /**
     * A game engine used to run the Episode.
     */

    protected GameEngine gameEngine;
    /**
     * A descriptive name for the Episode.
     */
    protected String name;

    /**
     * Initializes the episode.
     */
    public Episode() {
        gameEngine = new GameEngine();
    }

    /**
     * Returns if this Episode can be played, provided the outcome of another Episode.
     * This method needs to be overriden, if to be able to execute this episode, there exists a prerequisite
     * from a previous episode.
     * @param state The state of the previous episode.
     * @return True, if the provided state indicates that this episode is still plausible.
     */
    public boolean isAccessible(EpisodeEndState state){
        return true;
    }

    /**
     * Runs the episode, rendering it - through a RenderingEngine - and supporting user interaction through an
     * appropriate handler.
     * @param renderingEngine The engine that renders the Episode.
     * @param userInputHandler The user input handler that provides the interaction.
     * @return An instance of an object which descirbes how the episode ended.
     */
    public EpisodePlayResult<T> play(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        gameEngine.setRenderingEngine(renderingEngine);
        gameEngine.setInputHandler(userInputHandler);
        try {
            return new EpisodePlayResult<>(true, this.call());
        } catch (Exception e ) {
            e.printStackTrace();
            return new EpisodePlayResult<>(false, null, e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * This method sets the initial state of the Episode.
     * It allows e.g. information from previous Episodes to be passed on to this Episode.
     * @param initialEpisodeState The transferred Episode state.
     */
    public void setInitialEpisodeState(EpisodeState initialEpisodeState) {
        gameEngine.setInitialGameState(initialEpisodeState.getGameState());
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName();
    }

    public abstract void init();

    protected synchronized Semaphore disposeEpisodeResources() throws InterruptedException {
        // Keep track of resource disposal
        final Semaphore sFinished = new Semaphore(1);
        sFinished.acquire();

        rules.disposeResources();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                gameEngine.disposeResources();
                // Release resource semaphore when complete
                sFinished.release();
            }
        });
        rules = null;

        // Return the semaphore to allow synch
        return sFinished;
    }
}
