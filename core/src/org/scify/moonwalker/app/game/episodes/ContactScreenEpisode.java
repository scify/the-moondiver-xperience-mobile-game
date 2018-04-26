package org.scify.moonwalker.app.game.episodes;

import org.scify.engine.EpisodeWithEndState;
import org.scify.moonwalker.app.game.rules.episodes.ContactScreenEpisodeRules;

public class ContactScreenEpisode extends EpisodeWithEndState {
    public ContactScreenEpisode() {
        super(new ContactScreenEpisodeRules());
    }
}
