package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.renderables.*;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.FunctionEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FadingTableRenderable extends TableRenderable {
    //audio
    public static final String CLICK_AUDIO_PATH = "audio/button1.mp3";
    public static final String WRONG_BUTTON_AUDIO_PATH = "audio/wrong.mp3";
    public static final String MOBILE_AUDIO_PATH = "audio/message.mp3";
    public static final String DAYPASSED_AUDIO_PATH = "audio/episode_location/day_passed.mp3";
    public static final String NIGHT_AUDIO_PATH = "audio/episode_cockpit/owl.mp3";
    public static final String MOON_TAKE_OFF_AUDIO_PATH = "audio/episode_cockpit/moon_take_off.mp3";

//    public static final int TABLE_BG_DEFAULT_Z_INDEX = 1;
    public static final int RENDERABLE_DEFAULT_Z_INDEX = 2;


    protected List<Runnable> beforeFadeIn = Collections.synchronizedList(new ArrayList<Runnable>());
    protected List<Runnable> afterFadeIn = Collections.synchronizedList(new ArrayList<Runnable>());
    protected List<Runnable> beforeFadeOut = Collections.synchronizedList(new ArrayList<Runnable>());
    protected List<Runnable> afterFadeOut = Collections.synchronizedList(new ArrayList<Runnable>());

    public void addBeforeFadeOut(Runnable beforeFadeOut) {
        this.beforeFadeOut.add(beforeFadeOut);
    }

    public void addAfterFadeOut(Runnable afterFadeOut) {
        this.afterFadeOut.add(afterFadeOut);
    }

    public FadingTableRenderable(String type, String id) {
        super(type, id);
//        tableBGRenderable.setZIndex(TABLE_BG_DEFAULT_Z_INDEX);
        this.setZIndex(RENDERABLE_DEFAULT_Z_INDEX);
        setPositionDrawable(false);
    }

    /**
     * Create a fading renderable that starts in invisible state.
     *
     * @param xPos        The x position.
     * @param yPos        The y position.
     * @param width       The width of the renderable.
     * @param height      The height of the renderable.
     * @param type        The type of the renderable.
     * @param id          The id of the renderable.
     * @param bgImagePath The background image path.
     */
    public FadingTableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath) {
        super(xPos, yPos, width, height, type, id, bgImagePath);
//        tableBGRenderable.setZIndex(TABLE_BG_DEFAULT_Z_INDEX);
        this.setZIndex(RENDERABLE_DEFAULT_Z_INDEX);
        setVisible(false);
        setPositionDrawable(false);
    }

    public FadingTableRenderable(float xPos, float yPos, float width, float height, String type, String id, String bgImagePath, boolean bStartVisibility) {
        super(xPos, yPos, width, height, type, id);

        tableBGRenderable = new ImageRenderable("bg", bgImagePath);
//        tableBGRenderable.setZIndex(TABLE_BG_DEFAULT_Z_INDEX);
        this.setZIndex(RENDERABLE_DEFAULT_Z_INDEX);
        setVisible(bStartVisibility);
        setPositionDrawable(false);
    }

    public synchronized void fadeIn() {
        EffectSequence fadeInEffects = new EffectSequence();
        // Add before effects
        for (final Runnable rCur : beforeFadeIn) {
            fadeInEffects.addEffect(new FunctionEffect(rCur));
        }
        // Add actual fade effect
        fadeInEffects.addEffect(new FadeEffect(1.0, 0.0, 0));
        fadeInEffects.addEffect(new VisibilityEffect(true));
        fadeInEffects.addEffect(new FadeEffect(0.0, 1.0, 1000));
        // Add after effects
        for (final Runnable rCur : afterFadeIn) {
            fadeInEffects.addEffect(new FunctionEffect(rCur));
        }
        this.addEffect(fadeInEffects);
    }


    public synchronized void setBeforeFadeIn(Runnable beforeFadeIn) {
        this.beforeFadeIn.add(beforeFadeIn);
    }

    public synchronized void addAfterFadeIn(Runnable afterFadeIn) {
        this.afterFadeIn.add(afterFadeIn);
    }

    public synchronized void fadeOut() {
        EffectSequence fadeOutEffects = new EffectSequence();
        // Add before effects
        for (final Runnable rCur : beforeFadeOut) {
            fadeOutEffects.addEffect(new FunctionEffect(rCur));
        }
        // Add actual fade effects
        fadeOutEffects.addEffect(getFadeOutEffect());

        // Add after effects
        for (final Runnable rCur : afterFadeOut) {
            fadeOutEffects.addEffect(new FunctionEffect(rCur));
        }
        this.addEffect(fadeOutEffects);

    }

    public EffectSequence getFadeOutEffect() {
        EffectSequence fadeOutSeq = new EffectSequence();
        fadeOutSeq.addEffect(new FadeEffect(1.0, 0.0, 1000));
        fadeOutSeq.addEffect(new VisibilityEffect(false));
        return fadeOutSeq;
    }

    protected ActionButtonRenderable createImageButton(String id, String img, UserAction userAction, boolean positionDrawable, boolean visibility, int zIndex) {
        ActionButtonRenderable ret = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, id);
        ret.setImgPath(img);
        ret.setUserAction(userAction);
        setRenderableAttributes(ret, positionDrawable, visibility, zIndex);
        return ret;
    }

    public EffectSequence getShowEffect() {
        EffectSequence ret = new EffectSequence();
        ret.addEffect(new FadeEffect(1,0, 0));
        ret.addEffect(new VisibilityEffect(true));
        ret.addEffect(new FadeEffect(0,1, 1000));
        return ret;
    }

    public ImageRenderable getTableBGRenderable() {
        return tableBGRenderable;
    }

    public synchronized void reveal(Renderable renderable) {
        renderable.addEffect(getShowEffect());
    }

}
