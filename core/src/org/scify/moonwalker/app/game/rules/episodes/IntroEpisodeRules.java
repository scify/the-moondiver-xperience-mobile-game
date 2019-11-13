
package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.*;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.renderables.FadingTableRenderable;
import org.scify.moonwalker.app.ui.renderables.IntroRenderable;

import java.util.ArrayList;

public class IntroEpisodeRules extends BaseEpisodeRules {
    protected static final String RENDERABLE_ID = "intro";
    protected int introStep;
    protected IntroRenderable renderable;

    public IntroEpisodeRules() {
        super();
        renderable = null;
        introStep = 0;
    }

    @Override
    public synchronized void episodeStartedEvents(final GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            if (gameInfo.getSelectedPlayer().equals(SelectedPlayer.boy)) {
                renderable = new IntroRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, true);
            } else {
                renderable = new IntroRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), RENDERABLE_ID, false);
            }
            // Save game
            gameInfo.setMainEpisodeCounter(1);
            gameInfo.save();

            reveal(renderable.getBgImg(), new Runnable() {
                @Override
                public void run() {
                    introStep = 1;
                    reveal(renderable.getLeftImage(), null);
                    reveal(renderable.getArrowButton(), new Runnable() {
                        @Override
                        public void run() {
                            renderable.setInputEnabled(true);
                        }
                    });

                }
            });

            // Add all other renderable
            currentState.addRenderables(new ArrayList<>(renderable.getAllRenderables()));
            currentState.addRenderable(renderable);

            super.episodeStartedEvents(currentState);
        }
    }

    protected synchronized void endEpisode(final GameState gameState, final String sEpisodeEndEventType) {
        gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_STOP_UI, renderable.MAINMENU_AUDIO_PATH));

        for (Renderable r : renderable.getAllRenderables()) {
            if (r != renderable.getBgImg()) {
                fadeOut(r, null);
            } else {
                fadeOut(r, new Runnable() {
                    @Override
                    public void run() {
                        endEpisodeAndAddEventWithType(gameState, sEpisodeEndEventType);
                    }
                });
            }
        }


    }

    @Override
    protected synchronized void handleUserAction(GameState gameState, UserAction userAction) {
        // Ignore early events
        if (!isEpisodeStarted(gameState)) {
            return;
        }

        switch (userAction.getActionCode()) {
            case UserActionCode.BUTTON_PRESSED:
            case UserActionCode.SCREEN_TOUCHED:
                if (renderable.isReadyForInput()) {
                    gameState.addGameEvent(new GameEvent(GAME_EVENT_AUDIO_START_UI, FadingTableRenderable.CLICK_AUDIO_PATH));
                    if (introStep == 1) {
                        introStep = 2;
                        reveal(renderable.getRightImage(), new Runnable() {
                            @Override
                            public void run() {
                                introStep = 3;
                            }
                        });
                    } else if (introStep == 3) {
                        introStep++;
                        endEpisode(gameState, "");
                    }
                    break;
                }
        }
        super.handleUserAction(gameState, userAction);
    }

    @Override
    public synchronized EpisodeEndState determineEndState(GameState currentState) {
        EpisodeEndState endState = new EpisodeEndState(EpisodeEndStateCode.EPISODE_FINISHED_SUCCESS, currentState);
        cleanUpGameState(currentState);
        return endState;
    }


    public EffectSequence getFadeOutEffect() {
        EffectSequence fadeOutSeq = new EffectSequence();
        fadeOutSeq.addEffect(new FadeEffect(1.0, 0.0, 1000));
        fadeOutSeq.addEffect(new VisibilityEffect(false));
        return fadeOutSeq;
    }

    public EffectSequence getFadeInEffect() {
        EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1,0, 0));
        ret.addEffect(new VisibilityEffect(true));
        ret.addEffect(new FadeEffect(0,1, 1000));
        return ret;
    }

    public synchronized void reveal(Renderable renderable, Runnable rAfter) {
        EffectSequence es = new EffectSequence();
        es.addEffect(getFadeInEffect());
        if (rAfter != null) {
            es.addEffect(new FunctionEffect(rAfter));
        }
        renderable.addEffect(es);
    }

    public synchronized void fadeOut(Renderable renderable, Runnable rAfter) {
        EffectSequence es = new EffectSequence();
        es.addEffect(getFadeOutEffect());
        if (rAfter != null) {
            es.addEffect(new FunctionEffect(rAfter));
        }

        renderable.addEffect(es);
    }
}

