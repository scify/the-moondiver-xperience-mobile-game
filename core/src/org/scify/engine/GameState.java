package org.scify.engine;

import org.scify.moonwalker.app.actors.Player;

import java.util.*;

public abstract class GameState {
    protected List<GameEvent> eventQueue;
    protected List<Renderable> renderableList;
    protected Player player;

    public GameState(List<GameEvent> eventQueue) {
        this.eventQueue = eventQueue;
        renderableList = Collections.synchronizedList(new LinkedList<Renderable>());
    }

    /**
     * Returns a list of game events that express reactions to user actions.
     * @return A list of GameEvent objects, expected to be handled by a {@link RenderingEngine}.
     */
    public List<GameEvent> getEventQueue() {
        return eventQueue;
    }

    public void addGameEvent(GameEvent event) {
        eventQueue.add(event);
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

    public List<Renderable> getRenderableList() {
        return renderableList;
    }

    public void addRenderable(Renderable r) {
        renderableList.add(r);
    }

    public GameEvent getEventWithType(String gameEventType) {
        GameEvent toReturn = null;
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.type.equals(gameEventType))
                    toReturn = currentGameEvent;
            }
        }
        return toReturn;
    }

    public Player getPlayer() {
        return player;
    }
}
