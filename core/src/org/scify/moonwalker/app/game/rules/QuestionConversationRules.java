package org.scify.moonwalker.app.game.rules;

import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.moonwalker.app.game.quiz.*;

import java.util.*;

import static org.scify.moonwalker.app.game.rules.episodes.BaseEpisodeRules.GAME_EVENT_AUDIO_START_UI;

public class QuestionConversationRules extends ConversationRules {
    public static final String PLAYER_HAS_3_CORRECT = "player_has_3_correct";
    public static final String CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String PLAYER_HAS_LESS_THAN_3_CORRECT = "player_has_less_than_3_correct";

    protected QuestionService questionService;
    protected List<QuestionCategory> questionCategories;
    protected List<Question> questions;
    protected String correctAudioPath;
    protected String wrongAudioPath;

    // this variable describes whether the last process conversation line
    // was of a question type.
    protected boolean lineProcessedIsQuestion;
    // conversation file to be shown if the quiz is successful
    protected String quizSuccessFulConversationFilePath;
    // conversation file to be shown if the quiz is not successful
    protected String quizFailedConversationFilePath;
    // we keep track of the last answer of the user to the quiz
    protected boolean lastQuizAnswerCorrect;

    public QuestionConversationRules(String conversationJSONFilePath, String bgImgPath, String quizSuccessFulConversationFilePath, String quizFailedConversationFilePath, String correctAudioPath, String wrongAudioPath) {
        super(conversationJSONFilePath, bgImgPath);
        questionService = QuestionServiceJSON.getInstance();
        questionService.init();
        questionCategories = questionService.getQuestionCategories();
        questions = questionService.getQuestions();
        this.quizSuccessFulConversationFilePath = quizSuccessFulConversationFilePath;
        this.quizFailedConversationFilePath = quizFailedConversationFilePath;
        this.correctAudioPath = correctAudioPath;
        this.wrongAudioPath = wrongAudioPath;
    }

    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        if(gameState.getAdditionalDataEntry(CORRECT_ANSWERS) == null)
            gameState.setAdditionalDataEntry(CORRECT_ANSWERS, "0");
        if (gotAnswer(userAction)) {
            // we need to check whether this answer was for a question-type
            // conversation line (which means that we got an answer
            // for a Question and we need to evaluate it)
            if (lineProcessedIsQuestion) {
                ConversationLine selectedLine = (ConversationLine) userAction.getActionPayload();
                // the answer instance was set as a payload in the conversation line
                // which is set as a payload to the user action
                Answer answer = (Answer) selectedLine.getPayload();
                evaluateAnswerAndSetGameInfo(answer, gameState);
            }
        }
        if (Integer.valueOf((String) gameState.getAdditionalDataEntry(CORRECT_ANSWERS)) >= 3) {
            gameInfo.setConversationFileForContactScreen(this.quizSuccessFulConversationFilePath);
        } else {
            gameInfo.setConversationFileForContactScreen(this.quizFailedConversationFilePath);
        }
        return super.getNextState(gameState, userAction);
    }

    protected void evaluateAnswerAndSetGameInfo(Answer answer, GameState gameState) {
        if (answer.isCorrect()) {
            increaseCorrectAnswers(gameState);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, correctAudioPath));
        }else {
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, wrongAudioPath));
        }
        this.lastQuizAnswerCorrect = answer.isCorrect();
    }

    @Override
    protected List<ConversationLine> extractNextLines(GameState currentGameState, UserAction userAction) {
        lineProcessedIsQuestion = false;
        List<ConversationLine> possibleNextLines = new LinkedList<>(super.extractNextLines(currentGameState, userAction));
        ListIterator<ConversationLine> iLines = possibleNextLines.listIterator();
        // For each item
        while (iLines.hasNext()) {
            // Remove it, if it cannot cope with the prerequisites
            if (!satisfiesPrerequisites(iLines.next(), currentGameState))
                iLines.remove();
        }

        return possibleNextLines;
    }

    @Override
    protected boolean satisfiesPrerequisites(ConversationLine next, GameState currentGameState) {
        boolean bSuperOK = super.satisfiesPrerequisites(next, currentGameState);

        if (!bSuperOK)
            return false;

        // If we need 3 correct
        if (next.getPrerequisites().contains(PLAYER_HAS_3_CORRECT)) {
            return Integer.valueOf((String) currentGameState.getAdditionalDataEntry(CORRECT_ANSWERS)) >= 3;
        }

        if (next.getPrerequisites().contains(PLAYER_HAS_LESS_THAN_3_CORRECT)) {
            return Integer.valueOf((String) currentGameState.getAdditionalDataEntry(CORRECT_ANSWERS)) < 3;
        }


        return true;
    }

    protected void increaseCorrectAnswers(GameState currentGameState) {
        // Initialize value as needed
        if (!currentGameState.additionalDataEntryExists(CORRECT_ANSWERS)) {
            currentGameState.setAdditionalDataEntry(CORRECT_ANSWERS, String.valueOf(1));
        } else {
            currentGameState.setAdditionalDataEntry(CORRECT_ANSWERS, String.valueOf(
                    Integer.valueOf((String) currentGameState.getAdditionalDataEntry(CORRECT_ANSWERS)) + 1));
        }
    }

    @Override
    protected void addSingleChoiceConversationLine(ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        // If we do not have a random question/answer line
        if (!(conversationLineHasRandomQuestionEvent(conversationLine) || conversationLineHasRandomResponseEvent(conversationLine)))
            // handle things as usual
            super.addSingleChoiceConversationLine(conversationLine, gameState, newSpeaker);
        else {
            // else replace the single line conversation with an appropriate replacement
            replaceSingleLineWithDynamicContent(conversationLine, gameState);
        }
    }


    private boolean conversationLineHasRandomQuestionEvent(ConversationLine conversationLine) {
        for (String eventName : conversationLine.getOnEnterCurrentOrderTrigger())
            if (eventName.contains(conversationRules.EVENT_LOAD_QUESTION))
                return true;
        return false;
    }

    private boolean conversationLineHasRandomResponseEvent(ConversationLine conversationLine) {
        for (String eventName : conversationLine.getOnEnterCurrentOrderTrigger())
            if (eventName.contains(conversationRules.EVENT_RANDOM_RESPONSE))
                return true;
        return false;
    }

    /**
     * This function, in the place of a given conversation line, loads and creates renderables for a multiple-choice
     * question, or a randomly chosen single line.
     *
     * @param lineEntered The line to be replaced.
     * @param gsCurrent   The current game state.
     */
    protected void replaceSingleLineWithDynamicContent(ConversationLine lineEntered, GameState gsCurrent) {
        Set<String> eventTrigger = lineEntered.getOnEnterCurrentOrderTrigger();

        for (String eventName : eventTrigger) {
            if (eventName.contains(conversationRules.EVENT_LOAD_QUESTION)) {
                loadRandomQuestion(eventName, lineEntered, gsCurrent);
                lineProcessedIsQuestion = true;
            } else if (eventName.equals(conversationRules.EVENT_RANDOM_RESPONSE)) {
                loadResponseForQuestion(lineEntered, gsCurrent);
            }
        }
    }

    protected void loadRandomQuestion(String eventName, ConversationLine lineEntered, GameState gsCurrent) {
        Question question;
        if (eventName.equals(conversationRules.EVENT_LOAD_QUESTION)) {
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
            responseLine.setText(randomResponseFactory.getRandomResponseFor(this.lastQuizAnswerCorrect ? EVENT_RANDOM_CORRECT : EVENT_RANDOM_WRONG));
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
        for (Answer answer : question.getAnswers()) {
            ConversationLine line = new ConversationLine();
            line.setText(answer.getText());
            line.setId(lineEntered.getId());
            line.setSpeakerId(lineEntered.getSpeakerId());
            lines.add(line);
            line.setPayload(answer);
        }
        addMultipleConversationLines(lines, gsCurrent, false, new MultipleChoiceConversationRenderable(lines.get(0).getId(), question.getTitle(), bgImgPath, keepFirstDuringParsing), getIntroEffectForMultipleChoiceRenderable());
    }
}
