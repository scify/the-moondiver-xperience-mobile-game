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
        // If got an answer

        GameState conversationState = super.getNextState(gameState, userAction);
        onEnterConversationOrder(conversationState, getCurrentConversationLine(conversationState));
        if (gotAnswer(userAction)) {
            // we need to check whether this answer was for a question-type
            // conversation line (which means that we got an answer
            // for a Question and we need to evaluate it)
            if(conversationLineHasRandomQuestionEvent(getCurrentConversationLine(conversationState))) {
                // the answer instance was set as a payload in the conversation line
                // which is set as a payload to the user action
                ConversationLine selectedLine = (ConversationLine) userAction.getActionPayload();
                Answer answer = (Answer) selectedLine.getPayload();
                evaluateAnswerAndSetGameInfo(answer);
            }
        }
        return conversationState;
    }

    protected void evaluateAnswerAndSetGameInfo(Answer answer) {
        gameInfo.setLastQuizAnswerCorrect(answer.isCorrect());
    }

    @Override
    protected void addSingleChoiceConversationLine(ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        if(!conversationLine.getText().equals("") && !conversationLineHasRandomQuestionEvent(conversationLine))
            super.addSingleChoiceConversationLine(conversationLine, gameState, newSpeaker);
        else {
            Set<String> eventTrigger;
            eventTrigger = conversationLine.getOnEnterCurrentOrderTrigger();
            processSingleLine(eventTrigger, conversationLine, gameState);
        }
    }

    private boolean conversationLineHasRandomQuestionEvent(ConversationLine conversationLine) {
        for(String eventName: conversationLine.getOnEnterCurrentOrderTrigger())
            if(eventName.contains(conversationRules.EVENT_LOAD_QUESTION))
                return true;
        return false;
    }

    private boolean conversationLineHasRandomResponseEvent(ConversationLine conversationLine) {
        for(String eventName: conversationLine.getOnEnterCurrentOrderTrigger())
            if(eventName.contains(conversationRules.EVENT_RANDOM_RESPONSE))
                return true;
        return false;
    }

    protected void processSingleLine(Set<String> eventTrigger, ConversationLine lineEntered, GameState gsCurrent) {
        for(String eventName: eventTrigger) {
            if(eventName.contains(conversationRules.EVENT_LOAD_QUESTION)) {
                loadRandomQuestion(eventName, lineEntered, gsCurrent);
            } else if(eventName.equals("load_response_for_question")) {
                loadResponseForQuestion(lineEntered, gsCurrent);
            }
        }
    }

    protected void loadRandomQuestion(String eventName, ConversationLine lineEntered, GameState gsCurrent) {
        Question question;
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

    protected void loadResponseForQuestion(ConversationLine lineEntered, GameState gsCurrent) {
        ConversationLine responseLine = new ConversationLine();
        try {
            responseLine.setText(randomResponseFactory.getRandomResponseFor(gameInfo.isLastQuizAnswerCorrect() ? EVENT_RANDOM_CORRECT : EVENT_RANDOM_WRONG));
        } catch (Exception e) {
            e.printStackTrace();
            responseLine.setText(e.getMessage());
        }
        responseLine.setSpeakerId(lineEntered.getSpeakerId());
        responseLine.setId(lineEntered.getId());
        addSingleChoiceConversationLine(responseLine, gsCurrent, false);
    }

    protected void addQuestionAsRenderable(Question question, GameState gsCurrent, ConversationLine lineEntered) {
        List<ConversationLine> lines = new ArrayList<>();
        lineEntered.setPayload(question);
        for(Answer answer: question.getAnswers()) {
            ConversationLine line = new ConversationLine();
            line.setText(answer.getText());
            line.setId(lineEntered.getId());
            line.setSpeakerId(lineEntered.getSpeakerId());
            lines.add(line);
            line.setPayload(answer);
        }
        addMultipleConversationLines(lines, gsCurrent, false, new MultipleChoiceConversationRenderable(lines.get(0).getId(), question.getTitle()), getIntroEffectForMultipleChoiceRenderable());
    }
}
