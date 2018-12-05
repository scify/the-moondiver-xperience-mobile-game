package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.moonwalker.app.ui.actors.FadingTableActor;
import org.scify.moonwalker.app.ui.actors.ImageButtonWithEffect;
import org.scify.moonwalker.app.ui.actors.ImageWithEffect;
import org.scify.moonwalker.app.ui.renderables.IntroRenderable;

public class IntroActor extends FadingTableActor<IntroRenderable> {

    protected Image leftBanner;
    protected Button nextButton;
    protected Image rightBanner;

    public IntroActor(Skin skin, IntroRenderable renderable)  {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected synchronized void init() {
        float screenHeight = getHeight();
        float screenWidth = getWidth();

        add().width(0.01f *screenWidth).height(screenHeight);

        leftBanner = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getLeftImage());
        add(leftBanner).width(convertWidth(leftBanner.getWidth())).height(convertHeight(leftBanner.getHeight())).center();

        nextButton = (ImageButtonWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getArrowButton());
        Table buttonTable = new Table();
        float ratio = nextButton.getWidth() /nextButton.getHeight();
        float height = screenHeight * 0.15f;
        buttonTable.add(nextButton).height(height).width(ratio * height);
        buttonTable.padBottom(0.05f * screenHeight);
        add(buttonTable).expand().bottom();

        rightBanner = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getRightImage());
        add(rightBanner).width(convertWidth(rightBanner.getWidth())).height(convertHeight(rightBanner.getHeight())).center();

        add().width(0.01f *screenWidth).height(screenHeight);
    }
}
