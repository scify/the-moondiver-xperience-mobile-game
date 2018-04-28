package org.scify.engine.renderables;

import org.scify.engine.Positionable;
import org.scify.engine.UserInputHandler;
import org.scify.engine.renderables.effects.Effect;
import org.scify.engine.renderables.effects.EffectTarget;
import org.scify.moonwalker.app.helpers.AppInfo;

import java.util.*;

/**
 * Describes an object that is used in game and has substance in terms of game logic.
 */
public class Renderable extends Positionable implements EffectTarget {

    public static final String LABEL = "label";
    public static final String ROTATABLE_LABEL = "rotatable_label";
    public static final String IMAGE = "image";
    public static final String TABLE = "table";
    public static final String TEXT_BUTTON = "text_button";
    public static final String ROTATABLE_TEXT_BUTTON = "rotatable_text_button";
    public static final String IMAGE_BUTTON = "image_button";

    public static final String NEXT_CONVERSATION = "next_conversation";
    public static final String MULTIPLE_CHOICE_CONVERSATION = "multiple_choice_conversation";
    public static final String TWO_CHOICE_CONVERSATION = "two_choice_conversation";

    public static final String DIALOG = "DIALOG";

    public static final String MAIN_MENU = "main_menu";
    public static final String ROOM = "room";
    public static final String FOREST = "forest";
    public static final String COCKPIT = "cockpit";
    public static final String CALCULATOR = "calculator";
    public static final String SPACESHIP_CHARGER = "spaceship_charger";
    public static final String MAP_LOCATION = "map_location";
    public static final String CONTACT_SCREEN = "contact_screen";

    protected String type;
    /**
     * Identifier for the renderable. Used to differentiate similar renderables,
     * and for debugging reasons.
     */
    protected String id;
    protected UserInputHandler userInputHandler;
    protected String imgPath;
    protected AppInfo appInfo;
    protected boolean needsUpdate;
    /**
     * timestamp describing when the component was last updated
     */
    protected long renderableLastUpdated = new Date().getTime();

    /**
     * A map holding current effect state info.
     */
    protected Set<Effect> effectInfo = new HashSet<>();

    /**
     * Describes the visibility of the renderable. A non-visible renderable is also disabled, i.e. cannot
     * accept events, and does not take up any space (TODO: Check if the statement about space stands).
     */
    protected boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public Renderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height);
        this.type = type;
        this.id = id;
        appInfo = AppInfo.getInstance();
        visible = true;
        needsUpdate = true;
    }

    public Renderable(String type, String id) {
        super(0, 0, 0, 0);
        this.type = type;
        this.id = id;
        appInfo = AppInfo.getInstance();
        visible = true;
        needsUpdate = true;
    }

    public boolean needsUpdate() {
        needsUpdate = needsUpdate || (getEffects().size() > 0);
        return needsUpdate;
    }

    public void wasUpdated() {
        this.needsUpdate = false;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    /**
     * Set the class that will handle all user actions regarding this renderable.
     * @param userInputHandler The handler that deals with the renderable actions.
     */
    public void setUserInputHandler(UserInputHandler userInputHandler) {
        this.userInputHandler = userInputHandler;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "Type: " + type + " id: " + id + " x: " + xPos + " y: " + yPos;
    }

    public long getRenderableLastUpdated() {
        return renderableLastUpdated;
    }
    
    protected void renderableWasUpdated() {
        needsUpdate = true;
        renderableLastUpdated = new Date().getTime();
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return new HashSet<>(effectInfo);
    }

    @Override
    public EffectTarget addEffect(Effect effectOfInterest) {
        effectInfo.add(effectOfInterest);
        return this;
    }

    @Override
    public EffectTarget removeEffect(Effect eToRemove) {
        effectInfo.remove(eToRemove);
        return this;
    }

    public void setVisible(boolean visible) {
        needsUpdate = true;
        this.visible = visible;
    }
}
