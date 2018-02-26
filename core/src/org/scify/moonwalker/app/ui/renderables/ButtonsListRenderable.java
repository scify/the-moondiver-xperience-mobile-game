package org.scify.moonwalker.app.ui.renderables;

import org.scify.engine.Renderable;
import org.scify.moonwalker.app.ui.actors.ActionButton;

import java.util.LinkedList;
import java.util.List;

public class ButtonsListRenderable extends Renderable {

    protected List<ActionButton> buttonList;

    public ButtonsListRenderable(float xPos, float yPos, float width, float height, String type, String id) {
        super(xPos, yPos, width, height, type, id);
        this.buttonList = new LinkedList<>();
    }

    public void  setButtonsList(List<ActionButton> buttonList) {
        this.buttonList = buttonList;
    }

    public List<ActionButton> getButtonList() {
        return buttonList;
    }

    public void addButton(ActionButton button) {
        buttonList.add(button);
    }
}
