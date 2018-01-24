package org.scify.moonwalker.app.actors;


import org.scify.engine.Renderable;

public class Player extends Renderable {

    protected String name;
    protected int score = 0;
    protected int lives = 0;

    public Player(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
