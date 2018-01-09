package org.scify.engine;

import org.scify.moonwalker.app.game.GameState;

public interface Rules {

    GameState getInitialState();
    GameState getNextState(GameState gsCurrent, UserAction userAction);
    boolean isGameFinished(GameState gsCurrent);

}
