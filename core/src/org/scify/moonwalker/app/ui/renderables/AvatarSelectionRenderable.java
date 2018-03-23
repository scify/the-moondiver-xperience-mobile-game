package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

public class AvatarSelectionRenderable extends Renderable {

    protected ActionButton boySelection;
    protected ActionButton girlSelection;
    protected ActionButton selectBtn;
    protected ActionButton selectedAvatar;

    public AvatarSelectionRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "avatar_selection", id);
    }

    public ActionButton getBoySelection() {
        return boySelection;
    }

    public void setBoySelection(ActionButton boySelection) {
        this.boySelection = boySelection;
    }

    public ActionButton getGirlSelection() {
        return girlSelection;
    }

    public void setGirlSelection(ActionButton girlSelection) {
        this.girlSelection = girlSelection;
    }

    public ActionButton getSelectBtn() {
        return selectBtn;
    }

    public void setSelectBtn(ActionButton selectBtn) {
        this.selectBtn = selectBtn;
    }

    public ActionButton getSelectedAvatar() {
        return selectedAvatar;
    }

    public void setSelectedAvatar(ActionButton selectedAvatar) {
        this.selectedAvatar = selectedAvatar;
        renderableWasUpdated();
    }
}
