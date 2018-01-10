package org.scify.engine;

public interface Rules {

    GameState getInitialState();
    GameState getNextState(GameState gsCurrent, UserAction userAction);
    boolean isGameFinished(GameState gsCurrent);

}
