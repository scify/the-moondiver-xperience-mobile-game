package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.UserAction;
import org.scify.engine.UserActionCode;
import org.scify.engine.renderables.ActionButtonRenderable;
import org.scify.engine.renderables.ImageRenderable;
import org.scify.engine.renderables.Renderable;
import org.scify.engine.renderables.effects.EffectSequence;
import org.scify.engine.renderables.effects.FadeEffect;
import org.scify.engine.renderables.effects.VisibilityEffect;
import org.scify.moonwalker.app.helpers.AppInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class IntroRenderable extends Renderable {

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
    protected ImageRenderable arrowButton;
    protected Renderable bg;
    protected boolean inputEnabled = false;

    public Renderable getBgImg() {
        return bg;
    }

    //audio
    public static final String MAINMENU_AUDIO_PATH = "audio/episode_main_menu/bg.mp3";

    protected Set<Renderable> allRenderables;

    public synchronized Set<Renderable> getAllRenderables() {
        return allRenderables;
    }


    public IntroRenderable(float xPos, float yPos, float width, float height, String id, boolean isBoy) {
        super(xPos, yPos, width, height, ACTOR_EPISODE_INTRO, id);

        allRenderables = Collections.synchronizedSet(new HashSet<Renderable>());

        // Add BG
        bg = getBackground(isBoy);
        allRenderables.add(bg);

        // Add images
        if (isBoy) {
            leftImage = new ImageRenderable(LEFT_ID,BOY_AUNT_IMG_PATH);
            leftImage.setPositionDrawable(true);
            leftImage.setWidth(appInfo.convertX(452));
            leftImage.setHeight(appInfo.convertY(815));
            leftImage.setxPos(0.01f * appInfo.getScreenWidth());
            leftImage.setyPos(0.14f * appInfo.getScreenHeight());

            rightImage = new ImageRenderable(RIGHT_ID, BOY_PLAYER_IMG_PATH);
            rightImage.setPositionDrawable(true);
            rightImage.setWidth(appInfo.convertX(469));
            rightImage.setHeight(appInfo.convertY(814));
            rightImage.setxPos(0.72f * appInfo.getScreenWidth());
            rightImage.setyPos(0.14f * appInfo.getScreenHeight());

        }else {
            leftImage = new ImageRenderable(LEFT_ID,GIRL_AUNT_IMG_PATH);
            leftImage.setWidth(appInfo.convertX(448));
            leftImage.setHeight(appInfo.convertY(749));
            leftImage.setxPos(0.01f * appInfo.getScreenWidth());
            leftImage.setyPos(0.14f * appInfo.getScreenHeight());

            rightImage = new ImageRenderable(RIGHT_ID, GIRL_PLAYER_IMG_PATH);
            rightImage.setWidth(appInfo.convertX(423));
            rightImage.setHeight(appInfo.convertY(824));
            rightImage.setxPos(0.75f * appInfo.getScreenWidth());
            rightImage.setyPos(0.14f * appInfo.getScreenHeight());
        }
        leftImage.setVisible(false);
        leftImage.setZIndex(10);

        rightImage.setVisible(false);
        rightImage.setZIndex(10);

        allRenderables.add(leftImage);
        allRenderables.add(rightImage);

        // Add arrow
        //arrowButton = new ActionButtonRenderable(Renderable.ACTOR_IMAGE_BUTTON, ARROW_ID);
        arrowButton = new ImageRenderable(Renderable.ACTOR_IMAGE_BUTTON, ARROW_ID);
        arrowButton.setImgPath(ARROW_IMG_PATH);
        //arrowButton.setUserAction(new UserAction(UserActionCode.BUTTON_PRESSED));
        arrowButton.setPositionDrawable(true);
        arrowButton.setVisible(false);
        arrowButton.setZIndex(3);

        float arrowRatio = 0.945f;
        float arrowHeight = appInfo.getScreenHeight() * 0.15f;
        float arrowWidth = arrowHeight * arrowRatio;
        arrowButton.setHeight(arrowHeight);
        arrowButton.setWidth(arrowWidth);
        arrowButton.setxPos(appInfo.convertX((1920f - arrowWidth) / 2));
        arrowButton.setyPos(0.07f * appInfo.getScreenHeight());
        //arrowButton.markAsNeedsUpdate();
        //arrowButton.setyPos(appInfo.getScreenHeight() - 10);
        //arrowButton.setPadding(0f);
        //arrowButton.setyPos(appInfo.convertY(19f));
        //arrowButton.setyPos(appInfo.convertY(500f));
//        arrowButton.setyPos(0.01f * appInfo.getScreenHeight());

        allRenderables.add(arrowButton);
    }

    public synchronized ImageRenderable getLeftImage() {
        return leftImage;
    }

    public synchronized ImageRenderable getRightImage() {
        return rightImage;
    }

    public synchronized ImageRenderable getArrowButton() {
        return arrowButton;
    }

    public Renderable getBackground(boolean isBoy) {
        ImageRenderable ir = new ImageRenderable(0, 0, appInfo.getScreenWidth(), appInfo.getScreenHeight(), "bg",
                isBoy ? BOY_BG_IMG_PATH : GIRL_BG_IMG_PATH);
        ir.setZIndex(0);
        ir.setVisible(false);
        return ir;
    }

    public boolean isReadyForInput() {
        return inputEnabled;
    }

    public void setInputEnabled(boolean inputEnabled) { this.inputEnabled = inputEnabled; }

}
