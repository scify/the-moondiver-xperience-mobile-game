package org.scify.engine;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    /**
     * Checks if a given event exists in the events list
     * @param eventType the type of the event
     * @return true if the event exists in the events list
     */
    public boolean eventsQueueContainsEvent(String eventType) {
        Iterator<GameEvent> iter = eventQueue.iterator();
        GameEvent currentGameEvent;
        while (iter.hasNext()) {
            currentGameEvent = iter.next();
            if(currentGameEvent.type.equals(eventType))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(GameEvent gameEvent: eventQueue) {
            sb.append("\n" + gameEvent.type + "\t" + gameEvent.parameters);
        }
        return sb.toString();
    }

    public void removeGameEventsWithType(String gameEventType) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.type.equals(gameEventType))
                    listIterator.remove();
            }
        }
    }
}
