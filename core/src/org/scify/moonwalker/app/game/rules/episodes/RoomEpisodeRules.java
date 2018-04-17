package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.utils.Timer;
import org.scify.engine.*;
import org.scify.engine.conversation.ConversationLine;
import org.scify.engine.renderables.NextConversationRenderable;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.RoomRenderable;

import java.util.Date;
import java.util.Iterator;

public class RoomEpisodeRules extends BaseEpisodeRules {
    protected RoomRenderable renderable;
    protected boolean ringStarted;
    protected boolean nextButtonActivated;
    protected long lastRingDate;
    protected boolean ringStopped;
    protected boolean readyToEndEpisode;
    protected boolean outroInitiated;


    public RoomEpisodeRules() {
        super();
        renderable = null;
        ringStarted = false;
        ringStopped = false;
        nextButtonActivated = false;
        readyToEndEpisode = false;
        outroInitiated = false;
        lastRingDate = 0;
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction userAction) {
        GameEvent room_outro_event = gsCurrent.getGameEventsWithType("ROOM_OUTRO");
        if (room_outro_event != null) {
            gsCurrent.removeGameEventsWithType("ROOM_OUTRO");
            outroInitiated = true;
            renderable.turnOffPhone();
            LGDXEffectList fadeOutEffects = new LGDXEffectList();
            fadeOutEffects.addEffect(new FadeLGDXEffect(1.0, 0.0, 2000));
            fadeOutEffects.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    readyToEndEpisode = true;
                }
            }));
            renderable.apply(fadeOutEffects);
        }else if (renderable != null && renderable.isReadyForPhoneRinging() && outroInitiated == false) {
            gsCurrent = handleConversationRules(gsCurrent, userAction);
        }
        return super.getNextState(gsCurrent, userAction);
    }

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            renderable = new RoomRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "room");
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
            //createConversation(gsCurrent, "conversations/episode_room_test_font.json");
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
        ConversationLine currLine = conversationRules.getCurrentConversationLine(gameState);
        switch (currLine.getTriggerEvent()) {
            case "ring_start":
                if (ringStarted == false) {
                    ringStarted = true;
                    ((NextConversationRenderable) conversationRules.getLastConversationRenderable()).setButtonNextInActive();

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            renderable.togglePhone();
                            if (!nextButtonActivated) {
                                nextButtonActivated = true;
                                ((NextConversationRenderable) conversationRules.getLastConversationRenderable()).setButtonNextActive();
                            }
                        }
                    }, 3, 1);

                    gameState.addGameEvent(new GameEvent("AUDIO_START_LOOP_UI", renderable.MOBILE_AUDIO_PATH, new Date().getTime() + 3000, false));
                }
                break;
            case "ring_stop":
                if (ringStopped == false) {
                    ringStopped = true;
                    Timer.instance().clear();
                    gameState.addGameEvent(new GameEvent("AUDIO_STOP_UI", renderable.MOBILE_AUDIO_PATH));
                    gameState.addGameEvent(new GameEvent("AUDIO_DISPOSE_UI", renderable.MOBILE_AUDIO_PATH));
                    renderable.turnOnPhone();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return readyToEndEpisode;
    }

    @Override
    public EpisodeEndState determineEndState(GameState gsCurrent) {
        EpisodeEndStateCode code = EpisodeEndStateCode.EPISODE_FINISHED_FAILURE;
        if (gsCurrent.eventsQueueContainsEvent("CONVERSATION_FINISHED"))
            code = EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS;
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
