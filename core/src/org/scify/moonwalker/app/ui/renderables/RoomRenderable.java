package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

public class RoomRenderable extends Renderable {

    public final String PHONE_OFF_IMG_PATH = "img/episode_room/phoneMusic.png";
    public final String PHONE_ON_IMG_PATH = "img/episode_room/phoneCalling.png";

    protected String phoneImagePath;

    public RoomRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
    }

    public String getPhoneImagePath() {
        return phoneImagePath;
    }

    public void setPhoneImagePathValue(String imagePath) {
        phoneImagePath = imagePath;
        renderableWasUpdated();
    }

    public void turnOnPhone () {
        setPhoneImagePathValue(PHONE_ON_IMG_PATH);
    }

    public void turnOffPhone () {
        setPhoneImagePathValue(PHONE_OFF_IMG_PATH);
    }

}
