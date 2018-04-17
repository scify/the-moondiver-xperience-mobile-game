package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.MoonWalkerGameState;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.*;

public class MainMenuEpisodeRules extends BaseEpisodeRules {

    protected float BUTTON_WIDTH;
    protected float BUTTON_HEIGHT;
    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;
    protected MainMenuRenderable renderable;
    protected boolean mainMenuButtonsEnabled;

    public MainMenuEpisodeRules() {
        super();
        this.BUTTON_WIDTH = appInfo.getScreenWidth() * 0.2f;
        this.BUTTON_HEIGHT = appInfo.getScreenHeight() * 0.2f;
        mainMenuButtonsEnabled = true;
    }

    @Override
    public GameState getNextState(final GameState gameState, UserAction userAction) {
        long timestamp = new Date().getTime();
        GameEvent coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
        if (coolDownEvent != null && timestamp > coolDownEvent.delay) {
            renderable.decreaseCountDown();
            gameState.removeGameEventsWithType("COOLDOWN");
            if (renderable.getCountDownValue() < 0) {
                gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", "audio/mainMenu/menu.mp3"));
                gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/mainMenu/menu.mp3"));
                LGDXEffectList fadeOutEffect = new LGDXEffectList();
                fadeOutEffect.addEffect(new FadeLGDXEffect(1.0, 0.0, 2000));
                fadeOutEffect.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        endGameAndAddEventWithType(new MoonWalkerGameState((MoonWalkerGameState)gameState), "NEW_GAME");
                    }
                }));
                renderable.apply(fadeOutEffect);
            } else {
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                gameState.addGameEvent(new GameEvent("COOLDOWN", timestamp + 1000, false, this));
            }
        }

        return super.getNextState(gameState, userAction);
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        GameEvent coolDownEvent;
        switch (userAction.getActionCode()) {
            case NEW_GAME:
                if (mainMenuButtonsEnabled) {
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
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
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                    endGameAndAddEventWithType(gameState, "APP_QUIT");
                }
                break;
            case BOY_SELECTED:
                if (!mainMenuButtonsEnabled) {
                    coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
                    if (coolDownEvent != null)
                        gameState.removeGameEventsWithType("COOLDOWN");
                    renderable.resetCountDown();
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                    gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime() + 1000, false, this));
                    removePreviousAvatarSelectionAndAddNew(gameState, "boy");
                    renderable.setSelectedAvatarButton(renderable.getBoyButton());
                    gameInfo.setSelectedPlayer(SelectedPlayer.boy);
                }
                break;
            case GIRL_SELECTED:
                if (!mainMenuButtonsEnabled) {
                    coolDownEvent = gameState.getGameEventsWithType("COOLDOWN");
                    if (coolDownEvent != null)
                        gameState.removeGameEventsWithType("COOLDOWN");
                    renderable.resetCountDown();
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                    gameState.addGameEvent(new GameEvent("COOLDOWN", new Date().getTime() + 1000, false, this));
                    removePreviousAvatarSelectionAndAddNew(gameState, "girl");
                    renderable.setSelectedAvatarButton(renderable.getGirlButton());
                    gameInfo.setSelectedPlayer(SelectedPlayer.girl);
                }
                break;
        }
        super.handleUserAction(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            loadAudio(currentState);
            super.episodeStartedEvents(currentState);

            //addEpisodeBackgroundImage(currentState, "img/mainMenu/bg.png");

            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "main_menu");

            //renderable.setImgPath("img/mainMenu/bg.png");
            createAndAddMainMenuButtons();
            createAvatarSelectionRenderable();
            currentState.addRenderable(renderable);
        }
    }

    private void loadAudio(GameState currentState) {
        currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/mainMenu/menu.mp3"));
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/boy/music.mp3"));
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/girl/music.mp3"));
        currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/mobile.mp3"));
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButton start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ", "img/mainMenu/start.png", UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButton cont = createActionButton("ΣΥΝΕΧΙΣΕ", "img/mainMenu/continue.png", UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButton toggle = createActionButton("ΗΧΟΣ ON/OFF", "img/mainMenu/toggleAudio.png", UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButton about = createActionButton("ABOUT", "img/mainMenu/about.png", UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButton quit = createActionButton("ΕΞΟΔΟΣ", "img/mainMenu/quit.png", UserActionCode.QUIT);
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
