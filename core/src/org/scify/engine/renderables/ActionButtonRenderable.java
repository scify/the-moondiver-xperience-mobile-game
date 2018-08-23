package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;

public class ActionButtonRenderable extends Renderable {

    protected String title;
    protected UserAction userAction;
    protected float padding;
    protected String imgPath;
    protected boolean defaultButtonSkin;

    public ActionButtonRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        defaultButtonSkin = true;
    }

    public ActionButtonRenderable(String type, String id) {
        super(type, id);
        defaultButtonSkin = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDefaultButtonSkin() {
        return defaultButtonSkin;
    }

    public void setDefaultButtonSkin(boolean defaultButtonSkin) {
        this.defaultButtonSkin = defaultButtonSkin;
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
