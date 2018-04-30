package org.scify.engine;

/**
 * Each {@link Episode} instance finished by producing an instance of this class.
 * This class describes "how" the episode finished ({@link EpisodeEndStateCode})
 * as well as carrying the last instance of the {@link GameState} game state
 */
public class EpisodeEndState extends EpisodeState {

    /**
     * The code describes "how" the episode ended.
     * For example, the episode may end with a "success" or "failure" status
     */
    protected String endStateCode;

    public EpisodeEndState(String endStateCode, GameState gameState) {
        super(gameState);
        this.endStateCode = endStateCode;
    }

    public String getEndStateCode() {
        return endStateCode;
    }

    @Override
    public String toString() {
        return endStateCode.toString();
    }
}
