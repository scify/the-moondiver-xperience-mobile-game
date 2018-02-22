package org.scify.engine;

public class EpisodeState {
    protected GameState gameState;

    public EpisodeState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

}
