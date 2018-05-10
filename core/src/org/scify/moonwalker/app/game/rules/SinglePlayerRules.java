package org.scify.moonwalker.app.game.rules;

import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.Player;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SinglePlayerRules extends MoonWalkerBaseRules {

    protected Player pPlayer;

    public SinglePlayerRules() {
        super();
    }

    @Override
    public GameState getInitialState() {
        List<GameEvent> eventQueue = Collections.synchronizedList(new LinkedList<GameEvent>());
        if(initialGameState == null)
            initialGameState = new MoonWalkerGameState(eventQueue, pPlayer, physics.world);
        return initialGameState;
    }

    @Override
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return false;
    }
}
