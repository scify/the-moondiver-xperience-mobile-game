package org.scify.moonwalker.app.game.rules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.*;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.conversation.RandomResponseFactory;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.*;

public class ConversationRules extends MoonWalkerBaseRules {
    /**
     * Constant event types
     */
    public static final String ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT = "ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT";
    public static final String ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT = "ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT";
    public static final String CONVERSATION_FINISHED = "CONVERSATION_FINISHED";
    public static final String CONVERSATION_PAUSED = "CONVERSATION_PAUSED";
    public static final String CONVERSATION_STARTED = "CONVERSATION_STARTED";
    public static final String CONVERSATION_FAILED = "CONVERSATION_FAILED";
    public static final String TAG_FAIL = "fail";
    public static final String TAG_END = "end";
    public static final String EVENT_RANDOM_CORRECT = "random_correct";
    public static final String EVENT_RANDOM_WRONG = "random_wrong";
    public static final String EVENT_RANDOM_BORING = "random_boring";
    public static final String EVENT_LOAD_QUESTION = "load_question";
    public static final String EVENT_RING_PHONE = "ring_phone";
    public static final String EVENT_TADA = "tada";
    public static final String EVENT_SHOW_QUIZ_EPISODE = "show_quiz_episode";
    public static final String EVENT_RANDOM_RESPONSE = "load_response_for_question";
    public static final String EVENT_WAIT_UNTIL_NIGHT = "wait_until_night";
    public static final String EVENT_ROOSTER = "rooster";

    /**
     * All conversation lines that are read from the json file for
     * this conversation.
     */
    protected List<ConversationLine> conversationLines;
    private static Json json;
    protected static ResourceLocator resourceLocator;
    protected Renderable lastConversationRenderable;
    protected RandomResponseFactory randomResponseFactory;
    protected String bgImgPath;
    protected boolean keepFirstDuringParsing;
    protected Map<String, String> replaceLexicon;

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    // State information
    protected boolean started = false;
    protected boolean finished = false;
    protected boolean paused = false;

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

    public ConversationRules(String conversationJSONFilePath, String bgImgPath) {
        appInfo = AppInfo.getInstance();
        conversationLines = new ArrayList<>();
        resourceLocator = ResourceLocator.getInstance();
        if (json == null) {
            json = new Json();
            json.setUsePrototypes(false);
            json.setIgnoreUnknownFields(false);
        }
        conversationLines = json.fromJson(ArrayList.class, ConversationLine.class, Gdx.files.internal(resourceLocator.getFilePath(conversationJSONFilePath)));
        ID = UUID.randomUUID().toString();
        oldConversationLines = new ArrayList<>();
        lastConversationRenderable = null;
        currentConversationOrderId = 0;
        randomResponseFactory = RandomResponseFactory.getInstance();
        this.bgImgPath = bgImgPath;
        keepFirstDuringParsing = true;
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
        boolean isConversationAnswer = gotAnswer(userAction);
        if (isConversationAnswer) {
            // Clear paused conversation flag
            resumeConversation(gameState);
            ConversationLine selected = getSelectedConversationLine(gameState, userAction);
            removeActiveConversationComponents(gameState);
            if (selected != null) {
                // Update what line was selected
                onExitConversationOrder(gameState, selected);
                setCurrentConversationLine(gameState, selected);
            }
        }
        // If conversation is paused
        if (isPaused() || isFinished())
            // return the current game state
            return gameState;

        // Move to appropriate next order
        updateCurrentConversationOrder(gameState, userAction);

        nextLines = extractNextLines(gameState, userAction);
        // Call event that handles conversation order change
        handleNextConversationState(gameState, userAction);

        handleOnEnterConversationOrder(gameState);
        return gameState;
    }

    protected void updateCurrentConversationOrder(GameState gameState, UserAction userAction) {
        if (userAction == null)
            normallyUpdateConversationOrder();
        else {
            ConversationLine cl = getSelectedConversationLine(gameState, userAction);
            if (cl == null)
                normallyUpdateConversationOrder();
            else {
                int nextOrder = cl.getNextOrder();
                if (nextOrder == 0) {
                    currentConversationOrderId++; // Move to next normally
                } else {
                    // else update order based on request from current conversation line
                    currentConversationOrderId = nextOrder;
                }
            }
        }
    }

    protected void normallyUpdateConversationOrder() {
        currentConversationOrderId = currentConversationOrderId == 0 ? conversationLines.get(0).getOrder() : currentConversationOrderId + 1;
    }

