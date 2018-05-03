package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainMenuEpisodeRules extends FadingEpisodeRules<MainMenuRenderable> {

    public static final String AUDIO_START_UI = "AUDIO_START_UI";
    public static final String AUDIO_START_LOOP_UI = "AUDIO_START_LOOP_UI";
    public static final String AUDIO_LOAD_UI = "AUDIO_LOAD_UI";

    public static final String AUDIO_STOP_UI = "AUDIO_STOP_UI";
    public static final String AUDIO_DISPOSE_UI = "AUDIO_DISPOSE_UI";
    public static final String AUDIO_TOGGLE_UI = "AUDIO_TOGGLE_UI";

    public static final String COOLDOWN = "COOLDOWN";
    public static final String AVATAR_SELECTED = "AVATAR_SELECTED";
    public static final String APP_QUIT = "APP_QUIT";
    public static final String NEW_GAME = "NEW_GAME";

    public static final String MAIN_MENU_ID = "main_menu";

    protected boolean mainMenuButtonsEnabled;
    protected boolean fadeInEffectsCompleted;

    public MainMenuEpisodeRules() {
        super();
        mainMenuButtonsEnabled = true;
        fadeInEffectsCompleted = false;
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        // Examine if we have a cooldown event
        GameEvent coolDownEvent = gameState.getGameEventsWithType(COOLDOWN);
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
                        initPlayerSelection(gameState);
                    }
                    break;
                case UserActionCode.TOGGLE_AUDIO:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(AUDIO_TOGGLE_UI));
                    }
                    break;
                case UserActionCode.QUIT:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                        endEpisode(gameState, APP_QUIT);
                    }
                    break;
                case UserActionCode.BOY_SELECTED:
                    setSelectedPlayer(gameState, SelectedPlayer.boy);
                    break;
                case UserActionCode.GIRL_SELECTED:
                    setSelectedPlayer(gameState, SelectedPlayer.girl);
                    break;
            }
        }
        super.handleUserAction(gameState, userAction);

    }

    protected void setSelectedPlayer(GameState gameState, String selectedPlayer) {
        GameEvent coolDownEvent;
        GameEvent avatarSelected;
        gameInfo.setSelectedPlayer(selectedPlayer);

        coolDownEvent = gameState.getGameEventsWithType(COOLDOWN);
        avatarSelected = gameState.getGameEventsWithType(AVATAR_SELECTED);
        gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
        if (coolDownEvent != null) {
            gameState.removeGameEventsWithType(COOLDOWN);
        }
        if (avatarSelected != null && avatarSelected.parameters.equals(selectedPlayer)) {
            renderable.setSelectedAvatarButton(renderable.getPlayerButton(selectedPlayer));
            renderable.forceCountDownToZero();
            gameState.addGameEvent(new GameEvent(COOLDOWN, new Date().getTime(), false, this));
        } else {
            removePreviousAvatarSelectionAndAddNew(gameState, selectedPlayer);
            renderable.setSelectedAvatarButton(renderable.getPlayerButton(selectedPlayer));
            renderable.resetCountDown();
            gameState.addGameEvent(new GameEvent(COOLDOWN, new Date().getTime() + 1000, false, this));
        }
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        // If we are just starting
        if (!isEpisodeStarted(currentState)) {
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), MAIN_MENU_ID);
            gameInfo.setSelectedPlayer(SelectedPlayer.unset);

            // Start sound
            currentState.addGameEvent(new GameEvent(AUDIO_START_LOOP_UI, renderable.MAINMENU_AUDIO_PATH));

            // Enable input after fade in
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableInput();
                }
            });

            // Call parent starting process
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            currentState.addRenderable(renderable);
            super.episodeStartedEvents(currentState);
        }
    }

    private void prepareNextEpisodeAudio(GameState currentState) {
        currentState.addGameEvent(new GameEvent(AUDIO_LOAD_UI, renderable.BOY_MUSIC_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent(AUDIO_LOAD_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
    }


    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType(AVATAR_SELECTED);
        currentState.addGameEvent(new GameEvent(AVATAR_SELECTED, newSelection, this));
    }

    protected void initPlayerSelection(GameState gameState) {
        renderable.disableInput();
        gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
        prepareNextEpisodeAudio(gameState);
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
            gameState.addGameEvent(new GameEvent(AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent(COOLDOWN, timestamp + 1000, false, this));
        }

    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if (currentState.eventsQueueContainsEvent(APP_QUIT))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if (currentState.eventsQueueContainsEvent(NEW_GAME))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }

    protected void endEpisode(final GameState gameState, String sEpisodeEndEventType) {
        renderable.disableInput();

        renderable.addBeforeFadeOut(new Runnable() {
            @Override
            public void run() {
                gameState.addGameEvent(new GameEvent(AUDIO_STOP_UI, renderable.MAINMENU_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(AUDIO_DISPOSE_UI, renderable.MAINMENU_AUDIO_PATH));
            }
        });

        endEpisodeAndAddEventWithType(gameState, sEpisodeEndEventType);

    }

}
