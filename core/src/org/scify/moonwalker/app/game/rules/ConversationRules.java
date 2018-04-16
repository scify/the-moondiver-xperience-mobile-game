package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.engine.EpisodeEndState;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.engine.renderables.TwoChoiceConversationRenderable;
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
    protected Renderable lastConversationRenderable;
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
        lastConversationRenderable = null;
    }

    public Renderable getLastConversationRenderable() { return lastConversationRenderable; }

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
            if (gameState.eventsQueueContainsEvent("CONVERSATION_READY_TO_FINISH"))
                gameState.addGameEvent(new GameEvent("CONVERSATION_FINISHED"));
        }
        // If conversation is paused
        if (isConversationPaused(gameState))
            // return the current game state
            return gameState;
        // Get next alternatives
        List<ConversationLine> nextLines = extractNextLines(gameState, userAction);
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
        return (userAction != null && (userAction.getActionCode().equals(UserActionCode.NEXT_CONVERSATION_LINE) ||
                userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)));
    }

    protected void resumeConversation(GameState gameState) {
        gameState.removeGameEventsWithType("CONVERSATION_PAUSED");
    }

    /**
     * Removing all past conversation lines so that the screen is cleared
     *
     * @param gameState the current game state
     */
    protected void removeActiveConversationComponents(GameState gameState) {
        for (Renderable oldLine : oldConversationLines) {
            Renderable line = gameState.getRenderable(oldLine);
            // setting a negative z-index value will cause the rendering engine
            // to hide the corresponding UI instance of the renderable.
            line.setZIndex(-1);
            line.setVisible(false);
        }
    }

    protected void handleAnswerToMultipleQuestion(GameState gameState, UserAction userAction) {
        if (userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)) {
            ConversationLine answered = getLineById((Integer) userAction.getActionPayload());
            if (answered != null)
                setCurrentConversationLine(gameState, answered);
        }
    }

    protected void handleNextConversationState(List<ConversationLine> nextLines, GameState gameState, UserAction userAction) {
        // NextType
        int nextLinesSize = nextLines.size();
        if (nextLinesSize == 1) {
            // render it
            addNextConversationLine(nextLines.get(0), gameState);
            // await next event
            pauseConversation(gameState);
        }else if (nextLinesSize == 2) {
            addTwoChoiceConversationLines (nextLines, gameState);
            pauseConversation(gameState);
        }
        else if (nextLinesSize > 1) {
            // render dialog
            addMultipleChoiceConversationLines(nextLines, gameState);
            pauseConversation(gameState);
        }
    }

    protected void addNextConversationLine(ConversationLine conversationLine, GameState gameState) {
        NextConversationRenderable nextConversationRenderable = new NextConversationRenderable("next_conversation_" + conversationLine.getId());
        lastConversationRenderable = nextConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        nextConversationRenderable.setConversationLine(conversationLine);
        nextConversationRenderable.setRelativeAvatarPath(getAvatar(conversationLine.getSpeakerId()));
        gameState.addRenderable(nextConversationRenderable);
        setCurrentConversationLine(gameState, conversationLine);
        oldConversationLines.add(nextConversationRenderable);
    }

    protected void addMultipleChoiceConversationLines(List<ConversationLine> nextLines, GameState gameState) {

        //edit as above method
        MultipleChoiceConversationRenderable multipleChoiceConversationRenderable = new MultipleChoiceConversationRenderable("multiple_choice_conversation");
        lastConversationRenderable = multipleChoiceConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        multipleChoiceConversationRenderable.setTitle(getCurrentConversationLine(gameState).getText());
        multipleChoiceConversationRenderable.setConversationLines(nextLines);
        multipleChoiceConversationRenderable.setRelativeAvatarImgPath(getAvatar(getCurrentConversationLine(gameState).getSpeakerId()));
        gameState.addRenderable(multipleChoiceConversationRenderable);
        oldConversationLines.add(multipleChoiceConversationRenderable);
    }

    protected void addTwoChoiceConversationLines(List<ConversationLine> nextLines, GameState gameState) {
        TwoChoiceConversationRenderable twoChoiceConversationRenderable = new TwoChoiceConversationRenderable("two_choice_conversation");
        lastConversationRenderable = twoChoiceConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        twoChoiceConversationRenderable.setConversationLines(nextLines);
        twoChoiceConversationRenderable.setRelativeAvatarImgPath(getAvatar(twoChoiceConversationRenderable.getConversationLines().get(0).getSpeakerId()));
        gameState.addRenderable(twoChoiceConversationRenderable);
        oldConversationLines.add(twoChoiceConversationRenderable);
    }

    protected String getAvatar(String speakerId) {
        String avatarsPath = "img/avatars/";
        if (speakerId.equals("player")) {
            switch (gameInfo.getSelectedPlayer()) {
                case boy:
                    return avatarsPath + "boy.png";
                case girl:
                    return avatarsPath + "girl.png";
                case unset:
                    return null;
                default:
                    return null;
            }
        } else {
            return avatarsPath + speakerId + ".png";
        }
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

    public org.scify.engine.renderables.Renderable getCurrentSpeaker(ConversationLine conversationLine) {
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
            org.scify.engine.renderables.Renderable newSpeaker = new Renderable(50, 70, appInfo.getScreenWidth() * 0.3f, appInfo.getScreenWidth() * 0.4f, currentLine.getSpeakerId(), currentLine.getSpeakerId());
            // update my lookup map
            addRenderableEntry(currentLine.getSpeakerId(), newSpeaker);
            // update state
            state.addRenderable(newSpeaker);
        }
    }

    protected List<ConversationLine> extractNextLines(GameState currentGameState, UserAction userAction) {
        List<ConversationLine> lines = new ArrayList<>();
        ConversationLine currentLine = getCurrentConversationLine(currentGameState);
        if (currentLine == null) {
            lines.add(conversationLines.get(0));
            return lines;
        }
        if (currentLine.getNextOrder() != 0)
            lines = getLinesWithOrder(currentLine.getNextOrder());
        else
            lines = getLinesWithOrder(currentLine.getOrder() + 1);
        return lines;
    }

    protected List<ConversationLine> getLinesWithOrder(int lineOrder) {
        List<ConversationLine> lines = new ArrayList<>();
        for (ConversationLine line : conversationLines) {
            if (line.getOrder() == lineOrder)
                lines.add(line);
        }
        return lines;
    }

    private ConversationLine getLineById(int id) {
        for (ConversationLine line : conversationLines)
            if (line.getId() == id)
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
