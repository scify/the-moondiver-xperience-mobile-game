package org.scify.moonwalker.app.rules;

import org.scify.engine.*;
import org.scify.engine.Positionable;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.*;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.engine.UserAction;

import java.util.*;

public abstract class MoonWalkerRules implements Rules {

    protected int worldX;
    protected int worldY;
    protected MoonWalkerPlayer pPlayer;
    protected Cloud cCloud;
    protected List<Positionable> lClouds;
    protected MoonWalkerPhysicsRules physics;
    protected GameInfo gameInfo;

    public MoonWalkerRules() {
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
        gsCurrent = physics.getNextState(gsCurrent, userAction); // Update physics
        handlePositionEvents(gsCurrent);
        return new MoonWalkerGameState(gsCurrent.getEventQueue(), pPlayer, physics.world);
    }

    protected void handlePositionEvents(GameState gameState) {
        if(gameState.eventsQueueContainsEvent("PLAYER_BOTTOM")) {
            if (!gameState.eventsQueueContainsEvent("DIALOG_1")) {
                gameState.getEventQueue().add(new GameEvent("DIALOG_1"));
                // TODO add dialog object in game event?
                gameState.getEventQueue().add(new GameEvent("ADD_DIALOG_UI"));
            }
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }
}
