package org.scify.engine;

public interface RenderingEngine<T extends GameState> {
    /**
     * This method is expected to <ul><li> render the game state and depict the current status</li>
     * <li>handle all the events in the game state, ignoring the ones it does not know how to handle. The
     * handled events should be removed from the event queue.</li></ul>
     * @param currentState the current state to draw
     */
    void drawGameState(T currentState);

    void cancelCurrentRendering();

    /**
     * Used to dispose all game objects. May be used between 2 Episodes so that the renreding engine
     * is cleaned up from any game object from the last playing episode.
     */
    void disposeRenderables();

    void setGameState(T toHandle);

    /**
     * Used to dispose and free all game resources (Sprites and other resource-intensive objects)
     * Used when the game has ended.
     */
    void disposeResources();

    void initialize();

    void render(Float delta);

    void resize(int width, int height);

    void sendHideEventToEpisode();
}
