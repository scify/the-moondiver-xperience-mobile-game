package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.helpers.AppInfo;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

public class MainMenuActor extends TableActor implements Updateable {

    protected AppInfo appInfo;
    protected MainMenuRenderable renderable;

    protected Button startButton;
    protected Button continueButton;
    protected Button toggleAudioButton;
    protected Button aboutButton;
    protected Button quitButton;

    protected Button boyButton;
    protected Button girlButton;

    protected Cell topBannerCell;
    protected Cell boyCell;
    protected Cell menuCell;
    protected Cell girlCell;

    protected Image boyImage;
    protected Image girlImage;

    protected float alphaSelectPLayer;
    protected float alphaMainMenu;


    public MainMenuActor(Skin skin, MainMenuRenderable renderable) {
        super(skin);
        this.renderable = renderable;
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        appInfo = AppInfo.getInstance();
        alphaSelectPLayer = 0f;
        alphaMainMenu = 1.0f;
        //addBackground(renderable.getImgPath());
    }

    public void init() {
        float screenWidth = getWidth();
        top();

        float heightLeft = createTopBanner();

        row().height(heightLeft).width(screenWidth);

        createBoySelection();
        createMenuButtons();
        createGirlSelection();

        //debugAll();
    }

    //returns heightLeftForBottom
    protected float createTopBanner() {
        Texture texture = imgUrlToTexture(renderable.TOP_BANNER_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        float width = convertWidth(image.getWidth());
        float height = convertHeight(image.getHeight());
        row().height(height).width(width);
        topBannerCell = add(image).maxHeight(height).maxWidth(width).colspan(3).top();
        Actor topBannerActor = topBannerCell.getActor();
        topBannerActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
        topBannerActor.setVisible(false);
        return getHeight() - height;
    }

    protected void createBoySelection() {
        Stack stack = new Stack();
        Texture texture = imgUrlToTexture(renderable.BOY_IMG_PATH);
        boyImage = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        boyImage.setColor(Color.DARK_GRAY);
        float width = convertWidth(boyImage.getWidth());
        float height = convertHeight(boyImage.getHeight());
        stack.addActor(boyImage);
        Table buttonTable = new Table();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(boyButton.getWidth());
        float buttonHeight = convertHeight(boyButton.getHeight());
        buttonTable.add(boyButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight/2);
        buttonTable.add();
        boyCell = add(stack).width(width).height(height);
        Actor boyActor = boyCell.getActor();
        boyActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
        boyActor.setVisible(false);
    }

    protected void createMenuButtons() {
        Table table = new Table();
        table.defaults();
        table.bottom();
        float width = convertWidth(startButton.getWidth());
        float height = convertHeight(startButton.getHeight());
        float spaceBetweenButtons = height * 0.6f;
        table.add(startButton).width(width).height(height);
        table.row();
        table.add().height(spaceBetweenButtons);
        table.row();
        table.add(continueButton).width(width).height(height);
        table.row();
        table.add().height(spaceBetweenButtons);
        table.row();
        table.add(toggleAudioButton).width(width).height(height);
        table.row();
        table.add().height(spaceBetweenButtons);
        table.row();
        table.add(aboutButton).width(width).height(height);
        table.row();
        table.add().height(spaceBetweenButtons);
        table.row();
        table.add(quitButton).width(width).height(height);
        table.row();
        table.add().width(width).height(height);
        menuCell = add(table).width(width);
    }

    protected void createGirlSelection() {
        Stack stack = new Stack();
        Texture texture = imgUrlToTexture(renderable.GIRL_IMG_PATH);
        girlImage = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        girlImage.setColor(Color.DARK_GRAY);
        float width = convertWidth(girlImage.getWidth());
        float height = convertHeight(girlImage.getHeight());
        stack.addActor(girlImage);
        Table buttonTable = new Table();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(girlButton.getWidth());
        float buttonHeight = convertHeight(girlButton.getHeight());
        buttonTable.add(girlButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight/2);
        buttonTable.add();
        girlCell = add(stack).width(width).height(height);
        Actor girlActor = girlCell.getActor();
        girlActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
        girlActor.setVisible(false);
    }

    public void setImageButtonsGreyedOutExcept(String selectedButtonId) {
        if (selectedButtonId.equals("boy")) {
            boyImage.setColor(Color.WHITE);
            girlImage.setColor(Color.DARK_GRAY);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    alphaSelectPLayer += 0.10f;
                    topBannerCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                    boyCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                    girlCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                    if (alphaMainMenu > 0f) {
                        topBannerCell.getActor().setVisible(true);
                        boyCell.getActor().setVisible(true);
                        girlCell.getActor().setVisible(true);
                    }

                }
            }, 0, 0.1f, 9);
        }else if (selectedButtonId.equals("girl")) {
            girlImage.setColor(Color.WHITE);
            boyImage.setColor(Color.DARK_GRAY);
        }
    }

    @Override
    public void update(Renderable renderable) {
        if (this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (MainMenuRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            if (((MainMenuRenderable) renderable).isPlayerSelectionInitiated() && alphaSelectPLayer == 0f) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        alphaSelectPLayer += 0.10f;
                        topBannerCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                        boyCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                        girlCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
                        if (alphaMainMenu > 0f) {
                            topBannerCell.getActor().setVisible(true);
                            boyCell.getActor().setVisible(true);
                            girlCell.getActor().setVisible(true);
                        }

                    }
                }, 0, 0.1f, 9);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        alphaMainMenu -= 0.10f;
                        menuCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaMainMenu);
                        if (alphaMainMenu <= 0f)
                            menuCell.getActor().setVisible(false);
                    }
                }, 0, 0.1f, 9);
            }
            if (this.renderable.getSelectedAvatar() != null)
                setImageButtonsGreyedOutExcept(this.renderable.getSelectedAvatar().getId());
        }
    }

    public void setStartButton(Button button) {
        startButton = button;
    }

    public void setContinueButton(Button button) {
        continueButton = button;
    }

    public void setToggleAudioButton(Button button) {
        toggleAudioButton = button;
    }

    public void setAboutButton(Button button) {
        aboutButton = button;
    }

    public void setQuitButton(Button button) {
        quitButton = button;
    }

    public void setBoyButton(Button button) {
        boyButton = button;
    }

    public void setGirlButton(Button button) {
        girlButton = button;
    }

    protected float convertHeight(float initialHeight) {
        int initialBackgroundHeight = 1080;
        float ret = getHeight() * initialHeight;
        ret = ret / initialBackgroundHeight;
        return ret;
    }

    protected float convertWidth(float initialWidth) {
        int initialBackgroundWidth = 1920;
        float ret = getWidth() * initialWidth;
        ret = ret / initialBackgroundWidth;
        return ret;
    }
}
