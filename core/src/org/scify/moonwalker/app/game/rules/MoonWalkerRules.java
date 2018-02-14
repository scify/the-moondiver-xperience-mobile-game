package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.engine.*;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.GameState;
import org.scify.engine.conversation.ConversationRules;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.util.HashMap;
import java.util.Map;

public abstract class MoonWalkerRules implements Rules<GameState, UserAction, EpisodeEndState> {

    protected int worldX;
    protected int worldY;
    protected Map<String, Renderable> idToRenderable;
    protected ConversationRules conversationRules;
    protected GameState gameState;
    protected GameInfo gameInfo;
    protected MoonWalkerPhysicsRules physics;
    protected float ESCAPE_BUTTON_WIDTH;
    protected float ESCAPE_BUTTON_HEIGHT;

    public MoonWalkerRules() {
        idToRenderable = new HashMap<>();
        gameInfo = GameInfo.getInstance();
        worldX = gameInfo.getScreenWidth();
        worldY = gameInfo.getScreenHeight();
        physics = new MoonWalkerPhysicsRules(worldX, worldY);
        ESCAPE_BUTTON_WIDTH = gameInfo.getScreenHeight() * 0.2f;
        ESCAPE_BUTTON_HEIGHT = ESCAPE_BUTTON_WIDTH;
    }

    /**
     * This setter serves the need for an episode
     * to define their own set of physics rules.
     * for example, an episode might have physics rules
     * without gravity
     * @param physics the new {@link MoonWalkerPhysicsRules} instance
     */
    public void setPhysics(MoonWalkerPhysicsRules physics) {
        this.physics = physics;
    }
    protected boolean renderableExist(String rId) {
        return idToRenderable.get(rId) != null;
    }

    protected void addRenderableEntry(String rId, Renderable renderable) {
        idToRenderable.put(rId, renderable);
    }

    public Renderable getRenderableById(String rId) {
        return idToRenderable.get(rId);
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
            gameState.getPlayer().incrementScore();
            gameState.getEventQueue().add(new GameEvent("PLAYER_SCORE", gameState.getPlayer().getScore()));
        } else {
            gameState.getEventQueue().add(new GameEvent("WRONG_ANSWER"));
            gameState.getPlayer().setLives(gameState.getPlayer().getLives() - 1);
            gameState.getEventQueue().add(new GameEvent("PLAYER_LIVES", gameState.getPlayer().getLives()));
        }
        resumeGame(gameState);
    }

    protected void pauseGame(GameState gameState) {
        gameState.getEventQueue().add(new GameEvent("PAUSE_GAME"));
    }

    protected void resumeGame(GameState gameState) {
        gameState.removeGameEventsWithType("PAUSE_GAME");
    }

    protected void createConversation(GameState gsCurrent, String conversationResFile) {
        conversationRules = new ConversationRules(conversationResFile);
        gsCurrent.addGameEvent(new GameEvent("CONVERSATION_STARTED"));
    }

    @Override
    public void disposeResources() {
        physics.disposeResources();
    }

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
}
