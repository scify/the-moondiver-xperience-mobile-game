package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

import java.util.HashSet;
import java.util.Set;

public class IntroRenderable extends FadingTableRenderable{

    //Images
    protected final static String BOY_IMG_PATH = "img/episode_intro/boy/";
    protected final static String BOY_BG_IMG_PATH = BOY_IMG_PATH + "bg.png";
    protected final static String BOY_AUNT_IMG_PATH = BOY_IMG_PATH + "aunt.png";
    protected final static String BOY_PLAYER_IMG_PATH = BOY_IMG_PATH + "player.png";
    protected final static String GIRL_IMG_PATH = "img/episode_intro/girl/";
    protected final static String GIRL_BG_IMG_PATH = GIRL_IMG_PATH + "bg.png";
    protected final static String GIRL_AUNT_IMG_PATH = GIRL_IMG_PATH + "aunt.png";
    protected final static String GIRL_PLAYER_IMG_PATH = GIRL_IMG_PATH + "player.png";
    protected final static String ARROW_IMG_PATH = "img/episode_intro/arrow.png";

    protected final static String ARROW_ID = "arrow";
    protected final static String LEFT_ID = "left";
    protected final static String RIGHT_ID = "right";

    protected ImageRenderable leftImage;
    protected ImageRenderable rightImage;
    protected ActionButtonRenderable arrowButton;

    //audio
    public static final String MAINMENU_AUDIO_PATH = "audio/episode_main_menu/bg.mp3";

    protected Set<Renderable> allRenderables;

    public Set<Renderable> getAllRenderables() {
        return allRenderables;
    }


    public IntroRenderable(float xPos, float yPos, float width, float height, String id, boolean isBoy) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_INTRO, id, isBoy ? BOY_BG_IMG_PATH : GIRL_BG_IMG_PATH);
        allRenderables = new HashSet<>();
        if (isBoy) {
            leftImage = createImageRenderable(LEFT_ID,BOY_AUNT_IMG_PATH,false, false, 10);
            rightImage = createImageRenderable(RIGHT_ID, BOY_PLAYER_IMG_PATH,false, false, 10);
        }else {
            leftImage = createImageRenderable(LEFT_ID,GIRL_AUNT_IMG_PATH,false, false, 10);
            rightImage = createImageRenderable(RIGHT_ID, GIRL_PLAYER_IMG_PATH,false, false, 10);
        }
        allRenderables.add(leftImage);
        allRenderables.add(rightImage);
        arrowButton = createImageButton(ARROW_ID, ARROW_IMG_PATH, new UserAction(UserActionCode.BUTTON_PRESSED), false, false, 10);
        allRenderables.add(arrowButton);
    }

    private EffectSequence getShowEffect() {
        EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1,0, 0));
        ret.addEffect(new VisibilityEffect(true));
        ret.addEffect(new FadeEffect(0,1, 1000));
        return ret;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public void reveal(ImageRenderable renderable) {
        renderable.addEffect(getShowEffect());
    }

    public void reveal(ActionButtonRenderable renderable) {
        renderable.addEffect(getShowEffect());
    }

    public ImageRenderable getLeftImage() {
        return leftImage;
    }

    public ImageRenderable getRightImage() {
        return rightImage;
    }

    public ActionButtonRenderable getArrowButton() {
        return arrowButton;
    }
}
