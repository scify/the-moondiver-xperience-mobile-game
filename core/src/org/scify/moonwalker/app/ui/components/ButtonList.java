package org.scify.moonwalker.app.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import org.scify.moonwalker.app.helpers.GameInfo;
import org.scify.moonwalker.app.ui.input.UserInputHandlerImpl;

public class ButtonList extends Table{

    protected Label label;
    protected GameInfo gameInfo;
    protected final float MAIN_LABEL_PADDING_PIXELS = 12;
    protected final float BUTTON_PADDING_PIXELS = 10;
    protected boolean isVertical;

    public ButtonList(Skin skin, boolean isVertical) {
        super(skin);
        this.gameInfo = GameInfo.getInstance();
        this.isVertical = isVertical;
        setFillParent(true);
        center().center();
    }

    public void addMainLabel(String labelTxt) {
        label = new Label(labelTxt, getSkin());
        label.setFontScale(2);
        label.setAlignment(Align.center);
        add(label).colspan(3).pad(gameInfo.pixelsWithDensity(MAIN_LABEL_PADDING_PIXELS)).row();
    }

    public void addButton(Actor button, UserInputHandlerImpl userInputHandler) {
        button.addListener(userInputHandler);
        if(isVertical) {
            add(button).pad(gameInfo.pixelsWithDensity(BUTTON_PADDING_PIXELS)).width(button.getWidth()).height(button.getHeight());
            row();
        } else {
            add(button).width(gameInfo.getScreenWidth() / 3f).center().height(button.getHeight()).expand().pad(gameInfo.pixelsWithDensity(BUTTON_PADDING_PIXELS));
        }

    }
}