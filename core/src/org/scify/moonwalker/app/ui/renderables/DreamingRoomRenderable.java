package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class DreamingRoomRenderable extends ChattableRenderable {

    protected ImageRenderable mobileRenderable;
    protected ImageRenderable eyesRenderable;

    //IMAGES BOY
    protected final static String BOY_IMG_PATH = "img/episode_dreaming_room/boy/";
    public final static String BOY_BG_IMG_PATH = BOY_IMG_PATH + "bg.png";
    public final static String BOY_MOBILE_IMG_PATH = BOY_IMG_PATH + "mobile.png";
    public final static String BOY_EYES_IMG_PATH = BOY_IMG_PATH + "bg2.png";
    public final static String BOY_CONVERSATION_BG_IMG_PATH = BOY_IMG_PATH + "conversation_bg.png";

    //IMAGES GIRL
    protected final static String GIRL_IMG_PATH = "img/episode_dreaming_room/girl/";
    public final static String GIRL_BG_IMG_PATH = GIRL_IMG_PATH + "bg.png";
    public final static String GIRL_MOBILE_IMG_PATH = GIRL_IMG_PATH + "mobile.png";
    public final static String GIRL_EYES_IMG_PATH = GIRL_IMG_PATH + "bg2.png";
    public final static String GIRL_CONVERSATION_BG_IMG_PATH = GIRL_IMG_PATH + "conversation_bg.png";

    //AUDIO
    public final static String WAKEUP_AUDIO_PATH = "audio/episode_dreaming_room/wakeup.mp3";
    public final static String CREDITS_AUDIO_PATH = "audio/episode_dreaming_room/credits.mp3";
    public final static String BOY_MUSIC_AUDIO_PATH = "audio/episode_room/boy_music.mp3";
    public final static String GIRL_MUSIC_AUDIO_PATH = "audio/episode_room/girl_music.mp3";

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public DreamingRoomRenderable(float xPos, float yPos, float width, float height, String id, boolean isBoy) {
        super(xPos, yPos, width, height, Renderable.ACTOR_EPISODE_DREAMING_ROOM, id, isBoy ? BOY_BG_IMG_PATH : GIRL_BG_IMG_PATH);

        chatEnabled = false;
        if (isBoy) {
            mobileRenderable = createImageRenderable("mobile", BOY_MOBILE_IMG_PATH,false,false,3);
            eyesRenderable = createImageRenderable("eyes", BOY_EYES_IMG_PATH,false,false, 2);
        } else {
            mobileRenderable = createImageRenderable("mobile", GIRL_MOBILE_IMG_PATH,false,false,3);
            eyesRenderable = createImageRenderable("eyes", GIRL_EYES_IMG_PATH,false,false, 2);
        }
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();
        allRenderables.add(eyesRenderable);
        allRenderables.add(mobileRenderable);
    }

    public void togglePhone() {
        if (mobileRenderable.isVisible())
            mobileRenderable.setVisible(false);
        else
            mobileRenderable.setVisible(true);
    }

    public ImageRenderable getMobileRenderable() {
        return mobileRenderable;
    }

    public ImageRenderable getEyesRenderable() {
        return eyesRenderable;
    }
}
