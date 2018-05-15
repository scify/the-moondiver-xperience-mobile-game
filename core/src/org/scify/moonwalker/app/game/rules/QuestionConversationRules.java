package org.scify.moonwalker.app.game.rules;

import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.moonwalker.app.game.quiz.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestionConversationRules extends ConversationRules {
    protected QuestionService questionService;
    protected List<QuestionCategory> questionCategories;
    protected List<Question> questions;

    public QuestionConversationRules(String conversationJSONFilePath) {
        super(conversationJSONFilePath);
        questionService = QuestionServiceJSON.getInstance();
        questionService.init();
        questionCategories = questionService.getQuestionCategories();
        questions = questionService.getQuestions();
    }

    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        GameState conversationState = super.getNextState(gameState, userAction);
        onEnterConversationOrder(conversationState, getCurrentConversationLine(conversationState));
        return conversationState;
    }

    @Override
    protected void addSingleChoiceConversationLine(ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        if(!conversationLine.getText().equals("") && !conversationLineHasRandomQuestionEvent(conversationLine))
            super.addSingleChoiceConversationLine(conversationLine, gameState, newSpeaker);
        else {
            Set<String> eventTrigger;
            eventTrigger = conversationLine.getOnEnterCurrentOrderTrigger();
            loadRandomQuestionEvent(eventTrigger, conversationLine, gameState);

        }
    }

    private boolean conversationLineHasRandomQuestionEvent(ConversationLine conversationLine) {
        for(String eventName: conversationLine.getOnEnterCurrentOrderTrigger())
            if(eventName.contains(conversationRules.EVENT_LOAD_QUESTION))
                return true;
        return false;
    }

    protected void loadRandomQuestionEvent(Set<String> eventTrigger, ConversationLine lineEntered, GameState gsCurrent) {
        for(String eventName: eventTrigger) {
            if(eventName.contains(conversationRules.EVENT_LOAD_QUESTION)) {
                Question question = null;
                if(eventName.equals(conversationRules.EVENT_LOAD_QUESTION)) {
                    // load a random question regardless of it's category
                    question = questionService.nextQuestion();

                } else {
                    // if a category is present, we need to the the number after the last underscore
                    // which indicates the id of the question category
                    String categoryId = eventName.substring(eventName.lastIndexOf('_') + 1).trim();
                    question = questionService.nextQuestionForCategory(Integer.parseInt(categoryId));
                }
                addQuestionAsRenderable(question, gsCurrent, lineEntered);
            }
        }
    }

    protected void addQuestionAsRenderable(Question question, GameState gsCurrent, ConversationLine lineEntered) {
        List<ConversationLine> lines = new ArrayList<>();
        for(Answer answer: question.getAnswers()) {
            ConversationLine line = new ConversationLine();
            line.setText(answer.getText());
            line.setId(question.getId());
            line.setSpeakerId(lineEntered.getSpeakerId());
            lines.add(line);
        }
        addMultipleConversationLines(lines, gsCurrent, false, new MultipleChoiceConversationRenderable(lines.get(0).getId(), question.getTitle()), getIntroEffectForMultipleChoiceRenderable());
    }
}
