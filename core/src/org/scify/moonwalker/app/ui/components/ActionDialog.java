package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.UserInputHandler;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.awt.geom.Point2D;

public class ActionDialog implements Renderable {

    protected Dialog dialog;
    protected UserInputHandler inputHandler;
    protected Skin skin;
    protected String title;
    protected Point2D.Float position;
    protected GameInfo gameInfo;
    private ActionDialog instance = this;

    public ActionDialog(String title, String bodyText, Skin sKin) {
        gameInfo = GameInfo.getInstance();
        this.skin = sKin;
        this.title = title;
        dialog = new Dialog(title, skin, "dialog") {
            public void result(Object obj) {
                // TODO add inputHandler call here
                if (inputHandler != null)
                    inputHandler.addUserActionForRenderable(instance, obj);
            }
        };
        dialog.text(bodyText);
    }

    public void addButton(String buttonTitle, Object buttonProps) {
        dialog.button(buttonTitle, buttonProps);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void alignCenter() {
        dialog.setWidth(gameInfo.getScreenWidth() * 0.8f);
        dialog.setPosition(gameInfo.getScreenWidth() / 2f - dialog.getWidth() / 2f, gameInfo.getScreenHeight() / 2f - dialog.getHeight() / 2f);
    }

    @Override
    public String getType() {
        return "ACTION_DIALOG";
    }

    @Override
    public void setInputHandler(UserInputHandler userInputHandler) {
        this.inputHandler = userInputHandler;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public void setX(float fX) {

    }

    @Override
    public void setY(float fY) {

    }
}
