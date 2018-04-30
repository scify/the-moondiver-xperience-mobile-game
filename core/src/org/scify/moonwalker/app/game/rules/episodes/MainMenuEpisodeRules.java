package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

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

    public static final String EPISODE_FINISHED = "EPISODE_FINISHED";

    public static final String BOY_BUTTON_ID = "boyButton";
    public static final String BOY_AVATAR_BUTTON_ID = "boyAvatarButton";
    public static final String GIRL_BUTTON_ID = "girlButton";
    public static final String GIRL_AVATAR_BUTTON_ID = "girlAvatarButton";
    public static final String MAIN_MENU_ID = "main_menu";

    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;
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
        GameEvent coolDownEvent;
        GameEvent avatarSelected;
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
            // Create the main renderable
            // and update rule class variable
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), MAIN_MENU_ID);

            // Create contained elements
            createAndAddMainMenuButtons();
            createAvatarSelectionRenderable();

            // Start sound
            currentState.addGameEvent(new GameEvent(AUDIO_START_LOOP_UI, renderable.BG_AUDIO_PATH));

            // Enable input after fade in
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.enableInput();
                }
            });

            // Call parent starting process
            super.episodeStartedEvents(currentState);
        }
    }

    private void prepareNextEpisodeAudio(GameState currentState) {
        currentState.addGameEvent(new GameEvent(AUDIO_LOAD_UI, renderable.BOY_MUSIC_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent(AUDIO_LOAD_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButtonRenderable start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ", renderable.START_BUTTON_IMG_PATH, UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButtonRenderable cont = createActionButton("ΣΥΝΕΧΙΣΕ", renderable.CONTINUE_BUTTON_IMG_PATH, UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButtonRenderable toggle = createActionButton("ΗΧΟΣ ON/OFF", renderable.TOGGLE_AUDIO_BUTTON_IMG_PATH, UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButtonRenderable about = createActionButton("ABOUT", renderable.ABOUT_BUTTON_IMG_PATH, UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButtonRenderable quit = createActionButton("ΕΞΟΔΟΣ", renderable.QUIT_BUTTON_IMG_PATH, UserActionCode.QUIT);
        renderable.setQuitButton(quit);
    }

    protected void createAvatarSelectionRenderable() {
        ActionButtonRenderable boyButton = createActionButton(BOY_BUTTON_ID, renderable.BOY_BUTTON_IMG_PATH, UserActionCode.BOY_SELECTED);
        ActionButtonRenderable boyAvatarButton = createActionButton(BOY_AVATAR_BUTTON_ID, renderable.BOY_IMG_PATH, UserActionCode.BOY_SELECTED);
        renderable.setBoyButton(boyButton);
        renderable.setBoyAvatarButton(boyAvatarButton);

        ActionButtonRenderable girlButton = createActionButton(GIRL_BUTTON_ID, renderable.GIRL_BUTTON_IMG_PATH, UserActionCode.GIRL_SELECTED);
        ActionButtonRenderable girlAvatarButton = createActionButton(GIRL_AVATAR_BUTTON_ID, renderable.GIRL_IMG_PATH, UserActionCode.GIRL_SELECTED);
        renderable.setGirlButton(girlButton);
        renderable.setGirlAvatarButton(girlAvatarButton);

        gameInfo.setSelectedPlayer(SelectedPlayer.unset);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if (currentState.eventsQueueContainsEvent(APP_QUIT))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if (currentState.eventsQueueContainsEvent(NEW_GAME))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        /*Iterator iter = currentState.getEventQueue().iterator();
        System.out.println("GameEvents:");
        while (iter.hasNext()) {
            System.out.println("\t" + ((GameEvent)iter.next()).type);
        }*/
        return endState;
    }


    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType(AVATAR_SELECTED);
        currentState.addGameEvent(new GameEvent(AVATAR_SELECTED, newSelection, this));
    }

    protected ActionButtonRenderable createActionButton(String id, String imgPath, String code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
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

    protected void endEpisode(final GameState gameState, String sEpisodeEndEventType) {
        renderable.disableInput();

        renderable.addBeforeFadeOut(new Runnable() {
            @Override
            public void run() {
                gameState.addGameEvent(new GameEvent(AUDIO_STOP_UI, renderable.BG_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent(AUDIO_DISPOSE_UI, renderable.BG_AUDIO_PATH));
            }
        });

        endGameAndAddEventWithType(gameState, sEpisodeEndEventType);

    }

}
