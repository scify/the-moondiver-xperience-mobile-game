package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.renderables.ContactScreenRenderable;

public class ContactScreenEpisodeRules extends TemporaryEpisodeRules {

    protected ContactScreenRenderable renderable;

    protected boolean readyToEndEpisode;

    public ContactScreenEpisodeRules() {
        super();
        readyToEndEpisode = false;
    }

    @Override
    public void episodeStartedEvents(GameState gsCurrent) {
        if (!isEpisodeStarted(gsCurrent)) {
            super.episodeStartedEvents(gsCurrent);
            initialize(gsCurrent);
        }
    }

    protected void initialize(GameState currentState) {
        renderable = new ContactScreenRenderable(0, 0, appInfo.getScreenWidth(),
                appInfo.getScreenHeight(), "contact_screen", "contact_screen");
        renderable.setImgPath("img/contact_screen/bg.png");
        currentState.addRenderable(renderable);
    }
}
