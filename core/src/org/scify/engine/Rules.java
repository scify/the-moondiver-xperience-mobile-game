package org.scify.engine;

public interface Rules<StateType, Action, EndStateType> {

    StateType getInitialState();
    StateType getNextState(StateType currentState, Action userAction);
    boolean isGameFinished(StateType currentState);
    boolean isGamePaused(StateType currentState);
    void disposeResources();
    EndStateType determineEndState(StateType currentState);
    /**
     * Removes from game state any {@link GameEvent}s that
     * we want to be repeated if this episode is started again (resumed)
     * For example, If we want the conversation to happen again,
     * we should remove all conversation-relevant {@link GameEvent}s
     * @param currentState the current {@link GameState} instance
     */
    void cleanUpState(StateType currentState);
}
