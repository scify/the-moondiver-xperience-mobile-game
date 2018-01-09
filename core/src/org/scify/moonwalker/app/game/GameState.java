package org.scify.moonwalker.app.game;


import org.scify.engine.GameEvent;
import org.scify.engine.RenderingEngine;

import java.util.List;
import java.util.Queue;

public abstract class GameState {
    protected List<GameEvent> eventQueue;


    public GameState(List<GameEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    /**
     * Returns a list of game events that express reactions to user actions.
     * @return A list of GameEvent objects, expected to be handled by a {@link RenderingEngine}.
     */
    public List<GameEvent> getEventQueue() {
        return eventQueue;
    }

}
