package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.Renderable;

public class RoomRenderable extends Renderable {

    public final String PHONE_OFF_IMG_PATH = "img/episode_room/phoneMusic.png";
    public final String PHONE_ON_IMG_PATH = "img/episode_room/phoneCalling.png";
    protected boolean permanentlyOn;

    protected String phoneImagePath;

    public RoomRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        permanentlyOn = false;
        phoneImagePath = PHONE_OFF_IMG_PATH;
        setImgPath("img/episode_room/bg.png");
    }

    public String getPhoneImagePath() {
        return phoneImagePath;
    }

    public void setPhoneImagePathValue(String imagePath) {
        phoneImagePath = imagePath;
        renderableWasUpdated();
    }

    public void togglePhone() {
        if (!permanentlyOn) {
            if (phoneImagePath.equals(PHONE_ON_IMG_PATH))
                setPhoneImagePathValue(PHONE_OFF_IMG_PATH);
            else
                setPhoneImagePathValue(PHONE_ON_IMG_PATH);
        }
    }

    public void turnOnPhone () {
        permanentlyOn = true;
        setPhoneImagePathValue(PHONE_ON_IMG_PATH);
    }

    public void turnOffPhone () {
        setPhoneImagePathValue(PHONE_OFF_IMG_PATH);
    }

}
