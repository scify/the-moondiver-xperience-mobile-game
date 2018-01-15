package org.scify.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scenario {

    protected Episode currentEpisode;
    protected Map<Episode, List<Episode>> episodeListMap;

    public Scenario(Episode firstEpisode) {
        episodeListMap = new HashMap<>();
        episodeListMap.put(firstEpisode, new ArrayList<Episode>());
        this.currentEpisode = firstEpisode;
    }

    public void addEpisodeAfter(Episode newEpisode, Episode episodeBefore) {
        // add newEpisode as a possible next episode for episodeBefore
        // add newEpisode to episodes
    }

    public Episode getFirstEpisode() {
        return null;
    }

    public Episode getCurrentEpisode() {
        return currentEpisode;
    }

    public Episode getNextEpisode(ScenarioState state) {
        // get possible next episodes for
        // current episode, given the scenario state
        // check if the possible episode is accessible
        return null;
    }
}
