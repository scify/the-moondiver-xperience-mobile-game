package org.scify.engine;

/**
 * This class describes the state in which an {@link Episode} is at any given time.
 */
public class EpisodeState {

    /**
     * The state of the game that the episode is currently handling.
     */
    protected GameState gameState;

    public EpisodeState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

}
