package org.scify.engine;

import com.badlogic.gdx.Screen;

import org.scify.moonwalker.app.game.GameState;

public interface RenderingEngine<T extends GameState> extends Screen {
    /**
     * This method is expected to <ul><li> render the game state and depict the current status</li>
     * <li>handle all the events in the game state, ignoring the ones it does not know how to handle. The
     * handled events should be removed from the event queue.</li></ul>
     * @param currentState the current state to draw
     */
    void drawGameState(T currentState);

    /**
     * Method to play game over sounds, graphics, etc (e.g a game over message)
     */
    void playGameOver();

    void cancelCurrentRendering();

    void disposeDrawables();

    void setGameState(T toHandle);

    void initializeGameState(T initialState);
}
