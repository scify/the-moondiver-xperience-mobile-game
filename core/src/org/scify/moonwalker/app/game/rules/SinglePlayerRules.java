package org.scify.moonwalker.app.game.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Cloud;
import org.scify.moonwalker.app.actors.MoonWalkerPlayer;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SinglePlayerRules extends MoonWalkerRules {

    protected MoonWalkerPlayer pPlayer;
    protected Cloud cCloud;
    protected List<Positionable> lClouds;
    protected MoonWalkerPhysicsRules physics;
    protected GameInfo gameInfo;

    public SinglePlayerRules() {
        super();
        gameInfo = GameInfo.getInstance();
        worldX = gameInfo.getScreenWidth();
        worldY = gameInfo.getScreenHeight();
        pPlayer = new MoonWalkerPlayer("Paul", worldX / 2f,  worldY / 2f);
        pPlayer.setLives(5);
        pPlayer.setScore(0);
        cCloud = new Cloud(0, 10);
        lClouds = new ArrayList<>();
        physics = new MoonWalkerPhysicsRules(worldX, worldY);
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        return new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
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
        MoonWalkerGameState gameState = (MoonWalkerGameState) gsCurrent;
        return gameState.getPlayer().getLives() == 0;
    }

    @Override
    public void disposeResources() {
        physics.disposeResources();
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        return null;
    }
}
