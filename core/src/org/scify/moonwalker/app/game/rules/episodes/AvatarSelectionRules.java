package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.AvatarSelectionRenderable;

public class AvatarSelectionRules extends BaseEpisodeRules {

    protected final float SELECT_BUTTON_HEIGHT_PIXELS = 50;
    protected AvatarSelectionRenderable renderable;

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BOY_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gsCurrent, "boy");
                renderable.setSelectedAvatar(renderable.getBoySelection());
                break;
            case GIRL_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gsCurrent, "girl");
                renderable.setSelectedAvatar(renderable.getGirlSelection());
                break;
        }
        super.handleUserAction(gsCurrent, userAction);
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
        if (!gameHasStarted(currentState)) {
            addGameStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/Andromeda-galaxy.jpg");
            createAvatarSelectionRenderable(currentState);

            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            addRenderableEntry("back_button", escape);
        }
    }

    protected void removePreviousAvatarSelectionAndAddNew(GameState currentState, String newSelection) {
        currentState.removeGameEventsWithType("AVATAR_SELECTED");
        currentState.addGameEvent(new GameEvent("AVATAR_SELECTED", newSelection));
    }

    protected void createAvatarSelectionRenderable(GameState currentState) {
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

        renderable = new AvatarSelectionRenderable(0,0,gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "avatar_selection");
        renderable.setBoySelection(boyBtn);
        renderable.setGirlSelection(girlBtn);
        renderable.setSelectBtn(selectBtn);

        renderable.setSelectedAvatar(boyBtn);
        removePreviousAvatarSelectionAndAddNew(currentState, "boy");
        currentState.addRenderable(renderable);
    }
}
