package org.scify.engine;

import org.scify.engine.rules.Rules;

import java.util.*;

/**
 * Represents the global state of the game at any given time.
 * The instances of this class are handled by the {@link Rules} classes and the {@link RenderingEngine}
 * and hold {@link GameEvent} instances that are used for communication between the game components.
 */
public abstract class GameState {
    protected List<GameEvent> eventQueue;
    protected List<Renderable> renderableList;
    protected Player player;
    protected Map<String, Object> additionalDataMap;

    public GameState(List<GameEvent> eventQueue) {
        this.eventQueue = eventQueue;
        renderableList = Collections.synchronizedList(new LinkedList<Renderable>());
        additionalDataMap = new HashMap<>();
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

    public boolean eventsQueueContainsEventOwnedBy(String eventType, Object owner) {
        Iterator<GameEvent> iter = eventQueue.iterator();
        GameEvent currentGameEvent;
        while (iter.hasNext()) {
            currentGameEvent = iter.next();
            if(currentGameEvent.type.equals(eventType) && currentGameEvent.owner == owner)
                return true;
        }
        return false;
    }

    public void storeAdditionalDataEntry(String dataId, Object dataPayload) {
        additionalDataMap.put(dataId, dataPayload);
    }

    public Object getAdditionalDataEntry(String dataId) {
        return additionalDataMap.get(dataId);
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

    public void removeGameEventsWithTypeOwnedBy(String gameEventType, Object owner) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.type.equals(gameEventType) && currentGameEvent.owner.equals(owner))
                    listIterator.remove();
            }
        }
    }

    public void removeAllGameEventsOwnedBy(Object owner) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.owner != null && currentGameEvent.owner.equals(owner))
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

    public Player getPlayer() {
        return player;
    }
}
