package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.MultipleChoiceConversationRenderable;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TwoChoiceConversationRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.awt.geom.Point2D;
import java.util.*;

import static org.scify.moonwalker.app.game.SelectedPlayer.girl;

public class ConversationRules extends MoonWalkerRules {
    /**
     * Constant event types
     */
    public static final String ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT = "ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT";
    public static final String ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT = "ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT";
    public static final String CONVERSATION_READY_TO_FINISH = "CONVERSATION_READY_TO_FINISH";
    public static final String CONVERSATION_FINISHED = "CONVERSATION_FINISHED";
    public static final String CONVERSATION_PAUSED = "CONVERSATION_PAUSED";

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

    protected int currentConversationOrderId;

    protected List<ConversationLine> nextLines;

    /**
     * Every conversation line that is added to the game state
     * is added to this list as well.
     */
    protected List<Renderable> oldConversationLines;
    protected String sPrvSpeakerID;

    public ConversationRules(String conversationJSONFilePath) {
        appInfo = AppInfo.getInstance();
        conversationLines = new ArrayList<>();
        resourceLocator = new ResourceLocator();
        json = new Json();
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(false);
        conversationLines = json.fromJson(ArrayList.class, ConversationLine.class, Gdx.files.internal(resourceLocator.getFilePath(conversationJSONFilePath)));
        ID = UUID.randomUUID().toString();
        oldConversationLines = new ArrayList<>();
        lastConversationRenderable = null;
        this.currentConversationOrderId = 0;
    }

    public Renderable getLastConversationRenderable() {
        return lastConversationRenderable;
    }

    @Override
    public GameState getInitialState() {
        throw new RuntimeException("Conversation Rules cannot initialize a Game State.");
    }

    @Override
    public GameState getNextState(GameState gameState, UserAction userAction) {
        // If got an answer (NEXT, TEXT, BUTTON, ...)
        if (gotAnswer(userAction)) {
            // Clear paused conversation flag
            resumeConversation(gameState);
            removeActiveConversationComponents(gameState);

            // Get if something was selected
            ConversationLine selected = getSelectedConversationLine(gameState, userAction);

            if(selected != null) {
                // Update what line was selected
                setCurrentConversationLine(gameState, selected);
                handleOnExitEventForCurrentConversationOrder(gameState, selected);
            }

            if (gameState.eventsQueueContainsEvent(CONVERSATION_READY_TO_FINISH))
                gameState.addGameEvent(new GameEvent(CONVERSATION_FINISHED, null, this));

        }

        // If conversation is paused
        if (isConversationPaused(gameState))
            // return the current game state
            return gameState;

        // Move to appropriate next order
        // If we just started
        if (currentConversationOrderId == 0) {
            // Get next alternatives
            nextLines = extractNextLines(gameState, userAction);
            // Update current order to initialize
            currentConversationOrderId = nextLines.get(0).getOrder();
        }
        // else, if we have gone beyond initialization
        else {
            // If no explicit next order has been requested
            if (getSelectedConversationLine(gameState, userAction).getNextOrder() == 0) {
                currentConversationOrderId++; // Move to next normally
            } else {
                // else update order based on request from current conversation line
                currentConversationOrderId = getSelectedConversationLine(gameState, userAction).getNextOrder();
            }

            // Actually retrieve the next lines
            nextLines = extractNextLines(gameState, userAction);
        }

        // Call event that handles conversation order change
        handleNextConversationState(gameState, userAction);
        // Call event that signifies conversation order change
        handleOnEnterEventForCurrentConversationOrder(gameState);



        return gameState;
    }

    protected void handleOnEnterEventForCurrentConversationOrder(GameState gameState) {
        if (nextLines == null)
            return;

        // For every possible next line
        Set<String> sAllEvents = new HashSet<>();
        for (ConversationLine currLine : nextLines) {
            sAllEvents.addAll(currLine.getOnEnterCurrentOrderTrigger());
        }

        // Send event indicating we entered a new order
        gameState.addGameEvent(new GameEvent(ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT, sAllEvents, this));
    }

    protected void handleOnExitEventForCurrentConversationOrder(GameState gameState, ConversationLine selectedLine) {
        Set<String> sAllEvents = selectedLine.getOnExitCurrentOrderTrigger();

        // Examine what the trigger is and handle it
        if (sAllEvents.contains("end")) {
                gameState.addGameEvent(new GameEvent(CONVERSATION_READY_TO_FINISH, null, this));
        }

        // Throw a "Conversation on exit order event"
        gameState.addGameEvent(new GameEvent(ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT, sAllEvents, this));
    }

