package org.scify.moonwalker.app.game.rules;

import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.quiz.*;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.ThemeController;

import javax.swing.*;
import java.util.*;

import static org.scify.moonwalker.app.game.rules.episodes.BaseEpisodeRules.GAME_EVENT_AUDIO_START_UI;

public class QuestionConversationRules extends ConversationRules {
    public static final String PLAYER_HAS_ENOUGH_CORRECT = "player_has_enough_correct";
    public static final String CORRECT_ANSWERS = "CORRECT_ANSWERS";
    public static final String FIRST_TIME = "first_time";
    public static final String RETRY = "retry";
    public static final String PLAYER_HAS_LESS_THAN_ENOUGH_CORRECT = "player_has_less_than_enough_correct";
    public static final String AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY = "at_lest_one_day_remaining";
    public static final String AT_LEAST_ONE_DAY_NOTIFICATION_NOT_SHOWN = "notification_not_shown";
    public static final String AT_LEAST_ONE_DAY_NOTIFICATION_SHOWN = "notification_shown";
    public static final String NO_DAYS_REMAINING_FOR_THE_JOURNEY = "no_days_remaining";
    public static final String SET_NOTIFICATION_SHOWN_EVENT = "set_notification_shown";
    public static final String GAME_EVENT_HIGHLIGHT_AS_CORRECT = "GAME_EVENT_HIGHLIGHT_AS_CORRECT";
    public static final String GAME_EVENT_HIGHLIGHT_AS_WRONG = "GAME_EVENT_HIGHLIGHT_AS_WRONG";

    protected QuestionService questionService;
    protected List<QuestionCategory> questionCategories;
    protected List<Question> questions;
    protected String correctAudioPath;
    protected String wrongAudioPath;
    protected UserAction selectionUserAction = null;

    // this variable describes whether the last process conversation line
    // was of a question type.
    protected boolean lineProcessedIsQuestion;
    // conversation file to be shown if the quiz is successful
    protected String quizSuccessFulConversationFilePath;
    // conversation file to be shown if the quiz is not successful
    protected String quizFailedConversationFilePath;
    // we keep track of the last answer of the user to the quiz
    protected boolean lastQuizAnswerCorrect;
    // we keep track of whether this scientist is visited for the first time
    protected boolean firstTime;
    //minimum correct answers required
    protected int minimumCorrectAnswers;

    public QuestionConversationRules(String conversationJSONFilePath, String bgImgPath, String quizSuccessFulConversationFilePath, String quizFailedConversationFilePath, String correctAudioPath, String wrongAudioPath, boolean firstTime, int minimumCorrectAnswers) {
        super(conversationJSONFilePath, bgImgPath);
        //questionService = QuestionServiceJSON.getInstance();
        questionService = new QuestionServiceJSON();
        questionService.init();
        questionCategories = questionService.getQuestionCategories();
        questions = questionService.getQuestions();
        this.quizSuccessFulConversationFilePath = quizSuccessFulConversationFilePath;
        this.quizFailedConversationFilePath = quizFailedConversationFilePath;
        this.correctAudioPath = correctAudioPath;
        this.wrongAudioPath = wrongAudioPath;
        this.firstTime = firstTime;
        this.minimumCorrectAnswers = minimumCorrectAnswers;
    }