    @Override
    protected void onExitConversationOrder(GameState gsCurrent, ConversationLine lineExited) {
        Set<String> sAllEvents = lineExited.getOnExitCurrentOrderTrigger();

        // Examine what the trigger is and handle it
        if (sAllEvents.contains(TAG_END)) {
            setFinished(true);
            gsCurrent.addGameEvent(new GameEvent(CONVERSATION_FINISHED, null, this));
        }
        // Throw a "Conversation on exit order event"
        gsCurrent.addGameEvent(new GameEvent(ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT, sAllEvents, this));

        super.onExitConversationOrder(gsCurrent, lineExited);
    }

    /**
     * This function creates an onEnter event for each next line.
     *
     * @param gameState The current game state.
     */
    protected void handleOnEnterConversationOrder(GameState gameState) {
        if (nextLines == null)
            return;

        // For every possible next line
        Set<String> sAllEvents = new HashSet<>();
        for (ConversationLine currLine : nextLines) {
            // Check if we have a trigger and add apropriate event
            sAllEvents.addAll(currLine.getOnEnterCurrentOrderTrigger());
        }

        // Send event indicating we entered a new order
        gameState.addGameEvent(new GameEvent(ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT, sAllEvents, this));
    }

    /**
     * Updates the current line, by changing its text with some randomly generated (appropriate) text.
     *
     * @param currLine The line the text of which we update.
     * @throws Exception Thrown if we cannot generate an appropriate response text.
     */
    protected void handleRandomTextEventForCurrentLine(ConversationLine currLine) throws Exception {
        Set<String> eventNames = currLine.getOnEnterCurrentOrderTrigger();
        for (String eventName : eventNames) {
            if (eventName.equals(EVENT_RANDOM_CORRECT) || eventName.equals(EVENT_RANDOM_WRONG) || eventName.equals(EVENT_RANDOM_BORING))
                currLine.setText(randomResponseFactory.getRandomResponseFor(eventName));
        }
    }


    protected boolean gotAnswer(UserAction userAction) {
        return (userAction != null && (userAction.getActionCode().equals(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE) ||
                userAction.getActionCode().equals(UserActionCode.MULTIPLE_SELECTION_ANSWER)));
    }

    protected void resumeConversation(GameState gameState) {
        gameState.removeGameEventsWithType(CONVERSATION_PAUSED);
        setPaused(false);
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
                userAction.getActionCode().equals(UserActionCode.SINGLE_CHOICE_CONVERSATION_LINE)) {
            ConversationLine clickedLine = (ConversationLine) userAction.getActionPayload();
            answered = getLineById(clickedLine.getId());
        }

