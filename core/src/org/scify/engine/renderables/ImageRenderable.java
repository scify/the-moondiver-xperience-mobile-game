package org.scify.engine.renderables;

public class ImageRenderable extends Renderable {
    protected boolean imgPathWasUpdated;

    public ImageRenderable(float xPos, float yPos, float width, float height, String id, String sImgPath) {
        super(xPos, yPos, width, height, ACTOR_IMAGE, id);
        imgPath = sImgPath;
        imgPathWasUpdated = false;
    }

    public ImageRenderable(String id, String sImgPath) {
        super(ACTOR_IMAGE, id);
        imgPath = sImgPath;
        imgPathWasUpdated = false;
    }

    @Override
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPathWasUpdated(boolean value) {
        imgPathWasUpdated = value;
    }

    public boolean getImgPathWasUpdated() {
        return imgPathWasUpdated;
    }
}
