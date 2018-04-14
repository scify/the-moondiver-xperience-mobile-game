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
    /**
     * timestamp describing when the component was last updated
     */
    protected long renderableLastUpdated = new Date().getTime();

    /**
     * A map holding current effect state info.
     */
    protected Map<Effect, Map<String,String>> effectInfo = new HashMap<>();

    /**
     * Holds the last effect that was added.
     */
    protected Effect lastEffectAdded;

    public Renderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height);
        this.type = type;
        this.id = id;
        appInfo = AppInfo.getInstance();
    }

    public Renderable(String type, String id) {
        super(0, 0, 0, 0);
        this.type = type;
        this.id = id;
        appInfo = AppInfo.getInstance();
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
        this.renderableLastUpdated = new Date().getTime();
    }

    @Override
    public EffectTarget apply(Effect toApply) {
        return toApply.applyTo(this);
    }

    @Override
    public Set<Effect> getEffects() {
        return effectInfo.keySet();
    }

    @Override
    public Map<String, String> getEffectInfo(Effect effectOfInterest) {
        return effectInfo.get(effectOfInterest);
    }

    @Override
    public void setEffectInfo(Effect effectOfInterest, Map<String, String> updatedInfo) {
        effectInfo.put(effectOfInterest, updatedInfo);
    }

    @Override
    public void removeEffect(Effect eToRemove) {
        effectInfo.remove(eToRemove);
    }
}
