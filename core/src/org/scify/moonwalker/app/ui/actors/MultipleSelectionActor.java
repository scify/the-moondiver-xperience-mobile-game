package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.MultipleConversationLines;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;


public class MultipleSelectionActor extends TableActor {

    protected String relativeAvatarPath;
    protected Image avatarImg;
    protected Sprite avatarSprite;
    protected String questionText;
    protected ResourceLocator resourceLocator;
    protected AppInfo appInfo;
    protected Image background;
    MultipleConversationLines renderable;

    public MultipleSelectionActor(Skin skin, MultipleConversationLines renderable) {
        super(skin);
        this.resourceLocator = new ResourceLocator();
        this.relativeAvatarPath = renderable.getRelativeAvatarImgPath();
        this.questionText = renderable.getTitle();
        this.appInfo = AppInfo.getInstance();
        this.renderable = renderable;
        initTable();
    }

    protected void initTable() {
        center().bottom().padBottom(40);
        setHeight(renderable.getHeight());
        setWidth(renderable.getWidth());
        setFillParent(true);
        addBackground();
        if(questionText != null)
            addTitleToTable();
//        if(relativeAvatarPath != null)
//            addAvatarToTable();
        debug();
    }

    protected void addBackground() {
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        background.setSize(appInfo.getScreenWidth(), appInfo.getScreenHeight() / 2f);
        addActor(background);
    }

    private void addTitleToTable() {
        Label titleLabel = new Label(questionText, getSkin());
        titleLabel.setWrap(true);
        add(titleLabel).align(Align.center).center().center();
        row();
    }

    private void addAvatarToTable() {
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath("img/avatars/" + relativeAvatarPath)));
        avatarSprite.setSize((float) (appInfo.getScreenWidth() * 0.15), (float) (appInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));

        add(avatarImg).width(appInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
    }

    public void addButton(Button button, int btnIndex) {
        int alignment = Align.left;
        if(btnIndex % 2 == 0)
            alignment = Align.right;
        button.padBottom(5).padTop(5).padLeft(10).padRight(10);
        // set the width as 1/2 of the screen, minus 40 pixels margin
        add(button).width(appInfo.getScreenWidth() * 0.5f - 40).align(alignment).pad(10);
        if(btnIndex % 2 == 0) {
            row();
        }
    }
}
