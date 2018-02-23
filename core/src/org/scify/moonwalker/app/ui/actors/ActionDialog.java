package org.scify.moonwalker.app.ui.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.UserInputHandler;
import org.scify.engine.Renderable;
import org.scify.moonwalker.app.helpers.GameInfo;

import java.awt.geom.Point2D;

public class ActionDialog extends Renderable {

    protected Dialog dialog;
    protected Skin skin;
    protected String title;
    protected GameInfo gameInfo;
    private ActionDialog instance = this;

    public ActionDialog(float xPos, float yPos, float width, float height, String title, String bodyText, Skin sKin) {
        super(xPos, yPos, width, height, "dialog", title);
        gameInfo = GameInfo.getInstance();
        this.skin = sKin;
        this.title = title;
        dialog = new Dialog(title, skin, "dialog") {
            public void result(Object obj) {
                // TODO add inputHandler execute here
                if (userInputHandler != null)
                    userInputHandler.addUserActionForRenderable(instance, obj);
            }
        };
        dialog.setWidth(width);
        dialog.setPosition(xPos - width / 2f, yPos - height / 2f);
        dialog.text(bodyText);
    }

    public void addButton(String buttonTitle, Object buttonProps) {
        dialog.button(buttonTitle, buttonProps);
    }

    public Dialog getDialog() {
        return dialog;
    }

}
