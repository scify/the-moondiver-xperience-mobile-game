package org.scify.moonwalker.app.actors;


public abstract class Player {
    protected String name;
    protected float score;
    protected float lives;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getLives() {
        return lives;
    }

    public void setLives(float lives) {
        this.lives = lives;
    }
}
