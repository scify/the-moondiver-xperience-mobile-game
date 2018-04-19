package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MainMenuRenderable extends Renderable {

    protected ImageRenderable tableBGRenderable;
    protected ImageRenderable topBannerRenderable;

    public final String BG_IMG_PATH = "img/mainMenu/bg.png";
    public final String TOP_BANNER_IMG_PATH = "img/mainMenu/top.png";
    public final String BOY_IMG_PATH = "img/mainMenu/boy.png";
    public final String GIRL_IMG_PATH = "img/mainMenu/girl.png";
    public final String BOY_BUTTON_IMG_PATH = "img/mainMenu/boyButton.png";
    public final String GIRL_BUTTON_IMG_PATH = "img/mainMenu/girlButton.png";
    public final String START_BUTTON_IMG_PATH = "img/mainMenu/start.png";
    public final String CONTINUE_BUTTON_IMG_PATH = "img/mainMenu/continue.png";
    public final String TOGGLE_AUDIO_BUTTON_IMG_PATH = "img/mainMenu/toggleAudio.png";
    public final String ABOUT_BUTTON_IMG_PATH = "img/mainMenu/about.png";
    public final String QUIT_BUTTON_IMG_PATH = "img/mainMenu/quit.png";

    //AUDIO
    public final String BG_AUDIO_PATH = "audio/mainMenu/menu.mp3";
    public final String CLICK_AUDIO_PATH = "audio/button1.mp3";
    //AUDIO BUFFERING FOR NEXT EPISODE
    public final String BOY_MUSIC_AUDIO_PATH = "audio/room_episode/boy/music.mp3";
    public final String GIRL_MUSIC_AUDIO_PATH = "audio/room_episode/girl/music.mp3";

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
    protected boolean inputEnabled;

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id);
        inputEnabled = false;
        playerSelectionStatus = false;
        countDownValue = 5;
        tableBGRenderable = new ImageRenderable("bg", BG_IMG_PATH);
        topBannerRenderable = new ImageRenderable("topbannerImg", TOP_BANNER_IMG_PATH);
        topBannerRenderable.setVisible(false);
    }

    public void enableInput() { inputEnabled = true; }

    public void disableInput() { inputEnabled = false; }

    public boolean isReadyForInput() { return inputEnabled; }

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

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void resetCountDown() {
        countDownValue = 5;
        renderableWasUpdated();
    }

    public void forceCountDownToZero() {
        countDownValue = 0;
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

    public void initiatePlayerSelection() {
        if (playerSelectionStatus == false) {
            playerSelectionStatus = true;
            selectedAvatarButton = null;
            topBannerRenderable.setVisible(true);
            boyAvatarButton.setVisible(true);
            girlAvatarButton.setVisible(true);
            boyButton.setVisible(true);
            girlButton.setVisible(true);
            double fadingEffectsDuration = 1500;

            LGDXEffectList imgEffect = new LGDXEffectList();
            imgEffect.addEffect(new FadeLGDXEffect(0.0, 0.5, fadingEffectsDuration));
            imgEffect.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    inputEnabled = true;
                }
            }));
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
