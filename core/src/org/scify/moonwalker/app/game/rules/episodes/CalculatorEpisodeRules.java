package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.Renderable;

/**
 * This is a self-contained episode (meaning that it usually gets invoked
 * by another episode), presenting a simple calculator to the user.
 * The constructor takes a {@link GameState} instance as an argument
 * in order to set the already defined (in another episode) game state
 * to the rules.
 */
public class CalculatorEpisodeRules extends TemporaryEpisodeRules {

    @Override
    public void episodeStartedEvents(GameState gsCurrent) {
        if (!isEpisodeStarted(gsCurrent)) {
            super.episodeStartedEvents(gsCurrent);
            addEpisodeBackgroundImage(gsCurrent, "img/calculator_episode/bg.jpg");
            org.scify.engine.renderables.Renderable calculator = new Renderable("calculator", "calculator_button");
            gsCurrent.addRenderable(calculator);
            ActionButtonRenderable escape = createEscapeButton();
            escape.setUserAction(new UserAction(UserActionCode.BACK));
            gsCurrent.addRenderable(escape);
            addRenderableEntry("calculator_finished_button", escape);
        }
    }
}
