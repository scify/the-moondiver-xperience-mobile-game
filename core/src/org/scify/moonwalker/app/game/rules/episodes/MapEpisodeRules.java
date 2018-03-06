package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.MapLocationRenderable;

import java.util.LinkedList;
import java.util.List;

public class MapEpisodeRules extends TemporaryEpisodeRules {

    protected List<Renderable> mapLocationRenderables;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/map.jpg");
            addPlayerAvatar(currentState);
            ActionButton escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            currentState.addRenderable(escape);
            createMapLocationRenderables();
            currentState.addRenderables(mapLocationRenderables);
        }
    }

    protected void createMapLocationRenderables() {
        // use linked list to be ordered
        mapLocationRenderables = new LinkedList<>();
        MapLocationRenderable renderable1 = new MapLocationRenderable(appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(150), appInfo.pixelsWithDensity(100), "location1");
        ActionButton location1Btn = new ActionButton(appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(25), appInfo.pixelsWithDensity(20), appInfo.pixelsWithDensity(20), "image_button", "location1Btn");
        location1Btn.setUserAction(new UserAction(UserActionCode.FINISH_EPISODE));
        location1Btn.setImgPath("img/acropolis.jpg");
        renderable1.setButton(location1Btn);
        renderable1.setDestinationDistance(1000.5f);
        renderable1.setDestinationName("Athens");
        renderable1.setImgPath("img/component_background.png");

        MapLocationRenderable renderable2 = new MapLocationRenderable(appInfo.pixelsWithDensity(300), appInfo.pixelsWithDensity(200), appInfo.pixelsWithDensity(150), appInfo.pixelsWithDensity(100), "location2");
        ActionButton location2Btn = new ActionButton(appInfo.pixelsWithDensity(50), appInfo.pixelsWithDensity(25), appInfo.pixelsWithDensity(20), appInfo.pixelsWithDensity(20), "image_button", "location3Btn");
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
