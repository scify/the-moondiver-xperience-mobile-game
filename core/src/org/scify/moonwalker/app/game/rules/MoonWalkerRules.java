package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.game.quiz.QuestionService;
import org.scify.moonwalker.app.game.quiz.QuestionServiceJSON;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class MoonWalkerRules implements Rules {

    protected int worldX;
    protected int worldY;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        MoonWalkerGameState gameState = (MoonWalkerGameState) gsCurrent;
        if(userAction != null)
            handleUserAction(userAction, gameState);

        return gameState;
    }

    @Override
    public boolean isGamePaused(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PAUSE_GAME");
    }

    protected void handleGameFinishedEvents(GameState gsCurrent) {
        if(!gsCurrent.eventsQueueContainsEvent("EPISODE_FINISHED")) {
            gsCurrent.getEventQueue().add(new GameEvent("EPISODE_FINISHED"));
            gsCurrent.getEventQueue().add(new GameEvent("DISPOSE_RESOURCES_UI"));
            gsCurrent.getEventQueue().add(new GameEvent("EPISODE_SUCCESS_UI"));
        }
    }

    private void handleUserAction(UserAction userAction, MoonWalkerGameState gameState) {
        switch (userAction.getActionCode()) {
            case ANSWER_SELECTION:
                Answer answer = (Answer) userAction.getActionPayload();
                addEventsForAnswer(gameState, answer.isCorrect());
                break;
            case ANSWER_TEXT:
                HashMap.SimpleEntry<Question, TextField> questionText = (HashMap.SimpleEntry<Question, TextField>) userAction.getActionPayload();
                TextField textField = questionText.getValue();
                Question question = questionText.getKey();
                String userAnswer = textField.getText();
                boolean correctAns = question.isTextAnswerCorrect(userAnswer);
                addEventsForAnswer(gameState, correctAns);
                break;
        }
    }

    private void addEventsForAnswer(MoonWalkerGameState gameState, boolean isAnswerCorrect) {
        if(isAnswerCorrect) {
            gameState.getEventQueue().add(new GameEvent("CORRECT_ANSWER"));
            gameState.removeGameEventsWithType("PAUSE_GAME");
            gameState.getPlayer().setScore(gameState.getPlayer().getScore() + 1);
            gameState.getEventQueue().add(new GameEvent("PLAYER_SCORE", gameState.getPlayer().getScore()));
        } else {
            gameState.getEventQueue().add(new GameEvent("WRONG_ANSWER"));
            gameState.removeGameEventsWithType("PAUSE_GAME");
            gameState.getPlayer().setLives(gameState.getPlayer().getLives() - 1);
            gameState.getEventQueue().add(new GameEvent("PLAYER_LIVES", gameState.getPlayer().getLives()));
        }
    }
}
