package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.*;

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
        GameEvent coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
        if (coolDownEvent != null && timestamp > coolDownEvent.delay) {
            renderable.decreaseCountDown();
            gameState.removeGameEventsWithType("COOLDOWN");
            if (renderable.getCountDownValue() < 0) {
                gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.BG_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BG_AUDIO_PATH));
                LGDXEffectList fadeOutEffects = new LGDXEffectList();
                fadeOutEffects.addEffect(new FadeLGDXEffect(1.0, 0.0, 1000));
                fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        endGameAndAddEventWithType(gameState, "NEW_GAME");
                    }
                }));
                renderable.apply(fadeOutEffects);
            } else {
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
                gameState.addGameEvent(new GameEvent("COOLDOWN", timestamp + 1000, false, this));
            }
        }
        if (fadeInEffectsCompleted == false && renderable != null && renderable.isReadyForInput()) {
            fadeInEffectsCompleted = true;
        }

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
                        renderable.disableInput();
                        gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
                        prepareNextEpisodeAudio(gameState);
                        renderable.initiatePlayerSelection();
                        mainMenuButtonsEnabled = false;
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
                    gameInfo.setSelectedPlayer(SelectedPlayer.boy);
                    coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
                    avatarSelected = gameState.getGameEventsWithType("AVATAR_SELECTED");
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
                    if (coolDownEvent != null) {
                        gameState.removeGameEventsWithType("COOLDOWN");
                    }
                    if (avatarSelected != null && avatarSelected.parameters.equals("boy")) {
                        renderable.setSelectedAvatarButton(renderable.getBoyButton());
                        renderable.forceCountDownToZero();
                        gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime(), false, this));
                    } else {
                        removePreviousAvatarSelectionAndAddNew(gameState, "boy");
                        renderable.setSelectedAvatarButton(renderable.getBoyButton());
                        renderable.resetCountDown();
                        gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime() + 1000, false, this));
                    }
                    break;
                case GIRL_SELECTED:
                    gameInfo.setSelectedPlayer(SelectedPlayer.girl);
                    coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
                    avatarSelected = gameState.getGameEventsWithType("AVATAR_SELECTED");
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.CLICK_AUDIO_PATH));
                    if (coolDownEvent != null) {
                        gameState.removeGameEventsWithType("COOLDOWN");
                    }
                    if (avatarSelected != null && avatarSelected.parameters.equals("girl")) {
                        renderable.setSelectedAvatarButton(renderable.getGirlButton());
                        renderable.forceCountDownToZero();
                        gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime(), false, this));
                    } else {
                        removePreviousAvatarSelectionAndAddNew(gameState, "girl");
                        renderable.setSelectedAvatarButton(renderable.getGirlButton());
                        renderable.resetCountDown();
                        gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime() + 1000, false, this));
                    }
                    break;
            }
        }
        super.handleUserAction(gameState, userAction);

    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "main_menu");
            currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.BG_AUDIO_PATH));
            super.episodeStartedEvents(currentState);
            createAndAddMainMenuButtons();
            createAvatarSelectionRenderable();
            currentState.addRenderable(renderable);
            //addEpisodeBackgroundImage(currentState, "img/mainMenu/bg.png");
        }
    }

    private void prepareNextEpisodeAudio(GameState currentState) {
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", renderable.BOY_MUSIC_AUDIO_PATH));
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButton start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ", renderable.START_BUTTON_IMG_PATH, UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButton cont = createActionButton("ΣΥΝΕΧΙΣΕ", renderable.CONTINUE_BUTTON_IMG_PATH, UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButton toggle = createActionButton("ΗΧΟΣ ON/OFF", renderable.TOGGLE_AUDIO_BUTTON_IMG_PATH, UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButton about = createActionButton("ABOUT", renderable.ABOUT_BUTTON_IMG_PATH, UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButton quit = createActionButton("ΕΞΟΔΟΣ", renderable.QUIT_BUTTON_IMG_PATH, UserActionCode.QUIT);
        renderable.setQuitButton(quit);
    }

    protected void createAvatarSelectionRenderable() {
        ActionButton boyButton = createActionButton("boyButton", renderable.BOY_BUTTON_IMG_PATH, UserActionCode.BOY_SELECTED);
        ActionButton boyAvatarButton = createActionButton("boyAvatarButton", renderable.BOY_IMG_PATH, UserActionCode.BOY_SELECTED);
        renderable.setBoyButton(boyButton);
        renderable.setBoyAvatarButton(boyAvatarButton);

        ActionButton girlButton = createActionButton("girlButton", renderable.GIRL_BUTTON_IMG_PATH, UserActionCode.GIRL_SELECTED);
        ActionButton girlAvatarButton = createActionButton("girlAvatarButton", renderable.GIRL_IMG_PATH, UserActionCode.GIRL_SELECTED);
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

    protected ActionButton createActionButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
    }
}
