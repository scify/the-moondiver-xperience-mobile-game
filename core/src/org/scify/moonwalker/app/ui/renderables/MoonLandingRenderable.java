package org.scify.moonwalker.app.ui.renderables;

public class MoonLandingRenderable extends ChattableRenderable {

    // the following two static variables need to be set BEFORE the Class instance
    // is created.
    public static String IMG_PATH = "img/episode_final/";
    public static String BG_IMG_PATH;

    public static String CONVERSATION_BG_IMG_PATH;
    //Audio
//    public final static String FOREST_AUDIO_PATH = "audio/episode_forest/bg.mp3";
//    public final static String BORING_MUSIC_AUDIO_PATH = "audio/episode_forest/boring_music.mp3";
//    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";
//    public final static String MAINMENU_AUDIO_PATH = "audio/episode_main_menu/bg.mp3";



    public MoonLandingRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, id, id, BG_IMG_PATH);
    }



}
