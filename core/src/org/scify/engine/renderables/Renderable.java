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
    public void addEffect(Effect effectOfInterest) {
        effectInfo.add(effectOfInterest);
    }

    @Override
    public void removeEffect(Effect eToRemove) {
        effectInfo.remove(eToRemove);
    }

    public void setVisible(boolean visible) {
        needsUpdate = true;
        this.visible = visible;
    }
}
