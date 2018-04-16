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
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

public class MainMenuActor extends TableActor<MainMenuRenderable> implements Updateable {

    protected AppInfo appInfo;

    protected Button startButton;
    protected Button continueButton;
    protected Button toggleAudioButton;
    protected Button aboutButton;
    protected Button quitButton;

    protected Table menuTable;
    protected Table countDownTable;
    protected Label countDownLabel;

    protected Button boyButton;
    protected Button girlButton;

    protected Cell topBannerCell;
    protected Cell boyCell;
    protected Cell girlCell;

    protected Image boyImage;
    protected Image girlImage;

    protected float alphaSelectPLayer;
    protected float alphaMainMenu;

    protected boolean actorInitiated;

    protected ActorFactory factory;


    public MainMenuActor(Skin skin, MainMenuRenderable renderable) {
        super(skin, renderable);

        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        appInfo = AppInfo.getInstance();
        alphaSelectPLayer = 0.0f;
        alphaMainMenu = 1.0f;
        actorInitiated = false;

        init();
    }

    public void init() {
        // Get actor factory
        factory = ActorFactory.getInstance();

        float screenWidth = getWidth();
        float screenHeight= getHeight();
        top();
        float heightOfTopRow = 0.05f * screenHeight;
        add().colspan(3).height(heightOfTopRow).width(screenWidth);
        row();
        float heightLeft = createTopBanner(heightOfTopRow);
        row().height(heightLeft).width(screenWidth);


        // create actors
        setStartButton(factory.createButton(renderable.getStartGameButton()));
        setContinueButton(factory.createButton(renderable.getContinueGameButton()));
        setToggleAudioButton(factory.createButton(renderable.getToggleAudioButton()));
        setAboutButton(factory.createButton(renderable.getAboutButton()));
        setQuitButton(factory.createButton(renderable.getQuitButton()));
        setBoyButton(factory.createButton(renderable.getBoySelectionButton()));
        setGirlButton(factory.createButton(renderable.getGirlSelectionButton()));

        createBoySelection();
        createMenuButtons();
        createGirlSelection();


        actorInitiated = true;
        //debugAll();
    }

    //returns heightLeftForBottom
    protected float createTopBanner(float heightOfTopRow) {
        Texture texture = imgUrlToTexture(renderable.TOP_BANNER_IMG_PATH);
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        float width = convertWidth(image.getWidth());
        float height = convertHeight(image.getHeight());
        row().height(height).width(width);
        topBannerCell = add(image).maxHeight(height).maxWidth(width).colspan(3).top();
        Actor topBannerActor = topBannerCell.getActor();
        topBannerActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
        return getHeight() - (height + heightOfTopRow);
    }

    protected void createBoySelection() {
        Stack stack = new Stack();

        // Add image
//        Texture texture = imgUrlToTexture(renderable.getBoyImage().getImgPath());
//        boyImage = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
        boyImage = (Image) factory.createResourceForType(renderable.getBoyImage());
        boyImage.setColor(Color.DARK_GRAY);

        float width = convertWidth(boyImage.getWidth());
        //float height = convertHeight(boyImage.getHeight());
        stack.addActor(boyImage);

        // Add button
        Table buttonTable = new Table();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(boyButton.getWidth());
        float buttonHeight = convertHeight(boyButton.getHeight());
        buttonTable.add(boyButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight/2);
        buttonTable.add();
        boyCell = add(stack).width(width);//.height(height);
        Actor boyActor = boyCell.getActor();
        boyActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);

