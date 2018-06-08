package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.*;
import org.scify.moonwalker.app.ui.renderables.CreditsRenderable;

public class CreditsActor extends FadingTableActor<CreditsRenderable> {
    protected Label aboutLabel;
    protected Image aboutBGImage;


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
        Table aboutTable = new Table();
        Stack aboutStack = new Stack();
        aboutBGImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutBGRenderable());
        aboutStack.add(aboutBGImage);

        aboutLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutLabel());
        aboutLabel.setAlignment(Align.center);
        Label.LabelStyle lsAbout = new Label.LabelStyle();
        lsAbout.font = ThemeController.getInstance().getFont();
        aboutLabel.setStyle(lsAbout);
        aboutLabel.setWrap(true);
        aboutStack.add(aboutLabel);
        aboutTable.add(aboutStack).width(0.8f * screenWidth).height(0.8f * screenHeight).center();
        add(aboutTable).width(screenWidth).height(screenHeight);
    }
}