package org.scify.engine;

import java.util.*;

/**
 * Represents the global state of the game at any given time.
 * Handles objects that are used for communication between the game actors.
 */
public abstract class GameState {

    /**
     * Queue of events to be handled by game actors
     */
    protected List<GameEvent> eventQueue;
    /**
     * List of in-game objects, like players, monsters, sprites, etc
     */
    protected List<Renderable> renderableList;
    protected Player player;
    /**
     * Bucket that serves the need for game components to arbitrarily store
     * key-value paired objects
     */
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

    /**
     * Checks if a given event owned by a given class exists in the events list
     * @param eventType the type of the event
     * @param owner the owner (creator) class of the event
     * @return true if the event exists in the events list
     */
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

    /**
     * Get the first game event with a given type
     * @param type
     * @return the first event found with that type
     */
    public GameEvent getGameEventsWithType(String type) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.type.equals(type))
                    return currentGameEvent;
            }
        }
        return null;
    }

    /**
     * Removes all game events that have a given type
     * @param gameEventType
     */
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

    /**
     * Remove all game events added by a given owner class
     * @param owner the owner of the game events
     */
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(GameEvent gameEvent: eventQueue) {
            sb.append("\n" + gameEvent.type + "\t" + gameEvent.parameters);
        }
        return sb.toString();
    }
}
