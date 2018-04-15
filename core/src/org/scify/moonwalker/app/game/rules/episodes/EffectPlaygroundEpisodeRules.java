package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectList;
import org.scify.engine.renderables.effects.ParallelEffectList;
import org.scify.engine.renderables.effects.libgdx.*;
import org.scify.moonwalker.app.ui.actors.ActionButton;
import org.scify.moonwalker.app.ui.actors.TextLabel;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 20000;
    Renderable aTest, aTest2, aTest3;


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
            ((ActionButton)aTest).setTitle("Normal (parallel)");
            currentState.addRenderable(aTest);

            aTest2 = new TextLabel("rotatable_label", "ID_ANOTHER");
            aTest2.setxPos(500);
            aTest2.setyPos(100);
            ((TextLabel)aTest2).setLabel("Consequtive");

            EffectList elList = new LGDXEffectList();
            elList.addEffect(new FadeLGDXEffect(1.0, 0.3, 2000.0));
            elList.addEffect(new RotateLGDXEffect(0.0, 720.0, 2000.0));
            elList.addEffect(new FadeLGDXEffect(0.3, 1.0, 2000.0));
            elList.addEffect(new RotateLGDXEffect(100.0, 0.0, 1000.0));
            elList.addEffect((new BounceLGDXEffect(0.0, 100.0, 3000.0)));
            aTest2.apply(elList);
            currentState.addRenderable(aTest2);

            aTest3 = new TextLabel("rotatable_label", "ID_3RD");
            aTest3.setxPos(200);
            aTest3.setyPos(50);

            ((TextLabel)aTest3).setLabel("Consequtive Parallels");
            EffectList elConPar = new LGDXEffectList();


            ParallelEffectList pelList = new LGDXParallelEffectList();
            pelList.addEffect(new FadeLGDXEffect(1.0, 0.5, 2000.0));
            pelList.addEffect(new RotateLGDXEffect(0.0, 180.0, 2000.0));
//            pelList.addEffect(new BounceLGDXEffect(50,50,1000.0));

            Effect eDelay = new DelayLGDXEffect(1000);

            ParallelEffectList pelList2 = new LGDXParallelEffectList();
            pelList2.addEffect(new FadeLGDXEffect(0.5, 1.0, 2000.0));
            pelList2.addEffect(new RotateLGDXEffect(180.0, 0.0, 2000.0));
//            pelList2.addEffect(new BounceLGDXEffect(10,10,1000.0));

            elConPar.addEffect(pelList);
            elConPar.addEffect(eDelay);
            elConPar.addEffect(pelList2);

            aTest3.apply(elConPar);


            currentState.addRenderable(aTest3);
        }

        super.episodeStartedEvents(currentState);
    }

}
