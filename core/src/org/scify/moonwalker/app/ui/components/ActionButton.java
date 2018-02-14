package org.scify.moonwalker.app.ui.components;

import org.scify.engine.Renderable;
import org.scify.engine.UserAction;

public class ActionButton extends Renderable {

    protected String title;
    protected UserAction userAction;
    protected float padding;
    protected String imgPath;

    public ActionButton(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
