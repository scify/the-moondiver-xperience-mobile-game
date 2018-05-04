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

    public boolean debugMode = false;

    // Actors
    public static final String ACTOR_LABEL = "label";
    public static final String ACTOR_ROTATABLE_LABEL = "rotatable_label";
    public static final String ACTOR_IMAGE = "image";
    public static final String ACTOR_TABLE = "table";
    public static final String ACTOR_TEXT_BUTTON = "text_button";
    public static final String ACTOR_ROTATABLE_TEXT_BUTTON = "rotatable_text_button";
    public static final String ACTOR_IMAGE_BUTTON = "image_button";

    // Sprites
    public static final String SPRITE_IMAGE = "image_sprite";
    public static final String SPRITE_BACKGROUND_IMAGE = "background_image";
    public static final String SPRITE_PLAYER = "player";



    public static final String CONVERSATION_SINGLE_CHOICE = "single_choice_conversation";
    public static final String CONVERSATION_MULTIPLE_CHOICE = "multiple_choice_conversation";
    public static final String CONVERSATION_TWO_CHOICE = "two_choice_conversation";

    public static final String ACTOR_DIALOG = "actor_dioalog";

    public static final String ACTOR_EPISODE_MAIN_MENU = "main_menu";
    public static final String ACTOR_EPISODE_ROOM = "room";
    public static final String ACTOR_EPISODE_FOREST = "forest";
    public static final String ACTOR_EPISODE_COCKPIT = "cockpit";
    public static final String ACTOR_EPISODE_CALCULATOR = "calculator";
    public static final String ACTOR_EPISODE_SPACESHIP_CHARGER = "spaceship_charger";
    public static final String ACTOR_EPISODE_MAP_LOCATION = "map_location";
    public static final String ACTOR_EPISODE_CONTACT_SCREEN = "contact_screen";

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

    public void renderableWasUpdated() {
        needsUpdate = true;
        renderableLastUpdated = new Date().getTime();
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
