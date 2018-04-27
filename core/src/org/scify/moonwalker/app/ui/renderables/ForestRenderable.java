package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

public class ForestRenderable extends Renderable {
    protected ImageRenderable tableBGRenderable;

    public final String BG_IMG_PATH = "img/episode_forest/bg.png";
    public final String MOBILE_AUDIO_PATH = "audio/message.mp3";

    protected boolean chatEnabled;

    public ForestRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "forest", id);
        chatEnabled = false;
        tableBGRenderable = new ImageRenderable("bg", BG_IMG_PATH);
    }

    public void enableChat() {
        chatEnabled = true;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }
}
