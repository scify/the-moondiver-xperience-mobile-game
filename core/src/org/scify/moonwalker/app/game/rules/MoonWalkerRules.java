package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.rules.Rules;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.engine.GameState;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.engine.UserAction;
import org.scify.moonwalker.app.game.quiz.Question;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.engine.renderables.ActionButtonWithEffect;

import java.util.HashMap;
import java.util.Map;

public abstract class MoonWalkerRules implements Rules<GameState, UserAction, EpisodeEndState> {

    protected int worldX;
    protected int worldY;
    protected Map<String, org.scify.engine.renderables.Renderable> idToRenderable;
    protected ConversationRules conversationRules;
    protected AppInfo appInfo;
    protected MoonWalkerPhysicsRules physics;
    protected float ESCAPE_BUTTON_SIZE_PIXELS = 70;
    protected float ESCAPE_BUTTON_PADDING_PIXELS = 10;
    protected GameState initialGameState;
    protected GameInfo gameInfo;

    public MoonWalkerRules() {
        idToRenderable = new HashMap<>();
        appInfo = AppInfo.getInstance();
        gameInfo = GameInfo.getInstance();
        worldX = appInfo.getScreenWidth();
        worldY = appInfo.getScreenHeight();
        physics = new MoonWalkerPhysicsRules(worldX, worldY);
    }

    public void setInitialState(GameState initialGameState) {
        this.initialGameState = initialGameState;
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

    protected void addRenderableEntry(String rId, org.scify.engine.renderables.Renderable renderable) {
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
        gsCurrent.addGameEvent(new GameEvent("CONVERSATION_STARTED", null, this));
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

    protected ActionButtonWithEffect createEscapeButton() {
        float btnRealSize = appInfo.pixelsWithDensity(ESCAPE_BUTTON_SIZE_PIXELS);
        ActionButtonWithEffect escape = new ActionButtonWithEffect(0, appInfo.getScreenHeight() - btnRealSize, btnRealSize, btnRealSize, "image_button", "escape_button");
        escape.setPadding(appInfo.pixelsWithDensity(ESCAPE_BUTTON_PADDING_PIXELS));
        escape.setImgPath("img/close.png");
        return escape;
    }

    protected void cleanUpGameState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
    }

    protected ActionButtonWithEffect createImageButton(String id, String imgPath, UserAction userAction, float widthPixels, float heightPixels) {
        ActionButtonWithEffect button = new ActionButtonWithEffect("image_button", id);
        button.setImgPath(imgPath);
        button.setUserAction(userAction);
        button.setWidth(appInfo.pixelsWithDensity(widthPixels));
        button.setHeight(appInfo.pixelsWithDensity(heightPixels));
        return button;
    }

    protected void addEpisodeBackgroundImage(GameState currentState, String imgPath) {
        currentState.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", imgPath));
    }

    protected boolean isConversationOngoing(GameState gsCurrent) {
        return isConversationStarted(gsCurrent) && ! isConversationFinished(gsCurrent);
    }

    protected boolean conversationHasNotStartedAndNotFinished(GameState gsCurrent) {
        return !isConversationStarted(gsCurrent) && !isConversationFinished(gsCurrent);
    }

    protected boolean isConversationStarted(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_STARTED");
    }

    protected boolean isConversationFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED");
    }

    protected void setFieldsForTimedEpisode(GameState gsCurrent, String imgPath, int millisecondsToLast) {
        if (imgPath != null) {
            gsCurrent.storeAdditionalDataEntry("timed_episode_img_path", imgPath);
        }
        gsCurrent.storeAdditionalDataEntry("timed_episode_milliseconds", millisecondsToLast);
    }
}