        return answered;
    }

    /**
     * Updates the game state with the next lines, if available, also creating appropriate renderables.
     *
     * @param gameState  The current game stage.
     * @param userAction Any user action we may need to handle.
     */
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
            addSingleChoiceConversationLine(nextLines.get(0), gameState, newSpeaker);
        } else if (nextLinesSize == 2) {
            final TwoChoiceConversationRenderable renderable = new TwoChoiceConversationRenderable(nextLines.get(0).getId(), bgImgPath, keepFirstDuringParsing, replaceLexicon);
            renderable.setAvatarImg(getAvatar(nextLines.get(0).getSpeakerId()));
            EffectSequence effectSequence = getIntroEffect(renderable, nextLines.get(0), gameState, newSpeaker);
            effectSequence.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    renderable.showButtons();
                }
            }));
            addMultipleConversationLines(nextLines, gameState, newSpeaker, renderable, effectSequence);
        } else if (nextLinesSize > 1) {
            final MultipleChoiceConversationRenderable renderable = new MultipleChoiceConversationRenderable(nextLines.get(0).getId(), null, bgImgPath, keepFirstDuringParsing, replaceLexicon);
            addMultipleConversationLines(nextLines, gameState, newSpeaker, renderable, getIntroEffectForMultipleChoiceRenderable());
        }
        if(nextLinesSize > 0)
            pause(gameState);
    }

    protected void addSingleChoiceConversationLine(final ConversationLine conversationLine, final GameState gameState, boolean newSpeaker) {
        final SingleChoiceConversationRenderable renderable = new SingleChoiceConversationRenderable(conversationLine.getId(), bgImgPath, keepFirstDuringParsing, replaceLexicon);
        if (lastConversationRenderable != null) {
            lastConversationRenderable.addEffect(getOutroEffect());
        }
        lastConversationRenderable = renderable;
        lastConversationRenderable.setVisible(false);
        lastConversationRenderable.setZIndex(2);
        renderable.setConversationLine(conversationLine);
        // Update previous speaker
        sPrvSpeakerID = conversationLine.getSpeakerId();
        renderable.setAvatarImg(getAvatar(conversationLine.getSpeakerId()));
        EffectSequence effectSequence = getIntroEffect(renderable, conversationLine, gameState, newSpeaker);
        effectSequence.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                renderable.showButton();
            }
        }));
        renderable.addEffect(effectSequence);
        gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
        gameState.addRenderable(renderable);
        oldConversationLines.add(renderable);
    }


    protected void addMultipleConversationLines(List<ConversationLine> nextLines, GameState gameState,
                                                boolean newSpeaker, ConversationRenderable renderable, Effect effect) {
        ConversationLine firstLine = nextLines.get(0);
        if (lastConversationRenderable != null) {
            lastConversationRenderable.addEffect(getOutroEffect());
        }
        lastConversationRenderable = renderable;
        lastConversationRenderable.setVisible(false);
        lastConversationRenderable.setZIndex(2);
        renderable.setConversationLines(nextLines);
        // Update previous speaker
        sPrvSpeakerID = firstLine.getSpeakerId();
        renderable.addEffect(effect);
        gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
        gameState.addRenderable(renderable);
        oldConversationLines.add(renderable);
    }

    protected EffectSequence getIntroEffectForMultipleChoiceRenderable() {
        EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1.0, 0.0, 0));
        ret.addEffect(new VisibilityEffect(true));
        ret.addEffect(new FadeEffect(0.0, 1.0, 2000));
        return ret;
    }

    protected EffectSequence getIntroEffect(Renderable target, ConversationLine conversationLine, GameState gameState, boolean newSpeaker) {
        EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1.0, 0.0, 0));
        ret.addEffect(new VisibilityEffect(true));

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
        ret.addEffect(lRes);

        return ret;
    }

    private Effect getOutroEffect() {
        final EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1.0, 0.0, 200));
        ret.addEffect(new VisibilityEffect(false));
        return ret;
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
    public boolean isEpisodeFinished(GameState gsCurrent) {
        return false;
    }

    @Override
    public void disposeResources() {
        oldConversationLines.clear();
        replaceLexicon.clear();
        lastConversationRenderable = null;
        nextLines.clear();
        sPrvSpeakerID = "";
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
        gameState.setAdditionalDataEntry(ID, currentLine);
        // TODO
        //addSpeakersAsNeeded(gameState, currentLine);
    }

    protected List<ConversationLine> extractNextLines(GameState currentGameState, UserAction userAction) {
        List<ConversationLine> lines = new LinkedList<>();
        if (this.currentConversationOrderId == 0)
            lines.add(conversationLines.get(0));
        else
            lines = getLinesWithOrder(this.currentConversationOrderId);

        // Post process returned lines to handle randomly generated lines
        for (ConversationLine currLine : lines) {
            try {
                // and call it appropriately to update the lines
                handleRandomTextEventForCurrentLine(currLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ListIterator<ConversationLine> iLines = lines.listIterator();
        // For each item
        while (iLines.hasNext()) {
            // Remove it, if it cannot cope with the prerequisites
            if (!satisfiesPrerequisites(iLines.next(), currentGameState))
                iLines.remove();
        }

        return lines;
    }

    protected boolean satisfiesPrerequisites(ConversationLine next, GameState currentGameState) {
        // Get all prerequisites
        Set<String> sPrereqs = next.getPrerequisites();
        // If no prereqs
        if (sPrereqs.size() == 0)
            // all is OK
            return true;

        return true;
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

    public void start(GameState gameState) {
        started = true;
        if (gameState != null) {
            gameState.addGameEvent(new GameEvent(CONVERSATION_STARTED, null, this));
        }
    }

    public void pause(GameState gameState) {
        paused = true;
        if (gameState != null) {
            gameState.addGameEvent(new GameEvent(CONVERSATION_PAUSED, null, this));
        }
    }

    public GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        return currentState;
    }

    public GameState cleanAllConversationEvents(GameState currentState) {
        currentState.removeGameEventsWithType(ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT);
        currentState.removeGameEventsWithType(ON_EXIT_CONVERSATION_ORDER_TRIGGER_EVENT);
        currentState.removeGameEventsWithType(CONVERSATION_FINISHED);
        currentState.removeGameEventsWithType(CONVERSATION_PAUSED);
        currentState.removeGameEventsWithType(CONVERSATION_STARTED);
        currentState.removeGameEventsWithType(CONVERSATION_FAILED);
        return currentState;
    }

    public void setKeepFirstDuringParsing(boolean keepFirstDuringParsing) {
        this.keepFirstDuringParsing = keepFirstDuringParsing;
    }

    public void setReplaceLexicon(Map<String, String> replaceLexicon) {
        this.replaceLexicon = replaceLexicon;
    }
}