    @Override
    public GameState getNextState(final GameState gameState, final UserAction userAction) {
        if (gameState.getAdditionalDataEntry(CORRECT_ANSWERS) == null)
            gameState.setAdditionalDataEntry(CORRECT_ANSWERS, "0");

        // Handle correct/wrong events
        if (gameState.eventsQueueContainsEvent(GAME_EVENT_HIGHLIGHT_AS_CORRECT)) {
            GameEvent geCorrect = gameState.getGameEventWithType(GAME_EVENT_HIGHLIGHT_AS_CORRECT);
            final Renderable rToHighlight = (Renderable)geCorrect.parameters;
            final QuestionConversationRules qcr = this;
            EffectSequence es = new EffectSequence();
            es.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    // TODO: Apply button update
                    ((ActionButtonRenderable)rToHighlight).setButtonSkin(ThemeController.SKIN_CORRECT);
                }
            }));
            es.addEffect(new DelayEffect(1000));
            // Delayed call to handle conversation end
            es.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    qcr.superGetNextState(gameState, selectionUserAction);
                    selectionUserAction = null;
                }
            }));
            rToHighlight.addEffect(es);
            gameState.removeGameEventsWithType(GAME_EVENT_HIGHLIGHT_AS_CORRECT);

            return gameState;
        }
        // Handle correct/wrong events
        if (gameState.eventsQueueContainsEvent(GAME_EVENT_HIGHLIGHT_AS_WRONG)) {
            GameEvent geWrong = gameState.getGameEventWithType(GAME_EVENT_HIGHLIGHT_AS_WRONG);
            final Renderable rToHighlight = (Renderable)geWrong.parameters;
            final QuestionConversationRules qcr = this;
            EffectSequence es = new EffectSequence();
            es.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    ((ActionButtonRenderable)rToHighlight).setButtonSkin(ThemeController.SKIN_WRONG);
                }
            }));
            es.addEffect(new DelayEffect(1000));
            // Delayed call to handle conversation end
            es.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    qcr.superGetNextState(gameState, selectionUserAction);
                    selectionUserAction = null;
                }
            }));
            rToHighlight.addEffect(es);
            gameState.removeGameEventsWithType(GAME_EVENT_HIGHLIGHT_AS_WRONG);

            return gameState;
        }


        if (gotAnswer(userAction)) {
            // we need to check whether this answer was for a question-type
            // conversation line (which means that we got an answer
            // for a Question and we need to evaluate it)
            if (lineProcessedIsQuestion) {
                Map.Entry<Renderable, ConversationLine> payload = (Map.Entry<Renderable, ConversationLine>)userAction.getActionPayload();
                ConversationLine selectedLine = (ConversationLine) payload.getValue();
                // the answer instance was set as a payload in the conversation line
                // which is set as a payload to the user action
                Answer answer = (Answer) selectedLine.getPayload();
                evaluateAnswerAndSetGameInfo(answer, gameState, payload.getKey());
                selectionUserAction = userAction; // Store user action
                return gameState; // Do NOT follow normal conversation handling sequence, by calling parent.
            }
        }
        if (Integer.valueOf((String) gameState.getAdditionalDataEntry(CORRECT_ANSWERS)) >= minimumCorrectAnswers) {
            gameInfo.setLastQuizSuccessFull(true);
        } else {
            gameInfo.setLastQuizSuccessFull(false);
        }

        // Go to parent to handle base conversation flow
        return super.getNextState(gameState, userAction);
    }

    /**
     * Shortcut to call super getNextState to ascertain handling by parent as needed.
     * @param gameState The current game state
     * @param userAction The current user action
     */
    protected GameState superGetNextState(GameState gameState, UserAction userAction) {
        return super.getNextState(gameState, userAction);
    }

    protected void evaluateAnswerAndSetGameInfo(Answer answer, GameState gameState, Renderable source) {
        if (answer.isCorrect()) {
            increaseCorrectAnswers(gameState);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, correctAudioPath));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_HIGHLIGHT_AS_CORRECT, source));
        } else {
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, wrongAudioPath));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_HIGHLIGHT_AS_WRONG, source));
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
    protected boolean satisfiesPrerequisite(String prerequisite, GameState currentGameState) {
        boolean satisfiesPrerequisite = true;
        if (prerequisite.equals(PLAYER_HAS_ENOUGH_CORRECT)) {
            satisfiesPrerequisite = Integer.valueOf((String) currentGameState.getAdditionalDataEntry(CORRECT_ANSWERS)) >= minimumCorrectAnswers;
        }

        if (prerequisite.equals(PLAYER_HAS_LESS_THAN_ENOUGH_CORRECT)) {
            satisfiesPrerequisite = Integer.valueOf((String) currentGameState.getAdditionalDataEntry(CORRECT_ANSWERS)) < minimumCorrectAnswers;
        }

        if (prerequisite.equals(FIRST_TIME)) {
            satisfiesPrerequisite = firstTime;
        }

        if (prerequisite.equals(RETRY)) {
            satisfiesPrerequisite = !firstTime;
        }

        if (prerequisite.equals(AT_LEAST_ONE_DAY_REMAINING_FOR_THE_JOURNEY)) {
            satisfiesPrerequisite = gameInfo.getDaysLeftForDestination() >= 1;
        }

        if (prerequisite.equals(NO_DAYS_REMAINING_FOR_THE_JOURNEY)) {
            satisfiesPrerequisite = gameInfo.getDaysLeftForDestination() < 1;
        }

        if (prerequisite.equals(AT_LEAST_ONE_DAY_NOTIFICATION_NOT_SHOWN)) {
            satisfiesPrerequisite = !gameInfo.isNoDaysLeftNotificationShown();
        }

        if (prerequisite.equals(AT_LEAST_ONE_DAY_NOTIFICATION_SHOWN)) {
            satisfiesPrerequisite = gameInfo.isNoDaysLeftNotificationShown();
        }

        return satisfiesPrerequisite;
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> sAllEvents = lineExited.getOnExitCurrentOrderTrigger();

        if (sAllEvents.contains(SET_NOTIFICATION_SHOWN_EVENT)) {
            gameInfo.setNoDaysLeftNotificationShown(true);
        }

        super.onExitConversationOrder(gsCurrent, lineExited);
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

    protected Question question = null;

    protected void loadRandomQuestion(String eventName, ConversationLine lineEntered, GameState gsCurrent) {
        if (minimumCorrectAnswers > 1 || question == null) {
            if (eventName.equals(conversationRules.EVENT_LOAD_QUESTION)) {
                // load a random question regardless of it's category
                question = questionService.nextQuestion();
            } else {
                // if a category is present, we need to the the number after the last underscore
                // which indicates the id of the question category
                String categoryId = eventName.substring(eventName.lastIndexOf('_') + 1).trim();
                question = questionService.nextQuestionForCategory(Integer.parseInt(categoryId));
            }
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
        addMultipleConversationLines(lines, gsCurrent, false, new MultipleChoiceConversationRenderable(lines.get(0).getId(), question.getTitle(), bgImgPath, keepFirstDuringParsing, replaceLexicon), getIntroEffectForMultipleChoiceRenderable());
    }
}
