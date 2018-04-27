package org.scify.engine.rules;

/**
 * The Rules interface describes the rules of a game. It outlines objects which can act upon a game state,
 * possibly given a set of input actions, to generate valid following states.
 * @param <StateType> The class of the states that the Rules object is called to act upon.
 * @param <Action> The class of actions that can trigger changes to a state handled by the Rules.
 * @param <EndStateType> The class of the states that are generated upon the end of a game.
 */
public interface Rules<StateType, Action, EndStateType> {

    /**
     * When the game starts, the rules are responsible to provide an initial valid state for the game.
     * @return the initial state instance
     */
    StateType getInitialState();
    /**
     * Given an action and a current game state, this method
     * determines the next game state, possibly by modifying the input state.
     * @param currentState  the current game state instance. <b>This state object should not be considered
     *                      immutable.</b>
     * @param action (nullable), an action (eg user action) that affects the game state and needs to be taken
     *               into account by the rules.
     * @return the modified state
     */
    StateType getNextState(StateType currentState, Action action);

    /**
     * Returns whether a given state indicates a finished game.
     * @param currentState The state to evaluate.
     * @return True if we have reached the end of the game. Otherwise, false.
     */
    boolean isEpisodeFinished(StateType currentState);

    /**
     * Returns whether the game is in a paused state. This can be used to indicate that the Rules
     * should not evaluate/change the current state, while the game is paused.
     * @param currentState The state to evaluate.
     * @return True if the game is paused. Otherwise, false.
     */
    boolean isGamePaused(StateType currentState);

    /**
     * This method should be called to dispose of any (non-trivial) resources that the Rules may
     * have initialized.
     */
    void disposeResources();

    /**
     * The Rules determines what the return end state of a game will be, provided the (last) valid
     * game state.
     * @param currentState the current/last game state instance
     * @return the EndStateType instance that identifies how the game ended
     */
    EndStateType determineEndState(StateType currentState);

    /**
     * Sets the current state of the related game to a specific, new game state.
     * @param gameState The new state.
     */
    void setInitialState(StateType gameState);
}
