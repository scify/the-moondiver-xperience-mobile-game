package org.scify.moonwalker.app.game.rules.episodes;

import com.badlogic.gdx.math.Vector2;
import org.scify.engine.GameState;
import org.scify.engine.UserAction;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.engine.renderables.effects.libgdx.PointRouteFadeEffectLGDX;

import java.util.ArrayList;
import java.util.List;

public class EffectPlaygroundEpisodeRules extends SimpleTimedImageEpisodeRules {
    protected final int MILLISECONDS_FOR_EPISODE = 20000;
    Renderable aTest, aTest2, aTest3;


    @Override
    public void episodeStartedEvents(GameState currentState) {
        if (!isEpisodeStarted(currentState)) {
            setFieldsForTimedEpisode(initialGameState, null, MILLISECONDS_FOR_EPISODE);
//"img/shady-forest.jpg"
//            addEpisodeBackgroundImage(currentState,"img/charge.png");
        }

        if (aTest == null) {
            getNormalParallelRenderable(currentState);

            getConsequtiveRenderable(currentState);
////
            getConsequtiveParallelsRenderable(currentState);

//
            getRouteWithManyPointsRenderablesAndEffect(currentState);
            getPointRouteWithSingleItemTypeRenderable(currentState);


//            RotateEffect rEffect = new RotateEffect(0.0, 360.0, 6000);
//            rEffect.setOriginPoint(15.0, 15.0);
//            rPl.addEffect(rEffect);

////
//            Renderable rCoin = new ImageRenderable(50,50,100,100,"player", "img/coin.png");
//            rCoin.setVisible(false);
////            ActionButtonRenderable rCoin = new ActionButtonRenderable(50, 50, 100, 100, Renderable.ACTOR_IMAGE, "coin!");
//            rCoin.setImgPath("img/coin.png");
////            rCoin.setUserAction(new UserAction("coin"));
//            rCoin.addEffect(elConPar);
//            currentState.addRenderable(rCoin);

        }

        super.episodeStartedEvents(currentState);
    }

    private void getPointRouteWithSingleItemTypeRenderable(GameState currentState) {
        // Point route with single item type alternative
        List<Vector2> lvPoints = new ArrayList<>();
        ImageRenderable rPl = new ImageRenderable(10,10,50,50,"pointType", "img/close.png");
        rPl.setVisible(false);
        // Create points
        for (int iCnt=0; iCnt < 5; iCnt++) {
            Vector2 vCur = new Vector2();
            vCur.set(50 + 50 * iCnt, (float)(50.0 + 20 * Math.sin(2.0 * Math.PI * (float)iCnt / 100.0)));
            lvPoints.add(vCur);
        }
        PointRouteSinglePointTypeEffect pRoute = new PointRouteSinglePointTypeEffect(lvPoints, 3.0, 2.0, 5000);
        rPl.addEffect(pRoute);

        currentState.addRenderable(rPl);
    }

    private void getRouteWithManyPointsRenderablesAndEffect(GameState currentState) {
        // Point route with many points alternative
        List<Renderable> lPoints = new ArrayList<>();
        for (int iCnt=0; iCnt < 10; iCnt++) {
            ImageRenderable rBasePoint = new ImageRenderable(10,10,50,50,"point" + String.valueOf(iCnt), "img/close.png");
            rBasePoint.setxPos(50 + 50 * iCnt);
            rBasePoint.setyPos((float)(50.0 + 100.0 * Math.sin(2.0 * Math.PI * iCnt / 8.0)));
            rBasePoint.setVisible(false);

            lPoints.add(rBasePoint);
            currentState.addRenderable(rBasePoint);
        }

        PointRouteFadeEffectLGDX pRoute = new PointRouteFadeEffectLGDX(2000, lPoints);
    }

    private void getConsequtiveRenderable(GameState currentState) {
        aTest2 = new TextLabelRenderable("rotatable_label", "ID_ANOTHER");
        aTest2.setxPos(500);
        aTest2.setyPos(100);
        ((TextLabelRenderable)aTest2).setLabel("Consequtive");

        EffectSequence elList = new EffectSequence();
        elList.addEffect(new FadeEffect(1.0, 0.3, 1000.0));
        RotateEffect rTmp = new RotateEffect(0.0, 720.0, 1000.0);
        rTmp.setOriginPoint(0, 0);
        elList.addEffect(rTmp);
        elList.addEffect(new VisibilityEffect(false));
        elList.addEffect(new DelayEffect(2000));
        elList.addEffect(new VisibilityEffect(true));
        elList.addEffect(new FadeEffect(0.3, 1.0, 2000.0));
        rTmp = new RotateEffect(100.0, 0.0, 1000.0);
        rTmp.setOriginPoint(aTest2.getWidth() / 2.0, aTest2.getHeight() / 2.0);
        elList.addEffect(rTmp);
        elList.addEffect((new BounceEffect(0.0, 100.0, 3000.0)));
        elList.addEffect(new FunctionEffect(new Runnable() {
                    @Override
                    public void run() {
                        System.err.println("Hello!");
                    }
                }));
        aTest2.addEffect(elList);
        currentState.addRenderable(aTest2);
    }

    private void getNormalParallelRenderable(GameState currentState) {
        aTest = new ActionButtonRenderable("rotatable_text_button", "ID_OK");
        ((ActionButtonRenderable) aTest).setUserAction(new UserAction("test"));
        aTest.setxPos(250);
        aTest.setyPos(250);
        aTest.setWidth(250);
        aTest.setHeight(100);
        aTest.addEffect(new FadeEffect(1.0, 0.3, 5000.0)).addEffect(new
                RotateEffect(0.0, 360.0, 8000.0));
//            aTest.apply(new SlideEffect(aTest.getxPos(), aTest.getyPos(), 270.0, 1000));
        ((ActionButtonRenderable)aTest).setTitle("Normal (parallel)");
        currentState.addRenderable(aTest);
    }

    private void getConsequtiveParallelsRenderable(GameState currentState) {
        aTest3 = new TextLabelRenderable("rotatable_label", "ID_3RD");
        aTest3.setxPos(200);
        aTest3.setyPos(50);
        ((TextLabelRenderable)aTest3).setLabel("Consequtive Parallels");
        EffectSequence elConPar = new EffectSequence();

        ParallelEffectList pelList = new ParallelEffectList();
        pelList.addEffect(new VisibilityEffect(true));
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
//
        aTest3.addEffect(elConPar);
        currentState.addRenderable(aTest3);
    }

    @Override
    protected void handleUserAction(GameState gsCurrent, UserAction userAction) {
        if (userAction.getActionCode().equals("test")) {
            System.err.println("Test pressed!");
        }
        if (userAction.getActionCode().equals("coin")) {
            System.err.println("Coin pressed!");
        }
        super.handleUserAction(gsCurrent, userAction);
    }
}
