package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

import java.util.LinkedList;
import java.util.List;

public class MapEpisodeRules extends BaseEpisodeRules {

    List<MapLocationRenderable> mapLocationRenderables;

    public MapEpisodeRules(GameState gsCurrent) {
        this.initialGameState = gsCurrent;
    }

    public MapEpisodeRules() {
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(userAction != null)
            handleUserAction(gsCurrent, userAction);
        return gsCurrent;
    }

    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case BACK:
                gameEndedEvents(gsCurrent);
                break;
        }
    }

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!gameHasStarted(currentState)) {
            addGameStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/map.jpg");
            addPlayerAvatar(currentState);
            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            createMapLocationRenderables();
            for(MapLocationRenderable renderable : mapLocationRenderables)
                currentState.addRenderable(renderable);
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE");
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        GameState toReturn = gsCurrent;
        if(initialGameState != null) {
            toReturn = this.initialGameState;
        }
        if(gsCurrent.eventsQueueContainsEvent("PREVIOUS_EPISODE"))
            return new EpisodeEndState(EpisodeEndStateCode.TEMP_EPISODE_FINISHED, toReturn);
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, toReturn);
    }

    @Override
    public void gameEndedEvents(GameState gsCurrent) {
        gsCurrent.addGameEvent(new GameEvent("PREVIOUS_EPISODE"));
        gsCurrent.addGameEvent(new GameEvent("EPISODE_FINISHED"));
    }

    protected void createMapLocationRenderables() {
        // use linked list to be ordered
        mapLocationRenderables = new LinkedList<>();
        MapLocationRenderable renderable1 = new MapLocationRenderable(gameInfo.pixelsWithDensity(50), gameInfo.pixelsWithDensity(50), gameInfo.pixelsWithDensity(150), gameInfo.pixelsWithDensity(100), "location1");
        ActionButton location1Btn = new ActionButton(gameInfo.pixelsWithDensity(50), gameInfo.pixelsWithDensity(25), gameInfo.pixelsWithDensity(20), gameInfo.pixelsWithDensity(20), "image_button", "location1Btn");
        location1Btn.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        location1Btn.setImgPath("img/acropolis.jpg");
        renderable1.setButton(location1Btn);
        renderable1.setDestinationDistance(1000.5f);
        renderable1.setDestinationName("Athens");
        renderable1.setImgPath("img/component_background.png");

        MapLocationRenderable renderable2 = new MapLocationRenderable(gameInfo.pixelsWithDensity(300), gameInfo.pixelsWithDensity(200), gameInfo.pixelsWithDensity(150), gameInfo.pixelsWithDensity(100), "location2");
        ActionButton location2Btn = new ActionButton(gameInfo.pixelsWithDensity(50), gameInfo.pixelsWithDensity(25), gameInfo.pixelsWithDensity(20), gameInfo.pixelsWithDensity(20), "image_button", "location3Btn");
        location2Btn.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        location2Btn.setImgPath("img/madrid.jpg");
        renderable2.setButton(location2Btn);
        renderable2.setDestinationDistance(3460.5f);
        renderable2.setDestinationName("Madrid");
        renderable2.setImgPath("img/component_background.png");

        mapLocationRenderables.add(renderable1);
        mapLocationRenderables.add(renderable2);
    }

}
