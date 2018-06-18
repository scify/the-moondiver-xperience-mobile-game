package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.ArrayList;
import java.util.Date;

public class MainMenuEpisodeRules extends FadingEpisodeRules<MainMenuRenderable> {
    public static final String COOLDOWN = "COOLDOWN";
    public static final String AVATAR_SELECTED = "AVATAR_SELECTED";
    public static final String APP_QUIT = "APP_HIDE";
    public static final String NEW_GAME = "NEW_GAME";
    public static final String LOAD = "LOAD";

    public static final String RENDERABLE_ID = "main_menu";

    protected boolean mainMenuButtonsEnabled;
    protected boolean fadeInEffectsCompleted;

    public MainMenuEpisodeRules() {
        super();
        mainMenuButtonsEnabled = true;
        fadeInEffectsCompleted = false;
    }

    @Override
    public void episodeStartedEvents(GameState gameState) {
        // If we are just starting
        if (!isEpisodeStarted(gameState)) {
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID);
            gameInfo.setSelectedPlayer(SelectedPlayer.unset);
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI));
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_DISPOSE_UI));
            // Start sound
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_LOOP_UI, renderable.MAINMENU_AUDIO_PATH));

            // Enable input after fade in
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableInput();
                }
            });

            // Call parent starting process
            super.episodeStartedEvents(gameState);
            // Add all children
            gameState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));

        }
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        // Examine if we have a cooldown event
        GameEvent coolDownEvent = gameState.getGameEventWithType(COOLDOWN);
        // If we do and we should update the cooldown counter (e.g. 1sec has passed)
        if (coolDownEvent != null && timestamp > coolDownEvent.delay) {
            doCountdown(gameState, timestamp);
        }
        // Check if fade in is now complete
        fadeInEffectsCompleted = fadeInEffectsCompleted == false && renderable != null && renderable.isReadyForInput();

        return super.getNextState(gameState, userAction);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        if (renderable.isReadyForInput()) {
            switch (userAction.getActionCode()) {
                case NEW_GAME:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        initPlayerSelection(gameState);
                    }
                    break;
                case UserActionCode.CONTINUE:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        if (gameInfo.isSaveAvailable()) {
                            endEpisode(gameState, LOAD);
                        }else {
                            initPlayerSelection(gameState);
                        }
                    }
                    break;
                case UserActionCode.TOGGLE_AUDIO:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_TOGGLE_UI));
                    }
                    break;
                case UserActionCode.QUIT:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        endEpisode(gameState, APP_QUIT);
                    }
                    break;
                case UserActionCode.ABOUT:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        renderable.disableInput();
                        renderable.showAbout();
                    }
                    break;
                case UserActionCode.BOY_SELECTED:
                    setSelectedPlayer(gameState, SelectedPlayer.boy);
                    break;
                case UserActionCode.GIRL_SELECTED:
                    setSelectedPlayer(gameState, SelectedPlayer.girl);
                    break;
                case UserActionCode.SCREEN_TOUCHED:
                    if (renderable.isAboutMode()) {
                        renderable.disableInput();
                        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        renderable.hideAbout();
                    }
                    break;
            }
        }
        super.handleUserAction(gameState, userAction);

    }

    protected void setSelectedPlayer(GameState gameState, String selectedPlayer) {
        GameEvent coolDownEvent;
        GameEvent avatarSelected;
        gameInfo.setSelectedPlayer(selectedPlayer);

        coolDownEvent = gameState.getGameEventWithType(COOLDOWN);
        avatarSelected = gameState.getGameEventWithType(AVATAR_SELECTED);
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
        if (coolDownEvent != null) {
            gameState.removeGameEventsWithType(COOLDOWN);
        }
        if (avatarSelected != null && avatarSelected.parameters.equals(selectedPlayer)) {
            renderable.setSelectedPlayer(selectedPlayer);
            renderable.forceCountDownToZero();
            gameState.addGameEvent(new GameEvent(COOLDOWN, new Date().getTime(), false, this));
        } else {
            removePreviousAvatarSelectionAndAddNew(gameState, selectedPlayer);
            renderable.setSelectedPlayer(selectedPlayer);
            renderable.resetCountDown();
            gameState.addGameEvent(new GameEvent(COOLDOWN, new Date().getTime() + 1000, false, this));
        }
    }


    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType(AVATAR_SELECTED);
        currentState.addGameEvent(new GameEvent(AVATAR_SELECTED, newSelection, this));
    }

    protected void initPlayerSelection(GameState gameState) {
        renderable.disableInput();
        renderable.initiatePlayerSelection();
        mainMenuButtonsEnabled = false;
    }

    protected void doCountdown(GameState gameState, long timestamp) {
        // update cooldown counter
        renderable.decreaseCountDown();
        gameState.removeGameEventsWithType(COOLDOWN);

        // If we have reached below zero
        if (renderable.getCountDownValue() < 0) {
            endEpisode(gameState, NEW_GAME);
        } else {
            // inform UI to update the cooldown
            gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent(COOLDOWN, timestamp + 1000, false, this));
        }

    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if (currentState.eventsQueueContainsEvent(LOAD)) {
            endState = new EpisodeEndState(EpisodeEndStateCode.SCENARIO_LOAD, currentState);
        } else if (currentState.eventsQueueContainsEvent(APP_QUIT))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if (currentState.eventsQueueContainsEvent(NEW_GAME))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }

    protected void endEpisode(final GameState gameState, String sEpisodeEndEventType) {
        renderable.disableInput();
        endEpisodeAndAddEventWithType(gameState, sEpisodeEndEventType);
    }

}
