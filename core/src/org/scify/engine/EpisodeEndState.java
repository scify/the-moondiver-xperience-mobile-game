package org.scify.engine;

/**
 * Each {@link Episode} instance finished by producing an instance of this class.
 * This class describes "how" the episode finished ({@link EpisodeEndStateCode})
 * as well as carrying the last instance of the {@link GameState} game state
 */
public class EpisodeEndState {
    protected EpisodeEndStateCode endStateCode;
    protected GameState gameState;

    public EpisodeEndState(EpisodeEndStateCode endStateCode, GameState gameState) {
        this.endStateCode = endStateCode;
        this.gameState = gameState;
    }

    public EpisodeEndStateCode getEndStateCode() {
        return endStateCode;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return endStateCode.toString();
    }
}
