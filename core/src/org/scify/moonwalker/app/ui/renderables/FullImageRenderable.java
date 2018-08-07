package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;

public class FullImageRenderable extends FadingTableRenderable {

    protected ImageRenderable imageBGRenderable;

    public FullImageRenderable(float xPos, float yPos, float width, float height, String type, String id, String imgPath) {
        super(xPos, yPos, width, height, type, id, imgPath);
        imageBGRenderable = createImageRenderable(id, imgPath, false, true, 1);
    }
}
