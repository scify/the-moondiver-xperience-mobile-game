package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;

import java.util.HashSet;
import java.util.Set;

public class ContactScreenRenderable extends FadingTableRenderable {
    //renderable image paths
    protected final static String BG_IMG_PATH = "img/episode_contact_screen/bg.png";
    protected final static String AUNT_TEXT_IMG_PATH = "img/episode_contact_screen/aunt_text.png";
    protected final static String AUNT_AVATAR_IMG_PATH = "img/episode_contact_screen/aunt_avatar.png";
    protected final static String EXIT_BUTTON_IMG_PATH = "img/episode_contact_screen/exit.png";
    //renderable ids
    protected final static String AUNT_TEXT_ID = "aunt_text";
    protected final static String AUNT_AVATAR_ID = "aunt_avatar";
    protected final static String EXIT_BUTTON_ID = "exit_button";
    //audio
    public final static String MOBILE_AUDIO_PATH = "audio/message.mp3";
    public final static String WRONG_BUTTON_AUDIO_PATH = "audio/wrong.mp3";

    protected ImageRenderable contactDescription;
    protected ImageRenderable contactAvatar;
    protected ActionButtonRenderable exitButton;

    protected boolean chatEnabled;

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

        exitButton = createImageButton(EXIT_BUTTON_ID, EXIT_BUTTON_IMG_PATH, new UserAction(UserActionCode.BACK), false, true, 1);
        allRenderables.add(exitButton);
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void enableChat () {
        chatEnabled = true;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public ImageRenderable getContactDescription() {
        return contactDescription;
    }

    public ImageRenderable getContactAvatar() {
        return contactAvatar;
    }

    public ActionButtonRenderable getExitButton() {
        return exitButton;
    }
}
