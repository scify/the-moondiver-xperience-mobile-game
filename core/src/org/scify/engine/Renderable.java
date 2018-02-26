package org.scify.engine;

import java.util.Date;

/**
 * Describes an object that is used in game and has substance in terms of game logic.
 */
public class Renderable extends Positionable {

    protected String type;
    /**
     * Identifier for the renderable. Used to differentiate similar renderables,
     * and for debugging reasons.
     */
    protected String id;
    protected UserInputHandler userInputHandler;
    protected String imgPath;

    /**
     * timestamp describing when the component was last updated
     */
    protected long renderableLastUpdated = new Date().getTime();

    public Renderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height);
        this.type = type;
        this.id = id;
    }

    public Renderable(String type, String id) {
        super(0, 0, 0, 0);
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    /**
     * Set the class that will handle all user actions regarding this renderable.
     * @param userInputHandler
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
    
    protected void componentWasUpdated() {
        this.renderableLastUpdated = new Date().getTime();
    }
}
