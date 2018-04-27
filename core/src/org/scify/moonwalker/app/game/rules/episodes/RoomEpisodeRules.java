package org.scify.moonwalker.app.game.rules.episodes;


import org.scify.engine.*;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.game.rules.ConversationRules;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Set;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable renderable;
    protected boolean readyToEndEpisode;
    protected boolean outroInitiated;


    public RoomEpisodeRules() {
        super();
        renderable = null;
        readyToEndEpisode = false;
        outroInitiated = false;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        GameEvent room_outro_event = gsCurrent.getGameEventsWithType("ROOM_OUTRO");
        if (room_outro_event != null) {
            gsCurrent.removeGameEventsWithType("ROOM_OUTRO");
            outroInitiated = true;
            renderable.turnOffPhone();
            EffectSequence fadeOutEffects = new EffectSequence();
            fadeOutEffects.addEffect(new FadeEffect(1.0, 0.0, 2000));
            fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    readyToEndEpisode = true;
                }
            }));
            renderable.addEffect(fadeOutEffects);
        } else if (renderable != null && renderable.isChatEnabled() && outroInitiated == false) {
            gsCurrent = handleConversationRules(gsCurrent, userAction);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy)
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "room", true);
            else
                renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "room", false);
            renderable.setVisible(false);
            currentState.addRenderable(renderable);
            super.episodeStartedEvents(currentState);
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.BOY_MUSIC_AUDIO_PATH));
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
            } else {
                currentState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
                currentState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BOY_MUSIC_AUDIO_PATH));

            }
        }
    }

    protected GameState handleConversationRules(GameState gsCurrent, UserAction userAction) {
        if (conversationHasNotStartedAndNotFinished(gsCurrent)) {
            // call base class create method, passing the resource file for this specific conversation
            createConversation(gsCurrent, "conversations/episode_room.json");
        }
        if (isConversationOngoing(gsCurrent)) {
            // ask the conversation rules to alter the current game state accordingly
            gsCurrent = conversationRules.getNextState(gsCurrent, userAction);
        }
        if (isConversationFinished(gsCurrent)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.BOY_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                gsCurrent.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
                gsCurrent.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            gsCurrent.addGameEvent(new GameEvent("ROOM_OUTRO"));
        }
        handleTriggerEventForCurrentConversationLine(gsCurrent);
        return gsCurrent;
    }

    protected void handleTriggerEventForCurrentConversationLine(GameState gameState) {
        Set<String> eventTrigger;
        if(gameState.eventsQueueContainsEvent(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT)) {
            eventTrigger = (Set<String>) gameState.getGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT).parameters;
            if (eventTrigger.contains("ring_phone")) {
                gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
                renderable.togglePhone();
            }
            if (eventTrigger.contains("toggle")) {
                renderable.togglePhone();
            }

            gameState.removeGameEventsWithType(ConversationRules.ON_ENTER_CONVERSATION_ORDER_TRIGGER_EVENT);
        }
        /*if(gameState.eventsQueueContainsEvent("ON_EXIT_CONVERSATION_TRIGGER_EVENT")) {
            eventTrigger = (String) gameState.getGameEventsWithType("ON_EXIT_CONVERSATION_TRIGGER_EVENT").parameters;
            switch (eventTrigger) {
                case "ring_phone":

                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
                    renderable.togglePhone();

                    break;
                case "end":
                    gameState.addGameEvent(new GameEvent("AUDIO_START_UI", renderable.MOBILE_AUDIO_PATH));
                    renderable.togglePhone();

                    break;
                case "toggle":

                    renderable.togglePhone();

                    break;
                default:
                    break;
            }
            gameState.removeGameEventsWithType("ON_ENTER_CONVERSATION_TRIGGER_EVENT");
        }*/
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return readyToEndEpisode;
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED")) {
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
            conversationRules.cleanUpState(gsCurrent);
        }

        return new EpisodeEndState(code, cleanUpState(gsCurrent));
    }

    protected GameState cleanUpState(GameState currentState) {
        currentState.removeAllGameEventsOwnedBy(this);
        currentState.removeGameEventsWithType("CONVERSATION_READY_TO_FINISH");
        currentState.removeGameEventsWithType("CONVERSATION_FINISHED");
        currentState.removeGameEventsWithType("CONVERSATION_STARTED");
        /*Iterator iter = currentState.getEventQueue().iterator();
        System.out.println("GameEvents:");
        while (iter.hasNext()) {
            System.out.println("\t" + ((GameEvent)iter.next()).type);
        }*/
        return currentState;
    }
}
