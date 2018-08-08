package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.moonwalker.app.ui.actors.*;
import org.scify.moonwalker.app.ui.renderables.CreditsRenderable;

public class CreditsActor extends FadingTableActor<CreditsRenderable> {

    protected Image aboutBGImage1;
    protected Image aboutBGImage2;

    public CreditsActor(Skin skin, CreditsRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        init();
    }

    protected void init() {

        float screenWidth = getWidth();
        float screenHeight = getHeight();
        top();

        // About
        Table aboutTable = new TableWithEffect();
        Stack aboutStack = new StackWithEffect<>();
        aboutBGImage1 = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutBGRenderable1());
        aboutBGImage2 = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutBGRenderable2());
        aboutStack.add(aboutBGImage1);
        aboutStack.add(aboutBGImage2);
        aboutTable.add(aboutStack).width(0.9f * screenWidth).height(0.9f * screenHeight).center();
        add(aboutTable).width(screenWidth).height(screenHeight);
    }
}