package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.rules.Rules;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.game.GameInfo;
import org.scify.moonwalker.app.game.quiz.*;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.MoonWalkerRenderingEngine;

import java.util.HashMap;
import java.util.Map;

public abstract class MoonWalkerBaseRules implements Rules<GameState, UserAction, EpisodeEndState> {

    public static final String TIMED_EPISODE_IMG_PATH = "timed_episode_img_path";
    public static final String TIMED_EPISODE_MILLISECONDS = "timed_episode_milliseconds";
    protected int worldX;
    protected int worldY;
    protected Map<String, org.scify.engine.renderables.Renderable> idToRenderable;
    protected ConversationRules conversationRules;
    protected AppInfo appInfo;
    protected MoonWalkerPhysicsRules physics;
    protected GameState initialGameState;
    protected GameInfo gameInfo;

    public MoonWalkerBaseRules() {
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
            case UserActionCode.ANSWER_SELECTION:
                Answer answer = (Answer) userAction.getActionPayload();
                addEventsForAnswer(gameState, answer.isCorrect());
                break;
            case UserActionCode.ANSWER_TEXT:
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
        if (conversationRules == null) {
            conversationRules = new ConversationRules(conversationResFile);
            conversationRules.setStarted(true);
        }
    }

    @Override
    public void disposeResources() {
        physics.disposeResources();
    }

    /**
     * Returns next game state. If a conversation has been created, by createConversation, then it is also handled.
     * @param gsCurrent The current state.
     * @param userAction Any user action received.
     * @return The updated state.
     */
    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        MoonWalkerGameState gameState = (MoonWalkerGameState) gsCurrent;
        if(userAction != null)
            handleUserAction(userAction, gameState);

        // If I have a conversation
        if (conversationRules != null) {
            // Handle it
            handleConversationRules(gsCurrent, userAction);
        }
        return gameState;
    }

    @Override
    public boolean isGamePaused(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PAUSE_GAME");
    }

    protected GameState cleanUpGameState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        if (conversationRules != null) {
            conversationRules.cleanAllConversationEvents(currentState);
            conversationRules = null;
        }
        return currentState;
    }


    protected void addEpisodeBackgroundImage(GameState currentState, String imgPath) {
        currentState.addGameEvent(new GameEvent(MoonWalkerRenderingEngine.BACKGROUND_IMG_UI, imgPath));
    }


    protected void setFieldsForTimedEpisode(GameState gsCurrent, String imgPath, int millisecondsToLast) {
        if (imgPath != null) {
            gsCurrent.setAdditionalDataEntry(TIMED_EPISODE_IMG_PATH, imgPath);
        }
        gsCurrent.setAdditionalDataEntry(TIMED_EPISODE_MILLISECONDS, millisecondsToLast);
    }

    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        if (conversationRules == null)
        {
            return gsCurrent;
        }
        // If conversation ongoing
        if (conversationRules.isStarted() && !conversationRules.isFinished()) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }

        handleTriggerEventForCurrentConversationLine(gsCurrent, conversationRules.getCurrentConversationLine(gsCurrent));
        return gsCurrent;
    }

    /**
     * Handles all enter and exit events per conversation line.
     * @param gsCurrent The current game state.
     * @param currentConversationLine The current conversation line of interest (in multiple alternatives we expect
     *                                one event per alternative line).
     */
    protected void handleTriggerEventForCurrentConversationLine(GameState gsCurrent, ConversationLine currentConversationLine) {
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            onEnterConversationOrder(gsCurrent, currentConversationLine);
            gsCurrent.removeGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT);
        }
        if (gsCurrent.eventsQueueContainsEvent(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            onExitConversationOrder(gsCurrent, currentConversationLine);
            gsCurrent.removeGameEventsWithType(ConversationRules.ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT);
        }
    }

    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
    }

    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {

    }
}
