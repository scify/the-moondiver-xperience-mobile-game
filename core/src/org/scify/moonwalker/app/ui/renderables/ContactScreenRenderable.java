package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class ContactScreenRenderable extends ChattableRenderable {
    //renderable image paths
    protected final static String IMG_PATH = "img/episode_contact_screen/";
    protected final static String BG_IMG_PATH = IMG_PATH + "bg.png";
    protected final static String AUNT_TEXT_IMG_PATH = IMG_PATH +"aunt_text.png";
    protected final static String AUNT_AVATAR_IMG_PATH = IMG_PATH + "aunt_avatar.png";
    public final static String CONVERSATION_BG_IMG_PATH = IMG_PATH + "conversation_bg.png";
    //renderable ids
    protected final static String AUNT_TEXT_ID = "aunt_text";
    protected final static String AUNT_AVATAR_ID = "aunt_avatar";
    //audio
    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";
    public final static String WRONG_BUTTON_AUDIO_PATH = "audio/wrong.mp3";

    protected ImageRenderable contactDescription;
    protected ImageRenderable contactAvatar;
    protected ActionButtonRenderable exitButton;

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }

    public ContactScreenRenderable(float xPos, float yPos, float width, float height, String id) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_CONTACT_SCREEN, id, BG_IMG_PATH);
        initSubRenderables();
    }

    private void initSubRenderables() {
        allRenderables = new HashSet<>();

        contactDescription = createImageRenderable(AUNT_TEXT_ID, AUNT_TEXT_IMG_PATH, false, true, 1);
        allRenderables.add(contactDescription);

        contactAvatar = createImageRenderable(AUNT_AVATAR_ID, AUNT_AVATAR_IMG_PATH, false, true, 1);
        allRenderables.add(contactAvatar);

    }

    public ImageRenderable getContactDescription() {
        return contactDescription;
    }

    public ImageRenderable getContactAvatar() {
        return contactAvatar;
    }

}
