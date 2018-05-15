package org.scify.moonwalker.app.game.quiz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionServiceJSON implements QuestionService {

    private static QuestionServiceJSON instance;
    private Json json;
    private List<Question> allQuestions;
    private List<QuestionCategory> allQuestionCategories;
    protected ResourceLocator resourceLocator;
    protected int questionIndex;

    private QuestionServiceJSON() {
        json = new Json();
        resourceLocator = new ResourceLocator();
        allQuestions = getQuestionsFromDB();
        allQuestionCategories = getQuestionCategoriesFromDB();
        questionIndex = 0;
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

    @Override
    public List<QuestionCategory> getQuestionCategories() {
        return allQuestionCategories;
    }

    @Override
    public List<Question> getQuestionsForQuestionCategory(QuestionCategory category) {
        List<Question> questionsForCategory = new ArrayList<>();
        for(Question question: allQuestions) {
            if(question.categoryId == category.getId())
                questionsForCategory.add(question);
        }
        return questionsForCategory;
    }

    private List<Question> getQuestionsFromDB() {
        return json.fromJson(ArrayList.class, Question.class, Gdx.files.internal(resourceLocator.getFilePath("questions/questions.json")));
    }

    private List<QuestionCategory> getQuestionCategoriesFromDB() {
        return json.fromJson(ArrayList.class, QuestionCategory.class, Gdx.files.internal(resourceLocator.getFilePath("questions/categories.json")));
    }

    @Override
    public Question nextQuestion() {
        if(questionIndex == allQuestions.size())
            questionIndex = 0;
        Question question = allQuestions.get(questionIndex);
        questionIndex++;
        return question;
    }

    @Override
    public Question nextQuestionForCategory(int categoryId) {
        for(int i = questionIndex; i < allQuestions.size(); i++) {
            Question question = allQuestions.get(i);
            if(categoryId == question.categoryId) {
                allQuestions.remove(question);
                return question;
            }
        }
        return null;
    }

    @Override
    public void init() {
        this.questionIndex = 0;
        Collections.shuffle(allQuestions);
    }
}
