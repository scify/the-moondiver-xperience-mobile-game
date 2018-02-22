package org.scify.engine;

/**
 * Each {@link Episode} instance finished by producing an instance of this class.
 * This class describes "how" the episode finished ({@link EpisodeEndStateCode})
 * as well as carrying the last instance of the {@link GameState} game state
 */
public class EpisodeEndState extends EpisodeState {
    protected EpisodeEndStateCode endStateCode;
    public EpisodeEndState(EpisodeEndStateCode endStateCode, GameState gameState) {
        super(gameState);
        this.endStateCode = endStateCode;
    }

    public EpisodeEndStateCode getEndStateCode() {
        return endStateCode;
    }

    @Override
    public String toString() {
        return endStateCode.toString();
    }
}
