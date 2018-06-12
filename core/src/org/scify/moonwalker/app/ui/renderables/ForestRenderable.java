package org.scify.moonwalker.app.ui.renderables;

public class ForestRenderable extends ChattableRenderable {

    //Images
    protected final static String IMG_PATH = "img/episode_forest/";
    public final static String BG_IMG_PATH = IMG_PATH + "bg.png";
    public final static String CONVERSATION_BG_IMG_PATH = IMG_PATH + "conversation_bg.png";
    //Audio
    public final static String FOREST_AUDIO_PATH = "audio/episode_forest/bg.mp3";
    public final static String BORING_MUSIC_AUDIO_PATH = "audio/episode_forest/boring_music.mp3";

    public ForestRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_FOREST, id, BG_IMG_PATH);
        chatEnabled = false;
    }
}
