package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.quiz.Question;

import java.util.HashMap;

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
            gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
            gsCurrent.addGameEvent(new GameEvent("DISPOSE_RESOURCES_UI"));
            gsCurrent.addGameEvent(new GameEvent("EPISODE_SUCCESS_UI"));
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
            gameState.addGameEvent(new GameEvent("CORRECT_ANSWER"));
            gameState.getPlayer().incrementScore();
            gameState.addGameEvent(new GameEvent("PLAYER_SCORE", gameState.getPlayer().getScore()));
        } else {
            gameState.addGameEvent(new GameEvent("WRONG_ANSWER"));
            gameState.getPlayer().setLives(gameState.getPlayer().getLives() - 1);
            gameState.addGameEvent(new GameEvent("PLAYER_LIVES", gameState.getPlayer().getLives()));
        }
        unPauseGame(gameState);
    }

    protected void pauseGame(GameState gameState) {
        gameState.addGameEvent(new GameEvent("PAUSE_GAME"));
    }

    protected void unPauseGame(GameState gameState) {
        gameState.removeGameEventsWithType("PAUSE_GAME");
    }
}
