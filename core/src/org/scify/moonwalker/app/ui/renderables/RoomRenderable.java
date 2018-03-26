package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

public class RoomRenderable extends Renderable {

    public final String PHONE_OFF_IMG_PATH = "img/episode_0/phoneMusic.png";
    public final String PHONE_ON_IMG_PATH = "img/episode_0/phoneCalling.png";

    public RoomRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }
}
