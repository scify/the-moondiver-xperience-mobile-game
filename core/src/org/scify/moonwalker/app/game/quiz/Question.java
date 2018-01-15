package org.scify.moonwalker.app.game.quiz;

import java.util.List;

public class Question {
    protected String title;
    protected String body;
    protected List<Answer> answers;
    public QuestionType type;

    public String getBody(){
        return this.body;
    }

    public String getTitle() {
        return title;
    }

    public List<Answer> getAnswers(){
        return this.answers;
    }

    public Question() {
    }

    public Question(String title, String body, List<Answer> answers, QuestionType type) {
        this.title = title;
        this.body = body;
        this.answers = answers;
        this.type = type;
    }

    public Question(String body, List<Answer> answers, QuestionType type){
        this.body = body;
        this.answers = answers;
        this.type = type;
        this.title = "Ερώτηση";
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
