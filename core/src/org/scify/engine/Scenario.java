package org.scify.engine;

import java.util.*;

/**
 * The Scenario objects provide an episode-based view of an interactive story. It manages the order of appearance
 * of episodes, that make a story.
 *
 *
 * This class describes a full game with episodes ({@link Episode} instances), that are created and initialized by children classes
 * (that is why it is abstract). Each episode that is added, may also contain a list of episodes that
 * are possible to be played after this episode ends.
 * The Scenario class plays the current episode, and when it ends, it queries the list for the episode,
 * to get the list of possible next episodes. Upon finding it, it queries each episode sequentially
 * until it gets the next episode.
 * If no episode can be played after the last played episode, or if the last played episode does not have
 * possible next episodes, the scenario is considered as finished.
 * Important: When a new episode is added after another (parent) episode, the episode list map is affected in both ways:
 * 1. The episode is added to the list of candidate episodes, after the parent episode
 * 2. The episode is added as a top level node to the list, along with a new, empty list of candidate episodes to be followed.
 * Example:
 * Episode 1 is added to the list
 * List: [1=>[]]
 * Episode 2 is added as a candidate after episode 1, which is now a parent episode:
 * List: [1=>[2], 2=>[]]
 * Episode 3 is added as a candidate after episode 2
 * List: [1=>[2], 2=>[3], 3=> []]
 */
public abstract class Scenario {

    /***
     * The rendering engine which plays/renders the episodes.
     */
    protected RenderingEngine renderingEngine;
    /***
     * The handler which transforms the user input into actions.
     */
    protected UserInputHandler userInputHandler;

    /**
     * The mapping of episodes with other episodes, which may follow them.
     */
    private Map<Episode, List<Episode>> episodeListMap;
    /**
     * The currently playing episode.
     */
    protected Episode currentEpisode;
    /**
     * The episode that played before the currently playing one (can be null).
     */
    protected Episode lastEpisode;

    /**
     * Sets the first episode that begins the story.
     * @param firstEpisode the given episode
     */
    protected void setFirstEpisode(Episode firstEpisode) {
        initEpisodesList();
        initListForEpisode(firstEpisode);
        currentEpisode = firstEpisode;
    }

    /**
     * Initializes and clears the episode order structure.
     */
    protected void initEpisodesList() {
        episodeListMap = new HashMap<>();
    }

    /**
     * This methods starts playing the scenario, using a given rendering engine and
     * a UserInputHandler which supports interaction. This method is expected to be called once,
     * at the beginning of the scenario.
     * @param renderingEngine The engine that renders the current episode.
     * @param userInputHandler The handler that listens for and handles user events.
     */
    public void start(RenderingEngine renderingEngine, UserInputHandler userInputHandler) {
        this.renderingEngine = renderingEngine;
        this.userInputHandler = userInputHandler;
        playCurrentEpisode();
    }

    /**
     * Executes the actual loop of playing episodes, until there is no candidate episode left.
     */
    protected void playCurrentEpisode() {
        if(currentEpisode == null) {
            System.out.println("Scenario ended");
            return;
        }
        System.out.println("Ready to start episode: " + currentEpisode.getName());
        // The endState is a variable containing a code describing the way
        // the episode was terminated, as well as the current game state
        final EpisodeEndState endState = (EpisodeEndState) currentEpisode.play(renderingEngine, userInputHandler);
        // get the next episode from the list of candidate episodes and set it
        // as the current episode
        setCurrentEpisode(getNextEpisode(endState));
        playCurrentEpisode();
    }

    /**
     * Adds an episode as a candidate episode, after the current one.
     * @param episode The episode to be added.
     */
    protected void addEpisodeAfterCurrent(Episode episode) {
        addEpisodeAfter(currentEpisode, episode);
    }

