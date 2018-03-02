package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.SpaceshipChargerRenderable;

public class SpaceshipChargerEpisodeRules extends BaseEpisodeRules{

    SpaceshipChargerRenderable spaceshipChargerRenderable;

    public SpaceshipChargerEpisodeRules(GameState gsCurrent) {
        this.initialGameState = gsCurrent;
    }

    public SpaceshipChargerEpisodeRules() {
    }

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
        spaceshipChargerRenderable = new SpaceshipChargerRenderable(0,0,gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "spaceship_controller");
        spaceshipChargerRenderable.setImgPath("img/rocket_controller.png");
        spaceshipChargerRenderable.setCurrentMoonPhaseImgPath("img/moon.png");
        spaceshipChargerRenderable.setNextMoonPhaseImgPath1("img/moon.png");
        spaceshipChargerRenderable.setNextMoonPhaseImgPath2("img/moon.png");

        ActionButton escape = createEscapeButton();
        escape.setUserAction(new UserAction(UserActionCode.BACK));
        spaceshipChargerRenderable.setEscapeButton(escape);
        currentState.addRenderable(spaceshipChargerRenderable);
    }
}
