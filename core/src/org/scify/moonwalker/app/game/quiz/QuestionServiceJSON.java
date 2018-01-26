package org.scify.moonwalker.app.game.quiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.ArrayList;
import java.util.List;

public class QuestionServiceJSON implements QuestionService {

    private static QuestionServiceJSON instance;
    private Json json;
    private List<Question> allQuestions;
    protected ResourceLocator resourceLocator;

    private QuestionServiceJSON() {
        json = new Json();
        resourceLocator = new ResourceLocator();
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
        return json.fromJson(ArrayList.class, Question.class, Gdx.files.internal(resourceLocator.getFilePath("json_DB/questions.json")));
    }
}
