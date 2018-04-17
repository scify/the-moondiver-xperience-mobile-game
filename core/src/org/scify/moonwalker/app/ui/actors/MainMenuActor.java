package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.MoonWalkerGameState;
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
    protected Button boyAvatarButton;
    protected Button girlButton;
    protected Button girlAvatarButton;

    protected Image topBannerImage;
    protected Image tableBG;

    protected boolean actorInitiated;
    protected ActorFactory factory;



    public MainMenuActor(Skin skin, MainMenuRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        appInfo = AppInfo.getInstance();
        actorInitiated = false;
        init();
    }

    protected void init() {
        // Get actor factory
        factory = ActorFactory.getInstance();

        float screenWidth = getWidth();
        float screenHeight = getHeight();
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
        setBoyButton(factory.createButton(renderable.getBoyButton()));
        setBoyAvatarButton(factory.createButton(renderable.getBoyAvatarButton()));
        setGirlButton(factory.createButton(renderable.getGirlButton()));
        setGirlAvatarButton(factory.createButton(renderable.getGirlAvatarButton()));

        // Create episode-specific background
        createTableBG();

        createBoySelection();
        createMenuButtons();
        createGirlSelection();

        actorInitiated = true;

        // Fade-in everything
        LGDXEffectList fadeInEffects = new LGDXEffectList();
        fadeInEffects.addEffect(new FadeLGDXEffect(0.0, 1.0, 2000));
        fadeInEffects.addEffect(new FunctionEffect(new Runnable() {
            @Override
            public void run() {
                renderable.enableInput();
            }
        }));
        renderable.apply(fadeInEffects);

        //debugAll();
    }

    protected void createTableBG() {
        tableBG = new Image();
        tableBG.setDrawable(ActorFactory.getInstance().createImage(renderable.getTableBGRenderable().getImgPath(),
                renderable.getTableBGRenderable()).getDrawable());
        setBackground(tableBG.getDrawable());
        getChildrenActorsAndRenderables().put(tableBG, renderable.getTableBGRenderable());
    }

    //returns heightLeftForBottom
    protected float createTopBanner(float heightOfTopRow) {
        topBannerImage = (Image) factory.createResourceForType(renderable.getTopBannerRenderable());
        float width = convertWidth(topBannerImage.getWidth());
        float height = convertHeight(topBannerImage.getHeight());
        row().height(height).width(width);
        Actor topBannerActor = add(topBannerImage).maxHeight(height).maxWidth(width).colspan(3).top().getActor();
        getChildrenActorsAndRenderables().put(topBannerActor, renderable.getTopBannerRenderable());
        return getHeight() - (height + heightOfTopRow);
    }

    protected void createBoySelection() {
        Stack stack = new Stack();

        //Add Image
        float width = convertWidth(boyAvatarButton.getWidth());
        stack.addActor(boyAvatarButton);

        // Add button
        Table buttonTable = new Table();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(boyButton.getWidth());
        float buttonHeight = convertHeight(boyButton.getHeight());
        buttonTable.add(boyButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight / 2);
        buttonTable.add();
        add(stack).width(width);

        // Add to important children
        getChildrenActorsAndRenderables().put(boyButton, renderable.getBoyButton());
        getChildrenActorsAndRenderables().put(boyAvatarButton, renderable.getBoyAvatarButton());
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

        //Add Image
        float width = convertWidth(girlAvatarButton.getWidth());
        stack.addActor(girlAvatarButton);

        // Add button
        Table buttonTable = new Table();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(girlButton.getWidth());
        float buttonHeight = convertHeight(girlButton.getHeight());
        buttonTable.add(girlButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight / 2);
        buttonTable.add();
        add(stack).width(width);

        // Add to important children
        getChildrenActorsAndRenderables().put(girlButton, renderable.getGirlButton());
        getChildrenActorsAndRenderables().put(girlAvatarButton, renderable.getGirlAvatarButton());
    }

    public void updateSelectedAvatar(String selectedButtonId) {
        if (selectedButtonId.equals("boyButton") || selectedButtonId.equals("boyAvatarButton")) {
            LGDXEffect fadeIn = new FadeLGDXEffect(boyAvatarButton.getColor().a, 1.0, 500);
            LGDXEffect fadeOut = new FadeLGDXEffect(girlAvatarButton.getColor().a, 0.5, 500);

            renderable.getBoyAvatarButton().apply(fadeIn);
            renderable.getGirlAvatarButton().apply(fadeOut);
        } else if (selectedButtonId.equals("girlButton") || selectedButtonId.equals("girlAvatarButton")) {
            LGDXEffect fadeIn = new FadeLGDXEffect(girlAvatarButton.getColor().a, 1.0, 500);
            LGDXEffect fadeOut = new FadeLGDXEffect(boyAvatarButton.getColor().a, 0.5, 500);

            renderable.getBoyAvatarButton().apply(fadeOut);
            renderable.getGirlAvatarButton().apply(fadeIn);
        }
    }

    protected Renderable lastSelectedAvatarButton = null;

    @Override
    public void update(Renderable rRenderable) {
        if (actorInitiated && this.renderable.getRenderableLastUpdated() > timestamp) {
            this.renderable = (MainMenuRenderable) rRenderable;
            this.timestamp = this.renderable.getRenderableLastUpdated();
            if (this.renderable.getSelectedAvatarButton() != null) {
                countDownTable.setVisible(true);
                int countDown = this.renderable.getCountDownValue();
                if (countDown >= 0)
                    countDownLabel.setText(countDown + "");
                if (lastSelectedAvatarButton != this.renderable.getSelectedAvatarButton()) {
                    // Update the selected avatar
                    lastSelectedAvatarButton = this.renderable.getSelectedAvatarButton();
                    // Apply effects
                    updateSelectedAvatar(lastSelectedAvatarButton.getId());
                }
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
        getChildrenActorsAndRenderables().put(boyButton, renderable.getBoyButton());
    }

    public void setBoyAvatarButton(Button button) {
        boyAvatarButton = button;
        getChildrenActorsAndRenderables().put(boyAvatarButton, renderable.getBoyAvatarButton());
    }

    public void setGirlButton(Button button) {
        girlButton = button;
        getChildrenActorsAndRenderables().put(girlButton, renderable.getGirlButton());
    }

    public void setGirlAvatarButton(Button button) {
        girlAvatarButton = button;
        getChildrenActorsAndRenderables().put(girlAvatarButton, renderable.getGirlAvatarButton());
    }

}
