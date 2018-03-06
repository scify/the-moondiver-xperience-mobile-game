package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.SpaceshipChargerRenderable;

public class SpaceshipChargerEpisodeRules extends TemporaryEpisodeRules {

    protected SpaceshipChargerRenderable spaceshipChargerRenderable;

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case CALCULATOR_EPISODE:
                gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                break;
            default:
                super.handleUserAction(gsCurrent, userAction);
                break;
        }
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            super.episodeStartedEvents(currentState);
            addEpisodeBackgroundImage(currentState, "img/space1.png");
            initializeAndAddRocketController(currentState);
        }
    }

    protected void initializeAndAddRocketController(GameState currentState) {
        spaceshipChargerRenderable = new SpaceshipChargerRenderable(0,0,appInfo.getScreenWidth(), appInfo.getScreenHeight(), "spaceship_charger");
        spaceshipChargerRenderable.setImgPath("img/rocket_controller.png");
        spaceshipChargerRenderable.setCurrentMoonPhase(gameInfo.getCurrentMoonPhase());
        spaceshipChargerRenderable.setNextMoonPhase(gameInfo.getNextMoonPhase());
        spaceshipChargerRenderable.setPostNextMoonPhase(gameInfo.getPostNextMoonPhase());
        ActionButton escape = createEscapeButton();
        escape.setUserAction(new UserAction(UserActionCode.BACK));
        spaceshipChargerRenderable.setEscapeButton(escape);
        currentState.addRenderable(spaceshipChargerRenderable);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if(currentState.eventsQueueContainsEventOwnedBy("CALCULATOR_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpState(currentState));
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if(endStateFromParent != null)
            return endStateFromParent;
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
    }
}
