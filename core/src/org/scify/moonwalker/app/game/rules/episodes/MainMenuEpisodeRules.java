package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

import java.util.*;

public class MainMenuEpisodeRules extends BaseEpisodeRules {

    protected float BUTTON_WIDTH;
    protected float BUTTON_HEIGHT;
    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;
    protected MainMenuRenderable renderable;

    public MainMenuEpisodeRules() {
        super();
        this.BUTTON_WIDTH = appInfo.getScreenWidth() * 0.2f ;
        this.BUTTON_HEIGHT = appInfo.getScreenHeight() * 0.2f;
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case NEW_GAME:
                //endGameAndAddEventWithType(gameState, "NEW_GAME");
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                renderable.initiatePlayerSelection();
                break;
            case QUIT:
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                endGameAndAddEventWithType(gameState, "APP_QUIT");
                break;
            case BOY_SELECTED:
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                removePreviousAvatarSelectionAndAddNew(gameState, "boy");
                renderable.setSelectedAvatar(renderable.getBoySelectionButton());
                gameInfo.setSelectedPlayer(SelectedPlayer.boy);
                break;
            case GIRL_SELECTED:
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", "audio/button1.mp3"));
                removePreviousAvatarSelectionAndAddNew(gameState, "girl");
                renderable.setSelectedAvatar(renderable.getGirlSelectionButton());
                gameInfo.setSelectedPlayer(SelectedPlayer.girl);
                break;
        }
        super.handleUserAction(gameState, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", "audio/mainMenu/menu.mp3"));
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/boy/music.mp3"));
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/girl/music.mp3"));
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/mobile.mp3"));
            super.episodeStartedEvents(currentState);

            addEpisodeBackgroundImage(currentState, "img/mainMenu/bg.png");
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "main_menu");
            //renderable.setImgPath("img/mainMenu/bg.png");
            createAndAddMainMenuButtons();
            createAvatarSelectionRenderable();
            currentState.addRenderable(renderable);
        }
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButton start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ","img/mainMenu/start.png" , UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButton cont = createActionButton("ΣΥΝΕΧΙΣΕ","img/mainMenu/continue.png", UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButton toggle = createActionButton("ΗΧΟΣ ON/OFF","img/mainMenu/toggleAudio.png", UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButton about = createActionButton("ABOUT","img/mainMenu/about.png", UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButton quit = createActionButton("ΕΞΟΔΟΣ","img/mainMenu/quit.png", UserActionCode.QUIT);
        renderable.setQuitButton(quit);
    }

    protected void createAvatarSelectionRenderable() {
        ActionButton boy = createActionButton("boy","img/mainMenu/boyButton.png" , UserActionCode.BOY_SELECTED);
        renderable.setBoySelectionButton(boy);

        ActionButton girl = createActionButton("girl","img/mainMenu/girlButton.png" , UserActionCode.GIRL_SELECTED);
        renderable.setGirlSelectionButton(girl);

        //renderable.setSelectedAvatar(boy);
        gameInfo.setSelectedPlayer(SelectedPlayer.unset);
        //currentState.addRenderable(renderable);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if (currentState.eventsQueueContainsEvent("APP_QUIT"))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if (currentState.eventsQueueContainsEvent("NEW_GAME"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }

    @Override
    public void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }

    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType("AVATAR_SELECTED");
        currentState.addGameEvent(new GameEvent("AVATAR_SELECTED", newSelection));
    }

    protected ActionButton createActionButton(String id, String imgPath, UserActionCode code) {
        return createImageButton(id, imgPath, new UserAction(code), 0, 0);
    }
}
