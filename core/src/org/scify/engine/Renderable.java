package org.scify.engine;

public class Renderable extends Positionable {
    protected String type;
    protected String id;
    protected UserInputHandler userInputHandler;
    protected String imgPath;

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
}
