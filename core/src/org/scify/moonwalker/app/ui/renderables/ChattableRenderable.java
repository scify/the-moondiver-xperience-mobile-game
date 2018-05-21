package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;

public abstract class ChattableRenderable extends FadingTableRenderable{

    protected boolean chatEnabled;

    public ChattableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath) {
        super(xPos, yPos, width, height, type, id, bgImagePath);
        chatEnabled = false;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void enableChat () {
        chatEnabled = true;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }
}
