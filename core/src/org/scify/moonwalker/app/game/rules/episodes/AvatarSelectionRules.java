package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.graphics.Color;
import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;
import org.scify.moonwalker.app.ui.components.ActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvatarSelectionRules extends SinglePlayerRules {

    protected final float SELECT_BUTTON_HEIGHT_PIXELS = 50;

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    private void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {

        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if(currentState.eventsQueueContainsEvent("BACK"))
            endState = new EpisodeEndState(EpisodeEndStateCode.BACK, currentState);
        else if(currentState.eventsQueueContainsEvent("AVATAR_SELECTED"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        return endState;
    }

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!currentState.eventsQueueContainsEvent("EPISODE_STARTED")) {
            currentState.addGameEvent(new GameEvent("EPISODE_STARTED"));
            currentState.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/Andromeda-galaxy.jpg"));
            createAvatarsButtonsList(currentState);
        }
    }

    protected void createAvatarsButtonsList(GameState currentState) {
        ActionButton boyBtn = new ActionButton("image_button", "boy");
        boyBtn.setImgPath("img/boy.png");
        boyBtn.setUserAction(new UserAction(UserActionCode.BOY_SELECTED));
        ActionButton girlBtn = new ActionButton("image_button", "girl");
        girlBtn.setImgPath("img/girl.png");
        girlBtn.setUserAction(new UserAction(UserActionCode.GIRL_SELECTED));
        ActionButton selectBtn = new ActionButton("text_button", "select");
        selectBtn.setHeight(SELECT_BUTTON_HEIGHT_PIXELS);
        selectBtn.setTitle("Start Game");
        List<HashMap.SimpleEntry<ActionButton, Color>> buttons = new ArrayList();
        buttons.add(new HashMap.SimpleEntry<>(boyBtn, Color.WHITE));
        buttons.add(new HashMap.SimpleEntry<>(selectBtn, Color.WHITE));
        buttons.add(new HashMap.SimpleEntry<>(girlBtn, Color.DARK_GRAY));
        currentState.addGameEvent(new GameEvent("BUTTONS_LIST_HORIZONTAL", buttons, buttons.get(2)));
    }

    @Override
    public void gameEndedEvents(GameState currentState) {
        gameState.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }

    @Override
    public boolean isGameFinished(GameState currentState) {
        return gameState.eventsQueueContainsEvent("EPISODE_FINISHED");
    }
}
