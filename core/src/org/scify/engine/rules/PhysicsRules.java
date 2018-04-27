package org.scify.engine.rules;

import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.EpisodeEndState;

public abstract class PhysicsRules implements Rules<GameState, UserAction, EpisodeEndState> {

    protected int wordX, worldY;

    public PhysicsRules(int worldX, int worldY) {
        this.wordX = worldX;
        this.worldY = worldY;
    }

    @Override
    public GameState getInitialState() {
        return null;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        return gsCurrent;
    }

    @Override
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public boolean isGamePaused(GameState gsCurrent) {
        return false;
    }

}
