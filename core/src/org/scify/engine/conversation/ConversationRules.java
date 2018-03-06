package org.scify.engine.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.moonwalker.app.game.rules.MoonWalkerRules;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.engine.UserActionCode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConversationRules extends MoonWalkerRules {
    /**
     * All conversation lines that are read from the json file for
     * this conversation.
     */
    protected List<ConversationLine> conversationLines;
    private Json json;
    protected ResourceLocator resourceLocator;
    private AppInfo appInfo;
    /**
     * This id is used as a key when storing the current conversation line
     * in the game state
     */
    protected String ID;

    /**
     * Every conversation line that is added to the game state
     * is added to this list as well.
     */
    protected List<Renderable> oldConversationLines;

    public ConversationRules(String conversationJSONFilePath) {
        appInfo = AppInfo.getInstance();
        conversationLines = new ArrayList<>();
        resourceLocator = new ResourceLocator();
        json = new Json();
        conversationLines = json.fromJson(ArrayList.class, ConversationLine.class, Gdx.files.internal(resourceLocator.getFilePath(conversationJSONFilePath)));
        ID = UUID.randomUUID().toString();
        oldConversationLines = new ArrayList<>();
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
        if (isConversationPaused(gameState))
            // return the current game state
            return gameState;
        // Get next alternatives
        List<ConversationLine> nextLines = getPossibleNextLines(gameState, userAction);
        handleNextConversationState(nextLines, gameState, userAction);
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

    /**
     * Removind all past conversation lines so that the screen is cleared
     * @param gameState the current game state
     */
    protected void removeActiveConversationComponents(GameState gameState) {
        for(Renderable oldLine : oldConversationLines) {
            Renderable line = gameState.getRenderable(oldLine);
            // setting a negative z-index value will cause the rendering engine
            // to hide the corresponding UI instance of the renderable.
            line.setZIndex(-1);
        }
    }

    protected void handleAnswerToMultipleQuestion(GameState gameState, UserAction userAction) {
        if(userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)) {
            ConversationLine answered = getLineById((Integer) userAction.getActionPayload());
            if(answered != null)
                setCurrentConversationLine(gameState, answered);
        }
    }

    protected void handleNextConversationState(List<ConversationLine> nextLines, GameState gameState, UserAction userAction) {
        // If one line returned
        if (nextLines.size() == 1) {
            // render it
            addSingleConversationLine(nextLines.get(0), gameState);
            // await next event
            pauseConversation(gameState);
        } else if(nextLines.size() > 1){
            // render dialog
            addMultipleConversationLines(nextLines, gameState);
            pauseConversation(gameState);
        }
    }

    protected void addSingleConversationLine(ConversationLine conversationLine, GameState gameState) {
        SingleConversationLine singleConversationLine = new SingleConversationLine("new_single_conversation");
        singleConversationLine.setConversationLine(conversationLine);
        singleConversationLine.setRelativeAvatarPath(conversationLine.getSpeakerId() + ".jpg");

        gameState.addRenderable(singleConversationLine);
        setCurrentConversationLine(gameState, conversationLine);
        oldConversationLines.add(singleConversationLine);
    }

    protected void addMultipleConversationLines(List<ConversationLine> nextLines, GameState gameState) {
        MultipleConversationLines conversationLines = new MultipleConversationLines("multiple_lines");
        conversationLines.setTitle(getCurrentConversationLine(gameState).text);
        conversationLines.setConversationLines(nextLines);
        conversationLines.setRelativeAvatarImgPath(getCurrentConversationLine(gameState).getSpeakerId() + ".jpg");
        gameState.addRenderable(conversationLines);
        oldConversationLines.add(conversationLines);
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public void disposeResources() {
        oldConversationLines = new ArrayList<>();
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
        // TODO
        //addSpeakersAsNeeded(gameState, currentLine);
    }

    protected void addSpeakersAsNeeded(GameState state, ConversationLine currentLine) {
        // If the speaker does not exist
        if (!renderableExist(currentLine.getSpeakerId())) {
            // add the renderable character
            Renderable newSpeaker = new Renderable(50, 70, appInfo.getScreenWidth() * 0.3f, appInfo.getScreenWidth() * 0.4f, currentLine.speakerId, currentLine.speakerId);
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

    protected boolean isConversationPaused(GameState gameState) {
        return gameState.eventsQueueContainsEvent("CONVERSATION_PAUSED");
    }

    protected void pauseConversation(GameState gameState) {
        gameState.addGameEvent(new GameEvent("CONVERSATION_PAUSED"));
    }
}
