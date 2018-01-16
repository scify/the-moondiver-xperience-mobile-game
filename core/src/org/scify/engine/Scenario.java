package org.scify.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Scenario {

    protected Episode currentEpisode;
    protected Map<Episode, List<Episode>> episodeListMap;


    public Scenario() {
        episodeListMap = new HashMap<>();
    }

    public void start() {
        if(currentEpisode == null) {
            System.out.println("Scenario ended");
            return;
        }
        System.out.println("Ready to start episode: " + currentEpisode.getName());
        final EpisodeEndState endState = (EpisodeEndState) currentEpisode.play();
        System.err.println(endState);
        currentEpisode = getNextEpisode(currentEpisode, endState);
        start();
    }

    protected void addEpisodeAfter(Episode episodeBefore, Episode newEpisode) {
        List<Episode> episodesAfter = episodeListMap.get(episodeBefore);
        episodesAfter.add(newEpisode);
    }

    protected void setFirstEpisode(Episode firstEpisode) {
        episodeListMap = new HashMap<>();
        episodeListMap.put(firstEpisode, new ArrayList<Episode>());
        currentEpisode = firstEpisode;
    }

    protected Episode getNextEpisode(Episode episode, EpisodeEndState state) {
        // get possible next episodes for
        // current episode, given the last episode end state state
        List<Episode> possibleNextEpisodes = episodeListMap.get(episode);
        if(possibleNextEpisodes == null)
            return null;
        for(Episode candidateEpisode : possibleNextEpisodes) {
            if(candidateEpisode.isAccessible(state))
                return candidateEpisode;
        }
        // check if the possible episode is accessible
        return null;
    }
}
