package org.scify.engine.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.renderables.Renderable;

public class TextLabel extends Renderable {

    protected String label;
    protected UserAction userAction;
    protected float padding;
    protected String imgPath;

    public TextLabel(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public TextLabel(String type, String id) {
        super(type, id);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
