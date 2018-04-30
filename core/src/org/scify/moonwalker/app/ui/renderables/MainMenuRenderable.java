package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.SelectedPlayer;

public class MainMenuRenderable extends FadingTableRenderable {
    protected ImageRenderable topBannerRenderable;

    public final static String BG_IMG_PATH = "img/episode_mainMenu/bg.png";
    public final static String TOP_BANNER_IMG_PATH = "img/episode_mainMenu/top.png";
    public final static String BOY_IMG_PATH = "img/episode_mainMenu/boy.png";
    public final static String GIRL_IMG_PATH = "img/episode_mainMenu/girl.png";
    public final static String BOY_BUTTON_IMG_PATH = "img/episode_mainMenu/boyButton.png";
    public final static String GIRL_BUTTON_IMG_PATH = "img/episode_mainMenu/girlButton.png";
    public final static String START_BUTTON_IMG_PATH = "img/episode_mainMenu/start.png";
    public final static String CONTINUE_BUTTON_IMG_PATH = "img/episode_mainMenu/continue.png";
    public final static String TOGGLE_AUDIO_BUTTON_IMG_PATH = "img/episode_mainMenu/toggleAudio.png";
    public final static String ABOUT_BUTTON_IMG_PATH = "img/episode_mainMenu/about.png";
    public final static String QUIT_BUTTON_IMG_PATH = "img/episode_mainMenu/quit.png";

    //AUDIO
    public final static String MAINMENU_AUDIO_PATH = "audio/episode_mainMenu/bg.mp3";
    public final static String CLICK_AUDIO_PATH = "audio/button1.mp3";
    //AUDIO BUFFERING FOR NEXT EPISODE
    public final static String BOY_MUSIC_AUDIO_PATH = "audio/episode_room/boy_music.mp3";
    public final static String GIRL_MUSIC_AUDIO_PATH = "audio/episode_room/girl_music.mp3";

    protected ActionButtonRenderable boyAvatarButton;
    protected ActionButtonRenderable girlAvatarButton;
    protected ActionButtonRenderable boyButton;
    protected ActionButtonRenderable girlButton;
    protected ActionButtonRenderable selectedAvatarButton;
    protected ActionButtonRenderable startGameButton;
    protected ActionButtonRenderable continueGameButton;
    protected ActionButtonRenderable toggleAudioButton;
    protected ActionButtonRenderable aboutButton;
    protected ActionButtonRenderable quitButton;

    protected int countDownValue;
    protected boolean playerSelectionStatus;
    protected boolean inputEnabled;

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id, BG_IMG_PATH);
        inputEnabled = false;
        playerSelectionStatus = false;
        countDownValue = 5;
        topBannerRenderable = new ImageRenderable("topbannerImg", TOP_BANNER_IMG_PATH);
        topBannerRenderable.setVisible(false);
    }

    public void enableInput() { inputEnabled = true; }

    public void disableInput() { inputEnabled = false; }

    public boolean isReadyForInput() { return inputEnabled; }

    public void setBoyAvatarButton(ActionButtonRenderable boyAvatarButton) {
        this.boyAvatarButton = boyAvatarButton;
        this.boyAvatarButton.setVisible(false);
    }

    public ActionButtonRenderable getBoyAvatarButton() {
        return boyAvatarButton;
    }

    public void setGirlAvatarButton(ActionButtonRenderable girlAvatarButton) {
        this.girlAvatarButton = girlAvatarButton;
        this.girlAvatarButton.setVisible(false);
    }

    public ActionButtonRenderable getGirlAvatarButton() {
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
//            topBannerRenderable.setVisible(true);
//            boyAvatarButton.setVisible(true);
//            girlAvatarButton.setVisible(true);
//            boyButton.setVisible(true);
//            girlButton.setVisible(true);
            double fadingEffectsDuration = 1500;

            EffectSequence imgEffect = new EffectSequence();
            imgEffect.addEffect(new FadeEffect(1.0, 0.0, 0.0));
            imgEffect.addEffect(new VisibilityEffect(true));
            imgEffect.addEffect(new FadeEffect(0.0, 0.5, fadingEffectsDuration));
            imgEffect.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    inputEnabled = true;
                }
            }));
            boyAvatarButton.addEffect(imgEffect);
            girlAvatarButton.addEffect(imgEffect);

            imgEffect = new EffectSequence();
            imgEffect.addEffect(new FadeEffect(1.0, 0.0, 0.0));
            imgEffect.addEffect(new VisibilityEffect(true));
            imgEffect.addEffect(new FadeEffect(0.0, 1.0, fadingEffectsDuration));
            topBannerRenderable.addEffect(imgEffect);
            boyButton.addEffect(imgEffect);
            girlButton.addEffect(imgEffect);

            Effect fadeOutEffect = new FadeEffect(1.0, 0.0, fadingEffectsDuration);
            getStartGameButton().addEffect(fadeOutEffect);
            getContinueGameButton().addEffect(fadeOutEffect);
            getToggleAudioButton().addEffect(fadeOutEffect);
            getAboutButton().addEffect(fadeOutEffect);
            getQuitButton().addEffect(fadeOutEffect);

            renderableWasUpdated();
        }
    }

    public ActionButtonRenderable getPlayerButton(String btnName) {
        switch (btnName) {
            case SelectedPlayer.boy:
                return boyButton;
            case SelectedPlayer.girl:
                return girlButton;
            default:
                return null;
        }
    }

    public ActionButtonRenderable getBoyButton() {
        return boyButton;
    }

    public void setBoyButton(ActionButtonRenderable button) {
        this.boyButton = button;
        this.boyButton.setVisible(false);
    }

    public ActionButtonRenderable getGirlButton() {
        return girlButton;
    }

    public void setGirlButton(ActionButtonRenderable button) {
        this.girlButton = button;
        this.girlButton.setVisible(false);
    }

    public ActionButtonRenderable getSelectedAvatarButton() {
        return selectedAvatarButton;
    }

    public void setSelectedAvatarButton(ActionButtonRenderable selectedAvatarButton) {
        this.selectedAvatarButton = selectedAvatarButton;
        renderableWasUpdated();
    }

    public ActionButtonRenderable getStartGameButton() {
        return startGameButton;
    }

    public void setStartGameButton(ActionButtonRenderable button) {
        this.startGameButton = button;
    }

    public ActionButtonRenderable getContinueGameButton() {
        return continueGameButton;
    }

    public void setContinueGameButton(ActionButtonRenderable button) {
        this.continueGameButton = button;
    }

    public ActionButtonRenderable getToggleAudioButton() {
        return toggleAudioButton;
    }

    public void setToggleAudioButton(ActionButtonRenderable button) {
        this.toggleAudioButton = button;
    }

    public ActionButtonRenderable getAboutButton() {
        return aboutButton;
    }

    public void setAboutButton(ActionButtonRenderable button) {
        this.aboutButton = button;
    }

    public ActionButtonRenderable getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(ActionButtonRenderable button) {
        this.quitButton = button;
    }
}
