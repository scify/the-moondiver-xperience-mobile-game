package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.LGDXRenderableBookKeeper;
import org.scify.moonwalker.app.ui.actors.ActorWithEffects;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageButtonWithEffect;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.IntroRenderable;

public class IntroActor extends ActorWithEffects {

    protected Image leftBanner;
    protected Button nextButton;
    protected Image rightBanner;
    private final float scale = 1.0f;
    protected LGDXRenderableBookKeeper bookKeeper = LGDXRenderableBookKeeper.getInstance();
    protected IntroRenderable renderable;
    protected AppInfo appInfo;

    public IntroActor(Skin skin, IntroRenderable renderable)  {
        super();
        setWidth(renderable.getWidth() * scale);
        setHeight(renderable.getHeight() * scale);
        this.renderable = renderable;
        this.appInfo = AppInfo.getInstance();
        //init();
    }

    protected synchronized void init() {
        /*nextButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getArrowButton());
        ActionButtonRenderable rNext = renderable.getArrowButton();
        float arrowRatio = 0.945f;
        float arrowHeight = getHeight() * 0.15f;
        float arrowWidth = arrowHeight * arrowRatio;
        rNext.setHeight(arrowHeight);
        rNext.setWidth(arrowWidth);
        rNext.setxPos(appInfo.convertX((1920f - arrowWidth) / 2));
        rNext.setyPos(0.07f * appInfo.getScreenHeight());
        rNext.markAsNeedsUpdate();*/
    }

//    protected synchronized void init() {
//        float screenHeight = getHeight();
//        float screenWidth = getWidth();
//
////        add().width(0.01f *screenWidth).height(screenHeight);
//
//        leftBanner = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLeftImage());
//        // WARNING: Renderable MUST HAVE BEEN ASSIGNED correct width and height
//        float lW = renderable.getLeftImage().getWidth() * scale;
//        float lH = renderable.getLeftImage().getHeight() * scale;
////        add(leftBanner).width(convertWidth(lW)).height(convertHeight(lH)).center();
//
//        nextButton = (ImageButtonWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getArrowButton());
//        Table buttonTable = new Table();
//        float ratio = 0.945f;
//        float height = screenHeight * 0.15f;
//        buttonTable.add(nextButton).height(height).width(ratio * height);
//        buttonTable.padBottom(0.05f * screenHeight);
////        add(buttonTable).expand().bottom();
//
//        rightBanner = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getRightImage());
//        float rW = renderable.getRightImage().getWidth() * scale;
//        float rH = renderable.getRightImage().getHeight() * scale;
////        add(rightBanner).width(convertWidth(rW)).height(convertHeight(rH)).center();
//
////        add().width(0.01f * screenWidth).height(screenHeight);
//
//    }
}
