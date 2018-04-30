package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;

public class ForestRenderable extends FadingTableRenderable {

    public final static String BG_IMG_PATH = "img/episode_forest/bg.png";
    public final static String FOREST_AUDIO_PATH = "audio/episode_forest/bg.mp3";
    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";

    protected boolean chatEnabled;

    public ForestRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "forest", id, BG_IMG_PATH);
        chatEnabled = false;
        setAfterFadeIn(new Runnable() {
            @Override
            public void run() {
                chatEnabled = true;
            }
        });
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

}
