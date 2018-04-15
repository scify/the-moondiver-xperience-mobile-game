package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.EffectList;
import org.scify.engine.renderables.effects.libgdx.BounceLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.engine.renderables.effects.libgdx.RotateLGDXEffect;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.actors.TextLabel;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 20000;
    Renderable aTest;

    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, "img/shady-forest.jpg", MILLISECONDS_FOR_EPISODE);
        }

        if (aTest == null) {
            aTest = new ActionButton("rotatable_text_button", "ID_OK");
            aTest.setxPos(250);
            aTest.setyPos(250);
            aTest.apply(new FadeLGDXEffect(1.0, 0.3, 5000.0)).apply(new
                    RotateLGDXEffect(0.0, 360.0, 8000.0));
            ((ActionButton)aTest).setTitle("Parallel effects...");
            currentState.addRenderable(aTest);

            aTest = new TextLabel("rotatable_label", "ID_ANOTHER");
            aTest.setxPos(500);
            aTest.setyPos(100);
            ((TextLabel)aTest).setLabel("And consequtive here!");

            EffectList elList = new LGDXEffectList();
            elList.addEffect(new FadeLGDXEffect(1.0, 0.3, 2000.0));
            elList.addEffect(new RotateLGDXEffect(0.0, 720.0, 2000.0));
            elList.addEffect(new FadeLGDXEffect(0.3, 1.0, 2000.0));
            elList.addEffect(new RotateLGDXEffect(100.0, 0.0, 1000.0));
            elList.addEffect((new BounceLGDXEffect(0.0, 100.0, 3000.0)));
            aTest.apply(elList);


            currentState.addRenderable(aTest);
        }

        super.episodeStartedEvents(currentState);
    }

}
