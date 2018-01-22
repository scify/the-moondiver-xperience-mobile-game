package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.EpisodeEndState;
import org.scify.engine.GameEvent;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.actors.Player;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.game.quiz.QuestionService;
import org.scify.moonwalker.app.game.quiz.QuestionServiceJSON;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

import java.util.Collections;
import java.util.List;

public class KnightQuestionsRules extends SinglePlayerRules {

    protected List<Question> allQuestions;
    protected QuestionService questionService;
    protected int questionIndex = 0;

    public KnightQuestionsRules() {
        questionService = QuestionServiceJSON.getInstance();
        allQuestions = questionService.getQuestions();
        Collections.shuffle(allQuestions);
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        handleGameStartingRules(gsCurrent);
        handlePositionEvents(gsCurrent);
        if(isGameFinished(gsCurrent))
            handleGameFinishedEvents(gsCurrent);
        return gsCurrent;
    }

    private void handleGameFinishedEvents(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_FINISHED")) {
            gsCurrent.getEventQueue().add(new GameEvent("EPISODE_FINISHED"));
            gsCurrent.getEventQueue().add(new GameEvent("BACKGROUND_IMG_UI", "img/episode_2/forest.jpg"));
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        MoonWalkerGameState gameState = (MoonWalkerGameState) gsCurrent;
        Player player = gameState.getPlayer();
        if(player.getScore() >= 2)
            return true;
        return super.isGameFinished(gsCurrent);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsFinal) {
        if(gsFinal.eventsQueueContainsEvent("CORRECT_ANSWER"))
            return EpisodeEndState.EPISODE_FINISHED_SUCCESS;
        return EpisodeEndState.EPISODE_FINISHED_FAILURE;
    }

    protected void handleGameStartingRules(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("BACKGROUND_IMG")) {
            gsCurrent.getEventQueue().add(new GameEvent("BACKGROUND_IMG"));
            gsCurrent.getEventQueue().add(new GameEvent("BACKGROUND_IMG_UI", "img/episode_2/forest.jpg"));
        }
    }

    protected void handlePositionEvents(GameState gameState) {
        if(gameState.eventsQueueContainsEvent("PLAYER_BOTTOM_BORDER")) {
            // add dialog object in game event
            gameState.getEventQueue().add(new GameEvent("QUESTION_UI", nextQuestion((MoonWalkerGameState)gameState)));
            gameState.getEventQueue().add(new GameEvent("PAUSE_GAME"));
            gameState.removeGameEventsWithType("PLAYER_BOTTOM_BORDER");
        }
        if(gameState.eventsQueueContainsEvent("PLAYER_LEFT_BORDER")) {
            // add dialog object in game event
            gameState.getEventQueue().add(new GameEvent("QUESTION_UI", nextQuestion((MoonWalkerGameState) gameState)));
            gameState.getEventQueue().add(new GameEvent("PAUSE_GAME"));
            gameState.removeGameEventsWithType("PLAYER_LEFT_BORDER");
        }
    }

    protected Question nextQuestion(MoonWalkerGameState gameState) {
        if(questionIndex == allQuestions.size())
            questionIndex = 0;
        Question question = allQuestions.get(questionIndex);
        questionIndex++;
        return question;
    }
}
