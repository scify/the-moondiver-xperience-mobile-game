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
        episodeListMap = new LinkedHashMap<>();
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
        //printEpisodeList();
        if(currentEpisode == null) {
            System.out.println("Scenario ended");
            return;
        }
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
    @Deprecated
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
        lastEpisode = currentEpisode;
        currentEpisode = newCurrentEpisode;
        System.out.println("last episode is: " + lastEpisode.getClass().getName());
        System.out.println("current episode is: " + currentEpisode.getClass().getName());
    }

    /**
     * Removes the last playing episode from the list of episodes (with appropriate cleanup).
     */
    protected void removeLastEpisode() {
        System.out.println("removing last episode: " + lastEpisode.getClass().getName());
        // remove episode from scenario
        episodeListMap.remove(lastEpisode);
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

    private void printEpisodeList() {
        System.out.println("\n\nEpisode list:");
        for(Map.Entry<Episode, List<Episode>> episodeListSimpleEntry: episodeListMap.entrySet()) {
            System.out.println(episodeListSimpleEntry.getKey().toString());
            for(Episode candidateEpisode: episodeListSimpleEntry.getValue()) {
                System.out.println("\t" + candidateEpisode.toString());
            }
        }
        System.out.println("\n\n");
    }

    protected void addTemporaryEpisode(Episode temp) {
        try {
            // Create duplicate of current episode, keeping all the possible next ones.
            Episode clone = cloneCurrentEpisodeWithCandidateLinks();
            // Make the temporary episode point to the duplicate as its successor.
            initListForEpisode(temp);
            episodeListMap.get(temp).add(clone);
            // Assign the temporary episode as the first/only possible next episode to the current one
            addEpisodeAsFirstCandidateEpisodeAfterCurrentEpisode(temp);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clones an episode, using all the candidate next episodes for the clone as well.
     * @return The cloned episode.
     * @throws CloneNotSupportedException
     */
    protected Episode cloneCurrentEpisodeWithCandidateLinks() throws CloneNotSupportedException {
        // make sure that the episodeBefore has a list of candidate episodes
        initListForEpisode(currentEpisode);
        // Clone the episode
        Episode newEpisode = (Episode) currentEpisode.clone();
        // initializes a list of candidate episodes after the cloned episode
        initListForEpisode(newEpisode);
        // Make sure that the cloned episode has the same "next episode" list as the original
        // by getting all the candidate episodes that the original episode had
        // and set them as candidate episodes for the cloned episode.
        episodeListMap.get(newEpisode).addAll(episodeListMap.get(currentEpisode));

        return newEpisode;
    }
}