        // Add to important children
        getChildrenActorsAndRenderables().put(boyActor, renderable.getBoyImage());
    }

    protected void createMenuButtons() {
        Stack stack = new Stack();
        menuTable = new Table();
        menuTable.defaults();
        menuTable.bottom();
        float width = convertWidth(startButton.getWidth());
        float height = convertHeight(startButton.getHeight());
        float spaceBetweenButtons = height * 0.6f;
        menuTable.add(startButton).width(width).height(height);
        menuTable.row();
        menuTable.add().height(spaceBetweenButtons);
        menuTable.row();
        menuTable.add(continueButton).width(width).height(height);
        menuTable.row();
        menuTable.add().height(spaceBetweenButtons);
        menuTable.row();
        menuTable.add(toggleAudioButton).width(width).height(height);
        menuTable.row();
        menuTable.add().height(spaceBetweenButtons);
        menuTable.row();
        menuTable.add(aboutButton).width(width).height(height);
        menuTable.row();
        menuTable.add().height(spaceBetweenButtons);
        menuTable.row();
        menuTable.add(quitButton).width(width).height(height);
        menuTable.row();
        menuTable.add().width(width).height(height);
        stack.addActor(menuTable);

        countDownTable = new Table();
        countDownTable.defaults();
        countDownTable.center();
        countDownTable.setVisible(false);
        countDownLabel = new Label(renderable.getCountDownValue() + "", getSkin());
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = new ThemeController(30, "controls");
        ls.font = themeController.getFont();
        //ls.fontColor = Color.valueOf("912d25");
        countDownLabel.setStyle(ls);
        countDownTable.add(countDownLabel);
        stack.addActor(countDownTable);
        add(stack).width(width);
    }

    protected void createGirlSelection() {
        Stack stack = new Stack();
//        Texture texture = imgUrlToTexture(renderable.GIRL_IMG_PATH);
//        girlImage = new Image(new TextureRegionDrawable(new TextureRegion(texture)));

        girlImage = (Image) factory.createResourceForType(renderable.getGirlImage());
        girlImage.setColor(Color.DARK_GRAY);

        girlImage.setColor(Color.DARK_GRAY);
        float width = convertWidth(girlImage.getWidth());
        //float height = convertHeight(girlImage.getHeight());
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
        girlCell = add(stack).width(width);//.height(height);
        Actor girlActor = girlCell.getActor();
        girlActor.setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);

        // Add to important children
        getChildrenActorsAndRenderables().put(girlActor, renderable.getGirlImage());

    }

    public void setImageButtonsGreyedOutExcept(String selectedButtonId) {
        if (selectedButtonId.equals("boy")) {
            boyImage.setColor(Color.WHITE);
            girlImage.setColor(Color.DARK_GRAY);
        }else if (selectedButtonId.equals("girl")) {
            girlImage.setColor(Color.WHITE);
            boyImage.setColor(Color.DARK_GRAY);
        }
    }

    @Override
    public void update(Renderable renderable) {
        if (actorInitiated && this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (MainMenuRenderable) renderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();

//            if (((MainMenuRenderable) renderable).isPlayerSelectionInitiated() && alphaSelectPLayer == 0f) {
//
//            }
//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        alphaSelectPLayer += 0.10f;
//                        topBannerCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
//                        boyCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
//                        girlCell.getActor().setColor(1.0f, 1.0f, 1.0f, alphaSelectPLayer);
//                        if (alphaMainMenu > 0f) {
//                            topBannerCell.getActor().setVisible(true);
//                            boyCell.getActor().setVisible(true);
//                            girlCell.getActor().setVisible(true);
//                        }
//
//                    }
//                }, 0, 0.1f, 9);

//                Timer.schedule(new Timer.Task() {
//                    @Override
//                    public void run() {
//                        alphaMainMenu -= 0.10f;
//                        menuTable.setColor(1.0f, 1.0f, 1.0f, alphaMainMenu);
//                        if (alphaMainMenu <= 0f) {
//                            menuTable.setVisible(false);
//                        }
//                    }
//                }, 0, 0.1f, 9);
//            }
            if (this.renderable.getSelectedAvatar() != null) {
                countDownTable.setVisible(true);
                int countDown = this.renderable.getCountDownValue();
                if (countDown >= 0)
                    countDownLabel.setText(countDown + "");
                setImageButtonsGreyedOutExcept(this.renderable.getSelectedAvatar().getId());
            }
        }
    }

    protected void setStartButton(Button button) {
        startButton = button;
        getChildrenActorsAndRenderables().put(startButton, renderable.getStartGameButton());
    }

    public void setContinueButton(Button button) {
        continueButton = button;
        getChildrenActorsAndRenderables().put(continueButton, renderable.getContinueGameButton());
    }

    public void setToggleAudioButton(Button button) {
        toggleAudioButton = button;
        getChildrenActorsAndRenderables().put(toggleAudioButton, renderable.getToggleAudioButton());
    }

    public void setAboutButton(Button button) {
        aboutButton = button;
        getChildrenActorsAndRenderables().put(aboutButton, renderable.getAboutButton());
    }

    public void setQuitButton(Button button) {
        quitButton = button;
        getChildrenActorsAndRenderables().put(quitButton, renderable.getQuitButton());
    }

    public void setBoyButton(Button button) {
        boyButton = button;
        getChildrenActorsAndRenderables().put(boyButton, renderable.getBoySelectionButton());
    }

    public void setGirlButton(Button button) {
        girlButton = button;
        getChildrenActorsAndRenderables().put(girlButton, renderable.getGirlSelectionButton());
    }

}
