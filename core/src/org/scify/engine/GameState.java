package org.scify.engine;

import org.scify.engine.renderables.Renderable;

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
        clearRendereablesList();
        additionalDataMap = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    public synchronized void clearRendereablesList() {
        renderableList = Collections.synchronizedList(new LinkedList<Renderable>());
    }

    /**
     * Returns a list of game events that express reactions to user actions.
     * @return A list of GameEvent objects, expected to be handled by a {@link RenderingEngine}.
     */
    public List<GameEvent> getEventQueue() {
        return eventQueue;
    }

    public synchronized void addGameEvent(GameEvent event) {
        synchronized (eventQueue) {
            eventQueue.add(event);
        }
    }

    /**
     * Checks if a given event exists in the events list
     * @param eventType the type of the event
     * @return true if the event exists in the events list
     */
    public synchronized boolean eventsQueueContainsEvent(String eventType) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> iter = eventQueue.listIterator();
            GameEvent currentGameEvent;
            while (iter.hasNext()) {
                currentGameEvent = iter.next();
                if (currentGameEvent.type.equals(eventType))
                    return true;
            }
            return false;
        }
    }

    /**
     * Checks if a given event owned by a given class exists in the events list
     * @param eventType the type of the event
     * @param owner the owner (creator) class of the event
     * @return true if the event exists in the events list
     */
    public synchronized boolean eventsQueueContainsEventOwnedBy(String eventType, Object owner) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> iter = eventQueue.listIterator();
            GameEvent currentGameEvent;
            while (iter.hasNext()) {
                currentGameEvent = iter.next();
                if (currentGameEvent.type.equals(eventType) && currentGameEvent.owner == owner)
                    return true;
            }
            return false;
        }
    }

    public void setAdditionalDataEntry(String dataId, Object dataPayload) {
        additionalDataMap.put(dataId, dataPayload);
    }

    public Object getAdditionalDataEntry(String dataId) {
        return additionalDataMap.get(dataId);
    }

    public boolean additionalDataEntryExists(String dataId) {
        return additionalDataMap.containsKey(dataId);
    }

    /**
     * Get the first game event with a given type
     * @param type
     * @return the first event found with that type
     */
    public synchronized GameEvent getGameEventWithType(String type) {
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
    public synchronized void removeGameEventsWithType(String gameEventType) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.type.equals(gameEventType))
                    listIterator.remove();
            }
        }
    }

    public synchronized void removeGameEventsWithTypeOwnedBy(String gameEventType, Object owner) {
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
    public synchronized void removeAllGameEventsOwnedBy(Object owner) {
        synchronized (eventQueue) {
            ListIterator<GameEvent> listIterator = eventQueue.listIterator();
            while (listIterator.hasNext()) {
                GameEvent currentGameEvent = listIterator.next();
                if(currentGameEvent.owner != null && currentGameEvent.owner.equals(owner)) {
                    System.out.println(currentGameEvent.type);
                    listIterator.remove();
                }
            }
        }
    }

    public synchronized List<org.scify.engine.renderables.Renderable> getRenderableList() {
        return renderableList;
    }


    public synchronized void addRenderable(org.scify.engine.renderables.Renderable r) {
        renderableList.add(r);
    }

    public synchronized void addRenderables(List<org.scify.engine.renderables.Renderable> renderables) {
        renderableList.addAll(renderables);
    }

    public synchronized void removeRenderable(Renderable r) {
        renderableList.remove(r);
    }

    public synchronized Renderable getRenderable(Renderable renderable){
        synchronized (renderableList) {
            ListIterator<Renderable> listIterator = renderableList.listIterator();
            while (listIterator.hasNext()) {
                Renderable current = listIterator.next();
                if(current == renderable)
                    return renderable;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
