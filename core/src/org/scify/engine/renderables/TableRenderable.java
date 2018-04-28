package org.scify.engine.renderables;

public class TableRenderable extends Renderable {
    protected String bgImgPath;

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        bgImgPath = null;
    }

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImgPath) {
        super(xPos, yPos, width, height, type, id);
        this.bgImgPath = bgImgPath;
    }

    public TableRenderable(String type, String id) {
        super(type, id);
        bgImgPath = null;
    }

    // TODO: Implement add child?
}
