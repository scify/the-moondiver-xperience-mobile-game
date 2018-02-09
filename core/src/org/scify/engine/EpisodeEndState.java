package org.scify.engine;

public class EpisodeEndState {
    protected EpisodeEndStateCode endState;
    protected GameState gameState;

    public EpisodeEndState(EpisodeEndStateCode endState, GameState gameState) {
        this.endState = endState;
        this.gameState = gameState;
    }

    public EpisodeEndStateCode getEndState() {
        return endState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
