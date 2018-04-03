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
                endGameAndAddEventWithType(gameState, "NEW_GAME");
                break;
            case QUIT:
                endGameAndAddEventWithType(gameState, "APP_QUIT");
                break;
            case BOY_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gameState, "boy");
                renderable.setSelectedAvatar(renderable.getBoySelectionButton());
                gameInfo.setSelectedPlayer(SelectedPlayer.boy);
                break;
            case GIRL_SELECTED:
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
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/boy/music.mp3"));
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/girl/music.mp3"));
            currentState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/mobile.mp3"));
            super.episodeStartedEvents(currentState);

            addEpisodeBackgroundImage(currentState, "img/mainMenu/bg.png");
            renderable = new MainMenuRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "main_menu");
            //renderable.setImgPath("img/mainMenu/bg.png");
            createAndAddMainMenuButtons();
            //createAvatarSelectionRenderable();

            currentState.addRenderable(renderable);
        }
    }

    protected void createAndAddMainMenuButtons() {
        buttonTitlesAndActionCodes = new LinkedHashMap<>();

        ActionButton start = createActionButton("ΝΕΟ ΠΑΙΧΝΙΔΙ", UserActionCode.NEW_GAME);
        renderable.setStartGameButton(start);

        ActionButton cont = createActionButton("ΣΥΝΕΧΙΣΕ", UserActionCode.CONTINUE);
        renderable.setContinueGameButton(cont);

        ActionButton toggle = createActionButton("ΗΧΟΣ ON/OFF", UserActionCode.TOGGLE_AUDIO);
        renderable.setToggleAudioButton(toggle);

        ActionButton about = createActionButton("ABOUT", UserActionCode.ABOUT);
        renderable.setAboutButton(about);

        ActionButton quit = createActionButton("ΕΞΟΔΟΣ", UserActionCode.QUIT);
        renderable.setQuitButton(quit);
    }

    protected ActionButton createActionButton(String text, UserActionCode code) {
        ActionButton ret = new ActionButton(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, "text_button", text);
        buttonTitlesAndActionCodes.put(text, code);
        ret.setTitle(text);
        ret.setUserAction(new UserAction(code));
        return ret;
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

    protected void createAvatarSelectionRenderable() {
        ActionButton boyBtn = new ActionButton("image_button", "boy");
        boyBtn.setImgPath("img/boy.png");
        boyBtn.setUserAction(new UserAction(UserActionCode.BOY_SELECTED));
        ActionButton girlBtn = new ActionButton("image_button", "girl");
        girlBtn.setImgPath("img/girl.png");
        girlBtn.setUserAction(new UserAction(UserActionCode.GIRL_SELECTED));

        renderable.setBoySelectionButton(boyBtn);
        renderable.setGirlSelectionButton(girlBtn);
        renderable.setSelectedAvatar(boyBtn);

        //removePreviousAvatarSelectionAndAddNew(currentState, "boy");
        gameInfo.setSelectedPlayer(SelectedPlayer.boy);
        //currentState.addRenderable(renderable);
    }

    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType("AVATAR_SELECTED");
        currentState.addGameEvent(new GameEvent("AVATAR_SELECTED", newSelection));
    }
}
