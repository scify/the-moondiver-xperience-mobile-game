package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

import java.util.LinkedList;
import java.util.List;

public class MainMenuRenderable extends Renderable {

    protected ActionButton boySelectionButton;
    protected ActionButton girlSelectionButton;
    protected ActionButton selectedAvatar;
    protected ActionButton startGameButton;
    protected ActionButton continueGameButton;
    protected ActionButton toggleAudioButton;
    protected ActionButton aboutButton;
    protected ActionButton quitButton;

    public MainMenuRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "main_menu", id);
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

    public ActionButton getSelectedAvatar() { return selectedAvatar; }

    public void setSelectedAvatar(ActionButton selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
        renderableWasUpdated();
    }

    public ActionButton getStartGameButton() { return startGameButton; }

    public void setStartGameButton(ActionButton button) { this.startGameButton = button; }

    public ActionButton getContinueGameButton() { return continueGameButton; }

    public void setContinueGameButton(ActionButton button) { this.continueGameButton = button; }

    public ActionButton getToggleAudioButton() { return toggleAudioButton; }

    public void setToggleAudioButton(ActionButton button) { this.toggleAudioButton = button; }

    public ActionButton getAboutButton() { return aboutButton; }

    public void setAboutButton(ActionButton button) { this.aboutButton = button; }

    public ActionButton getQuitButton() { return quitButton; }

    public void setQuitButton(ActionButton button) { this.quitButton = button; }
}
