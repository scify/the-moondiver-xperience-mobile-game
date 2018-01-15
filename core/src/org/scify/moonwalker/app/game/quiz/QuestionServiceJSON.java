package org.scify.moonwalker.app.game.quiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceJSON implements QuestionService {

    private static QuestionServiceJSON instance;
    private Json json;
    private List<Question> allQuestions;

    private QuestionServiceJSON() {
        json = new Json();
        allQuestions = getQuestionsFromDB();
    }

    public static QuestionServiceJSON getInstance() {
        if(instance == null)
            instance = new QuestionServiceJSON();
        return instance;
    }

    @Override
    public List<Question> getQuestions() {
        return allQuestions;
    }

    private List<Question> getQuestionsFromDB() {
        return json.fromJson(ArrayList.class, Question.class, Gdx.files.internal("data/json_DB/questions.json"));
    }
}
