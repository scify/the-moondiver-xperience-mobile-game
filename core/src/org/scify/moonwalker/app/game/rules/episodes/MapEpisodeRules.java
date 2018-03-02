package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

import java.util.LinkedList;
import java.util.List;

public class MapEpisodeRules extends TemporaryEpisodeRules {

    List<MapLocationRenderable> mapLocationRenderables;

    public MapEpisodeRules(GameState gsCurrent) {
        super(gsCurrent);
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
