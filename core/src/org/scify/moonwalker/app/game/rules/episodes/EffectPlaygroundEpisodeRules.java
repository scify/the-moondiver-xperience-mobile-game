package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.effects.EffectList;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 20000;
    ActionButton aTest;
    ActionButton aTest2;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, "img/shady-forest.jpg", MILLISECONDS_FOR_EPISODE);
        }

        if (aTest == null) {
//            aTest = new ActionButton("rotatable_text_button", "ID_OK");
//            aTest.setxPos(250);
//            aTest.setyPos(250);
//            aTest.setTitle("Effects now active! :)");
//            aTest.apply(new FadeLGDXEffect(1.0, 0.3, 5000.0)).apply(new
//                    RotateLGDXEffect(0.0, 360.0, 8000.0));
//
//            currentState.addRenderable(aTest);

            aTest = new ActionButton("rotatable_text_button", "ID_ANOTHER");
            aTest.setxPos(100);
            aTest.setyPos(100);
            aTest.setTitle("And list here!");

            EffectList elList = new LGDXEffectList();
            elList.addEffect(new FadeLGDXEffect(1.0, 0.5, 2000.0));
            elList.addEffect(new FadeLGDXEffect(0.5, 1.0, 2000.0));
            aTest.apply(elList);

            currentState.addRenderable(aTest);
        }

        super.episodeStartedEvents(currentState);
    }

}
