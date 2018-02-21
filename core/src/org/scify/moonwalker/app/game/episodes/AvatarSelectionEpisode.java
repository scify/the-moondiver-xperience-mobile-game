package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.engine.RenderingEngine;
import org.scify.engine.UserInputHandler;
import org.scify.moonwalker.app.game.rules.episodes.AvatarSelectionRules;

public class AvatarSelectionEpisode extends EpisodeWithEndState {
    public AvatarSelectionEpisode() {
        super(new AvatarSelectionRules());
    }


}
