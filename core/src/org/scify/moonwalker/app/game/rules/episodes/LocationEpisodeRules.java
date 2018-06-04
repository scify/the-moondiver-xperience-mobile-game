package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.Location;
import org.scify.moonwalker.app.game.LocationController;
import org.scify.moonwalker.app.game.rules.QuestionConversationRules;
import org.scify.moonwalker.app.ui.renderables.LocationRenderable;

public class LocationEpisodeRules extends FadingEpisodeRules<LocationRenderable> {

    public static final String RENDERABLE_ID = "location";
    protected boolean outroInitiated;
    protected Location location;


    public LocationEpisodeRules (Location location) {
        super();
        renderable = null;
        outroInitiated = false;
        this.location = location;
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
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.LOCATION_AUDIO_PATH));
            /*currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.CORRECT_AUDIO_PATH));
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.WRONG_AUDIO_PATH));
            currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.DAYPASSED_AUDIO_PATH));*/
            super.episodeStartedEvents(currentState);
        }
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
            renderable.addBeforeFadeOut(new Runnable() {
                @Override
                public void run() {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.LOCATION_AUDIO_PATH));
                        if (dayPassed)
                            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.DAYPASSED_AUDIO_PATH));
                }
            });
            renderable.addAfterFadeOut(new Runnable() {
                @Override
                public void run() {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.LOCATION_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.CORRECT_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.WRONG_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI, renderable.DAYPASSED_AUDIO_PATH));
                }
            });
            endEpisodeAndAddEventWithType(gameState, "");
        } else if (renderable != null && renderable.isChatEnabled()) {
            // Initialize conversation
            if(conversationRules == null) {
                conversationRules = new QuestionConversationRules(location.getConversationPath(), location.getConversationBG(), location.getConversationSuccessFilePath(), location.getConversationFailureFilePath(), renderable.CORRECT_AUDIO_PATH, renderable.WRONG_AUDIO_PATH);
                conversationRules.setStarted(true);
                initializeParseVariables();
            }
        }
        return super.getNextState(gameState, userAction);
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
            }else {
                gameInfo.setContactRequestFlag();
            }
        }else {
            gameInfo.setContactRequestFlag();
            if (gameInfo.isLastQuizSuccessFull()) {
                gameInfo.setInventoryIncreased();
            }
        }
        gameInfo.setAfterLocationQuizEpisode(true);
        conversationRules.cleanUpState(gameState);
        return new EpisodeEndState(code, cleanUpGameState(gameState));
    }


}
