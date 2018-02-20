package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;
import org.scify.moonwalker.app.ui.components.ActionButton;

import java.util.*;

public class MainMenuEpisodeRules extends SinglePlayerRules {

    protected float BUTTON_WIDTH;
    protected Map<String, UserActionCode> buttonTitlesAndActionCodes;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        BUTTON_WIDTH = gameInfo.getScreenWidth() / 2f;
        initializeButtons();
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
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
    public boolean isGameFinished(GameState currentState) {
        return gameState.eventsQueueContainsEvent("EPISODE_FINISHED");
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));
            gsCurrent.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/Andromeda-galaxy.jpg"));
            createAndAddMainMenuButtons(gsCurrent);
        }
    }

    protected void createAndAddMainMenuButtons(GameState gsCurrent) {
        List<ActionButton> mainMenuButtons = new ArrayList<>();
        for(Map.Entry<String, UserActionCode> buttonTitleAndActionCode : buttonTitlesAndActionCodes.entrySet()) {
            ActionButton newGameBtn = new ActionButton(0,0, BUTTON_WIDTH, gameInfo.pixelsWithDensity(50), "text_button", buttonTitleAndActionCode.getKey());
            newGameBtn.setTitle(buttonTitleAndActionCode.getKey());
            newGameBtn.setUserAction(new UserAction(buttonTitleAndActionCode.getValue()));
            mainMenuButtons.add(newGameBtn);
        }
        gsCurrent.addGameEvent(new GameEvent("BUTTONS_LIST", mainMenuButtons));
    }

    private void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case NEW_GAME:
                gameEndedEvents(gsCurrent);
                gsCurrent.addGameEvent(new GameEvent("NEW_GAME"));
                break;
            case QUIT:
                gameEndedEvents(gsCurrent);
                gsCurrent.addGameEvent(new GameEvent("APP_QUIT"));
                break;
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if(currentState.eventsQueueContainsEvent("APP_QUIT"))
            endState = new EpisodeEndState(EpisodeEndStateCode.APP_QUIT, currentState);
        else if(currentState.eventsQueueContainsEvent("NEW_GAME"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        return endState;
    }

    @Override
    public void gameEndedEvents(GameState currentState) {
        gameState.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
