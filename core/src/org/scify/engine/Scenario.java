package org.scify.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scenario {

    protected Episode currentEpisode;
    protected Map<Episode, List<Episode>> episodeListMap;


    public Scenario() {
        episodeListMap = new HashMap<>();
    }

    public void createEpisodes() {}

    public void play() {
        Episode currentEpisode = getCurrentEpisode();
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<EpisodeEndState> future = es.submit(currentEpisode);
        es.shutdown();
        //this code will execute once the user exits the app
        // (either to go to next level or to exit)
        try {
            EpisodeEndState result = future.get();
            System.out.println("Game result: " + result);
        } catch (InterruptedException | ExecutionException e) {

        } finally {
            currentEpisode.disposeResources();
        }
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
