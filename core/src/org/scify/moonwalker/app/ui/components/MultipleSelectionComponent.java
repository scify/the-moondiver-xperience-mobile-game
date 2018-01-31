package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;


public class MultipleSelectionComponent extends Table {

    protected String relativeAvatarPath;
    protected Image avatarImg;
    protected Sprite avatarSprite;
    protected String labetTxt;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;

    public MultipleSelectionComponent(String labelTxt, String relativeAvatarPath) {
        this.relativeAvatarPath = relativeAvatarPath;
        this.labetTxt = labelTxt;
        this.gameInfo = GameInfo.getInstance();
    }

    public void initActor(Skin skin) {
        setHeight(300);
        setWidth(gameInfo.getScreenWidth() / 2f);
        debug();
        center().bottom();
        setSkin(skin);
        setFillParent(true);
        if(labetTxt != null)
            addLabelToTable();
        if(relativeAvatarPath != null)
            addAvatarToTable();
    }

    public void addButton(String btnTitle, UserInputHandlerImpl inputHandler) {
        TextButton newBtn = new TextButton(btnTitle, getSkin());
        newBtn.addListener(inputHandler);
        newBtn.align(Align.center);
        add(newBtn).width(getWidth()).padBottom(10);
        row();
    }

    private void addLabelToTable() {
        Label titleLabel = new Label(labetTxt, getSkin());
        titleLabel.setWrap(true);
        // set the title label the same width as the entire table
        titleLabel.setWidth(getWidth());
        add(titleLabel).align(Align.left).padBottom(10);
        row();
    }

    private void addAvatarToTable() {
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (gameInfo.getScreenWidth() * 0.15), (float) (gameInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));

        add(avatarImg).width(gameInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
    }
}
