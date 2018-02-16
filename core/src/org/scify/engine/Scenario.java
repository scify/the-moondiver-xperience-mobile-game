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

    /**
     * Ascertains that we have a candidate next episode list for a given episode.
     * @param episode
     */
    protected void initListForEpisode(Episode episode) {
        // If the before-episode does not exist
        if (!episodeListMap.containsKey(episode))
            // add it to the episode set
            episodeListMap.put(episode, new LinkedList<Episode>());

    }
    protected void addEpisodeAfter(Episode episodeBefore, Episode newEpisode) {
        initListForEpisode(episodeBefore);

        // In any case, update the list with the new episode
        episodeListMap.get(episodeBefore).add(newEpisode);
    }

    protected void addAfterXEpisodeLikeY(Episode episodeBefore, Episode episodeToClone) throws CloneNotSupportedException {
        initListForEpisode(episodeBefore);
        // Clone the episode
        Episode newEpisode = (Episode) episodeToClone.clone();
        // Make sure that the cloned episode has the same "next episode" list as the original
        initListForEpisode(newEpisode);
        episodeListMap.get(newEpisode).addAll(episodeListMap.get(episodeToClone));
        // Actually add the episode after the requested one
        addEpisodeAfter(episodeBefore, newEpisode);
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

    protected void removeLastEpisodeAndCandidateEpisodes() {
        // get last episode's first possible next episode
        Episode firstPossibleEpisodeAfterLastEpisode = episodeListMap.get(lastEpisode).get(0);
        // remove this episode from episode set
        episodeListMap.remove(firstPossibleEpisodeAfterLastEpisode);
        // remove all possible episodes from first candidate position of the previous episode
        episodeListMap.get(lastEpisode).remove(0);
    }

    protected void addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(Episode episode) {
        episodeListMap.get(currentEpisode).add(0, episode);
    }
}
