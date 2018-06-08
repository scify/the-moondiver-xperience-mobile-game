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

    public MoonLandingRenderable(float xPos, float yPos, float width, float height, String id, String playerSex, boolean isGameFullySuccessfullyCompleted) {
        super(xPos, yPos, width, height, id, id, BG_IMG_PATH);

    }

    public static void calculateResPaths(String playerSex, boolean isGameFullySuccessfullyCompleted) {
        String imagePath = IMG_PATH + (isGameFullySuccessfullyCompleted ? "full_success/" : "simple_success/");
        BG_IMG_PATH = playerSex.equals(SelectedPlayer.boy) ? imagePath + "boy_bg.png" : imagePath + "girl_bg.png";
        CONVERSATION_BG_IMG_PATH = imagePath + "conversation_bg.png";
        String audioPath = AUDIO_PATH + (isGameFullySuccessfullyCompleted ? "full_success/" : "simple_success/");
        AUDIO_BG_PATH = audioPath + "bg.mp3";
    }

}