    protected boolean gotAnswer(UserAction userAction) {
        return (userAction != null && (userAction.getActionCode().equals(UserActionCode.NEXT_CONVERSATION_LINE) ||
                userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)));
    }

    protected void resumeConversation(GameState gameState) {
        gameState.removeGameEventsWithType(CONVERSATION_PAUSED);
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

    protected ConversationLine getSelectedConversationLine(GameState gameState, UserAction userAction) {
        ConversationLine answered = null;
        if (userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER) ||
                userAction.getActionCode().equals(UserActionCode.NEXT_CONVERSATION_LINE)) {
            answered = getLineById((Integer) userAction.getActionPayload());
        }

        return answered;
    }

    protected void handleNextConversationState(GameState gameState, UserAction userAction) {
        if (nextLines == null)
            return;

        // NextType
        int nextLinesSize = nextLines.size();
        // Determine whether there is a new speaker

        // If we just started, then consider that we have a new speaker
        boolean newSpeaker = sPrvSpeakerID == null;
        if (sPrvSpeakerID != null) {
            if (nextLinesSize > 0) {
                newSpeaker = !sPrvSpeakerID.equals(nextLines.get(0).getSpeakerId());
            }
        }

        // render dialog appropriately
        if (nextLinesSize == 1) {
            addNextConversationLine(nextLines.get(0), gameState, newSpeaker);
        } else if (nextLinesSize == 2) {
            addTwoChoiceConversationLines(nextLines, gameState, newSpeaker);
        } else if (nextLinesSize > 1) {
            addMultipleChoiceConversationLines(nextLines, gameState, newSpeaker);
        }
        // await next event
        pauseConversation(gameState);

    }

    protected void addNextConversationLine(final ConversationLine conversationLine, final GameState gameState, boolean newSpeaker) {
        NextConversationRenderable nextConversationRenderable =
                new NextConversationRenderable("next_conversation_" + conversationLine.getId());
        if (lastConversationRenderable != null) {
            lastConversationRenderable.apply(getOutroEffect(lastConversationRenderable, conversationLine, gameState, newSpeaker));
        }
        lastConversationRenderable = nextConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        nextConversationRenderable.setConversationLine(conversationLine);
        // Update previous speaker
        sPrvSpeakerID = conversationLine.getSpeakerId();
        nextConversationRenderable.setRelativeAvatarPath(getAvatar(conversationLine.getSpeakerId()));
//        nextConversationRenderable.apply(new FadeLGDXEffect(0.0, 1.0, 1000));
        nextConversationRenderable.apply(getIntroEffect(nextConversationRenderable, conversationLine, gameState, newSpeaker));

        gameState.addRenderable(nextConversationRenderable);
        oldConversationLines.add(nextConversationRenderable);
    }

    protected Effect getIntroEffect(Renderable target, ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        ParallelEffectList lRes = new ParallelEffectList();
        lRes.addEffect(new FadeEffect(0.0, 1.0, 500));
        final boolean bOther = !conversationLine.getSpeakerId().contains("player");

        if (newSpeaker) {
            // Full slide
//            double dStartX = -target.getWidth();
//            if (bOther) {
//                dStartX = AppInfo.getInstance().getScreenWidth() + target.getWidth();
//            }

            // Partial slide
            double dStartX = target.getxPos() - (target.getWidth() / 4);
            if (bOther) {
                dStartX = target.getxPos() + (target.getWidth() / 4);
            }


            lRes.addEffect(new MoveEffect(dStartX, target.getyPos(), target.getxPos(), target.getyPos(), 200));
        }

        return lRes;
    }

    private Effect getOutroEffect(Renderable target, ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        ParallelEffectList lRes = new ParallelEffectList();
        lRes.addEffect(new FadeEffect(1.0, 0.0, 200));

        return lRes;
    }


    protected void addMultipleChoiceConversationLines(List<ConversationLine> nextLines, GameState gameState, boolean newSpeaker) {
        // TODO: Fix later and include effects
        //edit as above method
        MultipleChoiceConversationRenderable multipleChoiceConversationRenderable = new MultipleChoiceConversationRenderable("multiple_choice_conversation");
        lastConversationRenderable = multipleChoiceConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        //multipleChoiceConversationRenderable.setTitle(getCurrentConversationLine(gameState).getText());
        multipleChoiceConversationRenderable.setConversationLines(nextLines);
        //multipleChoiceConversationRenderable.setRelativeAvatarImgPath(getAvatar(getCurrentConversationLine(gameState).getSpeakerId()));
        gameState.addRenderable(multipleChoiceConversationRenderable);
        oldConversationLines.add(multipleChoiceConversationRenderable);
    }

    protected void addTwoChoiceConversationLines(List<ConversationLine> nextLines, GameState gameState, boolean newSpeaker) {
        TwoChoiceConversationRenderable twoChoiceConversationRenderable = new TwoChoiceConversationRenderable("two_choice_conversation");
        if (lastConversationRenderable != null) {
            lastConversationRenderable.apply(getOutroEffect(lastConversationRenderable, nextLines.get(0), gameState, newSpeaker));
        }
        lastConversationRenderable = twoChoiceConversationRenderable;
        lastConversationRenderable.setZIndex(100);
        twoChoiceConversationRenderable.setConversationLines(nextLines);
        // Update previous speaker
        sPrvSpeakerID = conversationLines.get(0).getSpeakerId();
        twoChoiceConversationRenderable.setRelativeAvatarImgPath(getAvatar(twoChoiceConversationRenderable.getConversationLines().get(0).getSpeakerId()));
        twoChoiceConversationRenderable.apply(getIntroEffect(twoChoiceConversationRenderable, nextLines.get(0), gameState, newSpeaker));

        gameState.addRenderable(twoChoiceConversationRenderable);
        oldConversationLines.add(twoChoiceConversationRenderable);
    }

    protected String getAvatar(String speakerId) {
        String avatarsPath = "img/avatars/";
        if (speakerId.equals("player")) {
            switch (gameInfo.getSelectedPlayer()) {
                case SelectedPlayer.boy:
                    return avatarsPath + "boy.png";
                case SelectedPlayer.girl:
                    return avatarsPath + "girl.png";
                case SelectedPlayer.unset:
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

    protected List<ConversationLine> extractNextLines(GameState currentGameState, UserAction userAction) {
        List<ConversationLine> lines = new ArrayList<>();
        if (this.currentConversationOrderId == 0)
            lines.add(conversationLines.get(0));
        else
            lines = getLinesWithOrder(this.currentConversationOrderId);
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
        return gameState.eventsQueueContainsEvent(CONVERSATION_PAUSED);
    }

    protected void pauseConversation(GameState gameState) {
        gameState.addGameEvent(new GameEvent(CONVERSATION_PAUSED, null, this));
    }

    public GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);

        return currentState;
    }
}
