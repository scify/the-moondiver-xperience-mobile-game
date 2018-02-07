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
    protected String questionText;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;

    public MultipleSelectionComponent(String labelTxt, String relativeAvatarPath) {
        this.relativeAvatarPath = relativeAvatarPath;
        this.questionText = labelTxt;
        this.gameInfo = GameInfo.getInstance();
    }

    public void initActor(Skin skin) {
        setHeight(300);
        //debug();
        center().bottom().padBottom(40);
        setSkin(skin);
        setFillParent(true);
        if(questionText != null)
            addLabelToTable();
        if(relativeAvatarPath != null)
            addAvatarToTable();
    }

    public void addButton(String btnTitle, UserInputHandlerImpl inputHandler, int btnIndex) {
        int alignment = Align.left;
        if(btnIndex % 2 == 0)
            alignment = Align.right;
        TextButton newBtn = new TextButton(btnTitle, getSkin());
        newBtn.padBottom(5).padTop(5).padLeft(10).padRight(10);
        newBtn.addListener(inputHandler);
        // set the width as 1/2 of the screen, minus 40 pixels margin
        add(newBtn).width(gameInfo.getScreenWidth() * 0.5f - 40).align(alignment).pad(10);
        if(btnIndex % 2 == 0) {
            row();
        }
    }

    private void addLabelToTable() {
        Label titleLabel = new Label(questionText, getSkin());
        titleLabel.setWrap(true);
        add(titleLabel).align(Align.center).padBottom(15);
        row();
    }

    private void addAvatarToTable() {
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (gameInfo.getScreenWidth() * 0.15), (float) (gameInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));

        add(avatarImg).width(gameInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
    }
}
