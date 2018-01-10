package org.scify.moonwalker.app.ui.components;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.scify.engine.UserInputHandler;

public class SelectDialog extends ActionDialog {

    public SelectDialog(String title, String bodyText, Skin sKin) {
        super(title, bodyText, sKin);
    }

    @Override
    public String getType() {
        return "SELECT_DIALOG";
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
