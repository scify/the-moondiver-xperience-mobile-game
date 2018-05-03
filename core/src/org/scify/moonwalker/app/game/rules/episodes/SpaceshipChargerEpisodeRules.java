package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.ui.renderables.SpaceshipChargerRenderable;

public class SpaceshipChargerEpisodeRules extends TemporaryEpisodeRules {

    protected SpaceshipChargerRenderable spaceshipChargerRenderable;

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.CALCULATOR_EPISODE:
                gsCurrent.addGameEvent(new GameEvent("CALCULATOR_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                break;
            case UserActionCode.CHARGE_SPACESHIP_PASS_DAY:
                setFieldsForTimedEpisode(gsCurrent, "img/next_day.jpg", 4000);
                gsCurrent.addGameEvent(new GameEvent("SIMPLE_TIMED_IMAGE_EPISODE_STARTED", null, this));
                episodeEndedEvents(gsCurrent);
                chargeAndPassDay(gsCurrent);
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
            currentState.addGameEvent(new GameEvent("SCREEN_FADE_IN", 1f, this));
        }
    }

    protected void initializeAndAddRocketController(GameState currentState) {
        spaceshipChargerRenderable = new SpaceshipChargerRenderable(0,0,appInfo.getScreenWidth(), appInfo.getScreenHeight(), "spaceship_charger");
        spaceshipChargerRenderable.setImgPath("img/rocket_controller.png");
        setMoonPhases();
        spaceshipChargerRenderable.setRemainingEnergy(gameInfo.getRemainingEnergy());

        //ActionButtonRenderable escape = createEscapeButton();
        //escape.setUserAction(new UserAction(UserActionCode.BACK));

        //spaceshipChargerRenderable.setEscapeButton(escape);
        currentState.addRenderable(spaceshipChargerRenderable);
    }

    protected void chargeAndPassDay(GameState currentState) {
        gameInfo.dayPassed();
    }

    protected void setMoonPhases() {
        spaceshipChargerRenderable.setCurrentMoonPhase(gameInfo.getCurrentMoonPhase());
        spaceshipChargerRenderable.setNextMoonPhase(gameInfo.getNextMoonPhase());
        spaceshipChargerRenderable.setPostNextMoonPhase(gameInfo.getPostNextMoonPhase());
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        if(currentState.eventsQueueContainsEventOwnedBy("CALCULATOR_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.CALCULATOR_STARTED, cleanUpState(currentState));
        if(currentState.eventsQueueContainsEventOwnedBy("SIMPLE_TIMED_IMAGE_EPISODE_STARTED", this))
            return new EpisodeEndState(EpisodeEndStateCode.SIMPLE_TIMED_IMAGE_EPISODE, cleanUpState(currentState));
        EpisodeEndState endStateFromParent = super.determineEndState(currentState);
        if(endStateFromParent != null)
            return endStateFromParent;
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(currentState));
    }
}
