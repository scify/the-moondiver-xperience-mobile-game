package org.scify.moonwalker.app.game.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Player;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SinglePlayerRules extends MoonWalkerRules {

    protected Player pPlayer;

    public SinglePlayerRules() {
        super();
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        if(gameState == null)
            gameState = new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
        return gameState;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

}
