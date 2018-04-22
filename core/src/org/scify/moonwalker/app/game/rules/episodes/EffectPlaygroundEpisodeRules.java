package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.RotateEffect;
import org.scify.engine.renderables.effects.SlideEffect;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 20000;
    Renderable aTest, aTest2, aTest3;


    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, null, MILLISECONDS_FOR_EPISODE);
//"img/shady-forest.jpg"
            addEpisodeBackgroundImage(currentState,"img/charge.png");
        }

        if (aTest == null) {
            aTest = new ActionButton("rotatable_text_button", "ID_OK");
            aTest.setxPos(250);
            aTest.setyPos(250);
            aTest.setWidth(250);
            aTest.setHeight(100);
            aTest.apply(new FadeEffect(1.0, 0.3, 5000.0)).apply(new
                    RotateEffect(0.0, 360.0, 8000.0));
            aTest.apply(new SlideEffect(aTest.getxPos(), aTest.getyPos(), 270.0, 1000));
            ((ActionButton)aTest).setTitle("Normal (parallel)");
            currentState.addRenderable(aTest);

//            aTest2 = new TextLabel("rotatable_label", "ID_ANOTHER");
//            aTest2.setxPos(500);
//            aTest2.setyPos(100);
//            ((TextLabel)aTest2).setLabel("Consequtive");
//
//            EffectSequence elList = new EffectSequence();
//            elList.addEffect(new FadeEffect(1.0, 0.3, 2000.0));
//            elList.addEffect(new RotateEffect(0.0, 720.0, 2000.0));
//            elList.addEffect(new FadeEffect(0.3, 1.0, 2000.0));
//            elList.addEffect(new RotateEffect(100.0, 0.0, 1000.0));
//            elList.addEffect((new BounceEffect(0.0, 100.0, 3000.0)));
//            aTest2.apply(elList);
//            currentState.addRenderable(aTest2);
//
//            aTest3 = new TextLabel("rotatable_label", "ID_3RD");
//            aTest3.setxPos(200);
//            aTest3.setyPos(50);
//
//            ((TextLabel)aTest3).setLabel("Consequtive Parallels");
//            EffectSequence elConPar = new EffectSequence();
//
//
//            ParallelEffectList pelList = new ParallelEffectList();
//            pelList.addEffect(new FadeEffect(1.0, 0.5, 2000.0));
//            pelList.addEffect(new RotateEffect(0.0, 180.0, 2000.0));
//            pelList.addEffect(new BounceEffect(50,0,5000.0));
//
//            Effect eDelay = new DelayEffect(1000);
//
//            ParallelEffectList pelList2 = new ParallelEffectList();
//            pelList2.addEffect(new FadeEffect(0.5, 1.0, 2000.0));
//            pelList2.addEffect(new RotateEffect(180.0, 0.0, 2000.0));
////            pelList2.addEffect(new BounceLGDXEffect(10,10,1000.0));
//
//            elConPar.addEffect(pelList);
//            elConPar.addEffect(eDelay);
//            elConPar.addEffect(pelList2);
//
////            aTest3.apply(elConPar);
////            currentState.addRenderable(aTest3);
//
//            Renderable rPl = new Player(10,10,100,100,"player", "pl");
//            rPl.apply(elConPar);
//            currentState.addRenderable(rPl);

        }

        super.episodeStartedEvents(currentState);
    }

}
