package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;
import org.scify.moonwalker.app.game.SelectedPlayer;

import java.util.HashSet;
import java.util.Set;

public class MainMenuRenderable extends FadingTableRenderable {
    //renderable image paths
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

    //renderable ids
    public static final String TOP_BANNER_ID = "top_banner";
    public static final String BOY_BUTTON_ID = "boyButton";
    public static final String BOY_AVATAR_BUTTON_ID = "boyAvatarButton";
    public static final String GIRL_BUTTON_ID = "girlButton";
    public static final String GIRL_AVATAR_BUTTON_ID = "girlAvatarButton";
    public static final String START_BUTTON_ID = "startButton";
    public static final String CONTINUE_BUTTON_ID = "continueButton";
    public static final String TOGGLE_AUDIO_BUTTON_ID = "toggleAudioButton";
    public static final String ABOUT_BUTTON_ID = "aboutButton";
    public static final String QUIT_BUTOON_ID = "quitButton";
    public static final String COUNTDOWN_LABEL_ID = "countDownLabel";

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

    protected ImageRenderable topBannerRenderable;
    protected TextLabelRenderable countDownLabel;

    protected int countDownValue;
    protected boolean playerSelectionStatus;
    protected boolean inputEnabled;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id, BG_IMG_PATH);
        inputEnabled = false;
        playerSelectionStatus = false;
        countDownValue = 5;
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        topBannerRenderable = new ImageRenderable(TOP_BANNER_ID, TOP_BANNER_IMG_PATH);
        topBannerRenderable.setVisible(false);
        topBannerRenderable.setPositionDrawable(false);
        topBannerRenderable.setZIndex(1);
        allRenderables.add(topBannerRenderable);

        boyButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, BOY_BUTTON_ID);
        boyButton.setUserAction(new UserAction(UserActionCode.BOY_SELECTED));
        boyButton.setImgPath(BOY_BUTTON_IMG_PATH);
        boyButton.setVisible(false);
        boyButton.setPositionDrawable(false);
        boyButton.setZIndex(1);
        allRenderables.add(boyButton);

        boyAvatarButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, BOY_AVATAR_BUTTON_ID);
        boyAvatarButton.setUserAction(new UserAction(UserActionCode.BOY_SELECTED));
        boyAvatarButton.setImgPath(BOY_IMG_PATH);
        boyAvatarButton.setVisible(false);
        boyAvatarButton.setPositionDrawable(false);
        boyAvatarButton.setZIndex(1);
        allRenderables.add(boyAvatarButton);

        girlButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, GIRL_BUTTON_ID);
        girlButton.setUserAction(new UserAction(UserActionCode.GIRL_SELECTED));
        girlButton.setImgPath(GIRL_BUTTON_IMG_PATH);
        girlButton.setVisible(false);
        girlButton.setPositionDrawable(false);
        girlButton.setZIndex(1);
        allRenderables.add(girlButton);

        girlAvatarButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, GIRL_AVATAR_BUTTON_ID);
        girlAvatarButton.setUserAction(new UserAction(UserActionCode.GIRL_SELECTED));
        girlAvatarButton.setImgPath(GIRL_IMG_PATH);
        girlAvatarButton.setVisible(false);
        girlAvatarButton.setPositionDrawable(false);
        girlAvatarButton.setZIndex(1);
        allRenderables.add(girlAvatarButton);

        startGameButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, START_BUTTON_ID);
        startGameButton.setImgPath(START_BUTTON_IMG_PATH);
        startGameButton.setUserAction(new UserAction(UserActionCode.NEW_GAME));
        startGameButton.setPositionDrawable(false);
        startGameButton.setZIndex(1);
        allRenderables.add(startGameButton);

        continueGameButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, CONTINUE_BUTTON_ID);
        continueGameButton.setImgPath(CONTINUE_BUTTON_IMG_PATH);
        continueGameButton.setUserAction(new UserAction(UserActionCode.CONTINUE));
        continueGameButton.setPositionDrawable(false);
        continueGameButton.setZIndex(1);
        allRenderables.add(continueGameButton);

        toggleAudioButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, TOGGLE_AUDIO_BUTTON_ID);
        toggleAudioButton.setImgPath(TOGGLE_AUDIO_BUTTON_IMG_PATH);
        toggleAudioButton.setUserAction(new UserAction(UserActionCode.TOGGLE_AUDIO));
        toggleAudioButton.setPositionDrawable(false);
        toggleAudioButton.setZIndex(1);
        allRenderables.add(toggleAudioButton);

        aboutButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, ABOUT_BUTTON_ID);
        aboutButton.setImgPath(ABOUT_BUTTON_IMG_PATH);
        aboutButton.setUserAction(new UserAction(UserActionCode.ABOUT));
        aboutButton.setPositionDrawable(false);
        aboutButton.setZIndex(1);
        allRenderables.add(aboutButton);

        quitButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, QUIT_BUTOON_ID);
        quitButton.setImgPath(QUIT_BUTTON_IMG_PATH);
        quitButton.setUserAction(new UserAction(UserActionCode.QUIT));
        quitButton.setPositionDrawable(false);
        quitButton.setZIndex(1);
        allRenderables.add(quitButton);

        countDownLabel = new TextLabelRenderable(Renderable.ACTOR_LABEL, COUNTDOWN_LABEL_ID);
        countDownLabel.setLabel(countDownValue + "");
        countDownLabel.setZIndex(1);
        countDownLabel.setVisible(false);
        countDownLabel.setPositionDrawable(false);
        allRenderables.add(countDownLabel);
    }

    public void enableInput() {
        inputEnabled = true;
    }

    public void disableInput() {
        inputEnabled = false;
    }

    public boolean isReadyForInput() {
        return inputEnabled;
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

    public ActionButtonRenderable getContinueGameButton() { return continueGameButton; }

    public ActionButtonRenderable getToggleAudioButton() {
        return toggleAudioButton;
    }

    public ActionButtonRenderable getAboutButton() {
        return aboutButton;
    }

    public ActionButtonRenderable getQuitButton() {
        return quitButton;
    }

    public ActionButtonRenderable getBoyButton() {
        return boyButton;
    }

    public ActionButtonRenderable getBoyAvatarButton() {
        return boyAvatarButton;
    }

    public ActionButtonRenderable getGirlButton() {
        return girlButton;
    }

    public ActionButtonRenderable getGirlAvatarButton() {
        return girlAvatarButton;
    }

    public TextLabelRenderable getCountDownLabel() { return countDownLabel; }
}
