package org.scify.engine.renderables;

import org.scify.engine.UserAction;

public class TextLabelRenderable extends Renderable {

    protected String label;
    protected UserAction userAction;
    protected float padding;
    protected String imgPath;

    public TextLabelRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public TextLabelRenderable(String type, String id) {
        super(type, id);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
