package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.TextLabelRenderable;
import org.scify.engine.renderables.effects.*;

import java.util.HashSet;
import java.util.Set;

public class MainMenuRenderable extends FadingTableRenderable {
    //renderable image paths
    protected static final String BG_IMG_PATH = "img/episode_mainMenu/bg.png";
    protected static final String TOP_BANNER_IMG_PATH = "img/episode_mainMenu/top.png";
    protected static final String BOY_IMG_PATH = "img/episode_mainMenu/boy.png";
    protected static final String GIRL_IMG_PATH = "img/episode_mainMenu/girl.png";
    protected static final String BOY_BUTTON_IMG_PATH = "img/episode_mainMenu/boyButton.png";
    protected static final String GIRL_BUTTON_IMG_PATH = "img/episode_mainMenu/girlButton.png";
    protected static final String START_BUTTON_IMG_PATH = "img/episode_mainMenu/start.png";
    protected static final String CONTINUE_BUTTON_IMG_PATH = "img/episode_mainMenu/continue.png";
    protected static final String TOGGLE_AUDIO_BUTTON_IMG_PATH = "img/episode_mainMenu/toggleAudio.png";
    protected static final String ABOUT_BUTTON_IMG_PATH = "img/episode_mainMenu/about.png";
    protected static final String QUIT_BUTTON_IMG_PATH = "img/episode_mainMenu/quit.png";

    //renderable ids
    protected static final String TOP_BANNER_ID = "top_banner";
    protected static final String BOY_BUTTON_ID = "boyButton";
    protected static final String BOY_AVATAR_BUTTON_ID = "boyAvatarButton";
    protected static final String GIRL_BUTTON_ID = "girlButton";
    protected static final String GIRL_AVATAR_BUTTON_ID = "girlAvatarButton";
    protected static final String START_BUTTON_ID = "startButton";
    protected static final String CONTINUE_BUTTON_ID = "continueButton";
    protected static final String TOGGLE_AUDIO_BUTTON_ID = "toggleAudioButton";
    protected static final String ABOUT_BUTTON_ID = "aboutButton";
    protected static final String QUIT_BUTOON_ID = "quitButton";
    protected static final String COUNTDOWN_LABEL_ID = "countDownLabel";

    //AUDIO
    public static final String MAINMENU_AUDIO_PATH = "audio/episode_mainMenu/bg.mp3";
    public static final String CLICK_AUDIO_PATH = "audio/button1.mp3";
    //AUDIO BUFFERING FOR NEXT EPISODE
    public static final String BOY_MUSIC_AUDIO_PATH = "audio/episode_room/boy_music.mp3";
    public static final String GIRL_MUSIC_AUDIO_PATH = "audio/episode_room/girl_music.mp3";

    protected ActionButtonRenderable boyAvatarButton;
    protected ActionButtonRenderable girlAvatarButton;
    protected ActionButtonRenderable boyButton;
    protected ActionButtonRenderable girlButton;
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
    protected String selectedPlayer = "";

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_MAIN_MENU, id, BG_IMG_PATH);
        inputEnabled = false;
        playerSelectionStatus = false;
        countDownValue = 5;
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        topBannerRenderable = createImageRenderable(TOP_BANNER_ID, TOP_BANNER_IMG_PATH, false, false, 1);
        allRenderables.add(topBannerRenderable);

        startGameButton = createImageButton(START_BUTTON_ID, START_BUTTON_IMG_PATH, new UserAction(UserActionCode.NEW_GAME), false, true, 1);
        allRenderables.add(startGameButton);

        continueGameButton = createImageButton(CONTINUE_BUTTON_ID, CONTINUE_BUTTON_IMG_PATH, new UserAction(UserActionCode.CONTINUE), false, true, 1);
        allRenderables.add(continueGameButton);

        toggleAudioButton = createImageButton(TOGGLE_AUDIO_BUTTON_ID, TOGGLE_AUDIO_BUTTON_IMG_PATH, new UserAction(UserActionCode.TOGGLE_AUDIO), false, true, 1);
        allRenderables.add(toggleAudioButton);

        aboutButton = createImageButton(ABOUT_BUTTON_ID, ABOUT_BUTTON_IMG_PATH, new UserAction(UserActionCode.ABOUT), false, true, 1);
        allRenderables.add(aboutButton);

        quitButton = createImageButton(QUIT_BUTOON_ID, QUIT_BUTTON_IMG_PATH, new UserAction(UserActionCode.QUIT), false, true, 1);
        allRenderables.add(quitButton);

        UserAction boySelectedUserAction = new UserAction(UserActionCode.BOY_SELECTED);
        boyButton = createImageButton(BOY_BUTTON_ID, BOY_BUTTON_IMG_PATH, boySelectedUserAction, false, false, 1);
        allRenderables.add(boyButton);

        boyAvatarButton = createImageButton(BOY_AVATAR_BUTTON_ID, BOY_IMG_PATH, boySelectedUserAction, false, false, 1);
        allRenderables.add(boyAvatarButton);

        UserAction girlSelectedUserAction = new UserAction(UserActionCode.GIRL_SELECTED);
        girlButton = createImageButton(GIRL_BUTTON_ID, GIRL_BUTTON_IMG_PATH, girlSelectedUserAction, false, false, 1);
        allRenderables.add(girlButton);

        girlAvatarButton = createImageButton(GIRL_AVATAR_BUTTON_ID, GIRL_IMG_PATH, girlSelectedUserAction, false, false, 1);
        allRenderables.add(girlAvatarButton);

        countDownLabel = createTextLabelRenderable(COUNTDOWN_LABEL_ID, countDownValue + "", false, false, 1);
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
        countDownLabel.setLabel(countDownValue + "");
        countDownLabel.setVisible(true);
        markAsNeedsUpdate();
    }

    public void forceCountDownToZero() {
        countDownValue = 0;
        countDownLabel.setLabel(countDownValue + "");
    }

    public void decreaseCountDown() {
        countDownValue--;
        if (countDownValue >= 0) {
            countDownLabel.setLabel(countDownValue + "");
            markAsNeedsUpdate();
        }
    }

    public int getCountDownValue() {
        return countDownValue;
    }

    public void initiatePlayerSelection() {
        if (playerSelectionStatus == false) {
            playerSelectionStatus = true;
            double fadingEffectsDuration = 1500;
            selectedPlayer = "";

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

            markAsNeedsUpdate();
        }
    }

    public String getSelectedPlayer() {
        return selectedPlayer;
    }

    /**
     * 0 none
     * -1 left boy
     * 1 right girl
     *
     * @param selectedPlayer
     */
    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
        markAsNeedsUpdate();
    }

    public ActionButtonRenderable getStartGameButton() {
        return startGameButton;
    }

    public ActionButtonRenderable getContinueGameButton() {
        return continueGameButton;
    }

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

    public TextLabelRenderable getCountDownLabel() {
        return countDownLabel;
    }
}
