package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.ui.ThemeController;

public class ActionButtonRenderable extends Renderable {

    protected String title;
    protected UserAction userAction;
    protected float padding;
    protected String imgPath;
    protected String skin;

    public ActionButtonRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        skin = ThemeController.SKIN_DEFAULT;
    }

    public ActionButtonRenderable(String type, String id) {
        super(type, id);
        skin = ThemeController.SKIN_DEFAULT;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonSkin() {
        return skin;
    }

    public void setButtonSkin(String skin) {
        this.skin = skin;
        markAsNeedsUpdate();
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }
}
