package org.scify.moonwalker.app.ui.actors.episode;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.moonwalker.app.game.SelectedPlayer;
import org.scify.moonwalker.app.ui.ThemeController;
import org.scify.moonwalker.app.ui.actors.*;
import org.scify.moonwalker.app.ui.renderables.MainMenuRenderable;

public class MainMenuActor extends FadingTableActor<MainMenuRenderable> implements Updateable {

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
    protected boolean actorInitiated;


    public MainMenuActor(Skin skin, MainMenuRenderable renderable) {
        super(skin, renderable);
        setWidth(renderable.getWidth());
        setHeight(renderable.getHeight());
        addBackground(renderable.getTableBGRenderable());
        actorInitiated = false;
        init();
    }

    protected void init() {

        float screenWidth = getWidth();
        float screenHeight = getHeight();
        top();
        float heightOfTopRow = 0.05f * screenHeight;
        add().colspan(3).height(heightOfTopRow).width(screenWidth);
        row();
        float heightLeft = createTopBanner(heightOfTopRow);
        row().height(heightLeft).width(screenWidth);

        // create actors for menu
        startButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getStartGameButton());
        continueButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getContinueGameButton());
        toggleAudioButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getToggleAudioButton());
        aboutButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getAboutButton());
        quitButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getQuitButton());

        // create actors for character selection
        boyButton = (Button) bookKeeper.getUIRepresentationOfRenderable((renderable.getBoyButton()));
        boyAvatarButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getBoyAvatarButton());
        girlButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getGirlButton());
        girlAvatarButton = (Button) bookKeeper.getUIRepresentationOfRenderable(renderable.getGirlAvatarButton());

        // Create episode-specific background
        createBoySelection();
        createMenuButtons();
        createGirlSelection();

        actorInitiated = true;
    }

    //returns heightLeftForBottom
    protected float createTopBanner(float heightOfTopRow) {
        topBannerImage = (ImageWithEffect) bookKeeper.getUIRepresentationOfRenderable(renderable.getTopBannerRenderable());
        float width = convertWidth(topBannerImage.getWidth());
        float height = convertHeight(topBannerImage.getHeight());
        row().height(height).width(width);
        add(topBannerImage).maxHeight(height).maxWidth(width).colspan(3).top();

        return getHeight() - (height + heightOfTopRow);
    }

    protected void createBoySelection() {
        Stack stack = new StackWithEffect<>();

        //Add Image
        float width = convertWidth(boyAvatarButton.getWidth());
        stack.addActor(boyAvatarButton);

        // Add button
        //Table buttonTable = new TableActor<TableRenderable>(getDefaultSkin(), this.renderable);
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
    }

    protected void createMenuButtons() {
        Stack stack = new Stack();
        menuTable = new TableWithEffect();
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

        // Countdown
        countDownTable = new TableWithEffect();
        countDownTable.defaults();
        countDownTable.center();
        countDownLabel = (Label) bookKeeper.getUIRepresentationOfRenderable(renderable.getCountDownLabel());
        countDownLabel.setAlignment(Align.center);
        Label.LabelStyle ls = new Label.LabelStyle();
        ThemeController themeController = ThemeController.getInstance();
        ls.font = themeController.getFont(30, "controls");
        countDownLabel.setStyle(ls);
        countDownTable.add(countDownLabel).width(width).height(height);
        stack.add(countDownTable);

        add(stack).width(width);
    }

    protected void createGirlSelection() {
        Stack stack = new StackWithEffect();

        //Add Image
        float width = convertWidth(girlAvatarButton.getWidth());
        stack.addActor(girlAvatarButton);

        // Add closeButton
        Table buttonTable = new TableWithEffect();
        buttonTable.defaults();
        buttonTable.bottom();
        float buttonWidth = convertWidth(girlButton.getWidth());
        float buttonHeight = convertHeight(girlButton.getHeight());
        buttonTable.add(girlButton).height(buttonHeight).width(buttonWidth).center();
        stack.addActor(buttonTable);
        buttonTable.row().height(buttonHeight / 2);
        buttonTable.add();
        add(stack).width(width);
    }

    protected String selectedPlayer = "";

    @Override
    public void update(Renderable rRenderable) {
        // If we have an actor and it's been some time since the last update
        if (actorInitiated) {
            renderable = (MainMenuRenderable) rRenderable;
            // If we have selected an new avatar
            if (renderable.getSelectedPlayer() != "" && selectedPlayer != renderable.getSelectedPlayer()) {
                // Update the selected avatar
                selectedPlayer = renderable.getSelectedPlayer();
                // Apply effects
                updateSelectedAvatar();
            }
            renderable.wasUpdated();
        }
    }

    private void updateSelectedAvatar() {
        if (selectedPlayer == SelectedPlayer.boy) {
            Effect fadeIn = new FadeEffect(boyAvatarButton.getColor().a, 1.0, 500);
            Effect fadeOut = new FadeEffect(girlAvatarButton.getColor().a, 0.5, 500);

            renderable.getBoyAvatarButton().addEffect(fadeIn);
            renderable.getGirlAvatarButton().addEffect(fadeOut);
        } else if (selectedPlayer == SelectedPlayer.girl) {
            Effect fadeIn = new FadeEffect(girlAvatarButton.getColor().a, 1.0, 500);
            Effect fadeOut = new FadeEffect(boyAvatarButton.getColor().a, 0.5, 500);

            renderable.getBoyAvatarButton().addEffect(fadeOut);
            renderable.getGirlAvatarButton().addEffect(fadeIn);
        }
    }
}
