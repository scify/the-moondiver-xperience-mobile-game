package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.quiz.Answer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.game.rules.QuestionConversationRules;
import org.scify.moonwalker.app.ui.renderables.LocationRenderable;

import java.util.Set;

import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_RING_PHONE;
import static org.scify.moonwalker.app.game.rules.ConversationRules.EVENT_TADA;

public class LocationEpisodeRules extends FadingEpisodeRules<LocationRenderable> {

    public static final String RENDERABLE_ID = "location";
    protected boolean outroInitiated;
    protected Location location;


    public LocationEpisodeRules (Location location) {
        super();
        renderable = null;
        outroInitiated = false;
        this.location = location;
        gameInfo.setShowArrivalConversation(false);
     }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            gameInfo.setTutorialMode(false);
            renderable = new LocationRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, location.getEpisodeBackgroundImagePath());
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableChat();
                }
            });
            currentState.addRenderable(renderable);
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.LOCATION_AUDIO_PATH));
            appInfo.logEpisodeStarted(this.location.getName());
            super.episodeStartedEvents(currentState);
        }
    }

    @Override
    protected void episodeEndedEvents(GameState currentState) {
        appInfo.logEpisodeEnded(this.location.getName());
        super.episodeEndedEvents(currentState);
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        if (conversationRules != null && conversationRules.isFinished() && !outroInitiated) {
            outroInitiated = true;
            boolean success = true;
            if (!gameInfo.isLastQuizSuccessFull()) {
                success = false;
                gameInfo.dayPassed();
            }
            final boolean dayPassed = !success;
            renderable.addAfterFadeOut(new Runnable() {
                @Override
                public void run() {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.LOCATION_AUDIO_PATH));
                        if (dayPassed)
                            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.DAYPASSED_AUDIO_PATH));
                }
            });
            endEpisodeAndAddEventWithType(gameState, "");
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            if(conversationRules == null) {
                conversationRules = new QuestionConversationRules(location.getConversationPath(), location.getConversationBG(), location.getConversationSuccessFilePath(), location.getConversationFailureFilePath(), renderable.CORRECT_AUDIO_PATH, renderable.WRONG_AUDIO_PATH, gameInfo.isQuizFirstTime(), 3);
                conversationRules.setStarted(true);
                initializeParseVariables();
            }
        }
        return super.getNextState(gameState, userAction);
    }

    @Override
    protected void onEnterConversationOrder(GameState gsCurrent, ConversationLine lineEntered) {
        Set<String> eventTrigger;
        eventTrigger = (Set<String>) gsCurrent.getGameEventWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
        if (eventTrigger.contains(EVENT_TADA)) {
            gsCurrent.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.TADA_AUDIO_PATH));
        }
        super.onEnterConversationOrder(gsCurrent, lineEntered);
    }

    @Override
    public EpisodeEndState determineEndState(GameState gameState) {
        String code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
        LocationController locationController = LocationController.getInstance();
        // Handle failed conversation
        if (locationController.isLastLocation(gameInfo.getCurrentLocation())) {
            if (gameInfo.isLastQuizSuccessFull()) {
                gameInfo.setInventoryIncreased();
                gameInfo.setInventoryRequestFlag();
                gameInfo.setQuizFirstTime(true);
            }else {
                gameInfo.setContactRequestFlag();
                gameInfo.setQuizFirstTime(false);
            }
        }else {
            gameInfo.setContactRequestFlag();
            if (gameInfo.isLastQuizSuccessFull()) {
                gameInfo.setInventoryIncreased();
                gameInfo.setQuizFirstTime(true);
            }else {
                gameInfo.setQuizFirstTime(false);
            }
        }
        gameInfo.setAfterLocationQuizEpisode(true);
        conversationRules.cleanUpState(gameState);
        return new EpisodeEndState(code, cleanUpGameState(gameState));
    }


}
