package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.helpers.ResourceLocator;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;


public class MultipleSelectionComponent extends Group {

    protected String relativeAvatarPath;
    protected Image avatarImg;
    protected Sprite avatarSprite;
    protected String questionText;
    protected ResourceLocator resourceLocator;
    protected GameInfo gameInfo;
    protected Table table;

    public MultipleSelectionComponent(String labelTxt, String relativeAvatarPath) {
        this.resourceLocator = new ResourceLocator();
        this.relativeAvatarPath = relativeAvatarPath;
        this.questionText = labelTxt;
        this.gameInfo = GameInfo.getInstance();
        setWidth(gameInfo.getScreenWidth());
    }

    public void initActor(Skin skin) {
        table = new Table();
        //table.debug();
        table.center().bottom().padBottom(40);
        setHeight(300);
        table.setSkin(skin);
        table.setFillParent(true);
        if(questionText != null)
            addLabelToTable();
        if(relativeAvatarPath != null)
            addAvatarToTable();
        Image img = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(resourceLocator.getFilePath("img/component_background.png")))));
        img.setSize(gameInfo.getScreenWidth(), gameInfo.getScreenHeight() / 2f);
        addActor(img);
        addActor(table);
    }

    public void addButton(String btnTitle, UserInputHandlerImpl inputHandler, int btnIndex) {
        int alignment = Align.left;
        if(btnIndex % 2 == 0)
            alignment = Align.right;
        TextButton newBtn = new TextButton(btnTitle, table.getSkin());
        newBtn.padBottom(5).padTop(5).padLeft(10).padRight(10);
        newBtn.addListener(inputHandler);
        // set the width as 1/2 of the screen, minus 40 pixels margin
        table.add(newBtn).width(gameInfo.getScreenWidth() * 0.5f - 40).align(alignment).pad(10);
        if(btnIndex % 2 == 0) {
            table.row();
        }
    }

    private void addLabelToTable() {
        Label titleLabel = new Label(questionText, table.getSkin());
        titleLabel.setWrap(true);
        table.add(titleLabel).align(Align.center).padBottom(15);
        table.row();
    }

    private void addAvatarToTable() {
        avatarSprite = new Sprite(new Texture(resourceLocator.getFilePath(relativeAvatarPath)));
        avatarSprite.setSize((float) (gameInfo.getScreenWidth() * 0.15), (float) (gameInfo.getScreenWidth() * 0.1));
        avatarImg = new Image(new SpriteDrawable(avatarSprite));

        table.add(avatarImg).width(gameInfo.getScreenWidth() * 0.2f).padLeft(5).padBottom(10);
    }
}
