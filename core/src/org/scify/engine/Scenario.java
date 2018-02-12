package org.scify.engine;

import java.util.*;

public abstract class Scenario {

    protected Episode currentEpisode;
    protected Map<Episode, List<Episode>> episodeListMap;
    protected Episode lastEpisode;

    public Scenario() {
        episodeListMap = new HashMap<>();
    }

    public void start() {
        playEpisode(currentEpisode);
    }

    protected void playEpisode(Episode episode) {
        if(episode == null) {
            System.out.println("Scenario ended");
            return;
        }
        System.out.println("Ready to start episode: " + episode.getName());
        final EpisodeEndState endState = (EpisodeEndState) episode.play();
        System.err.println(endState);
        setCurrentEpisode(getNextEpisode(endState));
        playEpisode(currentEpisode);
    }

    protected void appendEpisode(Episode episode) {
        addEpisodeAfter(currentEpisode, episode);
    }

    protected void addEpisodeAfter(Episode episodeBefore, Episode newEpisode) {
        // If the before-episode does not exist
        if (!episodeListMap.containsKey(episodeBefore))
            // add it to the episode set
            episodeListMap.put(episodeBefore, new LinkedList<Episode>());
        else
            episodeListMap.get(episodeBefore).add(newEpisode);
    }

    protected void setFirstEpisode(Episode firstEpisode) {
        episodeListMap = new HashMap<>();
        episodeListMap.put(firstEpisode, new ArrayList<Episode>());
        currentEpisode = firstEpisode;
    }

    protected Episode getNextEpisode(EpisodeEndState state) {
        // get possible next episodes for
        // current episode, given the last episode end state state
        List<Episode> possibleNextEpisodes = episodeListMap.get(currentEpisode);
        if(possibleNextEpisodes == null)
            return null;
        for(Episode candidateEpisode : possibleNextEpisodes) {
            if(candidateEpisode.isAccessible(state)) {
                return candidateEpisode;
            }
        }
        // check if the possible episode is accessible
        return null;
    }

    public void setCurrentEpisode(Episode newCurrentEpisode) {
        lastEpisode = this.currentEpisode;
        this.currentEpisode = newCurrentEpisode;
    }
}
