package org.scify.engine.renderables;

public class TableRenderable extends Renderable {
    protected ImageRenderable tableBGRenderable = null;
    protected String bgImgPath;

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        bgImgPath = null;
    }

    public TableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImgPath) {
        super(xPos, yPos, width, height, type, id);
        this.bgImgPath = bgImgPath;

        if (bgImgPath != null) {
            tableBGRenderable = new ImageRenderable("bg", bgImgPath);
        }
    }

    public TableRenderable(String type, String id) {
        super(type, id);
        bgImgPath = null;
    }

    // TODO: Implement add child?
}