    /**
     * Ascertains that we have a candidate next episode list for a given episode.
     * @param episode the given episode.
     */
    protected void initListForEpisode(Episode episode) {
        // If the before-episode does not exist
        if (!episodeListMap.containsKey(episode))
            // add it to the episode set
            episodeListMap.put(episode, new LinkedList<Episode>());
    }

    /**
     * Adds the episode as a candidate one, after the episodeBefore episode.
     * @param episodeBefore the episode that should precede the new one.
     * @param newEpisode the new candidate episode.
     */
    protected void addEpisodeAfter(Episode episodeBefore, Episode newEpisode) {
        initListForEpisode(episodeBefore);
        // In any case, update the list with the new episode
        episodeListMap.get(episodeBefore).add(newEpisode);
    }

    /**
     * Creates a clone of a given {@link Episode} and adds it as a candidate after another episode.
     * This is useful when one wants to add an intermediate episode between two identical ones.
     * @param episodeBefore The episode the precedes the clone.
     * @param episodeToClone The episode we want to clone.
     * @throws CloneNotSupportedException
     */
    protected void addAfterXEpisodeLikeY(Episode episodeBefore, Episode episodeToClone) throws CloneNotSupportedException {
        // make sure that the episodeBefore has a list of candidate episodes
        initListForEpisode(episodeBefore);
        // Clone the episode
        Episode newEpisode = (Episode) episodeToClone.clone();
        // initializes a list of candidate episodes after the cloned episode
        initListForEpisode(newEpisode);
        // Make sure that the cloned episode has the same "next episode" list as the original
        // by getting all the candidate episodes that the original episode had
        // and set them as candidate episodes for the cloned episode.
        episodeListMap.get(newEpisode).addAll(episodeListMap.get(episodeToClone));
        // Actually add the episode after the requested one
        addEpisodeAfter(episodeBefore, newEpisode);
    }

    /**
     * Should contain the logic for the sequence of episodes.
     * The default method implementation queries sequentially all the candidate episodes
     * after the currentEpisode, until it finds an episode that is accessible (can be played)
     * @param state the state that the currently playing episode finished with
     * @return a {@link Episode} instance, if there is an accessible episode. Otherwise, null (which implies
     * scenario end or dead-end).
     */
    protected Episode getNextEpisode(EpisodeEndState state) {
        // get possible next episodes for
        // current episode, given the last episode end state state
        List<Episode> possibleNextEpisodes = episodeListMap.get(currentEpisode);
        if(possibleNextEpisodes == null)
            return null;
        for(Episode candidateEpisode : possibleNextEpisodes) {
            if(candidateEpisode.isAccessible(state)) {
                candidateEpisode.setInitialEpisodeState(state);
                return candidateEpisode;
            }
        }
        // check if the possible episode is accessible
        return null;
    }

    /**
     * Sets a new episode as the current one. Also sets the current one
     * as the last played one.
     * @param newCurrentEpisode The new episode.
     */
    protected void setCurrentEpisode(Episode newCurrentEpisode) {
        lastEpisode = this.currentEpisode;
        this.currentEpisode = newCurrentEpisode;
    }

    /**
     * Removes the last playing episode from the list of episodes (with appropriate cleanup).
     */
    protected void removeLastEpisodeAndCandidateEpisodes() {
        // get last episode's first possible next episode
        Episode firstPossibleEpisodeAfterLastEpisode = episodeListMap.get(lastEpisode).get(0);
        // remove this episode from episode set
        episodeListMap.remove(firstPossibleEpisodeAfterLastEpisode);
        // remove all possible episodes from first candidate position of the previous episode
        episodeListMap.get(lastEpisode).remove(0);
    }

    /**
     * Inserts a given {@link Episode} as the first candidate episode, after the currently playing episode.
     * This method can help impose an episode as the next one.
     * @param episode the episode that will be inserted.
     */
    protected void addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(Episode episode) {
        initListForEpisode(currentEpisode);
        episodeListMap.get(currentEpisode).add(0, episode);
    }
}
