package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class MainMenuRenderable extends Renderable {

    public final String TOP_BANNER_IMG_PATH = "img/mainMenu/top.png";
    public final String BOY_IMG_PATH = "img/mainMenu/boy.png";
    public final String GIRL_IMG_PATH = "img/mainMenu/girl.png";

    protected ActionButton boySelectionButton;
    protected ActionButton girlSelectionButton;
    protected ActionButton selectedAvatar;
    protected ActionButton startGameButton;
    protected ActionButton continueGameButton;
    protected ActionButton toggleAudioButton;
    protected ActionButton aboutButton;
    protected ActionButton quitButton;

    protected boolean playerSelectionStatus;

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id);
        playerSelectionStatus = false;
    }

    public boolean isPlayerSelectionInitiated() {
        return playerSelectionStatus;
    }

    public void initiatePlayerSelection() {
        if (playerSelectionStatus == false) {
            playerSelectionStatus = true;
            selectedAvatar = null;
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
