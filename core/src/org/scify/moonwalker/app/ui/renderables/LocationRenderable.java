package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

public class LocationRenderable extends ChattableRenderable {

    public static final String LOCATION_AUDIO_PATH = "audio/episode_location/bg.mp3";
    public static final String CORRECT_AUDIO_PATH = "audio/episode_location/correct.mp3";
    public static final String WRONG_AUDIO_PATH = "audio/episode_location/wrong.mp3";
    public static final String DAYPASSED_AUDIO_PATH = "audio/episode_location/day_passed.mp3";

    public LocationRenderable(float xPos, float yPos, float width, float height, String id, String backgroundImagePath) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_LOCATION, id, backgroundImagePath);
    }
}
