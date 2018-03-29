package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.AvatarSelectionRenderable;

public class AvatarSelectionRules extends BaseEpisodeRules {

    protected final float SELECT_BUTTON_HEIGHT_PIXELS = 50;
    protected AvatarSelectionRenderable renderable;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/Andromeda-galaxy.jpg");
            createAvatarSelectionRenderable(currentState);
            try {
                Thread.sleep(1000);
            }catch (Exception e) {

            }

            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            addRenderableEntry("back_button", escape);
        }
    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BOY_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gameState, "boy");
                renderable.setSelectedAvatar(renderable.getBoySelection());
                gameInfo.setSelectedPlayer(SelectedPlayer.boy);
                gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/girl/music.mp3"));
                gameState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/boy/music.mp3"));
                gameState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/mobile.mp3"));
                break;
            case GIRL_SELECTED:
                removePreviousAvatarSelectionAndAddNew(gameState, "girl");
                renderable.setSelectedAvatar(renderable.getGirlSelection());
                gameInfo.setSelectedPlayer(SelectedPlayer.girl);
                gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", "audio/room_episode/boy/music.mp3"));
                gameState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/girl/music.mp3"));
                gameState.addGameEvent(new GameEvent("AUDIO_LOAD_UI", "audio/room_episode/mobile.mp3"));
                break;

        }
        super.handleUserAction(gameState, userAction);
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

        renderable = new AvatarSelectionRenderable(0,0,appInfo.getScreenWidth(), appInfo.getScreenHeight(), "avatar_selection");
        renderable.setBoySelection(boyBtn);
        renderable.setGirlSelection(girlBtn);
        renderable.setSelectBtn(selectBtn);

        renderable.setSelectedAvatar(boyBtn);
        removePreviousAvatarSelectionAndAddNew(currentState, "boy");
        gameInfo.setSelectedPlayer(SelectedPlayer.boy);
        currentState.addRenderable(renderable);
    }
}
