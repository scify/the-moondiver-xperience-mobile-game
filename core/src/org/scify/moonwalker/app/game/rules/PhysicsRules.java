package org.scify.moonwalker.app.game.rules;

import org.scify.engine.Renderable;
import org.scify.engine.Rules;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;

public abstract class PhysicsRules implements Rules {

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
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public boolean isGamePaused(GameState gsCurrent) {
        return false;
    }

}
