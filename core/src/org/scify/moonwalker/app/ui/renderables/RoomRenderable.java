package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class RoomRenderable extends FadingTableRenderable {

    protected ImageRenderable phoneOnRenderable;
    protected ImageRenderable phoneOffRenderable;

    //IMAGES BOY
    protected final static String BOY_IMG_PATH = "img/episode_room/boy/";
    public final static String BOY_BG_IMG_PATH = BOY_IMG_PATH + "bg.png";
    public final static String BOY_PHONE_IMG_PATH = BOY_IMG_PATH + "phone.png";
    public final static String BOY_PHONE_MESSAGE_IMG_PATH = BOY_IMG_PATH + "phoneMessage.png";
    public final static String BOY_CONVERSATION_BG_IMG_PATH = BOY_IMG_PATH + "conversation_bg.png";

    //IMAGES GIRL
    protected final static String GIRL_IMG_PATH = "img/episode_room/girl/";
    public final static String GIRL_BG_IMG_PATH = GIRL_IMG_PATH + "bg.png";
    public final static String GIRL_PHONE_IMG_PATH = GIRL_IMG_PATH + "phone.png";
    public final static String GIRL_PHONE_MESSAGE_IMG_PATH = GIRL_IMG_PATH + "phoneMessage.png";
    public final static String GIRL_CONVERSATION_BG_IMG_PATH = GIRL_IMG_PATH + "conversation_bg.png";

    //AUDIO
    public final static String BOY_MUSIC_AUDIO_PATH = "audio/episode_room/boy_music.mp3";
    public final static String GIRL_MUSIC_AUDIO_PATH = "audio/episode_room/girl_music.mp3";
    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";

    public final static String FOREST_AUDIO_PATH = "audio/episode_forest/bg.mp3";

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
            phoneOnRenderable = new ImageRenderable("phone_on", BOY_PHONE_MESSAGE_IMG_PATH);
            phoneOffRenderable = new ImageRenderable("phone_off", BOY_PHONE_IMG_PATH);
        }else {
            phoneOnRenderable = new ImageRenderable("phone_on", GIRL_PHONE_MESSAGE_IMG_PATH);
            phoneOffRenderable = new ImageRenderable("phone_off", GIRL_PHONE_IMG_PATH);
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
