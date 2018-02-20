package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.graphics.Color;
import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;
import org.scify.moonwalker.app.ui.components.ActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            case BOY_SELECTED:
               removePreviousAvatarSelectionAndAddNew(gsCurrent, "boy");
                break;
            case GIRL_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gsCurrent, "girl");
                break;
            case FINISH_EPISODE:
                gameEndedEvents(gameState);
                break;
            case BACK:
                gsCurrent.addGameEvent(new GameEvent("BACK", null, this));
                gameEndedEvents(gameState);
                break;
        }
    }

    protected void removePreviousAvatarSelectionAndAddNew(GameState gsCurrent, String newSelection) {
        gsCurrent.removeGameEventsWithType("AVATAR_SELECTED");
        gsCurrent.addGameEvent(new GameEvent("AVATAR_SELECTED", newSelection));
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = null;
        if(currentState.eventsQueueContainsEventOwnedBy("BACK", this))
            endState = new EpisodeEndState(EpisodeEndStateCode.PREVIOUS_EPISODE, currentState);
        else if(currentState.eventsQueueContainsEvent("AVATAR_SELECTED"))
            endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!currentState.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this)) {
            currentState.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));
            currentState.addGameEvent(new GameEvent("BACKGROUND_IMG_UI", "img/Andromeda-galaxy.jpg"));
            createAvatarsButtonsList(currentState);

            ActionButton escape = createEscapeButton(currentState);
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            addRenderableEntry("calculator_finished_button", escape);
            // set the boy selected by default
            removePreviousAvatarSelectionAndAddNew(gameState, "boy");
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
        selectBtn.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        List<HashMap.SimpleEntry<ActionButton, Color>> buttons = new ArrayList();
        //white color means transparent (no color)
        buttons.add(new HashMap.SimpleEntry<>(boyBtn, Color.WHITE));
        buttons.add(new HashMap.SimpleEntry<>(selectBtn, Color.WHITE));
        buttons.add(new HashMap.SimpleEntry<>(girlBtn, Color.DARK_GRAY));
        currentState.addGameEvent(new GameEvent("BUTTONS_LIST_HORIZONTAL", buttons, buttons.get(2)));
    }

    @Override
    public void gameEndedEvents(GameState currentState) {
        gameState.addGameEvent(new GameEvent("EPISODE_FINISHED", null, this));
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }

    @Override
    public boolean isGameFinished(GameState currentState) {
        return gameState.eventsQueueContainsEvent("EPISODE_FINISHED");
    }
}
