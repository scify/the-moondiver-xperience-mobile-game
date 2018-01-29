package org.scify.engine;

public interface Rules<StateType, Action, EndStateType> {

    StateType getInitialState();
    StateType getNextState(StateType gsCurrent, Action userAction);
    boolean isGameFinished(StateType gsCurrent);
    boolean isGamePaused(StateType gsCurrent);
    void disposeResources();
    EndStateType determineEndState(StateType gsCurrent);
}
