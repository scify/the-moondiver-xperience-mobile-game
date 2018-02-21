package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.rules.SinglePlayerRules;

public class CockpitRules extends SinglePlayerRules {

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        gsCurrent = super.getNextState(gsCurrent, userAction);
        if(isGamePaused(gsCurrent))
            return gsCurrent;
        gameStartedEvents(gsCurrent);
        gameResumedEvents(gsCurrent);
        return gsCurrent;
    }

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEventOwnedBy("EPISODE_STARTED", this)) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED", null, this));

            Renderable cockpitImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "cockpit");
            cockpitImage.setImgPath("img/cockpit.png");
            cockpitImage.setZIndex(2);
            gsCurrent.addRenderable(cockpitImage);

            Renderable spaceImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "space");
            spaceImage.setImgPath("img/space1.png");
            gsCurrent.addRenderable(spaceImage);


        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        return null;
    }

    @Override
    public void gameEndedEvents(GameState currentState) {

    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
