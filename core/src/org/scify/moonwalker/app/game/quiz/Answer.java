package org.scify.moonwalker.app.game.quiz;

public class Answer {
    private String text;
    private boolean isCorrect;

    public String getText() {

        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public Answer() {
    }

    public Answer(String text) {
        this.text = text;
        this.isCorrect = false;
    }
    public Answer(String text, boolean isCorrect){
        this.text = text;
        this.isCorrect = isCorrect;
    }
}
