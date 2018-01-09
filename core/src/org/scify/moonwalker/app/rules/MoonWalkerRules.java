package org.scify.moonwalker.app.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.*;
import org.scify.moonwalker.app.game.GameState;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.engine.UserAction;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MoonWalkerRules implements Rules {

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

    private void handlePositionEvents(GameState gameState) {
        if(pPlayer.getX() < gameInfo.getScreenWidth() / 2f) {
//            gameState.getEventQueue().add(new GameEvent("NEW_CLOUD",
//                    new Point2D.Float(gameInfo.getScreenWidth() / 2f, (gameInfo.getScreenHeight() / 2f) - 100f)));
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }
}
