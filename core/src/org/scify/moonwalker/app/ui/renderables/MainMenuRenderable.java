package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MainMenuRenderable extends Renderable {

    public final String TOP_BANNER_IMG_PATH = "img/mainMenu/top.png";
    public final String BOY_IMG_PATH = "img/mainMenu/boy.png";
    public final String GIRL_IMG_PATH = "img/mainMenu/girl.png";
    public final String BOY_BUTTON_IMG_PATH = "img/mainMenu/boyButton.png";
    public final String GIRL_BUTTON_IMG_PATH = "img/mainMenu/girlButton.png";

    protected ImageRenderable topBannerRenderable;

    protected ActionButton boyAvatarButton;
    protected ActionButton girlAvatarButton;
    protected ActionButton boyButton;
    protected ActionButton girlButton;
    protected ActionButton selectedAvatarButton;
    protected ActionButton startGameButton;
    protected ActionButton continueGameButton;
    protected ActionButton toggleAudioButton;
    protected ActionButton aboutButton;
    protected ActionButton quitButton;

    protected int countDownValue;

    protected boolean playerSelectionStatus;


    public void setBoyAvatarButton(ActionButton boyAvatarButton) {
        this.boyAvatarButton = boyAvatarButton;
        this.boyAvatarButton.setVisible(false);
    }

    public ActionButton getBoyAvatarButton() {
        return boyAvatarButton;
    }

    public void setGirlAvatarButton(ActionButton girlAvatarButton) {
        this.girlAvatarButton = girlAvatarButton;
        this.girlAvatarButton.setVisible(false);
    }

    public ActionButton getGirlAvatarButton() {
        return girlAvatarButton;
    }

    public ImageRenderable getTopBannerRenderable() {
        return topBannerRenderable;
    }

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id);
        playerSelectionStatus = false;
        countDownValue = 5;

        topBannerRenderable = new ImageRenderable("topbannerImg", TOP_BANNER_IMG_PATH);
        topBannerRenderable.setVisible(false);
    }

    public void resetCountDown() {
        countDownValue = 5;
        renderableWasUpdated();
    }

    public void decreaseCountDown() {
        countDownValue--;
        if (countDownValue >= 0)
            renderableWasUpdated();
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    public boolean isPlayerSelectionInitiated() {
        return playerSelectionStatus;
    }

    public void initiatePlayerSelection() {
        if (playerSelectionStatus == false) {
            playerSelectionStatus = true;
            selectedAvatarButton = null;
            boyAvatarButton.setVisible(true);
            girlAvatarButton.setVisible(true);
            boyButton.setVisible(true);
            girlButton.setVisible(true);
            topBannerRenderable.setVisible(true);
            double fadingEffectsDuration = 2500;

            LGDXEffectList imgEffect = new LGDXEffectList();
            imgEffect.addEffect(new FadeLGDXEffect(0.0, 0.5, fadingEffectsDuration));
            /*imgEffect.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    boyRenderable.setVisible(false);
                    System.err.println("Visibility off!");
                }
            }));*/
            boyAvatarButton.apply(imgEffect);
            girlAvatarButton.apply(imgEffect);

            FadeLGDXEffect fadeInFullEffect = new FadeLGDXEffect(0.0, 1.0, fadingEffectsDuration);
            topBannerRenderable.apply(fadeInFullEffect);
            boyButton.apply(fadeInFullEffect);
            girlButton.apply(fadeInFullEffect);

            Effect fadeOutEffect = new FadeLGDXEffect(1.0, 0.0, fadingEffectsDuration);
            getStartGameButton().apply(fadeOutEffect);
            getContinueGameButton().apply(fadeOutEffect);
            getToggleAudioButton().apply(fadeOutEffect);
            getAboutButton().apply(fadeOutEffect);
            getQuitButton().apply(fadeOutEffect);

            renderableWasUpdated();
        }
    }

    public ActionButton getBoyButton() {
        return boyButton;
    }

    public void setBoyButton(ActionButton button) {
        this.boyButton = button;
        this.boyButton.setVisible(false);
    }

    public ActionButton getGirlButton() {
        return girlButton;
    }

    public void setGirlButton(ActionButton button) {
        this.girlButton = button;
        this.girlButton.setVisible(false);
    }

    public ActionButton getSelectedAvatarButton() {
        return selectedAvatarButton;
    }

    public void setSelectedAvatarButton(ActionButton selectedAvatarButton) {
        this.selectedAvatarButton = selectedAvatarButton;
        renderableWasUpdated();
    }

    public ActionButton getStartGameButton() {
        return startGameButton;
    }

    public void setStartGameButton(ActionButton button) {
        this.startGameButton = button;
    }

    public ActionButton getContinueGameButton() {
        return continueGameButton;
    }

    public void setContinueGameButton(ActionButton button) {
        this.continueGameButton = button;
    }

    public ActionButton getToggleAudioButton() {
        return toggleAudioButton;
    }

    public void setToggleAudioButton(ActionButton button) {
        this.toggleAudioButton = button;
    }

    public ActionButton getAboutButton() {
        return aboutButton;
    }

    public void setAboutButton(ActionButton button) {
        this.aboutButton = button;
    }

    public ActionButton getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(ActionButton button) {
        this.quitButton = button;
    }
}
