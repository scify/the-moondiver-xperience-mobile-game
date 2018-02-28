package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.SpaceshipControllerRenderable;

public class SpaceshipControllerEpisodeRules extends BaseEpisodeRules{

    SpaceshipControllerRenderable spaceshipControllerRenderable;

    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!currentState.eventsQueueContainsEvent("EPISODE_STARTED")) {
            currentState.addGameEvent(new GameEvent("EPISODE_STARTED"));
            Renderable spaceImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "space");
            spaceImage.setImgPath("img/space1.png");
            currentState.addRenderable(spaceImage);
            addRenderableEntry("space", spaceImage);
            initializeAndAddRocketController(currentState);
        }
    }

    protected void initializeAndAddRocketController(GameState currentState) {
        spaceshipControllerRenderable = new SpaceshipControllerRenderable(0,0,gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "spaceship_controller");
        spaceshipControllerRenderable.setImgPath("img/rocket_controller.png");
        spaceshipControllerRenderable.setCurrentMoonPhaseImgPath("img/moon.png");
        spaceshipControllerRenderable.setNextMoonPhaseImgPath1("img/moon.png");
        spaceshipControllerRenderable.setNextMoonPhaseImgPath2("img/moon.png");

        ActionButton escape = createEscapeButton();
        escape.setUserAction(new UserAction(UserActionCode.BACK));
        spaceshipControllerRenderable.setEscapeButton(escape);
        currentState.addRenderable(spaceshipControllerRenderable);
    }
}
