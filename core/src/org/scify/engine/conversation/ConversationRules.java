package org.scify.engine.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.MoonWalkerRules;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
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
    }

    @Override
    public GameState getInitialState() {
        throw new RuntimeException("Conversation Rules cannot initialize a Game State.");
    }

    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        // If got an answer (TEXT, BUTTON, ...)
        if (gotAnswer(userAction)) {
            // Clear paused conversation flag
            resumeConversation(gameState);
            removeActiveConversationComponents(gameState);
            handleAnswerToMultipleQuestion(gameState, userAction);
            if(gameState.eventsQueueContainsEvent("CONVERSATION_READY_TO_FINISH"))
                gameState.addGameEvent(new GameEvent("CONVERSATION_FINISHED"));
        }
        // If conversation is paused
        if (gameState.eventsQueueContainsEvent("CONVERSATION_PAUSED"))
            // return the current game state
            return gameState;
        // Get next alternatives
        List<ConversationLine> nextLines = getPossibleNextLines(gameState, userAction);
        handleNextConversationLine(nextLines, gameState, userAction);
        handleTriggerEventForCurrentConversationLine(gameState);
        return gameState;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        ConversationLine currLine = getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {
            case "end":
                gameState.addGameEvent(new GameEvent("CONVERSATION_READY_TO_FINISH"));
                break;
        }
    }

    protected boolean gotAnswer(UserAction userAction) {
        return (userAction != null && (userAction.getActionCode().equals(UserActionCode.NEXT_CONVERSATION_LINE) || userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)));
    }

    protected void resumeConversation(GameState gameState) {
        gameState.removeGameEventsWithType("CONVERSATION_PAUSED");
    }

    protected void removeActiveConversationComponents(GameState gameState) {
        gameState.addGameEvent(new GameEvent("REMOVE_CONVERSATIONS"));
    }

    protected void handleAnswerToMultipleQuestion(GameState gameState, UserAction userAction) {
        if(userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)) {
            ConversationLine answered = getLineById((Integer) userAction.getActionPayload());
            if(answered != null)
                setCurrentConversationLine(gameState, answered);
        }
    }

    protected void handleNextConversationLine(List<ConversationLine> nextLines, GameState gameState, UserAction userAction) {
        // If one line returned
        if (nextLines.size() == 1) {
            // render it
            // TODO change
            SingleConversationLine singleConversationLine = new SingleConversationLine( nextLines.get(0),
                    "img/avatars/" + nextLines.get(0).getSpeakerId() + ".jpg", getCurrentSpeaker( nextLines.get(0)));
            ArrayList<Object> payload = new ArrayList<>();
            payload.add(singleConversationLine);
            // Declare which user action should be thrown when button is pressed
            payload.add(new UserAction(UserActionCode.NEXT_CONVERSATION_LINE));
            gameState.addGameEvent(new GameEvent("CONVERSATION_LINE",
                    payload));
            // update current line and speaker
            setCurrentConversationLine(gameState, nextLines.get(0));
            // await next event
            gameState.addGameEvent(new GameEvent("CONVERSATION_PAUSED"));
        } else if(nextLines.size() > 1){
            // render dialog
            gameState.addGameEvent(new GameEvent("CONVERSATION_PAUSED"));
            MultipleConversationLines conversationLines = new MultipleConversationLines(getCurrentConversationLine(gameState).text, nextLines);
            gameState.addGameEvent(new GameEvent("CONVERSATION_LINES", conversationLines));
        }
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

    @Override
    public void gameStartedEvents(GameState currentState) {

    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }

    public ConversationLine getCurrentConversationLine(GameState gameState) {
        return (ConversationLine) gameState.getAdditionalDataEntry(ID);
    }

    public Renderable getCurrentSpeaker(ConversationLine conversationLine) {
        return getRenderableById(conversationLine.getSpeakerId());
    }

    protected void setCurrentConversationLine(GameState gameState, ConversationLine currentLine) {
        gameState.storeAdditionalDataEntry(ID, currentLine);
        // TODO
        //addSpeakersAsNeeded(gameState, currentLine);
    }

    protected void addSpeakersAsNeeded(GameState state, ConversationLine currentLine) {
        // If the speaker does not exist
        if (!renderableExist(currentLine.getSpeakerId())) {
            // add the renderable character
            Renderable newSpeaker = new Renderable(50, 70, gameInfo.getScreenWidth() * 0.3f, gameInfo.getScreenWidth() * 0.4f, currentLine.speakerId, currentLine.speakerId);
            // update my lookup map
            addRenderableEntry(currentLine.speakerId, newSpeaker);
            // update state
            state.addRenderable(newSpeaker);
        }
    }

    public List<ConversationLine> getPossibleNextLines(GameState currentGameState, UserAction userAction) {
        List<ConversationLine> lines = new ArrayList<>();
        ConversationLine currentLine = getCurrentConversationLine(currentGameState);
        if(currentLine == null) {
            lines.add(conversationLines.get(0));
            return lines;
        }
        if(currentLine.getNextOrder() != 0)
            lines = getLinesWithOrder(currentLine.getNextOrder());
        else
            lines = getLinesWithOrder(currentLine.getOrder() + 1);
        return lines;
    }

    protected List<ConversationLine> getLinesWithOrder(int lineOrder) {
        List<ConversationLine> lines = new ArrayList<>();
        for (ConversationLine line : conversationLines) {
            if(line.getOrder() == lineOrder)
                lines.add(line);
        }
        return lines;
    }

    private ConversationLine getLineById(int id) {
        for(ConversationLine line : conversationLines)
            if(line.getId() == id)
                return line;
        return null;
    }
}
