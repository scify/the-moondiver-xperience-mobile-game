package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.Renderable;

public class ForestEpisodeRules extends BaseEpisodeRules{

    protected final int ROCKET_IMG_WIDTH_PIXELS = 200;
    protected final int ROCKET_IMG_HEIGHT_PIXELS = 200;
    @Override
    public void gameStartedEvents(GameState currentState) {
        if (!gameHasStarted(currentState)) {
            addGameStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/forest.jpg");
            final float rocketWidth = gameInfo.pixelsWithDensity(ROCKET_IMG_WIDTH_PIXELS);
            final float rocketHeight = gameInfo.pixelsWithDensity(ROCKET_IMG_HEIGHT_PIXELS);
            Renderable renderable = new Renderable("image", "rocket");
            renderable.setWidth(rocketWidth);
            renderable.setHeight(rocketHeight);
            renderable.setxPos(rocketWidth);
            renderable.setyPos(gameInfo.getScreenHeight() / 2f - rocketHeight);
            renderable.setImgPath("img/rocket.png");
            currentState.addRenderable(renderable);
        }
    }
}
