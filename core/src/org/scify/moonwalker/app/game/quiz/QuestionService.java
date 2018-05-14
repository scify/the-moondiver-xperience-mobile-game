package org.scify.moonwalker.app.game.quiz;

import java.util.List;

public interface QuestionService {

    List<Question> getQuestions();

    List<QuestionCategory> getQuestionCategories();

    List<Question> getQuestionsForQuestionCategory(QuestionCategory category);
}
