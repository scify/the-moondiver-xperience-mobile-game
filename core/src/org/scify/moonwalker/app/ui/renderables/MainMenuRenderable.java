package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.BaseEffect;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectList;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.libgdx.FadeLGDXEffect;
import org.scify.engine.renderables.effects.libgdx.LGDXEffectList;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MainMenuRenderable extends Renderable {

    public final String TOP_BANNER_IMG_PATH = "img/mainMenu/top.png";
    public final String BOY_IMG_PATH = "img/mainMenu/boy.png";
    public final String GIRL_IMG_PATH = "img/mainMenu/girl.png";

    protected ActionButton boySelectionButton;
    protected ActionButton girlSelectionButton;

    protected ImageRenderable BoyImage;
    protected ImageRenderable GirlImage;
    protected ImageRenderable TopBanner;

    protected ActionButton selectedAvatar;
    protected ActionButton startGameButton;
    protected ActionButton continueGameButton;
    protected ActionButton toggleAudioButton;
    protected ActionButton aboutButton;
    protected ActionButton quitButton;

    protected int countDownValue;

    protected boolean playerSelectionStatus;

    public ImageRenderable getBoyImage() {
        return BoyImage;
    }

    public ImageRenderable getGirlImage() {
        return GirlImage;
    }

    public ImageRenderable getTopBanner() {
        return TopBanner;
    }

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id);
        playerSelectionStatus = false;
        countDownValue = 5;

        BoyImage = new ImageRenderable("boyImg", BOY_IMG_PATH);
//        BoyImage.setVisible(false);

        GirlImage = new ImageRenderable("girlImg", GIRL_IMG_PATH);
//        GirlImage.setVisible(false);

        TopBanner = new ImageRenderable("topbannerImg", TOP_BANNER_IMG_PATH);
    }

    public void resetCountDown () {
        countDownValue = 5;
        renderableWasUpdated();
    }

    public void decreaseCountDown () {
        countDownValue --;
        if (countDownValue >= 0)
            renderableWasUpdated();
    }

    public int getCountDownValue () { return countDownValue;}

    public boolean isPlayerSelectionInitiated() {
        return playerSelectionStatus;
    }

    public void initiatePlayerSelection() {
        if (playerSelectionStatus == false) {
            playerSelectionStatus = true;
            selectedAvatar = null;
            // ggianna
            LGDXEffectList imgEffect = new LGDXEffectList();
            imgEffect.addEffect(new FadeLGDXEffect(0.0, 1.0, 2000));
            imgEffect.addEffect(new FunctionEffect(new Runnable() {
                @Override
                public void run() {
                    BoyImage.setVisible(false);
                    System.err.println("Visibility off!");
                }
            }));
            BoyImage.apply(imgEffect);
            GirlImage.apply(imgEffect);

            Effect effect = new FadeLGDXEffect(1.0, 0.0, 2000);
            getStartGameButton().apply(effect);
            getContinueGameButton().apply(effect);
            getToggleAudioButton().apply(effect);
            getAboutButton().apply(effect);
            getQuitButton().apply(effect);
            //////////

            renderableWasUpdated();
        }
    }

    public ActionButton getBoySelectionButton() {
        return boySelectionButton;
    }

    public void setBoySelectionButton(ActionButton button) {
        this.boySelectionButton = button;
    }

    public ActionButton getGirlSelectionButton() {
        return girlSelectionButton;
    }

    public void setGirlSelectionButton(ActionButton button) {
        this.girlSelectionButton = button;
    }

    public ActionButton getSelectedAvatar() {
        return selectedAvatar;
    }

    public void setSelectedAvatar(ActionButton selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
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
