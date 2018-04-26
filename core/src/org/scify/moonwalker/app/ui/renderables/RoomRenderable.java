package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

public class RoomRenderable extends Renderable {

    protected ImageRenderable tableBGRenderable;
    protected ImageRenderable phoneOffRenderable;
    protected ImageRenderable phoneOnRenderable;

    //IMAGES
    public final String BG_IMG_PATH = "img/episode_room/bg.png";
    public final String PHONE_OFF_IMG_PATH = "img/episode_room/phoneMusic.png";
    public final String PHONE_ON_IMG_PATH = "img/episode_room/phoneCalling.png";

    //AUDIO
    public final String BOY_MUSIC_AUDIO_PATH = "audio/room_episode/boy/music.mp3";
    public final String GIRL_MUSIC_AUDIO_PATH = "audio/room_episode/girl/music.mp3";
    public final String MOBILE_AUDIO_PATH = "audio/message.mp3";

    protected boolean permanentlyOn;
    protected boolean chatEnabled;


    public RoomRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, "room", id);
        permanentlyOn = false;
        chatEnabled = false;
        tableBGRenderable = new ImageRenderable("bg", BG_IMG_PATH);
        phoneOnRenderable = new ImageRenderable("phone_on", PHONE_ON_IMG_PATH);
        phoneOnRenderable.setZIndex(1);
        phoneOnRenderable.setVisible(false);
        phoneOffRenderable = new ImageRenderable("phone_off", PHONE_OFF_IMG_PATH);
    }

    public void enableChat() {
        chatEnabled = true;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void togglePhone() {
        if (!permanentlyOn) {
            if (phoneOnRenderable.isVisible())
                phoneOnRenderable.setVisible(false);
            else
                phoneOnRenderable.setVisible(true);
            renderableWasUpdated();
        }
    }

    public void turnOnPhone() {
        permanentlyOn = true;
        phoneOnRenderable.setVisible(true);
        renderableWasUpdated();
    }

    public void turnOffPhone() {
        phoneOnRenderable.setVisible(false);
        renderableWasUpdated();
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public ImageRenderable getPhoneOffRenderable() {
        return phoneOffRenderable;
    }

    public ImageRenderable getPhoneOnRenderable() {
        return phoneOnRenderable;
    }

}
