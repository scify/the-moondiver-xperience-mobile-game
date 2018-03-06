package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.ButtonsListRenderable;

import java.util.*;

public class MainMenuEpisodeRules extends BaseEpisodeRules {

    protected float BUTTON_WIDTH;
    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;
    protected ButtonsListRenderable mainMenuButtons;

    public MainMenuEpisodeRules() {
        this.BUTTON_WIDTH = appInfo.getScreenWidth() / 2f;
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case NEW_GAME:
                endGameAndAddEventWithType(gsCurrent,"NEW_GAME");
                break;
            case QUIT:
                endGameAndAddEventWithType(gsCurrent,"APP_QUIT");
                break;
        }
        super.handleUserAction(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState gsCurrent) {
        if (!isEpisodeStarted(gsCurrent)) {
            super.episodeStartedEvents(gsCurrent);
            addEpisodeBackgroundImage(gsCurrent, "img/Andromeda-galaxy.jpg");
            createAndAddMainMenuButtons(gsCurrent);
        }
    }

    protected void createAndAddMainMenuButtons(GameState gsCurrent) {
        initializeButtons();
        mainMenuButtons = new ButtonsListRenderable(0,0, BUTTON_WIDTH, appInfo.pixelsWithDensity(50), "buttons_list_vertical", "main_menu_buttons");
        for(Map.Entry<String, UserActionCode> buttonTitleAndActionCode : buttonTitlesAndActionCodes.entrySet()) {
            ActionButton newGameBtn = new ActionButton(0,0, BUTTON_WIDTH, appInfo.pixelsWithDensity(50), "text_button", buttonTitleAndActionCode.getKey());
            newGameBtn.setTitle(buttonTitleAndActionCode.getKey());
            newGameBtn.setUserAction(new UserAction(buttonTitleAndActionCode.getValue()));
            mainMenuButtons.addButton(newGameBtn);
        }
        mainMenuButtons.setZIndex(2);
        gsCurrent.addRenderable(mainMenuButtons);
    }

    protected void initializeButtons() {
        // using a LinkedHashMap to preserve the insertion order
        buttonTitlesAndActionCodes = new LinkedHashMap<>();
        buttonTitlesAndActionCodes.put("New Game", UserActionCode.NEW_GAME);
        buttonTitlesAndActionCodes.put("Preferences", UserActionCode.PREFERENCES);
        buttonTitlesAndActionCodes.put("About", UserActionCode.ABOUT);
        buttonTitlesAndActionCodes.put("Quit", UserActionCode.QUIT);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if(currentState.eventsQueueContainsEvent("APP_QUIT"))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if(currentState.eventsQueueContainsEvent("NEW_GAME"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }

    @Override
    public void episodeEndedEvents(GameState currentState) {
        currentState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }
}
