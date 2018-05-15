package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

public class LocationRenderable extends FadingTableRenderable {
    public final static String BERLIN_BG_IMG_PATH = "img/episode_forest/bg.png";

    protected boolean chatEnabled;

    public LocationRenderable(float xPos, float yPos, float width, float height, String id, String backgroundImagePath) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_LOCATION, id, backgroundImagePath);
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
