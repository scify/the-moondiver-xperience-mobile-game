package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class RoomRenderable extends FadingTableRenderable {

    protected ImageRenderable phoneOffRenderable;
    protected ImageRenderable phoneOnRenderable;

    //IMAGES BOY
    public final static String BOY_BG_IMG_PATH = "img/episode_room/boy/bg.png";
    public final static String BOY_PHONE_OFF_IMG_PATH = "img/episode_room/boy/phoneMusic.png";
    public final static String BOY_PHONE_ON_IMG_PATH = "img/episode_room/boy/phoneCalling.png";

    //IMAGES GIRL
    public final static String GIRL_BG_IMG_PATH = "img/episode_room/girl/bg.png";
    public final static String GIRL_PHONE_OFF_IMG_PATH = "img/episode_room/girl/phoneMusic.png";
    public final static String GIRL_PHONE_ON_IMG_PATH = "img/episode_room/girl/phoneCalling.png";

    //AUDIO
    public final static String BOY_MUSIC_AUDIO_PATH = "audio/episode_room/boy_music.mp3";
    public final static String GIRL_MUSIC_AUDIO_PATH = "audio/episode_room/girl_music.mp3";
    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";

    protected boolean permanentlyOn;
    protected boolean chatEnabled;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public RoomRenderable(float xPos, float yPos, float width, float height, String id, boolean isBoy) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_ROOM, id, isBoy ? BOY_BG_IMG_PATH : GIRL_BG_IMG_PATH);

        permanentlyOn = false;
        chatEnabled = false;
        if (isBoy) {
            phoneOnRenderable = new ImageRenderable("phone_on", BOY_PHONE_ON_IMG_PATH);
            phoneOffRenderable = new ImageRenderable("phone_off", BOY_PHONE_OFF_IMG_PATH);
        }else {
            phoneOnRenderable = new ImageRenderable("phone_on", GIRL_PHONE_ON_IMG_PATH);
            phoneOffRenderable = new ImageRenderable("phone_off", GIRL_PHONE_OFF_IMG_PATH);
        }
        phoneOnRenderable.setZIndex(2);
        phoneOnRenderable.setPositionDrawable(false);
        phoneOnRenderable.setVisible(false);
        phoneOffRenderable.setZIndex(1);
        phoneOffRenderable.setPositionDrawable(false);
        initSubRenderables();

    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(phoneOnRenderable);
        allRenderables.add(phoneOffRenderable);
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
        }
    }

    public void turnOnPhone() {
        permanentlyOn = true;
        phoneOnRenderable.setVisible(true);
    }

    public void turnOffPhone() {
        phoneOnRenderable.setVisible(false);
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
