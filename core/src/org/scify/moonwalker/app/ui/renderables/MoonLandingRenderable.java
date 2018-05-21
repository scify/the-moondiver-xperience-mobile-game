package org.scify.moonwalker.app.ui.renderables;

public class MoonLandingRenderable extends ChattableRenderable {

    // the following two static variables need to be set BEFORE the Class instance
    // is created, so that the Renderable constructor can initialise the corresponding images.
    public static String IMG_PATH = "img/episode_moon_landing/";
    public static String BG_IMG_PATH;
    public static String AUDIO_PATH = "audio/episode_moon_landing/";
    public static String CONVERSATION_BG_IMG_PATH;
    public static String AUDIO_BG;
    //Audio

    public MoonLandingRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, id, id, BG_IMG_PATH);
        CONVERSATION_BG_IMG_PATH = IMG_PATH + "conversation_bg.png";
        AUDIO_BG = AUDIO_PATH + "bg.mp3";
    }

    public String calculateBgImgPath() {
        return null;
    }

}
