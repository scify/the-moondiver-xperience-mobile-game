package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.ActionButtonWithEffect;

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

    protected ActionButtonWithEffect boyAvatarButton;
    protected ActionButtonWithEffect girlAvatarButton;
    protected ActionButtonWithEffect boyButton;
    protected ActionButtonWithEffect girlButton;
    protected ActionButtonWithEffect selectedAvatarButton;
    protected ActionButtonWithEffect startGameButton;
    protected ActionButtonWithEffect continueGameButton;
    protected ActionButtonWithEffect toggleAudioButton;
    protected ActionButtonWithEffect aboutButton;
    protected ActionButtonWithEffect quitButton;

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

    public void setBoyAvatarButton(ActionButtonWithEffect boyAvatarButton) {
        this.boyAvatarButton = boyAvatarButton;
        this.boyAvatarButton.setVisible(false);
    }

    public ActionButtonWithEffect getBoyAvatarButton() {
        return boyAvatarButton;
    }

    public void setGirlAvatarButton(ActionButtonWithEffect girlAvatarButton) {
        this.girlAvatarButton = girlAvatarButton;
        this.girlAvatarButton.setVisible(false);
    }

    public ActionButtonWithEffect getGirlAvatarButton() {
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

            EffectSequence imgEffect = new EffectSequence();
            imgEffect.addEffect(new FadeEffect(0.0, 0.5, fadingEffectsDuration));
            imgEffect.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    inputEnabled = true;
                }
            }));
            boyAvatarButton.apply(imgEffect);
            girlAvatarButton.apply(imgEffect);

            FadeEffect fadeInFullEffect = new FadeEffect(0.0, 1.0, fadingEffectsDuration);
            topBannerRenderable.apply(fadeInFullEffect);
            boyButton.apply(fadeInFullEffect);
            girlButton.apply(fadeInFullEffect);

            Effect fadeOutEffect = new FadeEffect(1.0, 0.0, fadingEffectsDuration);
            getStartGameButton().apply(fadeOutEffect);
            getContinueGameButton().apply(fadeOutEffect);
            getToggleAudioButton().apply(fadeOutEffect);
            getAboutButton().apply(fadeOutEffect);
            getQuitButton().apply(fadeOutEffect);

            renderableWasUpdated();
        }
    }

    public ActionButtonWithEffect getBoyButton() {
        return boyButton;
    }

    public void setBoyButton(ActionButtonWithEffect button) {
        this.boyButton = button;
        this.boyButton.setVisible(false);
    }

    public ActionButtonWithEffect getGirlButton() {
        return girlButton;
    }

    public void setGirlButton(ActionButtonWithEffect button) {
        this.girlButton = button;
        this.girlButton.setVisible(false);
    }

    public ActionButtonWithEffect getSelectedAvatarButton() {
        return selectedAvatarButton;
    }

    public void setSelectedAvatarButton(ActionButtonWithEffect selectedAvatarButton) {
        this.selectedAvatarButton = selectedAvatarButton;
        renderableWasUpdated();
    }

    public ActionButtonWithEffect getStartGameButton() {
        return startGameButton;
    }

    public void setStartGameButton(ActionButtonWithEffect button) {
        this.startGameButton = button;
    }

    public ActionButtonWithEffect getContinueGameButton() {
        return continueGameButton;
    }

    public void setContinueGameButton(ActionButtonWithEffect button) {
        this.continueGameButton = button;
    }

    public ActionButtonWithEffect getToggleAudioButton() {
        return toggleAudioButton;
    }

    public void setToggleAudioButton(ActionButtonWithEffect button) {
        this.toggleAudioButton = button;
    }

    public ActionButtonWithEffect getAboutButton() {
        return aboutButton;
    }

    public void setAboutButton(ActionButtonWithEffect button) {
        this.aboutButton = button;
    }

    public ActionButtonWithEffect getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(ActionButtonWithEffect button) {
        this.quitButton = button;
    }
}
