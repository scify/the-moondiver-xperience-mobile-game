package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.engine.renderables.ActionButtonWithEffect;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainMenuEpisodeRules extends BaseEpisodeRules {

    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;
    protected MainMenuRenderable renderable;
    protected boolean mainMenuButtonsEnabled;
    protected boolean fadeInEffectsCompleted;

    public MainMenuEpisodeRules() {
        super();
        mainMenuButtonsEnabled = true;
        fadeInEffectsCompleted = false;
        renderable = null;
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        // Examine if we have a cooldown event
        GameEvent coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
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
                case TOGGLE_AUDIO:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent("AUDIO_TOGGLE_UI"));
                    }
                    break;
                case QUIT:
                    if (mainMenuButtonsEnabled) {
                        gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
                        endGameAndAddEventWithType(gameState, "APP_QUIT");
                    }
                    break;
                case BOY_SELECTED:
                    setSelectedPlayer(gameState, SelectedPlayer.boy);
                    break;
                case GIRL_SELECTED:
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

        coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
        avatarSelected = gameState.getGameEventsWithType("AVATAR_SELECTED");
        gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
        if (coolDownEvent != null) {
            gameState.removeGameEventsWithType("COOLDOWN");
        }
        if (avatarSelected != null && avatarSelected.parameters.equals(selectedPlayer)) {
            renderable.setSelectedAvatarButton(renderable.getPlayerButton(selectedPlayer));
            renderable.forceCountDownToZero();
            gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime(), false, this));
        } else {
            removePreviousAvatarSelectionAndAddNew(gameState, selectedPlayer);
            renderable.setSelectedAvatarButton(renderable.getPlayerButton(selectedPlayer));
            renderable.resetCountDown();
            gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime() + 1000, false, this));
        }
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        // If we are just starting
        if (!isEpisodeStarted(currentState)) {
            // Create the main renderable (and friends)
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "main_menu");
            createAndAddMainMenuButtons();
            createAvatarSelectionRenderable();
            currentState.addRenderable(renderable);

            // Start sound
            currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.BG_AUDIO_PATH));

            // Perform fade in process
            fadeInStage();
            //addEpisodeBackgroundImage(currentState, "img/mainMenu/bg.png");

            // Make sure you share that you are started
            super.episodeStartedEvents(currentState);
        }
    }

    private void prepareNextEpisodeAudio(GameState currentState) {
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", renderable.BOY_MUSIC_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButtonWithEffect start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ", renderable.START_BUTTON_IMG_PATH, UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButtonWithEffect cont = createActionButton("ΣΥΝΕΧΙΣΕ", renderable.CONTINUE_BUTTON_IMG_PATH, UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButtonWithEffect toggle = createActionButton("ΗΧΟΣ ON/OFF", renderable.TOGGLE_AUDIO_BUTTON_IMG_PATH, UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButtonWithEffect about = createActionButton("ABOUT", renderable.ABOUT_BUTTON_IMG_PATH, UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButtonWithEffect quit = createActionButton("ΕΞΟΔΟΣ", renderable.QUIT_BUTTON_IMG_PATH, UserActionCode.QUIT);
        renderable.setQuitButton(quit);
    }

    protected void createAvatarSelectionRenderable() {
        ActionButtonWithEffect boyButton = createActionButton("boyButton", renderable.BOY_BUTTON_IMG_PATH, UserActionCode.BOY_SELECTED);
        ActionButtonWithEffect boyAvatarButton = createActionButton("boyAvatarButton", renderable.BOY_IMG_PATH, UserActionCode.BOY_SELECTED);
        renderable.setBoyButton(boyButton);
        renderable.setBoyAvatarButton(boyAvatarButton);

        ActionButtonWithEffect girlButton = createActionButton("girlButton", renderable.GIRL_BUTTON_IMG_PATH, UserActionCode.GIRL_SELECTED);
        ActionButtonWithEffect girlAvatarButton = createActionButton("girlAvatarButton", renderable.GIRL_IMG_PATH, UserActionCode.GIRL_SELECTED);
        renderable.setGirlButton(girlButton);
        renderable.setGirlAvatarButton(girlAvatarButton);

        gameInfo.setSelectedPlayer(SelectedPlayer.unset);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if (currentState.eventsQueueContainsEvent("APP_QUIT"))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if (currentState.eventsQueueContainsEvent("NEW_GAME"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        /*Iterator iter = currentState.getEventQueue().iterator();
        System.out.println("GameEvents:");
        while (iter.hasNext()) {
            System.out.println("\t" + ((GameEvent)iter.next()).type);
        }*/
        return endState;
    }

    @Override
    public void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }

    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType("AVATAR_SELECTED");
        currentState.addGameEvent(new GameEvent("AVATAR_SELECTED", newSelection, this));
    }

    protected ActionButtonWithEffect createActionButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
    }


    // Updated process
    protected void fadeInStage() {
        // Fade-in everything
        EffectSequence fadeInEffects = new EffectSequence();
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 1000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {

                renderable.enableInput();

            }
        }));
        renderable.apply(fadeInEffects);

    }

    protected void initPlayerSelection(GameState gameState) {
        renderable.disableInput();
        gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
        prepareNextEpisodeAudio(gameState);
        renderable.initiatePlayerSelection();
        mainMenuButtonsEnabled = false;
    }

    protected void doCountdown(GameState gameState, long timestamp) {
        // update cooldown counter
        renderable.decreaseCountDown();
        gameState.removeGameEventsWithType("COOLDOWN");

        // If we have reached below zero
        if (renderable.getCountDownValue() < 0) {
            fadeOutStage(gameState);
        } else {
            // inform UI to update the cooldown
            gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
            gameState.addGameEvent(new GameEvent("COOLDOWN", timestamp + 1000, false, this));
        }

    }

    protected void fadeOutStage(final GameState gameState) {
        renderable.disableInput();

        gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.BG_AUDIO_PATH));
        gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BG_AUDIO_PATH));
        // do fade out
        EffectSequence fadeOutEffects = new EffectSequence();
        fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 1000));
        fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                endGameAndAddEventWithType(gameState, "NEW_GAME");
            }
        }));
        renderable.apply(fadeOutEffects);

    }

}
