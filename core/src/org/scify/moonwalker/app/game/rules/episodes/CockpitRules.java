package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.ui.components.ActionButton;

public class CockpitRules extends BaseEpisodeRules {

    protected final float CALCULATOR_WIDTH_PIXELS = 80;

    @Override
    public void gameStartedEvents(GameState gsCurrent) {
        if (!gsCurrent.eventsQueueContainsEvent("EPISODE_STARTED")) {
            gsCurrent.addGameEvent(new GameEvent("EPISODE_STARTED"));

            Renderable spaceImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "space");
            spaceImage.setImgPath("img/space1.png");
            gsCurrent.addRenderable(spaceImage);
            addRenderableEntry("space", spaceImage);

            Renderable cockpitImage = new Renderable(0,0, gameInfo.getScreenWidth(), gameInfo.getScreenHeight(), "image", "cockpit");
            cockpitImage.setImgPath("img/cockpit.png");
            cockpitImage.setZIndex(2);
            gsCurrent.addRenderable(cockpitImage);
            addRenderableEntry("cockpit", cockpitImage);

            final float calculatorSizeUnits = gameInfo.pixelsWithDensity(CALCULATOR_WIDTH_PIXELS);
            final float calculatorXPos = gameInfo.getScreenWidth() - calculatorSizeUnits * 2;
            final float calculatorYPos = calculatorSizeUnits / 3;

            ActionButton calculator = new ActionButton(calculatorXPos, calculatorYPos, calculatorSizeUnits, calculatorSizeUnits, "image_button", "calculator");
            calculator.setImgPath("img/calculator.png");
            calculator.setUserAction(new UserAction(UserActionCode.CALCULATOR));
            calculator.setZIndex(3);
            gsCurrent.addRenderable(calculator);
            addRenderableEntry("calculator", calculator);
        }
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndState endStateFromParent = super.determineEndState(gsCurrent);
        if(endStateFromParent != null)
            return endStateFromParent;
        return new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_FAILURE, cleanUpState(gsCurrent));
    }

    @Override
    public void gameResumedEvents(GameState currentState) {

    }
}
