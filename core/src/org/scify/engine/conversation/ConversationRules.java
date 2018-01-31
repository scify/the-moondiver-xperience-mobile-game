package org.scify.engine.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.MoonWalkerRules;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.components.MultipleConversationLinesComponent;
import org.scify.moonwalker.app.ui.components.SingleConversationLineComponent;
import org.scify.moonwalker.app.ui.input.UserActionCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConversationRules extends MoonWalkerRules {
    protected List<ConversationLine> conversationLines;
    private Json json;
    protected ResourceLocator resourceLocator;
    protected String ID;
    private static final String TAG = ConversationRules.class.getName();
    private GameInfo gameInfo;
    public ConversationRules(String conversationJSONFilePath) {
        gameInfo = GameInfo.getInstance();
        conversationLines = new ArrayList<>();
        resourceLocator = new ResourceLocator();
        json = new Json();
        conversationLines = json.fromJson(ArrayList.class, ConversationLine.class, Gdx.files.internal(resourceLocator.getFilePath(conversationJSONFilePath)));
        ID = UUID.randomUUID().toString();
        System.out.println(conversationLines.size());
    }

    @Override
    public GameState getInitialState() {
        throw new RuntimeException("Conversation Rules cannot initialize a Game State.");
    }

    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        // If got an answer (TEXT, BUTTON, ...)
        if (userAction != null && userAction.getActionCode().equals(UserActionCode.NEXT_CONVERSATION_LINE)) {
            // Clear paused conversation flag
            gameState.removeGameEventsWithType("CONVERSATION_PAUSED");
            gameState.addGameEvent(new GameEvent("REMOVE_CONVERSATIONS"));
        }

        //TODO Add check for user answer to multiple choice question

        // If conversation is paused
        if (gameState.eventsQueueContainsEvent("CONVERSATION_PAUSED"))
            // return the current game state
            return gameState;

        // Get next alternatives
        List<ConversationLine> nextLines = getPossibleNextLines(gameState, userAction);

        // If one line returned
        if (nextLines.size() == 1) {
            // render it
            SingleConversationLineComponent conversationLineComponent = new SingleConversationLineComponent( nextLines.get(0), getCurrentSpeaker( nextLines.get(0)), "img/avatars/yoda-1.jpg", true);
            ArrayList<Object> payload = new ArrayList<>();
            payload.add(conversationLineComponent);
            // Declare which user action should be thrown when button is pressed
            payload.add(new UserAction(UserActionCode.NEXT_CONVERSATION_LINE));
            gameState.addGameEvent(new GameEvent("CONVERSATION_LINE",
                    payload));
            // update current line and speaker
            setCurrentConversationLine(gameState, nextLines.get(0));
            // await next event
            gameState.addGameEvent(new GameEvent("CONVERSATION_PAUSED"));
        } else {
            // render dialog
            // TODO add Multiple Selection Component
            //gameState.addGameEvent(new GameEvent("SHOW_DIALOG", nextLines));
            gameState.addGameEvent(new GameEvent("CONVERSATION_PAUSED"));
            MultipleConversationLinesComponent conversationLinesComponent = new MultipleConversationLinesComponent(getCurrentConversationLine(gameState).text, nextLines);
            gameState.addGameEvent(new GameEvent("CONVERSATION_LINES", conversationLinesComponent));
            //pauseGame(gameState);
        }

        return gameState;

    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public void disposeResources() {
        // Remove speaker renderables
        // Clear current line
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        return null;
    }

    public ConversationLine getCurrentConversationLine(GameState gameState) {
        return (ConversationLine) gameState.getAdditionalDataEntry(ID);
    }

    public Renderable getCurrentSpeaker(ConversationLine conversationLine) {
        return getRenderableById(conversationLine.getSpeakerId());
    }

    protected void setCurrentConversationLine(GameState gameState, ConversationLine currentLine) {
        gameState.storeAdditionalDataEntry(ID, currentLine);
        addSpeakersAsNeeded(gameState, currentLine);
    }

    protected void addSpeakersAsNeeded(GameState state, ConversationLine currentLine) {
        // If the speaker does not exist
        if (!renderableExist(currentLine.getSpeakerId())) {
            // add the renderable character
            Renderable newSpeaker = new Renderable(100, 200, gameInfo.getScreenWidth() * 0.3f, gameInfo.getScreenWidth() * 0.3f, currentLine.speakerId, currentLine.speakerId);
            // update my lookup map
            addRenderableEntry(currentLine.speakerId, newSpeaker);
            // update state
            state.addRenderable(newSpeaker);
            Gdx.app.log(TAG, "speaker: " + currentLine.speakerId + " was added");
        }
    }

    public List<ConversationLine> getPossibleNextLines(GameState currentGameState, UserAction userAction) {
        List<ConversationLine> lines = new ArrayList<>();
        ConversationLine currentLine = getCurrentConversationLine(currentGameState);
        if(currentLine == null) {
            lines.add(conversationLines.get(0));
            return lines;
        }
        for (ConversationLine line : conversationLines) {
            if(line.getOrder() == currentLine.getOrder() + 1)
                lines.add(line);
        }
        return lines;
    }
}
