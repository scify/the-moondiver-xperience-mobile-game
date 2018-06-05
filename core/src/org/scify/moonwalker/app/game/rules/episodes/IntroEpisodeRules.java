package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.IntroRenderable;

import java.util.ArrayList;

public class IntroEpisodeRules extends FadingEpisodeRules<IntroRenderable> {
    protected static final String RENDERABLE_ID = "intro";
    protected int introStep;

    public IntroEpisodeRules() {
        super();
        renderable = null;
        introStep = 0;
    }

    @Override
    public void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.getSelectedPlayer() == SelectedPlayer.boy) {
                renderable = new IntroRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, true);
                //currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.BOY_MUSIC_AUDIO_PATH));
            } else {
                renderable = new IntroRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, false);
                //currentState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_LOAD_UI, renderable.GIRL_MUSIC_AUDIO_PATH));
            }
            renderable.addAfterFadeIn(new Runnable() {
                @Override
                public void run() {
                    renderable.reveal(renderable.getLeftImage());
                    renderable.reveal(renderable.getArrowButton());
                    introStep = 1;
                }
            });
            currentState.addRenderable(renderable);
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            super.episodeStartedEvents(currentState);
        }
    }

    protected void endEpisode(final GameState gameState, String sEpisodeEndEventType) {
        renderable.addBeforeFadeOut(new Runnable() {
            @Override
            public void run() {
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.MAINMENU_AUDIO_PATH));
            }
        });
        endEpisodeAndAddEventWithType(gameState, sEpisodeEndEventType);

    }

    @Override
    protected void handleUserAction(GameState gameState, UserAction userAction) {
        switch (userAction.getActionCode()) {
            case UserActionCode.BUTTON_PRESSED:
                gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, renderable.CLICK_AUDIO_PATH));
                if (introStep == 1) {
                    renderable.reveal(renderable.getRightImage());
                    introStep = 2;
                }else if (introStep == 2){
                    introStep ++;
                    endEpisode(gameState, "");
                }
                break;
        }
        super.handleUserAction(gameState, userAction);
    }

    @Override
    public EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }
}
