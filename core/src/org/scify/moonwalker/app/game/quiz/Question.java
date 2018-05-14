package org.scify.moonwalker.app.game.quiz;

import java.util.List;

public class Question {

    protected int id;
    protected String title;
    protected List<Answer> answers;
    public QuestionType type;
    protected int categoryId;

    public String getTitle() {
        return title;
    }

    public List<Answer> getAnswers(){
        return this.answers;
    }

    public Question() {
    }

    public Question(int id, String title, List<Answer> answers, QuestionType type, int categoryId) {
        this.id = id;
        this.title = title;
        this.answers = answers;
        this.type = type;
        this.categoryId = categoryId;
    }

    public boolean checkIfCorrectAnswer(Answer answer){
        if (null == answer)
            return false;
        for (Answer a : answers){
            if (a == answer && answer.isCorrect())
                return true;
        }
        return false;
    }

    public boolean isTextAnswerCorrect(String text) {
        if (null == text)
            return false;
        for (Answer a : answers){
            if(a.getText().toLowerCase().equals(text.toLowerCase()))
                return true;
        }
        return false;
    }
}
