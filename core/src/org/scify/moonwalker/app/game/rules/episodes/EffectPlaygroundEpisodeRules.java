package org.scify.moonwalker.app.game.rules.episodes;

import org.scify.engine.GameState;
import org.scify.engine.renderables.ActionButtonWithEffect;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabel;
import org.scify.engine.renderables.effects.*;

import java.util.ArrayList;
import java.util.List;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 200000;
    Renderable aTest, aTest2, aTest3;


    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, null, MILLISECONDS_FOR_EPISODE);
//"img/shady-forest.jpg"
//            addEpisodeBackgroundImage(currentState,"img/charge.png");
        }

        if (aTest == null) {
            aTest = new ActionButtonWithEffect("rotatable_text_button", "ID_OK");
//            aTest.setxPos(250);
//            aTest.setyPos(250);
//            aTest.setWidth(250);
//            aTest.setHeight(100);
//            aTest.addEffect(new FadeEffect(1.0, 0.3, 5000.0)).addEffect(new
//                    RotateEffect(0.0, 360.0, 8000.0));
////            aTest.apply(new SlideEffect(aTest.getxPos(), aTest.getyPos(), 270.0, 1000));
//            ((ActionButtonWithEffect)aTest).setTitle("Normal (parallel)");
//            currentState.addRenderable(aTest);
//
//            aTest2 = new TextLabel("rotatable_label", "ID_ANOTHER");
//            aTest2.setxPos(500);
//            aTest2.setyPos(100);
//            ((TextLabel)aTest2).setLabel("Consequtive");
//
//            EffectSequence elList = new EffectSequence();
//            elList.addEffect(new FadeEffect(1.0, 0.3, 1000.0));
//            elList.addEffect(new RotateEffect(0.0, 720.0, 1000.0));
//            elList.addEffect(new VisibilityEffect(false));
//            elList.addEffect(new DelayEffect(2000));
//            elList.addEffect(new VisibilityEffect(true));
//            elList.addEffect(new FadeEffect(0.3, 1.0, 2000.0));
//            elList.addEffect(new RotateEffect(100.0, 0.0, 1000.0));
//            elList.addEffect((new BounceEffect(0.0, 100.0, 3000.0)));
//            elList.addEffect(new FunctionEffect(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.err.println("Hello!");
//                        }
//                    }));
//            aTest2.addEffect(elList);
//            currentState.addRenderable(aTest2);
//
            aTest3 = new TextLabel("rotatable_label", "ID_3RD");
            aTest3.setxPos(200);
            aTest3.setyPos(50);
            ((TextLabel)aTest3).setLabel("Consequtive Parallels");
//
//            List<Renderable> lPoints = new ArrayList<>();
//            for (int iCnt=0; iCnt < 10; iCnt++) {
//                ImageRenderable rPl = new ImageRenderable(10,10,50,50,"point" + String.valueOf(iCnt), "img/close.png");
//                rPl.setxPos(50 + 50 * iCnt);
//                rPl.setyPos((float)(50.0 + 100.0 * Math.sin(2.0 * Math.PI * iCnt / 8.0)));
//
//                lPoints.add(rPl);
//                currentState.addRenderable(rPl);
//            }
//
//            PointRouteFadeEffectLGDX pRoute = new PointRouteFadeEffectLGDX(2000, lPoints);
//            pRoute.applyTo(lPoints.get(0));

            EffectSequence elConPar = new EffectSequence();


            ParallelEffectList pelList = new ParallelEffectList();
            pelList.addEffect(new FadeEffect(1.0, 0.5, 2000.0));
            pelList.addEffect(new RotateEffect(0.0, 180.0, 2000.0));
            pelList.addEffect(new BounceEffect(50,0,5000.0));

            Effect eDelay = new DelayEffect(1000);
//
            ParallelEffectList pelList2 = new ParallelEffectList();
            pelList2.addEffect(new FadeEffect(0.5, 1.0, 2000.0));
            pelList2.addEffect(new RotateEffect(180.0, 0.0, 2000.0));
//
            elConPar.addEffect(pelList);
            elConPar.addEffect(eDelay);
            elConPar.addEffect(pelList2);

            aTest3.addEffect(elConPar);
            currentState.addRenderable(aTest3);
//
//            Renderable rPl = new Player(10,10,100,100,"player", "pl");
//            rPl.apply(elConPar);
//            currentState.addRenderable(rPl);

        }

        super.episodeStartedEvents(currentState);
    }

}
