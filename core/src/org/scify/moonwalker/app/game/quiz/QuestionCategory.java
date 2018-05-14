package org.scify.moonwalker.app.game.quiz;

public class QuestionCategory {

    protected int id;
    protected String name;

    public QuestionCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public QuestionCategory() {
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return name;
    }

}
