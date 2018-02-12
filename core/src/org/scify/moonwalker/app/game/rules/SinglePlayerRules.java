package org.scify.moonwalker.app.game.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Player;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SinglePlayerRules extends MoonWalkerRules {

    protected Player pPlayer;
    protected MoonWalkerPhysicsRules physics;

    public SinglePlayerRules() {
        super();
        physics = new MoonWalkerPhysicsRules(worldX, worldY);
    }

    /**
     * This setter serves the need for an episode
     * to define their own set of physics rules.
     * for example, an episode might have physics rules
     * without gravity
     * @param physics the new {@link MoonWalkerPhysicsRules} instance
     */
    public void setPhysics(MoonWalkerPhysicsRules physics) {
        this.physics = physics;
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        if(gameState == null)
            gameState = new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
        return gameState;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gsCurrent = physics.getNextState(gsCurrent, userAction);
        return gsCurrent;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public void disposeResources() {
        physics.disposeResources();
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        return null;
    }

    @Override
    public void cleanUpState(GameState currentState) {

    }
}
