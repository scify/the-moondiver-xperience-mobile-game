package org.scify.engine.renderables;

public class ImageRenderable extends Renderable {
    protected String imgPath;

    public ImageRenderable(float xPos, float yPos, float width, float height, String id, String sImgPath) {
        super(xPos, yPos, width, height, "image", id);
        imgPath = sImgPath;
    }

    public ImageRenderable(String id, String sImgPath) {
        super("image", id);
        imgPath = sImgPath;
    }

    @Override
    public String getImgPath() {
        return imgPath;
    }
}
