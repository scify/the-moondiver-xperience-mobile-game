package org.scify.engine.rules;

import org.scify.engine.GameEngine;
import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;

public interface Rules<StateType, Action, EndStateType> {

    /**
     * When the game starts, the rules are responsible of
     * producing a game state that will include all the
     * necessary game events that need to be dealt with
     * in the {@link RenderingEngine} instance
     * @return the initial state instance
     */
    StateType getInitialState();
    /**
     * Given a action and a current game state, this method
     * determines the next game state that will be dealt with in
     * the {@link RenderingEngine} instance, by modifying
     * the passed state.
     * @param currentState  the current game state instance
     * @param action (nullable), an action (eg user action)
     * @return the modified state
     */
    StateType getNextState(StateType currentState, Action action);
    boolean isGameFinished(StateType currentState);
    boolean isGamePaused(StateType currentState);
    /**
     * When the game has ended, the
     * {@link GameEngine} instance that runs the game
     * will trigger this method in order for the rules to
     * clean up any resources that are unwanted
     */
    void disposeResources();

    /**
     * The {@link GameEngine} instance that runs the game
     * asks for the eventual {@link EndStateType} that the game
     * ended with (eg success or failure)
     * @param currentState the current game state instance
     * @return the state that the game ended with
     */
    EndStateType determineEndState(StateType currentState);
    /**
     * Removes from the state any {@link GameEvent}s that
     * we want to be repeated if this episode is started again (resumed)
     * For example, If we want the conversation to happen again,
     * we should remove all conversation-relevant {@link GameEvent}s
     * This method should be called from the {@link GameEngine} instance
     * that runs the game, when the game has ended.
     * @param currentState the current state instance
     */
    //void cleanUpState(StateType currentState);

    /**
     * When the game starts, the rules might add or remove
     * a series of events in/ from the state parameter.
     * @param currentState the current state instance
     */
    void gameStartedEvents(StateType currentState);

    /**
     *
     * When the game ends, the rules might add or remove
     * a series of events in/ from the state parameter.
     * @param currentState the current state instance
     */
    void gameEndedEvents(StateType currentState);

    /**
     * When the game is resumed, the rules might add or remove
     * a series of events in/ from the state parameter.
     * @param currentState the current state instance
     */
    void gameResumedEvents(StateType currentState);

    void setInitialState(StateType type);
}
