package org.scify.moonwalker.app.ui.renderables;

import org.scify.moonwalker.app.game.SelectedPlayer;

public class MoonLandingRenderable extends ChattableRenderable {

    // the following two static variables need to be set BEFORE the Class instance
    // is created, so that the Renderable constructor can initialise the corresponding images.
    public static String IMG_PATH = "img/episode_moon_landing/";
    public static String BG_IMG_PATH;
    public static String AUDIO_PATH = "audio/episode_moon_landing/";
    public static String CONVERSATION_BG_IMG_PATH;
    public static String AUDIO_BG_PATH;
    //Audio
//    public final static String FOREST_AUDIO_PATH = "audio/episode_forest/bg.mp3";
//    public final static String BORING_MUSIC_AUDIO_PATH = "audio/episode_forest/boring_music.mp3";
//    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";
//    public final static String MAINMENU_AUDIO_PATH = "audio/episode_main_menu/bg.mp3";

    public MoonLandingRenderable(float xPos, float yPos, float width, float height, String id, String playerSex, boolean isGameFullySuccessfullyCompleted) {
        super(xPos, yPos, width, height, id, id, BG_IMG_PATH);

    }

    public static void calculateResPaths(String playerSex, boolean isGameFullySuccessfullyCompleted) {
        IMG_PATH += isGameFullySuccessfullyCompleted ? "full_success/" : "simple_success/";
        BG_IMG_PATH = playerSex.equals(SelectedPlayer.boy) ? MoonLandingRenderable.IMG_PATH + "boy_bg.png" : MoonLandingRenderable.IMG_PATH + "girl_bg.png";
        CONVERSATION_BG_IMG_PATH = IMG_PATH + "conversation_bg.png";
        AUDIO_PATH += isGameFullySuccessfullyCompleted ? "full_success/" : "simple_success/";
        AUDIO_BG_PATH = AUDIO_PATH + "bg.mp3";
    }

}
