package org.scify.moonwalker.app.game.conversation;

public class RandomResponse {

    protected String id;
    protected String text;
    // when a response gets read, this variable is set to true
    protected boolean visited;

    public RandomResponse(String id, String text) {
        this.id = id;
        this.text = text;
        this.visited = false;
    }

    public RandomResponse() {
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
