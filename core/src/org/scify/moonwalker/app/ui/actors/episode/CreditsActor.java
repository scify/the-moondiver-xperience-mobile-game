package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.*;
import org.scify.moonwalker.app.ui.renderables.CreditsRenderable;

public class CreditsActor extends FadingTableActor<CreditsRenderable> {
    protected Label aboutLabel1;
    protected Label aboutLabel2;
    protected Image aboutBGImage;
    protected Image aboutSciFYImage;
    protected Image aboutVodafoneImage;
    protected Image aboutStemYouthImage;


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
        aboutBGImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutBGRenderable());
        aboutStack.add(aboutBGImage);

        Table innerAboutTable = new TableWithEffect();
        Stack aboutLabelStack = new StackWithEffect<>();
        aboutLabel1 = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutLabel1());
        aboutLabel1.setAlignment(Align.center);
        Label.LabelStyle lsAbout = new Label.LabelStyle();
        lsAbout.font = ThemeController.getInstance().getFont();
        aboutLabel1.setStyle(lsAbout);
        aboutLabel1.setWrap(true);
        aboutLabelStack.add(aboutLabel1);
        aboutLabel2 = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutLabel2());
        aboutLabel2.setAlignment(Align.center);
        aboutLabel2.setStyle(lsAbout);
        aboutLabel2.setWrap(true);
        aboutLabelStack.add(aboutLabel2);
        innerAboutTable.add(aboutLabelStack).width(0.9f * screenWidth).height(0.4f * screenHeight).top().colspan(3);
        innerAboutTable.row();
        aboutSciFYImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutSciFYRenderable());
        aboutVodafoneImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutVodafoneRenderable());
        aboutStemYouthImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutStemYouthRenderable());
        float widthOfSciFyLogo = 0.2f * screenWidth;
        float widthOfVodafoneLogo = 0.35f * screenWidth;
        float widthOfStemYouthLogo = 0.35f * screenWidth;
        innerAboutTable.add(aboutVodafoneImage).width(widthOfVodafoneLogo).height(widthOfVodafoneLogo * 433 / 1157);
        innerAboutTable.add(aboutSciFYImage).width(widthOfSciFyLogo).height(widthOfSciFyLogo * 600 / 500);
        innerAboutTable.add(aboutStemYouthImage).width(widthOfStemYouthLogo).height(widthOfStemYouthLogo * 216 / 591);

        aboutStack.add(innerAboutTable);
        aboutTable.add(aboutStack).width(0.9f * screenWidth).height(0.9f * screenHeight).center();
        add(aboutTable).width(screenWidth).height(screenHeight);
    }
}